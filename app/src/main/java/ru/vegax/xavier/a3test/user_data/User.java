package ru.vegax.xavier.a3test.user_data;

public class User {



    private final int mId;
    private final String mName;
    private final String mUserName;
    private final String mEmail;
    private final Address mAddress;
    private final String mPhone;
    private final String mWebsite;
    private final Company mCompany;

    public User(int id, String name, String username, String email, Address address, String phone, String website, Company company) {
        mId = id;
        mName = name;
        mUserName = username;
        mEmail = email;
        mAddress = address;
        mPhone = phone;
        mWebsite = website;
        mCompany = company;
    }
    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getEmail() {
        return mEmail;
    }

    public Address getAddress() {
        return mAddress;
    }

    public String getPhone() {
        return mPhone;
    }

    public String getWebsite() {
        return mWebsite;
    }

    public Company getCompany() {
        return mCompany;
    }
}
