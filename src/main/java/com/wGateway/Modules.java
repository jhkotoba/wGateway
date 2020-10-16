package com.wGateway;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "modules")
public class Modules {
	
	private String gatewayUrl;
	
	private String adminUrl;
	private String adminBaseUrl;
	
	private String assetsPuiUrl;
	private String assetsPuiBaseUrl;
	
	private String assetsUrl;
	private String assetsBaseUrl;
	
	private String memberUrl;
	private String memberBaseUrl;
	
	public String getGatewayUrl() {
		return gatewayUrl;
	}
	public void setGatewayUrl(String gatewayUrl) {
		this.gatewayUrl = gatewayUrl;
	}
	public String getAdminUrl() {
		return adminUrl;
	}
	public void setAdminUrl(String adminUrl) {
		this.adminUrl = adminUrl;
	}
	public String getAdminBaseUrl() {
		return adminBaseUrl;
	}
	public void setAdminBaseUrl(String adminBaseUrl) {
		this.adminBaseUrl = adminBaseUrl;
	}
	public String getAssetsPuiUrl() {
		return assetsPuiUrl;
	}
	public void setAssetsPuiUrl(String assetsPuiUrl) {
		this.assetsPuiUrl = assetsPuiUrl;
	}
	public String getAssetsPuiBaseUrl() {
		return assetsPuiBaseUrl;
	}
	public void setAssetsPuiBaseUrl(String assetsPuiBaseUrl) {
		this.assetsPuiBaseUrl = assetsPuiBaseUrl;
	}
	public String getAssetsUrl() {
		return assetsUrl;
	}
	public void setAssetsUrl(String assetsUrl) {
		this.assetsUrl = assetsUrl;
	}
	public String getAssetsBaseUrl() {
		return assetsBaseUrl;
	}
	public void setAssetsBaseUrl(String assetsBaseUrl) {
		this.assetsBaseUrl = assetsBaseUrl;
	}
	public String getMemberUrl() {
		return memberUrl;
	}
	public void setMemberUrl(String memberUrl) {
		this.memberUrl = memberUrl;
	}
	public String getMemberBaseUrl() {
		return memberBaseUrl;
	}
	public void setMemberBaseUrl(String memberBaseUrl) {
		this.memberBaseUrl = memberBaseUrl;
	}
}
