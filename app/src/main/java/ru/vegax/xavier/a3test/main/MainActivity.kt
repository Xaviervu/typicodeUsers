package ru.vegax.xavier.a3test.main


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast

import ru.vegax.xavier.a3test.adapters.UserAdapter
import ru.vegax.xavier.a3test.albums.AlbumsActivity
import ru.vegax.xavier.a3test.R

import ru.vegax.xavier.a3test.models.User
import ru.vegax.xavier.a3test.models.UserWrapper

class MainActivity : AppCompatActivity() {
    private var mAdapter: UserAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)

        mainActivityViewModel.init()

        mAdapter = object : UserAdapter(this) {
            override fun onClick(v: View) {
                val intent = Intent(applicationContext, AlbumsActivity::class.java)
                if (v.tag != null) {
                    val userId = v.tag as Int
                    intent.putExtra(EXTRA_NAME_ID, userId)
                    startActivity(intent)
                }
            }

        }
        mainActivityViewModel.users?.observe(this, Observer { users ->
            if (users?.throwable== null) {
                mAdapter?.setUserList(users?.list)
            } else {

                Toast.makeText(applicationContext, users.throwable?.message, Toast.LENGTH_SHORT).show()
                mAdapter?.setUserList(null)
                users.throwable = null
            }
        })

        initRecyclerView()
    }

    private fun initRecyclerView() {
        //Initialize the RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerVUsers)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        //Set the Layout Manager
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = mAdapter
    }

    override fun onStop() {

        super.onStop()
    }

    companion object {
        private val TAG = "MainActivity"
        val EXTRA_NAME_ID = "EXTRA_NAME_ID"
    }
}
