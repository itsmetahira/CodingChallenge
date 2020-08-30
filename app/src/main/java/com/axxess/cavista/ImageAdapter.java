package com.axxess.cavista;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private Context mContext;
    private List<ImageModel> mList;
    private ImageItemClickListener mListener;

    public ImageAdapter(Context context, List<ImageModel> list, ImageItemClickListener listener) {
        this.mContext = context;
        this.mList = list;
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_grid_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ImageModel image = mList.get(position);

        if (image != null) {
            //Load the Image from the Url using Picasso library
            Picasso.get().load(image.getImageUrl()).into(holder.ivImage);

            if(holder.ivImage != null) {
                holder.ivImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onItemClick(v, image);
                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivImage;

        public ViewHolder(View itemView) {
            super(itemView);

            ivImage = itemView.findViewById(R.id.iv_image);
        }
    }
}
