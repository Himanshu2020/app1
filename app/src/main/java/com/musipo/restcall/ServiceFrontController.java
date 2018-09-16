package com.musipo.restcall;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.data;

public class ServiceFrontController {
	private static ServiceFrontController instance;
	private Context _context;
	private static HashMap<String, Class<? extends ICommand>> _map;

	private ServiceFrontController() {

	}

	public static synchronized ServiceFrontController getInstance() {
		if (instance == null) {
			instance = new ServiceFrontController();
			_map = new HashMap<String, Class<? extends ICommand>>();
		}
		return instance;
	}

	public void addMethod(String type, Class<? extends ICommand> command) {
		_map.put(type, command);
	}

	public void fireCommand(Context context, String type, HashMap<String, String> paramMap,
							IServiceListener listener, RequestMethod requestMethod) throws Exception {

		if (paramMap == null) {

			paramMap = new HashMap<String, String>();
		}
		paramMap.put("type", type);

		Class<? extends ICommand> cls = _map.get(type);
		if (cls != null) {

			ICommand tmpObj = cls.newInstance();
			tmpObj.setContext(context);
			tmpObj.execute(paramMap, listener,requestMethod);
		}

	}

	public void fireCommand(Context context, String type, JSONObject data,
							IServiceListener listener, RequestMethod requestMethod) throws Exception {

		if (data == null) {

			data = new JSONObject();
		}
		data.put("type", type);

		Class<? extends ICommand> cls = _map.get(type);
		if (cls != null) {

			ICommand tmpObj = cls.newInstance();
			tmpObj.setContext(context);
			tmpObj.execute(data, listener,requestMethod);
		}

	}

	public void result(String jsonResult) {
		Toast.makeText(_context, jsonResult, Toast.LENGTH_SHORT).show();
	}

	public void fault(String message) {
		Toast.makeText(_context, message, Toast.LENGTH_SHORT).show();
	}

}
