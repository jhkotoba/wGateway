package com.wGateway;

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
				//자산관리PC 페이지 이동
				.route("wAssetsPUI", r -> r
					.path("/assets")
					.uri(modules.getAssetsPuiBaseUrl())
				//로그인 페이지 이동
				).route("login", r -> r
					.path("/member/login")
					.uri(modules.getMemberBaseUrl() + "/login")
				)
				.build();
	}
}
