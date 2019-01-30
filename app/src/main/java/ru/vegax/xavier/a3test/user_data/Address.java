package ru.vegax.xavier.a3test.user_data;

public class Address {


    private final String mStreet;
    private final String mSuite;
    private final String mCity;
    private final String mZipcode;
    private final Geo mGeo;

    public Address(String street, String suite, String city, String zipcode, Geo geo) {
        mStreet = street;
        mSuite = suite;
        mCity = city;
        mZipcode = zipcode;
        mGeo = geo;
    }
    public String getStreet() {
        return mStreet;
    }

    public String getSuite() {
        return mSuite;
    }

    public String getCity() {
        return mCity;
    }

    public String getZipcode() {
        return mZipcode;
    }

    public Geo getGeo() {
        return mGeo;
    }
}
