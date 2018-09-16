package com.musipo.model.json;

import java.util.List;

public class ListOutputJson<T> {

	private String status;
	private List<T> data;
	private String message;

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<T> getData() {
		return data;
	}
	public void setData(List<T> data) {
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
