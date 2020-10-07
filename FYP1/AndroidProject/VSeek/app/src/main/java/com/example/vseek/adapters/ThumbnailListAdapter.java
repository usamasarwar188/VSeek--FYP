package com.example.vseek.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vseek.R;
import com.example.vseek.models.Video;

import java.io.IOException;
import java.util.ArrayList;

public class ThumbnailListAdapter extends RecyclerView.Adapter<ThumbnailListAdapter.ViewHolder> {

    ArrayList<Video> videoArrayList;
    Context context;
    OnThumbnailClickListener onThumbnailClickListener;
    OnPlayBtnClickListener onPlayBtnClickListener;
    public ThumbnailListAdapter(ArrayList<Video> videoArrayList, Context context, OnThumbnailClickListener onThumbnailClickListener, OnPlayBtnClickListener onPlayBtnClickListener){
        this.videoArrayList=videoArrayList;
        this.context=context;
        this.onThumbnailClickListener=onThumbnailClickListener;
        this.onPlayBtnClickListener=onPlayBtnClickListener;
    }

    @NonNull
    @Override
    public ThumbnailListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.thumbnail_list_item,parent,false);
        ViewHolder vh=new ViewHolder(view,onThumbnailClickListener,onPlayBtnClickListener);
        return vh;    }

    @Override
    public void onBindViewHolder(@NonNull ThumbnailListAdapter.ViewHolder holder, int position) {



        holder.setView(position);

    }

    @Override
    public int getItemCount() {
        return videoArrayList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

        TextView imageName;
        ImageView thumbnail;
        ProgressBar loadingLabel;
        ImageView successLabel;
        OnThumbnailClickListener onThumbnailClickListener;
        OnPlayBtnClickListener onPlayBtnClickListener;
        public ViewHolder(@NonNull View itemView,OnThumbnailClickListener onThumbnailClickListener,
                          OnPlayBtnClickListener onPlayBtnClickListener) {
            super(itemView);
            //imageName=itemView.findViewById(R.id.image_name);
            thumbnail=itemView.findViewById(R.id.thumbnail);
            loadingLabel=itemView.findViewById(R.id.label_loading);
            successLabel=itemView.findViewById(R.id.label_success);
            itemView.setOnClickListener(this);
            this.onThumbnailClickListener=onThumbnailClickListener;
            this.onPlayBtnClickListener= onPlayBtnClickListener;
        }

        public void setView(int position){
            //imageName.setText(videoArrayList.get(position).getName());
            Glide.with(context)
                    .asBitmap().fitCenter().centerCrop()
                    .load(videoArrayList.get(position).getUri()) // or URI/path
                    .into(thumbnail);

        }

        @Override
        public void onClick(View view) {

                PopupMenu popupMenu = new PopupMenu(view.getContext(),view);
                popupMenu.inflate(R.menu.video_options);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(this);
               // this.onThumbnailClickListener.onThumbnailClick(getAdapterPosition());

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId()){
                case R.id.play_btn:
                    this.onPlayBtnClickListener.onPlayClick(getAdapterPosition());
                    return true;
                case R.id.upload_btn:
                    try {
                        this.onThumbnailClickListener.onThumbnailClick(getAdapterPosition());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return true;
                default:
                    return false;
            }
        }
    }

    public interface OnThumbnailClickListener{

        void onThumbnailClick(int position) throws IOException;
    }

    public interface OnPlayBtnClickListener{
        void onPlayClick(int position);
    }


}
