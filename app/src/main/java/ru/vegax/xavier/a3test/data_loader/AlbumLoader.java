package ru.vegax.xavier.a3test.data_loader;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

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

import ru.vegax.xavier.a3test.user_data.UserAlbum;



import static android.os.AsyncTask.Status.FINISHED;


public abstract class AlbumLoader implements LoaderCallback {
    private static final String ALBUMS_URL = "http://jsonplaceholder.typicode.com./albums";
    private static final int TIMEOUT = 1000;
    protected Map<Integer, List<UserAlbum>> mAlbums;

    private static final String TAG = "AlbumLoader";
    private AsyncTask<String, Void, Void> mAlbumLoader;

    public AlbumLoader(@NonNull Map<Integer, List<UserAlbum>> albums) {
        mAlbums = albums;
        mAlbumLoader = new GetJSonAlbumsAsync(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,  ALBUMS_URL);
    }

    public void refreshData() {

        if (mAlbumLoader.getStatus() == FINISHED) {
            mAlbumLoader = new GetJSonAlbumsAsync(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ALBUMS_URL);
        }
    }
    public void cancelTasks() {

        if (mAlbumLoader != null) {
            mAlbumLoader.cancel(true);
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

    private static class GetJSonAlbumsAsync extends AsyncTask<String, Void, Void> {
        private final AlbumLoader mAlbumLoader;
        private Exception mE = null;


        GetJSonAlbumsAsync(AlbumLoader albumLoader) {
            mAlbumLoader = albumLoader;

        }

        @Override
        protected Void doInBackground(String... urls) {


            try {

                String resultAlbums = getContent(urls[0], this); //get content
                mAlbumLoader.mAlbums = (handleJsonAlbums(resultAlbums));

            } catch (Exception e) {

                mE = e;
            }

            return null;
        }


        private Map<Integer, List<UserAlbum>> handleJsonAlbums(String resultAlbums) throws JSONException {
            Map<Integer, List<UserAlbum>> userAlbumsMap = new LinkedHashMap<>();

            JSONArray jsonArray = new JSONArray(resultAlbums);


            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jAlbum = (JSONObject) jsonArray.get(i);
                int id = jAlbum.getInt("userId");

                UserAlbum userAlbum = new UserAlbum(jAlbum.getInt("userId"), jAlbum.getInt("id"), jAlbum.getString("title"));
                if (userAlbumsMap.get(id) != null) {
                    Objects.requireNonNull(userAlbumsMap.get(id)).add(userAlbum);
                } else {
                    ArrayList<UserAlbum> userAlbums = new ArrayList<>();
                    userAlbums.add(userAlbum);
                    userAlbumsMap.put(id, userAlbums);
                }
            }


            return userAlbumsMap;
        }

        @Override
        protected void onPostExecute(Void voids) {
            if (mE == null) {
                mAlbumLoader.notifyDataLoaded();
            } else {
                mAlbumLoader.notifyError(mE.getMessage());
            }
        }
    }

    public Map<Integer, List<UserAlbum>> getAlbums() {
        return mAlbums;
    }
}
