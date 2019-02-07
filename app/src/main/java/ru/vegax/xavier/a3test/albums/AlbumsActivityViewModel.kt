package ru.vegax.xavier.a3test.albums

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

import ru.vegax.xavier.a3test.models.User
import ru.vegax.xavier.a3test.models.UserAlbum
import ru.vegax.xavier.a3test.models.UserAlbumWrapper
import ru.vegax.xavier.a3test.repositories.UsersRepository

internal class AlbumsActivityViewModel : ViewModel() {

    private var mUserAlbums: MutableLiveData<UserAlbumWrapper>? = null

    val userAlbums: LiveData<UserAlbumWrapper>?
        get() = mUserAlbums


    fun init() {
        if (mUserAlbums != null) {
            return
        }
        val repo = UsersRepository.getInstance()
        mUserAlbums = repo.userAlbums
    }

}
