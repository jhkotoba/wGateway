package com.wGateway;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@RestController
public class WGatewayWebClient {
	
	@Autowired
    private WebClient.Builder builder;
	
	@Autowired
	private Modules modules;
	
	/**
	 * GET API
	 * @param request, module, apiurl 
	 * @return Flux<?>
	 */
	@GetMapping(value = "/api/{module}/{apiurl}")
	public Mono<?> getApi(ServerHttpRequest request, @PathVariable String module, @PathVariable String apiurl) {
		
		WebClient webClient = builder.build();		
		StringBuilder uri = null;
		
		switch(module) {
		case "admin" : uri = new StringBuilder(modules.getAdminUrl()); break;
		case "assets" : uri = new StringBuilder(modules.getAssetsUrl()); break;
		case "member" : uri = new StringBuilder(modules.getMemberUrl()); break;
		default : return null;
		}
		
		uri
		.append(Constant.SLASH).append(Constant.API)
		.append(Constant.SLASH).append(module)
		.append(Constant.SLASH).append(apiurl);
		
		if(Objects.nonNull(request.getURI().getRawQuery())) {
			uri.append(Constant.QUESTION_MARK).append(request.getURI().getRawQuery());
		}
		
		return webClient.get()
				.uri(uri.toString())
				.cookies(cookies -> {					
					for (String key : request.getCookies().keySet()) {
						cookies.add(key, request.getCookies().get(key).get(0).getValue());
					}
				})
				.retrieve()
				.bodyToMono(Object.class);
	}	
	
	/**
	 * POST API(Mono)
	 * @param request, bodyValue, module, apiurl
	 * @return
	 */
	@PostMapping(value = "/api/{module}/{apiurl}")
	public Mono<?> postMonoApi(ServerHttpRequest request, @RequestBody Object bodyValue, 
			@PathVariable String module, @PathVariable String apiurl) {		
				
		WebClient webClient = builder.build();		
		StringBuilder uri = null;
		
		switch(module) {
		case "admin" : uri = new StringBuilder(modules.getAdminUrl()); break;
		case "assets" : uri = new StringBuilder(modules.getAssetsUrl()); break;
		case "member" : uri = new StringBuilder(modules.getMemberUrl()); break;
		default : return null;
		}
		
		uri
		.append(Constant.SLASH).append(Constant.API)
		.append(Constant.SLASH).append(module)
		.append(Constant.SLASH).append(apiurl);		
		
		return webClient.post()
				.uri(uri.toString())
				.cookies(cookies -> {					
					for (String key : request.getCookies().keySet()) {
						cookies.add(key, request.getCookies().get(key).get(0).getValue());
					}
				})
				.contentType(MediaType.APPLICATION_JSON)
		        .accept(MediaType.APPLICATION_JSON)
		        .bodyValue(bodyValue)
				.retrieve()
				.bodyToMono(Map.class);
	}
	
	/**
	 * loginProcess API
	 * @param request, response, bodyValue
	 * @return
	 */
	@PostMapping(value = "/api/member/loginProcess")
	public Mono<?> loginProcess(ServerHttpRequest request, ServerHttpResponse response, @RequestBody Map<String, Object> bodyValue) {		
				
		WebClient webClient = builder.build();
		
		return webClient.post()
				.uri(modules.getMemberUrl() + "/api/member/loginProcess")
				.contentType(MediaType.APPLICATION_JSON)
		        .accept(MediaType.APPLICATION_JSON)
		        .bodyValue(bodyValue)
				.retrieve()
				.bodyToMono(Map.class)
				.map(map -> {
					if(Objects.nonNull(map.get("jwt"))) {
						response.addCookie(
							ResponseCookie.from(Constant.TOKEN, map.get("jwt").toString())
								.path("/")
								.maxAge(Duration.ofHours(10))
								.build()
						);
					}
					map.remove("jwt");
					return map;
				});
	}
}
