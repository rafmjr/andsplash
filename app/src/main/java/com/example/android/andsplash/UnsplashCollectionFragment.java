package com.example.android.andsplash;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

public class UnsplashCollectionFragment extends Fragment implements Response.Listener<JSONArray>,
    Response.ErrorListener {

    private static final String ARG_UNSPLASH_COLLECTION = "collection";

    private RecyclerView mRecyclerView;
    private CollectionAdapter mAdapter;
    private ProgressBar mProgressBar;
    private TextView mErrorTextView;

    private String mUrl;
    private String mEndpoint;

    public static UnsplashCollectionFragment newInstance(String endPoint) {
        UnsplashCollectionFragment instance = new UnsplashCollectionFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_UNSPLASH_COLLECTION, endPoint);
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

        // Initialize RecyclerView
        initRecyclerView();
        // Define api endpoint from which the images will come
        setEndpointFromArguments();
        // Fetch Collections
        fetchCollections();

        return view;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        hideProgressBar();
        showErrorMessage();
    }

    @Override
    public void onResponse(JSONArray response) {
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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void setEndpointFromArguments() {
        mEndpoint = getString(R.string.unsplash_collection_default);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mEndpoint = arguments.getString(ARG_UNSPLASH_COLLECTION);
        }
        mUrl = getString(R.string.unsplash_url) + mEndpoint;
    }

    public void fetchCollections() {
        Context context = getContext();
        JsonArrayRequest jsonObjectRequest = new UnsplashCollectionRequest(
                mUrl,
                this,
                this,
                context
        );
        StaticRequestQueue.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public UnsplashCollection[] getCollectionArrayFromResponse(JSONArray response) throws JSONException {
        UnsplashCollection[] collections = new UnsplashCollection[response.length()];
        for (int i = 0; i < collections.length; i++) {
            collections[i] = new UnsplashCollection(response.getJSONObject(i));
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
