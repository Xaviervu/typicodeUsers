package ru.vegax.xavier.a3test.main;


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

import ru.vegax.xavier.a3test.adapters.UserAdapter;
import ru.vegax.xavier.a3test.albums.AlbumsActivity;
import ru.vegax.xavier.a3test.R;

import ru.vegax.xavier.a3test.models.User;
import ru.vegax.xavier.a3test.models.UserWrapper;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String EXTRA_NAME_ID = "EXTRA_NAME_ID";
    private UserAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivityViewModel mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        mainActivityViewModel.init();

        mAdapter = new UserAdapter(this) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AlbumsActivity.class);
                if (v.getTag() != null) {
                    int userId = (int) v.getTag();
                    intent.putExtra(EXTRA_NAME_ID, userId);
                    startActivity(intent);
                }
            }

        };
        mainActivityViewModel.getUsers().observe(this, new Observer<UserWrapper>() {
            @Override
            public void onChanged(UserWrapper users) {
                if (users.getThrowable() == null) {
                    mAdapter.setUserList(users.getList());
                }else{
                    Toast.makeText(getApplicationContext(),users.getThrowable().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        initRecyclerView();
    }

    private void initRecyclerView() {
        //Initialize the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerVUsers);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //Set the Layout Manager
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onStop() {

        super.onStop();
    }
}
