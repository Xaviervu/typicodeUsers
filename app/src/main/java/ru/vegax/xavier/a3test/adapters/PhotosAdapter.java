package ru.vegax.xavier.a3test.adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import ru.vegax.xavier.a3test.R;
import ru.vegax.xavier.a3test.models.UserPhoto;


public abstract class PhotosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Integer mAlbumId;
    private List<UserPhoto> mUserPhotos;
    private List<UserPhoto> mCurrUserPhotos;
    private Context mContext;
//    private final ImageDownloader imageDownloader = new ImageDownloader();

    protected PhotosAdapter(@NonNull Context context) {
        mContext = context;
    }

    @Override
    public int getItemCount() {
        if (mCurrUserPhotos != null) {
            return mCurrUserPhotos.size();
        }
        return 0;
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
        UserPhoto currentItem = mCurrUserPhotos.get(position);

        viewHolderUserPhotos.bindTo(currentItem);


        TextView textView = viewHolderUserPhotos.getTxtVPhoto();
        //disable swipe for the switch, only click on the item will trigger the output
        ImageView imageView = viewHolderUserPhotos.getImageView();
        final ProgressBar progressBar = viewHolderUserPhotos.getProgressBar();

        if (currentItem != null && progressBar != null && imageView != null) {
            progressBar.setVisibility(View.VISIBLE);
            textView.setTag(currentItem.getId()); // album Id
            textView.setOnClickListener(this);
            Glide.with(mContext)
                    .asBitmap()
                    .load(currentItem.getUrl())
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                           progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })

                    .into(imageView);

          //  imageDownloader.download(currentItem.getUrl(), imageView, progressBar);
        }


    }


    public void setUserPhotos(List<UserPhoto> userPhotos, Integer albumId) {
        mAlbumId = albumId;
        mUserPhotos = userPhotos;
        mCurrUserPhotos = new ArrayList<>();
        for (UserPhoto userPhoto : mUserPhotos) {
            if (userPhoto.getAlbumId() == mAlbumId) {
                mCurrUserPhotos.add(userPhoto);
            }
        }
        notifyDataSetChanged();
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
            if (currentItem != null) {
                mTxtVPhoto.setText(currentItem.getTitle());
            }
        }

        ImageView getImageView() {
            return mImageView;
        }

        TextView getTxtVPhoto() {
            return mTxtVPhoto;
        }

        ProgressBar getProgressBar() {
            return mProgressBar;
        }
    }
}
