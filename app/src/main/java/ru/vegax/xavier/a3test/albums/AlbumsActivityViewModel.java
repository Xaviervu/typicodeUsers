package ru.vegax.xavier.a3test.albums;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import java.util.List;

import ru.vegax.xavier.a3test.models.User;
import ru.vegax.xavier.a3test.models.UserAlbum;
import ru.vegax.xavier.a3test.models.UserAlbumWrapper;
import ru.vegax.xavier.a3test.repositories.UsersRepository;

class AlbumsActivityViewModel extends ViewModel {

    private MutableLiveData<UserAlbumWrapper> mUserAlbums;


    void init(){
        if(mUserAlbums != null){
            return;
        }
        UsersRepository repo = UsersRepository.getInstance();
        mUserAlbums = repo.getUserAlbums();
    }

    @NonNull LiveData<UserAlbumWrapper> getUserAlbums() {
        return mUserAlbums;
    }

}
