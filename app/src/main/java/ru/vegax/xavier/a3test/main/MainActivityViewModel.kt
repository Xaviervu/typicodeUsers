package ru.vegax.xavier.a3test.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

import ru.vegax.xavier.a3test.models.User
import ru.vegax.xavier.a3test.models.UserWrapper
import ru.vegax.xavier.a3test.repositories.UsersRepository

internal class MainActivityViewModel : ViewModel() {

    private var mUsers: MutableLiveData<UserWrapper>? = null

    val users: LiveData<UserWrapper>?
        get() = mUsers

    fun init() {
        if (mUsers != null) {
            return
        }
        val repo = UsersRepository.getInstance()
        mUsers = repo.users
    }

}
