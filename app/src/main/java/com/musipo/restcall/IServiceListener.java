package com.musipo.restcall;

public interface IServiceListener {
	public void result(String jsonResult);
	public void fault(String message);
}
