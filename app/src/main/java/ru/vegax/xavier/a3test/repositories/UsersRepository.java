package ru.vegax.xavier.a3test.repositories;

import android.arch.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.vegax.xavier.a3test.models.User;
import ru.vegax.xavier.a3test.models.UserAlbum;
import ru.vegax.xavier.a3test.models.UserAlbumWrapper;
import ru.vegax.xavier.a3test.models.UserPhoto;
import ru.vegax.xavier.a3test.models.UserPhotoWrapper;
import ru.vegax.xavier.a3test.models.UserWrapper;
import ru.vegax.xavier.a3test.retrofit2.TypicodeApi;

//Singleton pattern
public class UsersRepository {
    private static UsersRepository instance;
    private final MutableLiveData<UserWrapper> userData = new MutableLiveData<>();
    private final MutableLiveData<UserAlbumWrapper> userAlbumData = new MutableLiveData<>();
    private final MutableLiveData<UserPhotoWrapper> userPhotoData = new MutableLiveData<>();
    private static TypicodeApi typicodeApi;

    public static UsersRepository getInstance(){
        if(instance == null){
             instance = new UsersRepository();
        }
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create()).build();



        typicodeApi = retrofit.create(TypicodeApi.class);

        return instance;
    }
    public MutableLiveData<UserWrapper> getUsers(){

        Call<List<User>> userCall = typicodeApi.getUsers();
        userCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NotNull Call<List<User>> call, @NotNull Response<List<User>> response) {
                if (!response.isSuccessful()) {
                    //send error to UI
                    userData.setValue(new UserWrapper(new Throwable("could not load code: " + response.code())));                           return;
                }
                List<User> myUserAlbums = response.body();
                userData.setValue(new UserWrapper(myUserAlbums));
            }

            @Override
            public void onFailure(@NotNull Call<List<User>> call, @NotNull Throwable t) {
                //send error to UI
                userData.setValue(new UserWrapper(new Throwable("could not load code: " + "no connection  " + t.getMessage() )));
            }
        });
        return userData;
    }
    public MutableLiveData<UserAlbumWrapper> getUserAlbums(){

        Call<List<UserAlbum>> userCall = typicodeApi.getUserAlbums();
        userCall.enqueue(new Callback<List<UserAlbum>>() {
            @Override
            public void onResponse(@NotNull Call<List<UserAlbum>> call, @NotNull Response<List<UserAlbum>> response) {
                if (!response.isSuccessful()) {
                    //send error to UI
                    userAlbumData.setValue(new UserAlbumWrapper(new Throwable("could not load code: " + response.code())));                           return;
                }
                List<UserAlbum> myUserAlbums = response.body();
                userAlbumData.setValue(new UserAlbumWrapper(myUserAlbums));
            }

            @Override
            public void onFailure(@NotNull Call<List<UserAlbum>> call, @NotNull Throwable t) {
                //send error to UI
                userAlbumData.setValue(new UserAlbumWrapper(new Throwable( "could not load code: " + "no connection  " + t.getMessage())) );
            }
        });
        return userAlbumData;
    }

    public MutableLiveData<UserPhotoWrapper> getUserPhoto(){

        Call<List<UserPhoto>> userCall = typicodeApi.getUserPhotos();
        userCall.enqueue(new Callback<List<UserPhoto>>() {
            @Override
            public void onResponse(@NotNull Call<List<UserPhoto>> call, @NotNull Response<List<UserPhoto>> response) {
                if (!response.isSuccessful()) {
                    //send error to UI
                    userPhotoData.setValue(new UserPhotoWrapper(new Throwable("could not load code: " + response.code())));
                    return;
                }
                List<UserPhoto> myUserPhotos = response.body();
                userPhotoData.setValue(new UserPhotoWrapper(myUserPhotos));
            }

            @Override
            public void onFailure(@NotNull Call<List<UserPhoto>> call, @NotNull Throwable t) {
               //send error to UI
                userPhotoData.setValue(new UserPhotoWrapper(new Throwable("could not load code: " + "no connection  " + t.getMessage() )));
            }
        });
        return userPhotoData;
    }
}
