package ru.vegax.xavier.a3test.photos;

import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import ru.vegax.xavier.a3test.R;
import ru.vegax.xavier.a3test.data_loader.PhotoLoader;
import ru.vegax.xavier.a3test.user_data.UserPhoto;


import static ru.vegax.xavier.a3test.albums.AlbumsActivity.EXTRA_ALBUM_ID;

public class PhotosActivity extends AppCompatActivity {
    private static final String TAG = "AlbumsActivity";

    private PhotosAdapter mAdapter;
    private PhotoLoader mPhotoLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        //Initialize the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerVPhotos);

        //Set the Layout Manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(40));
        ArrayList<UserPhoto> adapterList = new ArrayList<>();
        LinkedHashMap<Integer, ArrayList<UserPhoto>> albums = new LinkedHashMap<>();
        mAdapter = new PhotosAdapter(this, adapterList) {


            @Override
            public void onClick(View v) {

                if (v.getTag() != null) {
                    int photoId = (int) v.getTag();
                   Toast.makeText(getApplicationContext(),String.valueOf(photoId), Toast.LENGTH_SHORT).show();
                }
            }

        };

        recyclerView.setAdapter(mAdapter);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final Integer albumId = extras.getInt(EXTRA_ALBUM_ID);
            mPhotoLoader = new PhotoLoader(albums) {
                @Override
                public void notifyDataLoaded() {
                    ArrayList<UserPhoto> photoList = mPhotos.get(albumId);
                    if (photoList!= null) {
                        mAdapter.setAlbumList(photoList);
                        mAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void notifyError(String e) {
                    Toast.makeText(getApplicationContext(), getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "notifyError: "+e);
                }
            };
        }


    }
    @Override
    protected void onStop() {
        if (mPhotoLoader != null){
            mPhotoLoader.cancelTasks();
        }
        super.onStop();
    }
    public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int verticalSpaceHeight;

        public VerticalSpaceItemDecoration(int verticalSpaceHeight) {
            this.verticalSpaceHeight = verticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = verticalSpaceHeight;
        }
    }
}
