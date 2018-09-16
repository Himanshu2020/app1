package com.musipo.restcall;

import android.util.Log;


import com.musipo.constant.Constant;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.musipo.model.json.InputJson;
import com.musipo.model.json.ListOutputJson;
import com.musipo.model.json.OutputJson;
import com.musipo.model.json.ResponceJson;
import com.musipo.model.json.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class JsonController<T> {

    private static final String TAG = JsonController.class.getSimpleName();

    private InputJson<T> inputJson;
    private ListOutputJson<T> outputJson;
    private T t;
    private List<T> objs;
    private Gson gson;

    public JsonController() {
        initGson();
    }

    public InputJson<T> getInputJson() {
        return inputJson;
    }

    public void setInputJson(InputJson<T> inputJson) {
        this.inputJson = inputJson;
    }

    public ListOutputJson<T> getOutputJson() {
        return outputJson;
    }

    public void setOutputJson(ListOutputJson<T> outputJson) {
        this.outputJson = outputJson;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public List<T> getObjs() {
        return objs;
    }

    public void setObjs(List<T> objs) {
        this.objs = objs;
    }

    private void initGson() {
        gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setFieldNamingPolicy(
                        FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setPrettyPrinting().serializeNulls().create();
    }

    /**
     * @param
     * @return
     * @throws JSONException
     */
    public JSONObject processInputJson(T obj) throws JSONException {

        InputJson<T> inputJson = new InputJson<T>();
        inputJson.setParams(obj);
        inputJson.setApiKey(Constant.API_KEY);

        Log.d(TAG, "Input json \n" + gson.toJson(inputJson).toString());

        return new JSONObject(gson.toJson(inputJson));
    }

    /**
     * @param jsonString
     * @param outputJson
     * @return model bill_list eg. customer bill_list, plan bill_list, etc.
     * @throws JSONException
     */
    @SuppressWarnings("unchecked")
    public List<T> processOutputJson(String jsonString, ListOutputJson<T> outputJson)
            throws JSONException {

        Gson gson = new Gson();
        gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setFieldNamingPolicy(
                        FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setPrettyPrinting().serializeNulls().create();
        Log.d(TAG, "Output json \n" + jsonString);
        outputJson = gson.fromJson(jsonString, ListOutputJson.class);

        List<T> models = outputJson.getData();
        String status = outputJson.getStatus();
        String message = outputJson.getMessage();

        Log.d(TAG, "Output json \n status " + status + " message " + message
                + " data \n " + models);

        return models;
    }


    /**
     * @param jsonString
     * @return outputJson
     * @throws JSONException
     */
    @SuppressWarnings("unchecked")
    public ListOutputJson<T> getListOutputJsonFromString(String jsonString, Class<T> dataClass,
                                                         Type listType, List<T> dataList
    ) {

        Gson gson = new Gson();
        gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setFieldNamingPolicy(
                        FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setPrettyPrinting().serializeNulls().create();

        ListOutputJson<T> listOutputJson = gson.fromJson(jsonString, ListOutputJson.class);
        JSONObject dataObj;

        try {
            dataObj = new JSONObject(jsonString);
            String dataJsonString = dataObj.getString("data");
            dataList = gson.fromJson(dataJsonString, listType);

            listOutputJson.setData(dataList);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return listOutputJson;
    }

    /**
     * @param jsonString
     * @return outputJson
     * @throws JSONException
     */
    @SuppressWarnings("unchecked")
    public ListOutputJson<T> getListOutputJsonFromString(String jsonString, Class<T> dataClass) throws JSONException {

        Log.d(TAG, "Output json \n" + jsonString);
        ListOutputJson<T> listOutputJson = gson.fromJson(jsonString, ListOutputJson.class);
        JSONObject dataObj;
        try {
            dataObj = new JSONObject(jsonString);
            String dataJsonString = dataObj.getString("data");
            List<T> data = gson.fromJson(dataJsonString, new TypeToken<T>() {
            }.getType());
            listOutputJson.setData(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return listOutputJson;
    }


    /**
     * @param jsonString
     * @return outputJson
     * @throws JSONException
     */
    @SuppressWarnings("unchecked")
    public OutputJson<T> getOutputJsonFromString(String jsonString, Class<T> dataClass) {

        Log.d(TAG, "Output json \n" + jsonString);

        OutputJson<T> outputJson = gson.fromJson(jsonString, OutputJson.class);
        JSONObject dataObj;
        try {
            dataObj = new JSONObject(jsonString);
            String dataJsonString = dataObj.getString("data");
            T data = gson.fromJson(dataJsonString, dataClass);
            outputJson.setData(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return outputJson;
    }

//	/**
//	 * @param jsonString
//	 * @param outputJson
//	 * @return sugarcrmIds
//	 * @throws JSONException
//	 */
//	@SuppressWarnings("unchecked")
//	public List<String> processOutputJson(String jsonString)
//			throws JSONException {
//
//		Gson gson = new Gson();
//		gson = new GsonBuilder()
//				.disableHtmlEscaping()
//				.setFieldNamingPolicy(
//						FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
//				.setPrettyPrinting().serializeNulls().create();
//
//		Log.d(TAG, "Output json \n" + jsonString);
//		OutputJson<String> outputJson = gson.fromJson(jsonString,
//				OutputJson.class);
//
//		List<String> sugarcrmIds = outputJson.getData();
//		String status = outputJson.getStatus();
//		String message = outputJson.getMessage();
//
//		Log.d(TAG, "Output json \n status " + status + " message " + message
//				+ " data \n " + sugarcrmIds);
//
//		return sugarcrmIds;
//	}

    /**
     * @param jsonString
     * @param
     * @return sugarcrmIds
     * @throws JSONException
     */
    @SuppressWarnings("unchecked")
    public List<T> processOutputJson(String jsonString)
            throws JSONException {

        Gson gson = new Gson();
        gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setFieldNamingPolicy(
                        FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setPrettyPrinting().serializeNulls().create();

        Log.d(TAG, "Output json \n" + jsonString);
        ListOutputJson<T> outputJson = gson.fromJson(jsonString,
                ListOutputJson.class);

        List<T> models = outputJson.getData();
        String status = outputJson.getStatus();
        String message = outputJson.getMessage();

        Log.d(TAG, "Output json \n status " + status + " message " + message
                + " data \n " + models);

        return models;
    }


    public static Response getResponse(String jsonString) {
        Gson gson = new Gson();
        gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setFieldNamingPolicy(
                        FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setPrettyPrinting().serializeNulls().create();

        return gson.fromJson(jsonString, Response.class);
    }

    public T getModelFromData(String jsonString, Class<T> dataClass) {
        Gson gson = new Gson();
        gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setFieldNamingPolicy(
                        FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setPrettyPrinting().serializeNulls().create();

        return gson.fromJson(jsonString, dataClass);
    }

    public static ResponceJson getRespnceJson(String jsonString) {
        ResponceJson responceJson = new ResponceJson();
        Log.i(TAG, "jsonString :" + jsonString);
        try {

            JSONObject jsonObj = new JSONObject(jsonString);
            if(jsonObj.getInt("ResponseCode")==200) {
                String jsonStr = jsonObj.getString("ResponseString");
                JSONObject jsonResponce = new JSONObject(jsonStr);

                responceJson.setData(new JSONObject(jsonResponce.getString("DATA")));
                responceJson.setMessage(jsonResponce.getString("MESSAGE"));
                responceJson.setStatus(jsonResponce.getBoolean("STATUS"));
            }else{
                Log.e(TAG, "Server ERROR"+jsonObj.getString("ResponseString"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        Log.i(TAG, "ResponceJson:" + responceJson);
        return responceJson;
    }

    public static ResponceJson  getRespnceJsonWithDataAsString(String jsonString) {
        ResponceJson responceJson = new ResponceJson();
        Log.i(TAG, "jsonString :" + jsonString);
        try {

            JSONObject jsonObj = new JSONObject(jsonString);
            if(jsonObj.getInt("ResponseCode")==200) {
                String jsonStr = jsonObj.getString("ResponseString");
                JSONObject jsonResponce = new JSONObject(jsonStr);

                responceJson.setDataString(jsonResponce.getString("DATA"));
                responceJson.setMessage(jsonResponce.getString("MESSAGE"));
                responceJson.setStatus(jsonResponce.getBoolean("STATUS"));
            }else{
                Log.e(TAG, "Server ERROR"+jsonObj.getString("ResponseString"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        Log.i(TAG, "ResponceJson:" + responceJson);
        return responceJson;
    }

    public static final JSONObject getDataJsonObject(String responseString) {

        JSONObject rjson = null;
        JSONObject dataObj = null;
        try {

            rjson = new JSONObject(responseString);
            dataObj = new JSONObject(rjson.getString("data"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataObj;
    }

    public static final String getDataString(String responseString) {
        String data = null;
        JSONObject rjson = null;
        try {
            rjson = new JSONObject(responseString);
            data = rjson.getString("data");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static final String getStatus(String responseString) {
        String status = null;
        JSONObject rjson = null;
        try {
            rjson = new JSONObject(responseString);
            status = rjson.getString("status");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return status;
    }

    public static final String getMessage(String responseString) {
        String message = null;
        JSONObject rjson = null;
        try {
            rjson = new JSONObject(responseString);
            message = rjson.getString("message");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }

    public static final String getSyncStatus(String responseString) {
        String status = null;
        try {
            JSONObject rjson = new JSONObject(responseString);
            status = rjson.getString("status");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return status;
    }

    public static final String getValue(String responseString,String key) {
        String value = null;
        try {
            JSONObject rjson = new JSONObject(responseString);
             value = rjson.getString(key);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }

}
