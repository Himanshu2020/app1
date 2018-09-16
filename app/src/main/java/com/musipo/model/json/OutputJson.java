package com.musipo.model.json;

import java.io.Serializable;


public class OutputJson<T> implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String status;
	private T data;
	private String message;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public T getData() {
		
		return data;
	}
	public void setData(T data) {
		
		this.data = data;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return "OutputJson [status=" + status + ", data=" + data + ", message="
				+ message + "]";
	}	
}
