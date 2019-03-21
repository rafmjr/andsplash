package com.example.android.andsplash;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class UnsplashCollectionRequest extends JsonArrayRequest {

    private Context mContext;

    UnsplashCollectionRequest(String url, Response.Listener<JSONArray> listener, @Nullable Response.ErrorListener errorListener, Context context) {
        super(url, listener, errorListener);
        mContext = context;
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
