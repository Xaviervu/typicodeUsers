package ru.vegax.xavier.a3test.data_loader;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ru.vegax.xavier.a3test.user_data.UserPhoto;


import static android.os.AsyncTask.Status.FINISHED;


public abstract class PhotoLoader implements LoaderCallback {
    private static final String PHOTOS_URL = "http://jsonplaceholder.typicode.com./photos";
    private static final int TIMEOUT = 1000;

    protected Map<Integer, ArrayList<UserPhoto>> mPhotos;
    private static final String TAG = "PhotoLoader";
    private AsyncTask<String, Void, Void> mPhotoLoader;

    public PhotoLoader(Map<Integer, ArrayList<UserPhoto>> photos) {
        mPhotos = photos;
        mPhotoLoader = new GetJSonPhotoAsync(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,  PHOTOS_URL);
    }

    public void refreshData() {

        if (mPhotoLoader.getStatus() == FINISHED) {
            mPhotoLoader = new GetJSonPhotoAsync(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,  PHOTOS_URL);
        }
    }
    public void cancelTasks() {

        if (mPhotoLoader != null) {
            mPhotoLoader.cancel(true);
        }
    }
    private static String getContent(String path, AsyncTask task) throws IOException {
        URL url = new URL(path);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setConnectTimeout(TIMEOUT);// 0.1 seconds
        con.setRequestMethod("GET");
        con.connect();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            StringBuilder result = new StringBuilder();
            String line;
            while (((line = reader.readLine()) != null) && !task.isCancelled()) {
                result.append(line);
            }
            return result.toString();
        }
    }

    private static class GetJSonPhotoAsync extends AsyncTask<String, Void, Void> {
        private final PhotoLoader mPhotoLoader;
        private Exception mE = null;


        GetJSonPhotoAsync(PhotoLoader photoLoader) {
            mPhotoLoader = photoLoader;

        }

        @Override
        protected Void doInBackground(String... urls) {


            try {
                String resultPhotos = getContent(urls[0], this); //get photos
                mPhotoLoader.mPhotos = handleJsonPhotos(resultPhotos);
            } catch (Exception e) {
                mE = e;
            }

            return null;
        }

        private Map<Integer, ArrayList<UserPhoto>> handleJsonPhotos(String resultPhotos) throws JSONException {
            Map<Integer, ArrayList<UserPhoto>> userAlbumsMap = new LinkedHashMap<>();

            JSONArray jsonArray = new JSONArray(resultPhotos);


            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jPhoto = (JSONObject) jsonArray.get(i);
                int albumId = jPhoto.getInt("albumId");

                UserPhoto userPhoto = new UserPhoto(jPhoto.getInt("albumId"), jPhoto.getInt("id"), jPhoto.getString("title"),
                        jPhoto.getString("url"),jPhoto.getString("thumbnailUrl"));
                if (userAlbumsMap.get(albumId) != null) {
                    Objects.requireNonNull(userAlbumsMap.get(albumId)).add(userPhoto);
                } else {
                    ArrayList<UserPhoto> userPhotos = new ArrayList<>();
                    userPhotos.add(userPhoto);
                    userAlbumsMap.put(albumId, userPhotos);
                }
            }


            return userAlbumsMap;
        }

        @Override
        protected void onPostExecute(Void voids) {
            if (mE == null) {
                mPhotoLoader.notifyDataLoaded();
            } else {
                mPhotoLoader.notifyError(mE.getMessage());
            }
        }
    }



    public Map<Integer, ArrayList<UserPhoto>> getPhotos() {
        return mPhotos;
    }


}
