package com.musipo.model.json;

public class InputJson<T> {
	
	private String apiKey;
	private T params;
	
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public T getParams() {
		return params;
	}
	public void setParams(T params) {
		this.params = params;
	}
	@Override
	public String toString() {
		return "InputJson [apiKey=" + apiKey + ", params=" + params + "]";
	}
	
}
