package com.example.android.andsplash;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URL;

public class CollectionFragment extends Fragment implements Response.Listener<JSONObject>,
    Response.ErrorListener {

    private static final String ARG_QUERY_TERM = "collection";

    private RecyclerView mRecyclerView;
    private CollectionAdapter mAdapter;
    private ProgressBar mProgressBar;
    private TextView mErrorTextView;

    private String mUrl;

    public static CollectionFragment newInstance(String endPoint) {
        CollectionFragment instance = new CollectionFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_QUERY_TERM, endPoint);
        instance.setArguments(bundle);
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.collection_fragment, container, false);
        mProgressBar = view.findViewById(R.id.progressBar);
        mRecyclerView = view.findViewById(R.id.collection_rv);
        mErrorTextView = view.findViewById(R.id.error_tv);

        try {
            // Define api endpoint from which the images will come
            setEndpointFromArguments();
            // Initialize RecyclerView
            initRecyclerView();
            // Fetch Collections
            fetchCollections();
        } catch (Exception e) {
            showErrorMessage();
        }

        return view;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        hideProgressBar();
        showErrorMessage();
    }

    @Override
    public void onResponse(JSONObject response) {
        hideProgressBar();

        if (response.length() == 0) {
            showErrorMessage();
            return;
        }

        try {
            UnsplashCollection[] collectionsArray = getCollectionArrayFromResponse(response);
            mAdapter.swapCollection(collectionsArray);
        } catch (JSONException e) {
            showErrorMessage();
        }
    }

    public void initRecyclerView() {
        mAdapter = new CollectionAdapter(null);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    public void setEndpointFromArguments() throws Exception {
        Bundle arguments = getArguments();
        if (arguments == null) {
            throw new Exception("Fragment started without setting arguments");
        }

        String mEndpoint = arguments.getString(ARG_QUERY_TERM);
        Uri unsplashUri = new Uri.Builder()
                .scheme("https")
                .authority(getString(R.string.api_endpoint))
                .appendPath("search")
                .appendPath("photos")
                .appendQueryParameter("query", mEndpoint)
                .appendQueryParameter("orientation", "portrait")
                .build();

        mUrl = unsplashUri.toString();
    }

    public void fetchCollections() {
        Context context = getContext();
        UnsplashCollectionRequest jsonObjectRequest = new UnsplashCollectionRequest(
                mUrl,
                null,
                this,
                this,
                context
        );
        StaticRequestQueue.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public UnsplashCollection[] getCollectionArrayFromResponse(JSONObject response) throws JSONException {
        JSONArray results = response.getJSONArray("results");

        UnsplashCollection[] collections = new UnsplashCollection[results.length()];
        for (int i = 0; i < collections.length; i++) {
            collections[i] = new UnsplashCollection(results.getJSONObject(i));
        }

        return collections;
    }

    public void hideProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    public void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorTextView.setVisibility(View.VISIBLE);
    }
}
