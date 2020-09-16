package com.wGateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class WGatewayRoute {
	
	@Autowired
	private Properties info;
	
	@Bean
	public RouteLocator wGatewayRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("wAssetsPUI", r -> r 
					.path("/assets")					
					.uri(info.getAssetsBaseUrl())
				).route("login", r -> r
					.path("/member/login")					
					.uri(info.getMemberBaseUrl() + "/login")
				)
				.build();
	}
}
