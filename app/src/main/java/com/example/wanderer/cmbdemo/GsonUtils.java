package com.example.wanderer.cmbdemo;


import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * @author wyj
 * @time 2016/6/27 0027
 * @do GSON解析
 */

public final class GsonUtils {

	public static <T> T parseJSON(String json, Class<T> clazz) {
		Gson gson = new Gson();
		T info = gson.fromJson(json, clazz);
		return info;
	}

	/**
	 * Type type = new
	 TypeToken<ArrayList<TypeInfo>>(){}.getType();
	 <br>Type所在的包：java.lang.reflect
	 <br>TypeToken所在的包：com.google.gson.reflect.TypeToken
	 * @param
	 * @param type
	 * @return
	 */
	public static <T> T parseJSONArray(String jsonArr, Type type) {
		Gson gson = new Gson();
		T infos = gson.fromJson(jsonArr, type);
		return infos;
	}



	private GsonUtils(){}
}