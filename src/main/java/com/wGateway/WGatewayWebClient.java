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

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class WGatewayWebClient {
	
	@Autowired
    private WebClient.Builder builder;
	
	@Autowired
	private Properties info;
	
	/**
	 * GET API
	 * @param request, module, apiurl 
	 * @return Flux<?>
	 */
	@GetMapping(value = "/api/{module}/{apiurl}")
	public Flux<?> getApi(ServerHttpRequest request, @PathVariable String module, @PathVariable String apiurl) {
		
		WebClient webClient = builder.build();		
		StringBuilder uri = null;
		
		switch(module) {
		case "admin" : uri = new StringBuilder(info.getAdminUrl()); break;
		case "assets" : uri = new StringBuilder(info.getAssetsUrl()); break;
		case "member" : uri = new StringBuilder(info.getMemberUrl()); break;
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
				.retrieve()
				.bodyToFlux(Map.class);
	}	
	
	/**
	 * POST API
	 * @param request, bodyValue, module, apiurl
	 * @return
	 */
	@PostMapping(value = "/api/{module}/{apiurl}")
	public Mono<?> postApi(ServerHttpRequest request, @RequestBody Map<String, Object> bodyValue, 
			@PathVariable String module, @PathVariable String apiurl) {		
				
		WebClient webClient = builder.build();		
		StringBuilder uri = null;
		
		switch(module) {
		case "admin" : uri = new StringBuilder(info.getAdminUrl()); break;
		case "assets" : uri = new StringBuilder(info.getAssetsUrl()); break;
		case "member" : uri = new StringBuilder(info.getMemberUrl()); break;
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
				.uri(info.getMemberUrl() + "/api/member/loginProcess")
				.cookies(cookies -> {					
					for (String key : request.getCookies().keySet()) {
						cookies.add(key, request.getCookies().get(key).get(0).getValue());
					}
				})
				.contentType(MediaType.APPLICATION_JSON)
		        .accept(MediaType.APPLICATION_JSON)
		        .bodyValue(bodyValue)
				.retrieve()
				.bodyToMono(Map.class)
				.flatMap(map -> {										
					response.addCookie(
						ResponseCookie.from(Constant.TOKEN, map.get("jwt").toString())
							.path("/")
							.maxAge(Duration.ofHours(10))
							.build()
					);
					map.remove("jwt");
					return Mono.just(map);
				});
	}
}
