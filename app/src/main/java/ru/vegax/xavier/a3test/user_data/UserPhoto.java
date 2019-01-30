package ru.vegax.xavier.a3test.user_data;

public class UserPhoto {

    private final int mAlbumId;
    private final int mId;
    private final String mTitle;
    private final String mUrl;
    private final String mThumbnailUrl;

    public UserPhoto(int albumId, int id, String title, String url, String thumbnailUrl) {
        mAlbumId = albumId;
        mId = id;
        mTitle = title;
        mUrl = url;
        mThumbnailUrl = thumbnailUrl;
    }
    public int getAlbumId() {
        return mAlbumId;
    }

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

}
