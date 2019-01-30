package ru.vegax.xavier.a3test.user_data;

public class Company {

    private final String mName;
    private final String mCatchPhrase;
    private final String mBs;

    public Company(String name, String catchPhrase, String bs) {
        mName = name;
        mCatchPhrase = catchPhrase;
        mBs = bs;
    }
    public String getName() {
        return mName;
    }

    public String getCatchPhrase() {
        return mCatchPhrase;
    }

    public String getBs() {
        return mBs;
    }

}
