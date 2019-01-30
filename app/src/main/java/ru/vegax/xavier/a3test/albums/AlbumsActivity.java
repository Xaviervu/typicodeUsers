package ru.vegax.xavier.a3test.albums;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import ru.vegax.xavier.a3test.R;
import ru.vegax.xavier.a3test.data_loader.AlbumLoader;
import ru.vegax.xavier.a3test.photos.PhotosActivity;
import ru.vegax.xavier.a3test.user_data.UserAlbum;
import ru.vegax.xavier.a3test.user_data.UserPhoto;

import static ru.vegax.xavier.a3test.main.MainActivity.EXTRA_NAME_ID;

public class AlbumsActivity extends AppCompatActivity {
    private static final String TAG = "AlbumsActivity";
    public static final String EXTRA_ALBUM_ID = "EXTRA_ALBUM_ID";
    private static final int PHOTOS_REQUEST_ID = 2;
    private AlbumsAdapter mAdapter;
    private AlbumLoader mAlbumLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);


       //Initialize the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerVAlbums);

        //Set the Layout Manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<UserAlbum> adapterList = new ArrayList<>();
        LinkedHashMap<Integer, List<UserAlbum>> albums = new LinkedHashMap<>();
        mAdapter = new AlbumsAdapter(this, adapterList) {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PhotosActivity.class);
                if (v.getTag() != null) {
                    int albumId = (int) v.getTag();
                    intent.putExtra(EXTRA_ALBUM_ID, albumId);
                    startActivityForResult(intent, PHOTOS_REQUEST_ID);
                }
            }

        };

        recyclerView.setAdapter(mAdapter);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final Integer userId = extras.getInt(EXTRA_NAME_ID);
            mAlbumLoader = new AlbumLoader(albums) {
                @Override
                public void notifyDataLoaded() {
                    mAdapter.setAlbumList((ArrayList<UserAlbum>) Objects.requireNonNull(mAlbums.get(userId)));
                    mAdapter.notifyDataSetChanged();
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
        if (mAlbumLoader != null){
            mAlbumLoader.cancelTasks();
        }
        super.onStop();
    }
}
