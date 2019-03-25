package com.example.android.andsplash;

import org.json.JSONException;
import org.json.JSONObject;

class UnsplashCollection {
    private String description;
    private String coverURL;

    UnsplashCollection(JSONObject payload) throws JSONException {
        description = payload.getString("description");
        coverURL = payload.getJSONObject("urls").getString("regular");
    }

    String getDescription() { return description; }
    String getCoverURL() {
        return coverURL;
    }
}
