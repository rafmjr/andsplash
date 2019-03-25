package com.example.android.andsplash;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UnsplashCollectionRequest extends JsonObjectRequest {

    private Context mContext;

    public UnsplashCollectionRequest(String url, @Nullable JSONObject jsonRequest, Response.Listener<JSONObject> listener, @Nullable Response.ErrorListener errorListener, Context mContext) {
        super(url, jsonRequest, listener, errorListener);
        this.mContext = mContext;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        String clientId = mContext.getString(R.string.unsplash_client_id);

        HashMap headers = new HashMap();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Client-ID " + clientId);

        return headers;
    }
}
