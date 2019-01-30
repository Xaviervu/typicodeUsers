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
import java.util.Map;

import ru.vegax.xavier.a3test.user_data.Address;
import ru.vegax.xavier.a3test.user_data.Company;
import ru.vegax.xavier.a3test.user_data.Geo;
import ru.vegax.xavier.a3test.user_data.User;

public abstract class UserLoader implements LoaderCallback {
    private static final String USERS_URL = "http://jsonplaceholder.typicode.com./users";
    private static final int TIMEOUT = 1000;

    private Map<Integer, User> mUsers;
    private AsyncTask<String, Void, Void> mUserLoader;

    protected UserLoader(@NonNull Map<Integer, User> users) {
        mUsers = users;
        mUserLoader = new GetJSonUsersAsync(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, USERS_URL);
    }



    public void cancelTasks() {

        if (mUserLoader != null) {
            mUserLoader.cancel(true);
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

    private static class GetJSonUsersAsync extends AsyncTask<String, Void, Void> {
        private final UserLoader mUserLoader;
        private Exception mE = null;


        GetJSonUsersAsync(UserLoader userLoader) {
            mUserLoader = userLoader;

        }

        @Override
        protected Void doInBackground(String... urls) {


            try {
                String resultUsers = getContent(urls[0], this); // get users
                handleJsonUsers(resultUsers,mUserLoader.mUsers);
            } catch (Exception e) {

                mE = e;
            }

            return null;
        }

        private void handleJsonUsers(String resultUsers, Map<Integer, User> users ) throws JSONException {
           users.clear();
            JSONArray jsonArray = new JSONArray(resultUsers);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jUser = (JSONObject) jsonArray.get(i);
                JSONObject jAdress = jUser.getJSONObject("address");
                JSONObject jGeo = jAdress.getJSONObject("geo");

                Geo geo = new Geo(jGeo.getString("lat"), jGeo.getString("lng"));
                Address address = new Address(jAdress.getString("street"), jAdress.getString("suite"),
                        jAdress.getString("city"), jAdress.getString("zipcode"), geo);
                JSONObject jCompany = jUser.getJSONObject("company");
                Company company = new Company(jCompany.getString("name"), jCompany.getString("catchPhrase"), jCompany.getString("bs"));
                User user = new User(jUser.getInt("id"), jUser.getString("name"), jUser.getString("username"),
                        jUser.getString("email"), address, jUser.getString("phone"),
                        jUser.getString("website"), company);
                users.put(jUser.getInt("id"), user);

            }
        }

        @Override
        protected void onPostExecute(Void voids) {
            if (mE == null) {
                mUserLoader.notifyDataLoaded();
            } else {
                mUserLoader.notifyError(mE.getMessage());
            }
        }
    }

}
