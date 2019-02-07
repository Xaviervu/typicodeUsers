package ru.vegax.xavier.a3test.adapters


import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

import java.util.ArrayList

import ru.vegax.xavier.a3test.R
import ru.vegax.xavier.a3test.models.UserPhoto


abstract class PhotosAdapter
//    private final ImageDownloader imageDownloader = new ImageDownloader();

protected constructor(private val mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnClickListener {

    private var mAlbumId: Int? = null
    private var mUserPhotos: List<UserPhoto>? = null
    private var mCurrUserPhotos: MutableList<UserPhoto>? = null

    override fun getItemCount(): Int {
        return if (mCurrUserPhotos != null) {
            mCurrUserPhotos!!.size
        } else 0
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ViewHolderUserPhotos(LayoutInflater.from(mContext).inflate(R.layout.list_item_photos, parent, false))


    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val viewHolderUserPhotos = holder as ViewHolderUserPhotos
        //Get current item
        val currentItem = mCurrUserPhotos?.get(position)

        viewHolderUserPhotos.bindTo(currentItem)


        val textView = viewHolderUserPhotos.txtVPhoto
        //disable swipe for the switch, only click on the item will trigger the output
        val imageView = viewHolderUserPhotos.imageView
        val progressBar = viewHolderUserPhotos.progressBar

        if (currentItem != null && progressBar != null && imageView != null) {
            progressBar.visibility = View.VISIBLE
            textView.tag = currentItem.id // album Id
            textView.setOnClickListener(this)
            Glide.with(mContext)
                    .asBitmap()
                    .load(currentItem.url)
                    .listener(object : RequestListener<Bitmap> {
                        override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Bitmap>, isFirstResource: Boolean): Boolean {
                            progressBar.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(resource: Bitmap, model: Any, target: Target<Bitmap>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                            progressBar.visibility = View.GONE
                            return false
                        }
                    })

                    .into(imageView)

            //  imageDownloader.download(currentItem.getUrl(), imageView, progressBar);
        }


    }


    fun setUserPhotos(userPhotos: List<UserPhoto>?, albumId: Int?) {
        mAlbumId = albumId
        mUserPhotos = userPhotos
        if (mUserPhotos != null) {
            mCurrUserPhotos = ArrayList()
            for (userPhoto in mUserPhotos!!) {
                if (userPhoto.albumId == mAlbumId) {
                    mCurrUserPhotos!!.add(userPhoto)
                }
            }
        }
        notifyDataSetChanged()
    }

    internal inner class ViewHolderUserPhotos(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val progressBar: ProgressBar?


        val imageView: ImageView?
        val txtVPhoto: TextView


        init {
            //Initialize the views
            txtVPhoto = itemView.findViewById(R.id.txtVUser)
            imageView = itemView.findViewById(R.id.imageView)
            progressBar = itemView.findViewById(R.id.progressBar)
        }

        fun bindTo(currentItem: UserPhoto?) {
            if (currentItem != null) {
                txtVPhoto.text = currentItem.title
            }
        }
    }
}
