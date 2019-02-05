package ru.vegax.xavier.a3test.models;

import java.util.List;

import ru.vegax.xavier.a3test.models.User;

public class UserWrapper {


    private List<User> mList;
    private Throwable mThrowable;

    public UserWrapper(List<User> list) {
        mList = list;
    }

    public UserWrapper(Throwable throwable) {
        mThrowable = throwable;
    }

    public List<User> getList() {
        return mList;
    }

    public Throwable getThrowable() {
        return mThrowable;
    }

}


