package com.musipo.restcall;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Request implements ICommand {
    private IServiceListener _listener;
    private String _type;
    RequestMethod _requestMethod;

    @Override
    public void setContext(Context c) {

    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void execute(HashMap<String, String> data, IServiceListener listener, RequestMethod requestMethod) {
        _listener = listener;
        _requestMethod = requestMethod;
        _type = data.get("type");
        data.remove("type");
        AsyncServiceTaskMap task = new AsyncServiceTaskMap();
        task.execute(data);
    }

    @Override
    public void execute(JSONObject data, IServiceListener listener, RequestMethod requestMethod) {
        _listener = listener;
        _requestMethod = requestMethod;
        try {
            _type = data.getString("type");
            data.remove("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsyncServiceTask task = new AsyncServiceTask();
        task.execute(data);
    }


    private class AsyncServiceTask extends AsyncTask<JSONObject, Integer, JSONObject> {

        @Override
        protected JSONObject doInBackground(JSONObject... params) {
            return request(params[0]);
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);

            if (result != null) {
                try {
                    _listener.result(result.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    _listener.fault(e.getMessage());
                }
            }
        }

    }

    private class AsyncServiceTaskMap extends AsyncTask<HashMap<String, String>, Integer, JSONObject> {


        @Override
        protected JSONObject doInBackground(HashMap<String, String>... params) {
            return requestMap(params[0]);
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);

            if (result != null) {
                try {
                    _listener.result(result.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    _listener.fault(e.getMessage());
                }
            }
        }

    }

    private JSONObject requestMap(HashMap<String, String> mapData) {
        JSONObject jObj = null;
        try {
            String url = serviceUrl + mapData.get(methodName);
            mapData.remove(methodName);
            RestClient client = new RestClient(url);

            Log.d(TAG, "url \n" + url);

            if (_requestMethod.equals(RequestMethod.GET)) {
            //    client.addParam("rest_data", mapData.get(params));
                for (Map.Entry<String, String> entry : mapData.entrySet()) {
                    client.addParam(entry.getKey(), entry.getValue());
                }

            } else if (_requestMethod.equals(RequestMethod.POST)) {
                for (Map.Entry<String, String> entry : mapData.entrySet()) {
                    client.addParam(entry.getKey(), entry.getValue());
                }
            }

            client.execute(_requestMethod);

            if (client.getResponseCode() != 200) {
                jObj = new JSONObject();
                jObj.put("ResponseCode", client.getResponseCode());
                jObj.put("Message", client.getErrorMessage());
            } else {

                jObj = new JSONObject();
                jObj.put("ResponseString", client.getResponse());
                Log.e("String", client.getResponse());
                jObj.put("ResponseCode", client.getResponseCode());
            }

            jObj.put("type", _type);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jObj;
    }

    private JSONObject request(JSONObject data) {
        JSONObject jObj = null;
        try {
            String url = serviceUrl + data.getString(methodName);

            RestClient client = new RestClient(url);

            Log.d(TAG, "url \n" + url);

           /* client.addParam("method", _type);
            client.addParam("response_type", "json");
            client.addParam("input_type", "json");*/

            Log.d("REST DATA", data + "");

            if (_requestMethod.equals(RequestMethod.GET)) {
                client.addParam("rest_data", data.getString(params));
            } else if (_requestMethod.equals(RequestMethod.POST)) {
                client.addParam("rest_data", data.getString(params));
            }

            Log.d(TAG, "rest_data \n" + data.getString(params));

            client.execute(_requestMethod);

            if (client.getResponseCode() != 200) {
                jObj = new JSONObject();
                jObj.put("ResponseCode", client.getResponseCode());
                jObj.put("Message", client.getErrorMessage());
            } else {

                jObj = new JSONObject();
                jObj.put("ResponseString", client.getResponse());
                Log.e("String", client.getResponse());
                jObj.put("ResponseCode", client.getResponseCode());
            }

            jObj.put("type", _type);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jObj;
    }

}
