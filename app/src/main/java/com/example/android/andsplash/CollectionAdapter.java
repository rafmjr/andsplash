package com.example.android.andsplash;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.CollectionsViewHolder> {
    private UnsplashCollection[] collections;

    CollectionAdapter(UnsplashCollection[] collections) {
        this.collections = collections;
    }

    @NonNull
    @Override
    public CollectionsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.collection_list_item, viewGroup, false);
        return new CollectionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionsViewHolder holder, int position) {
        holder.bind(collections[position]);
    }

    @Override
    public int getItemCount() {
        if (collections == null) return 0;

        return collections.length;
    }

    void swapCollection(UnsplashCollection[] newCollections) {
        collections = newCollections;
        notifyDataSetChanged();
    }

    class CollectionsViewHolder extends RecyclerView.ViewHolder implements Response.Listener<Bitmap> {
        private ImageView mCoverView;
        private Context mContext;

        CollectionsViewHolder(@NonNull View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            mCoverView = itemView.findViewById(R.id.collection_cover_iv);
        }

        void bind(UnsplashCollection collection) {
            String coverURL = collection.getCoverURL();
            ImageRequest imageRequest = new ImageRequest(
                    coverURL,
                    this,
                    mCoverView.getWidth(),
                    mCoverView.getHeight(),
                    mCoverView.getScaleType(),
                    Bitmap.Config.RGB_565,
                    null
            );
            StaticRequestQueue.getInstance(mContext).addToRequestQueue(imageRequest);
        }

        @Override
        public void onResponse(Bitmap response) {
            mCoverView.setImageBitmap(response);
        }
    }
}
