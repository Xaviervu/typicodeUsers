package ru.vegax.xavier.a3test.albums

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
//import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast

import ru.vegax.xavier.a3test.R
import ru.vegax.xavier.a3test.adapters.AlbumsAdapter
import ru.vegax.xavier.a3test.main.MainActivity
import ru.vegax.xavier.a3test.photos.PhotosActivity


class AlbumsActivity : AppCompatActivity() {
    private var mAdapter: AlbumsAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_albums)

        val albumsActivityViewModel = ViewModelProviders.of(this).get(AlbumsActivityViewModel::class.java)

        val extras = intent.extras
        if (extras != null) {
            val userId = extras.getInt(MainActivity.EXTRA_NAME_ID)
            albumsActivityViewModel.init()
            mAdapter = object : AlbumsAdapter(this) {
                override fun onClick(v: View) {
                    val intent = Intent(applicationContext, PhotosActivity::class.java)
                    if (v.tag != null) {
                        val albumId = v.tag as Int
                        intent.putExtra(EXTRA_ALBUM_ID, albumId)
                        startActivity(intent)
                    }
                }

            }
            albumsActivityViewModel.userAlbums?.observe(this, Observer { userAlbums ->
                if (userAlbums!!.throwable == null) {
                    if (userAlbums.list != null) {
                        mAdapter?.setUserList(userAlbums.list, userId)
                    }
                } else {

                    Toast.makeText(applicationContext, userAlbums.throwable?.message, Toast.LENGTH_SHORT).show()
                    mAdapter?.setUserList(null, 0)
                    userAlbums.throwable = null // handle the exception
                }
            })

            initRecyclerView()
        }
    }

    private fun initRecyclerView() {
        //Initialize the RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerVAlbums)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        //Set the Layout Manager
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = mAdapter
    }

    companion object {
        private val TAG = "AlbumsActivity"
        val EXTRA_ALBUM_ID = "EXTRA_ALBUM_ID"
    }
}
