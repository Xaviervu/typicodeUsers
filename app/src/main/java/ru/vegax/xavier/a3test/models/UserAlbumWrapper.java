package ru.vegax.xavier.a3test.models;

import java.util.List;

public class UserAlbumWrapper {


    private List<UserAlbum> mList;
    private Throwable mThrowable;

    public UserAlbumWrapper(List<UserAlbum> list) {
        mList = list;
    }

    public UserAlbumWrapper(Throwable throwable) {
        mThrowable = throwable;
    }

    public List<UserAlbum> getList() {
        return mList;
    }

    public Throwable getThrowable() {
        return mThrowable;
    }

}


