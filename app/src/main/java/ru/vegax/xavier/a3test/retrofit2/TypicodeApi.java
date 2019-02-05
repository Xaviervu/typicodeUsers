package ru.vegax.xavier.a3test.retrofit2;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import ru.vegax.xavier.a3test.models.User;
import ru.vegax.xavier.a3test.models.UserAlbum;
import ru.vegax.xavier.a3test.models.UserPhoto;

public interface TypicodeApi {

    @GET("users")
    Call<List<User>> getUsers();

    @GET("photos")
    Call<List<UserPhoto>> getUserPhotos();

    @GET("albums")
    Call<List<UserAlbum>> getUserAlbums();
}
