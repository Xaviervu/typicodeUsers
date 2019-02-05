package ru.vegax.xavier.a3test.photos;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import java.util.List;

import ru.vegax.xavier.a3test.models.User;
import ru.vegax.xavier.a3test.models.UserPhoto;
import ru.vegax.xavier.a3test.models.UserPhotoWrapper;
import ru.vegax.xavier.a3test.repositories.UsersRepository;

class PhotosActivityViewModel extends ViewModel {

    private MutableLiveData<UserPhotoWrapper> mUserPhotos;

    void init(){
        if(mUserPhotos != null){
            return;
        }
        UsersRepository repo = UsersRepository.getInstance();
        mUserPhotos = repo.getUserPhoto();
    }

    @NonNull LiveData<UserPhotoWrapper> getUserPhotos() {
        return mUserPhotos;
    }

}
