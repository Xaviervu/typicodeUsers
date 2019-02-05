package ru.vegax.xavier.a3test.albums;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import ru.vegax.xavier.a3test.R;
import ru.vegax.xavier.a3test.adapters.AlbumsAdapter;
import ru.vegax.xavier.a3test.models.UserAlbumWrapper;
import ru.vegax.xavier.a3test.photos.PhotosActivity;
import ru.vegax.xavier.a3test.models.UserAlbum;

import static ru.vegax.xavier.a3test.main.MainActivity.EXTRA_NAME_ID;

public class AlbumsActivity extends AppCompatActivity {
    private static final String TAG = "AlbumsActivity";
    public static final String EXTRA_ALBUM_ID = "EXTRA_ALBUM_ID";
    private AlbumsAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);

        final AlbumsActivityViewModel albumsActivityViewModel = ViewModelProviders.of(this).get(AlbumsActivityViewModel.class);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final int userId = extras.getInt(EXTRA_NAME_ID);
            albumsActivityViewModel.init();
            mAdapter = new AlbumsAdapter(this) {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), PhotosActivity.class);
                    if (v.getTag() != null) {
                        int albumId = (int) v.getTag();
                        intent.putExtra(EXTRA_ALBUM_ID, albumId);
                        startActivity(intent);
                    }
                }

            };
              albumsActivityViewModel.getUserAlbums().observe(this, new Observer<UserAlbumWrapper>() {
                @Override
                public void onChanged(UserAlbumWrapper userAlbums) {
                    if (userAlbums.getThrowable() == null){
                        mAdapter.setUserList(userAlbums.getList(), userId);
                    }else{
                        Toast.makeText(getApplicationContext(),userAlbums.getThrowable().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });

            initRecyclerView();
        }
    }
    private void initRecyclerView() {
        //Initialize the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerVAlbums);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //Set the Layout Manager
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
    }
}
