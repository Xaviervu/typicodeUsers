package ru.vegax.xavier.a3test.retrofit2

import retrofit2.Call
import retrofit2.http.GET
import ru.vegax.xavier.a3test.models.User
import ru.vegax.xavier.a3test.models.UserAlbum
import ru.vegax.xavier.a3test.models.UserPhoto

interface TypicodeApi {

    @get:GET("users")
    val users: Call<List<User>>

    @get:GET("photos")
    val userPhotos: Call<List<UserPhoto>>

    @get:GET("albums")
    val userAlbums: Call<List<UserAlbum>>
}
