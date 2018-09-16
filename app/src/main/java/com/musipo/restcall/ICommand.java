package com.musipo.restcall;

import android.content.Context;


import com.musipo.constant.Constant;

import org.json.JSONObject;

import java.util.HashMap;


public interface ICommand
{
	public static final String TAG = ICommand.class.getSimpleName();
	public static final String params = "params";
	public static final String methodName = "methodName";
	public static final String serviceUrl = Constant.BASE_URL;

	public abstract void setContext(Context c);
	public abstract Context getContext();
	public abstract void execute(HashMap<String, String> data, IServiceListener listener, RequestMethod requestMethod);
	public abstract void execute(JSONObject data, IServiceListener listener, RequestMethod requestMethod);
//	public abstract void execute(JSONObject data, IServiceListener listener , String methodName);

}
