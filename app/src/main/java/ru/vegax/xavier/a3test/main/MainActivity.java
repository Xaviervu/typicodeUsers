package ru.vegax.xavier.a3test.main;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.LinkedHashMap;

import ru.vegax.xavier.a3test.albums.AlbumsActivity;
import ru.vegax.xavier.a3test.R;
import ru.vegax.xavier.a3test.data_loader.UserLoader;
import ru.vegax.xavier.a3test.user_data.User;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String EXTRA_NAME_ID = "EXTRA_NAME_ID";
    private static final int ALBUMS_REQUEST_ID = 1;
    private UserAdapter mAdapter;
    private UserLoader mUserLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initialize the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerVUsers);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //Set the Layout Manager
        recyclerView.setLayoutManager(layoutManager);


        LinkedHashMap<Integer, User> users = new LinkedHashMap<>();
        mAdapter = new UserAdapter(this, users) {


            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AlbumsActivity.class);
                if (v.getTag() != null) {
                    int userId = (int) v.getTag();
                    intent.putExtra(EXTRA_NAME_ID, userId);
                    startActivityForResult(intent, ALBUMS_REQUEST_ID);
                }
            }

        };

        recyclerView.setAdapter(mAdapter);

        mUserLoader = new UserLoader(users) {
            @Override
            public void notifyDataLoaded() {

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void notifyError(String e) {
                Toast.makeText(getApplicationContext(), getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "notifyError: "+e);
            }
        };

    }

    @Override
    protected void onStop() {
        if (mUserLoader!= null){
            mUserLoader.cancelTasks();
        }
        super.onStop();
    }
}
