package com.wGateway;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class WGatewayRoute {
	
	@Autowired
	private Modules modules;
	
	/**
	 * 게이트웨이 route
	 * @param builder
	 * @return
	 */
	@Bean
	public RouteLocator wGatewayRouteLocator(RouteLocatorBuilder builder) {
				
		return builder.routes()
				
				//로그인 페이지 이동
				.route("login", r -> r
					.path("/member/login")
					.uri(modules.getMemberBaseUrl() + "/login")
					.predicate(serverWebExchange -> serverWebExchange
							.getRequest()
							.getPath()
							.pathWithinApplication()
							.value().startsWith("/member"))				
				//자산관리 MO 페이지 이동
				).route("wAssetsMUI", r -> r
					.path("/assets*")					
					.uri(modules.getAssetsMuiBaseUrl())					
					.predicate(serverWebExchange -> {
						//headers의 User-Agent체크하여 디바이스 구분
						List<String> uaList = serverWebExchange.getRequest().getHeaders().get("User-Agent");
						boolean isMobile = false;
						for(String userAgent : uaList) {							
							if(userAgent.toUpperCase().indexOf("MOBILE") > -1) {
								isMobile = true;
								break;
							}
						}
						if(isMobile) {							
							if(serverWebExchange.getRequest().getPath().pathWithinApplication().value().startsWith("/assets")) {
								return true;
							}else {
								return false;
							}
						}else {
							return false;
						}	
					})
				//자산관리 PC 페이지 이동
				).route("wAssetsPUI", r -> r
					.path("/assets*")					
					.uri(modules.getAssetsPuiBaseUrl())
					.predicate(serverWebExchange -> serverWebExchange
							.getRequest()
							.getPath()
							.pathWithinApplication()
							.value().startsWith("/assets"))
				)
				.build();
	}
}
