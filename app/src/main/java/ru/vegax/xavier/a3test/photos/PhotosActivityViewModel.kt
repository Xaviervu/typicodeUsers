package ru.vegax.xavier.a3test.photos

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

import ru.vegax.xavier.a3test.models.User
import ru.vegax.xavier.a3test.models.UserPhoto
import ru.vegax.xavier.a3test.models.UserPhotoWrapper
import ru.vegax.xavier.a3test.repositories.UsersRepository

internal class PhotosActivityViewModel : ViewModel() {

    private var mUserPhotos: MutableLiveData<UserPhotoWrapper>? = null

    val userPhotos: LiveData<UserPhotoWrapper>?
        get() = mUserPhotos

    fun init() {
        if (mUserPhotos != null) {
            return
        }
        val repo = UsersRepository.getInstance()
        mUserPhotos = repo.userPhoto
    }

}
