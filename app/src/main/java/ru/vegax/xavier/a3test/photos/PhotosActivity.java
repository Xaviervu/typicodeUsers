package ru.vegax.xavier.a3test.photos;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import ru.vegax.xavier.a3test.R;
import ru.vegax.xavier.a3test.adapters.PhotosAdapter;
import ru.vegax.xavier.a3test.models.UserPhoto;
import ru.vegax.xavier.a3test.models.UserPhotoWrapper;

import static ru.vegax.xavier.a3test.albums.AlbumsActivity.EXTRA_ALBUM_ID;

public class PhotosActivity extends AppCompatActivity {
    private static final String TAG = "AlbumsActivity";

    private PhotosAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        PhotosActivityViewModel photosActivityViewModel = ViewModelProviders.of(this).get(PhotosActivityViewModel.class);
        photosActivityViewModel.init();





        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final Integer albumId = extras.getInt(EXTRA_ALBUM_ID);

            mAdapter = new PhotosAdapter(this) {


                @Override
                public void onClick(View v) {

                    if (v.getTag() != null) {
                        int photoId = (int) v.getTag();
                        Toast.makeText(getApplicationContext(),String.valueOf(photoId), Toast.LENGTH_SHORT).show();
                    }
                }

            };
            photosActivityViewModel.getUserPhotos().observe(this, new Observer<UserPhotoWrapper>() {
                @Override
                public void onChanged(UserPhotoWrapper userPhotos) {
                    if (userPhotos.getThrowable() == null){
                        mAdapter.setUserPhotos(userPhotos.getList(),albumId);
                        mAdapter.notifyDataSetChanged();

                    }else{
                        Toast.makeText(getApplicationContext(),userPhotos.getThrowable().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

            });
            initRecyclerView();
        }
    }
    private void initRecyclerView() {
        //Initialize the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerVPhotos);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //Set the Layout Manager
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(40));

        recyclerView.setAdapter(mAdapter);
    }
    public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int verticalSpaceHeight;

        VerticalSpaceItemDecoration(int verticalSpaceHeight) {
            this.verticalSpaceHeight = verticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(@NotNull Rect outRect, @NotNull View view, @NotNull RecyclerView parent,
                                   @NotNull RecyclerView.State state) {
            outRect.bottom = verticalSpaceHeight;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
