package ru.vegax.xavier.a3test.user_data;

public class Geo {


    private final String mLat;
    private final String mLng;

    public Geo(String lat, String lng) {
        mLat = lat;
        mLng = lng;
    }
    public String getLat() {
        return mLat;
    }

    public String getLng() {
        return mLng;
    }
}
