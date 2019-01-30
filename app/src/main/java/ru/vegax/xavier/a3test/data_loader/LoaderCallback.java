package ru.vegax.xavier.a3test.data_loader;

public interface LoaderCallback {
    void notifyDataLoaded();
    void notifyError(String e);
}
