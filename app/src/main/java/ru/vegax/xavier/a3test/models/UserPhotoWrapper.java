package ru.vegax.xavier.a3test.models;

import java.util.List;

public class UserPhotoWrapper {


    private List<UserPhoto> mList;
    private Throwable mThrowable;

    public UserPhotoWrapper(List<UserPhoto> list) {
        mList = list;
    }

    public UserPhotoWrapper(Throwable throwable) {
        mThrowable = throwable;
    }

    public List<UserPhoto> getList() {
        return mList;
    }

    public Throwable getThrowable() {
        return mThrowable;
    }

}


