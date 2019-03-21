package com.example.android.andsplash;

import org.json.JSONException;
import org.json.JSONObject;

class UnsplashCollection {
    private String title;
    private String coverURL;

    UnsplashCollection(JSONObject payload) throws JSONException {
        title = payload.getString("title");
        coverURL = payload.getJSONObject("cover_photo")
                .getJSONObject("urls")
                .getString("regular");
    }

    String getTitle() {
        return title;
    }

    String getCoverURL() {
        return coverURL;
    }
}
