package ru.vegax.xavier.a3test.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import java.util.ArrayList

import ru.vegax.xavier.a3test.R
import ru.vegax.xavier.a3test.models.UserAlbum


abstract class AlbumsAdapter protected constructor(private val mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnClickListener {

    private var mUserAlbums: List<UserAlbum>? = null
    private var mUserId: Int? = null
    private var mCurrentAlbums: MutableList<UserAlbum>? = null

    override fun getItemCount(): Int {


        return if (mCurrentAlbums != null) {

            mCurrentAlbums!!.size
        } else 0
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ViewHolderUserAlbums(LayoutInflater.from(mContext).inflate(R.layout.list_item_user, parent, false))


    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val viewHolderUsers = holder as ViewHolderUserAlbums
        //Get current item
        val currentItem = mCurrentAlbums!![position]

        viewHolderUsers.bindTo(currentItem)


        val textView = viewHolderUsers.textView
        //disable swipe for the switch, only click on the item will trigger the output

        textView.tag = currentItem.id // album Id
        textView.setOnClickListener(this)
    }

    fun setUserList(userAlbums: List<UserAlbum>?, userId: Int?) {
        mUserAlbums = userAlbums
        mUserId = userId
        mCurrentAlbums = ArrayList()
        if (userAlbums != null) {
            for (userAlbum in mUserAlbums!!) {
                if (userAlbum.userId == mUserId) {
                    mCurrentAlbums!!.add(userAlbum)
                }
            }
        }

        notifyDataSetChanged()
    }

    internal inner class ViewHolderUserAlbums(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textView: TextView


        init {
            //Initialize the views
            textView = itemView.findViewById(R.id.txtVUser)
        }

        fun bindTo(currentItem: UserAlbum?) {
            if (currentItem != null) {
                textView.text = currentItem.title
            }
        }
    }
}
