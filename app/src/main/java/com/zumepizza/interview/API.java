package com.zumepizza.interview;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.zumepizza.interview.model.Pizza;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by jimmy on 1/31/18.
 */

/**
 * Note from Alex: not sure if the expectation was to use this method, but I'm assuming it was
 */

public class API {
    private Context context;

    public API(Context context) {
        this.context = context;
    }

    interface ResponseHandler {
        public void completion(Map<String, List<Pizza>> response);
    }

    public void fetchMenu(final ResponseHandler responseHandler) {
        RequestQueue queue = Volley.newRequestQueue(this.context);

        String url = "https://api.myjson.com/bins/snyji";

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        HashMap<String, List<Pizza>> map = new HashMap<>();

                        try {
                            JSONArray json = new JSONArray(response);

                            for (int i = 0; i < json.length(); i++) {
                                JSONObject object = json.getJSONObject(i);

                                map.putAll(toMap(object));
                            }
                            responseHandler.completion(map);
                        } catch (Exception e) {
                            Log.d("API", "JSON conversion error: " + e.getLocalizedMessage());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("API", "Menu fetch error: " + error.getLocalizedMessage());
            }
        });
        queue.add(request);
    }

    private static Map<String, List<Pizza>> toMap(JSONObject jsonobj)  throws JSONException {
        Map<String, List<Pizza>> map = new HashMap<String, List<Pizza>>();
        Iterator<String> keys = jsonobj.keys();
        while(keys.hasNext()) {
            String key = keys.next();
            List<Pizza> value = toList(jsonobj.getJSONArray(key));
            map.put(key, value);
        }   return map;
    }

    private static List<Pizza> toList(JSONArray array) throws JSONException {
        List<Pizza> list = new ArrayList<Pizza>();
        for(int i = 0; i < array.length(); i++) {
            Pizza value = new Gson().fromJson(array.getJSONObject(i).toString(), Pizza.class);
            list.add(value);
        }   return list;
    }
}
