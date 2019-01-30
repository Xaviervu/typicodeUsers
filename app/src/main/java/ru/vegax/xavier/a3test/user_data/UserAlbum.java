package ru.vegax.xavier.a3test.user_data;

public class UserAlbum{


    private final int mUserId;
    private final int mId;
    private final String mTitle;

    public UserAlbum(int userId, int id, String title) {
        mUserId = userId;
        mId = id;
        mTitle = title;
    }
    public int getUserId() {
        return mUserId;
    }

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }
}
