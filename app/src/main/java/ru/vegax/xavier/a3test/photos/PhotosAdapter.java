package ru.vegax.xavier.a3test.photos;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.vegax.xavier.a3test.R;
import ru.vegax.xavier.a3test.data_loader.ImageDownloader;
import ru.vegax.xavier.a3test.user_data.UserPhoto;


public abstract class PhotosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private  List<UserPhoto> mUserPhotos;
    private Context mContext;
    private final ImageDownloader imageDownloader = new ImageDownloader();

    PhotosAdapter(@NonNull Context context, @NonNull List<UserPhoto> userPhotos) {
        mUserPhotos = userPhotos;
        mContext = context;
    }

    @Override
    public int getItemCount() {

        return mUserPhotos.size();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolderUserPhotos(LayoutInflater.from(mContext).inflate(R.layout.list_item_photos, parent, false));


    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ViewHolderUserPhotos viewHolderUserPhotos = (ViewHolderUserPhotos) holder;
        //Get current item
        UserPhoto currentItem = mUserPhotos.get(position);

        viewHolderUserPhotos.bindTo(currentItem);


        TextView textView = viewHolderUserPhotos.getTxtVPhoto();
        //disable swipe for the switch, only click on the item will trigger the output
        ImageView imageView  = viewHolderUserPhotos.getImageView();
        ProgressBar progressBar = viewHolderUserPhotos.getProgressBar();

        if (currentItem != null && progressBar != null && imageView != null) {
            textView.setTag(currentItem.getId()); // album Id
            textView.setOnClickListener(this);
            imageDownloader.download(currentItem.getUrl(),imageView, progressBar);
        }


    }

    public void setAlbumList(@NonNull ArrayList<UserPhoto> userAlbums){
        mUserPhotos = userAlbums;
    }

    class ViewHolderUserPhotos extends RecyclerView.ViewHolder {

        private final ProgressBar mProgressBar;



        private final ImageView mImageView;
        private TextView mTxtVPhoto;




        ViewHolderUserPhotos(View itemView) {
            super(itemView);
            //Initialize the views
            mTxtVPhoto = itemView.findViewById(R.id.txtVUser);
            mImageView = itemView.findViewById(R.id.imageView);
            mProgressBar = itemView.findViewById(R.id.progressBar);
        }

        void bindTo(UserPhoto currentItem) {
            if (currentItem!= null){
                mTxtVPhoto.setText(currentItem.getTitle());
            }
        }
        public ImageView getImageView() {
            return mImageView;
        }

        public TextView getTxtVPhoto() {
            return mTxtVPhoto;
        }
        public ProgressBar getProgressBar() {
            return mProgressBar;
        }
    }
}
