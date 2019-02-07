package ru.vegax.xavier.a3test.photos

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import android.widget.Toast

import ru.vegax.xavier.a3test.R
import ru.vegax.xavier.a3test.adapters.PhotosAdapter
import ru.vegax.xavier.a3test.albums.AlbumsActivity
import ru.vegax.xavier.a3test.models.UserPhoto
import ru.vegax.xavier.a3test.models.UserPhotoWrapper

import ru.vegax.xavier.a3test.albums.AlbumsActivity.Companion.EXTRA_ALBUM_ID

class PhotosActivity : AppCompatActivity() {

    private var mAdapter: PhotosAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photos)
        val photosActivityViewModel = ViewModelProviders.of(this).get(PhotosActivityViewModel::class.java)
        photosActivityViewModel.init()


        val extras = intent.extras
        if (extras != null) {
            val albumId = extras.getInt(AlbumsActivity.EXTRA_ALBUM_ID)


            mAdapter = object : PhotosAdapter(this) {


                override fun onClick(v: View) {

                    if (v.tag != null) {
                        val photoId = v.tag as Int
                        Toast.makeText(applicationContext, photoId.toString(), Toast.LENGTH_SHORT).show()
                    }
                }

            }
            photosActivityViewModel.userPhotos?.observe(this, Observer { userPhotos ->
                if (userPhotos?.throwable == null) {
                    mAdapter?.setUserPhotos(userPhotos?.list, albumId)

                } else {
                    Toast.makeText(applicationContext, userPhotos.throwable?.message, Toast.LENGTH_SHORT).show()
                    userPhotos.throwable = null
                    mAdapter?.setUserPhotos(null, 0)
                }
            })
            initRecyclerView()
        }
    }

    private fun initRecyclerView() {
        //Initialize the RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerVPhotos)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        //Set the Layout Manager
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(VerticalSpaceItemDecoration(40))

        recyclerView.adapter = mAdapter
    }

    inner class VerticalSpaceItemDecoration internal constructor(private val verticalSpaceHeight: Int) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                    state: RecyclerView.State) {
            outRect.bottom = verticalSpaceHeight
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    companion object {
        private val TAG = "AlbumsActivity"
    }
}
