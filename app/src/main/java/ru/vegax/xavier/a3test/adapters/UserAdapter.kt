package ru.vegax.xavier.a3test.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView

import java.util.LinkedHashMap
import java.util.Objects

import ru.vegax.xavier.a3test.R
import ru.vegax.xavier.a3test.models.User

abstract class UserAdapter protected constructor(private val mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnClickListener {

    //Member variables
    private var mUsers: List<User>? = null

    override fun getItemCount(): Int {
        return if (mUsers != null) {
            mUsers!!.size
        } else 0
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ViewHolderUsers(LayoutInflater.from(mContext).inflate(R.layout.list_item_user, parent, false))


    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val viewHolderUsers = holder as ViewHolderUsers
        //Get current item
        val currentItem = mUsers?.get(position)

        viewHolderUsers.bindTo(currentItem)


        val textView = viewHolderUsers.textView
        //disable swipe for the switch, only click on the item will trigger the output

        if (currentItem != null) {
            textView.tag = currentItem.id
            textView.setOnClickListener(this)
        }
    }

    fun setUserList(users: List<User>?) {
        mUsers = users
        notifyDataSetChanged()
    }

    internal inner class ViewHolderUsers(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textView: TextView


        init {
            //Initialize the views
            textView = itemView.findViewById(R.id.txtVUser)
        }

        fun bindTo(currentItem: User?) {
            if (currentItem != null) {
                textView.text = currentItem.name
            }
        }
    }
}
