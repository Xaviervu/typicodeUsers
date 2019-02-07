package ru.vegax.xavier.a3test.repositories

import android.arch.lifecycle.MutableLiveData

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.vegax.xavier.a3test.models.User
import ru.vegax.xavier.a3test.models.UserAlbum
import ru.vegax.xavier.a3test.models.UserAlbumWrapper
import ru.vegax.xavier.a3test.models.UserPhoto
import ru.vegax.xavier.a3test.models.UserPhotoWrapper
import ru.vegax.xavier.a3test.models.UserWrapper
import ru.vegax.xavier.a3test.retrofit2.TypicodeApi

//Singleton pattern
class UsersRepository {
    private val userData = MutableLiveData<UserWrapper>()
    private val userAlbumData = MutableLiveData<UserAlbumWrapper>()
    private val userPhotoData = MutableLiveData<UserPhotoWrapper>()
    //send error to UI
    //send error to UI
    val users: MutableLiveData<UserWrapper>
        get() {

            val userCall = typicodeApi?.users
            userCall?.enqueue(object : Callback<List<User>> {
                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    if (!response.isSuccessful) {
                        userData.value = UserWrapper(Throwable("could not load code: " + response.code()))
                        return
                    }
                    val myUserAlbums = response.body()
                    userData.value = UserWrapper(myUserAlbums)
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    userData.value = UserWrapper(Throwable("could not load code: " + "no connection  " + t.message))
                }
            })
            return userData
        }
    //send error to UI
    //send error to UI
    val userAlbums: MutableLiveData<UserAlbumWrapper>
        get() {

            val userCall = typicodeApi!!.userAlbums
            userCall.enqueue(object : Callback<List<UserAlbum>> {
                override fun onResponse(call: Call<List<UserAlbum>>, response: Response<List<UserAlbum>>) {
                    if (!response.isSuccessful) {
                        userAlbumData.value = UserAlbumWrapper(Throwable("could not load code: " + response.code()))
                        return
                    }
                    val myUserAlbums = response.body()
                    userAlbumData.value = UserAlbumWrapper(myUserAlbums)
                }

                override fun onFailure(call: Call<List<UserAlbum>>, t: Throwable) {
                    userAlbumData.value = UserAlbumWrapper(Throwable("could not load code: " + "no connection  " + t.message))
                }
            })
            return userAlbumData
        }

    //send error to UI
    //send error to UI
    val userPhoto: MutableLiveData<UserPhotoWrapper>
        get() {

            val userCall = typicodeApi!!.userPhotos
            userCall.enqueue(object : Callback<List<UserPhoto>> {
                override fun onResponse(call: Call<List<UserPhoto>>, response: Response<List<UserPhoto>>) {
                    if (!response.isSuccessful) {
                        userPhotoData.value = UserPhotoWrapper(Throwable("could not load code: " + response.code()))
                        return
                    }
                    val myUserPhotos = response.body()
                    userPhotoData.value = UserPhotoWrapper(myUserPhotos)
                }

                override fun onFailure(call: Call<List<UserPhoto>>, t: Throwable) {
                    userPhotoData.value = UserPhotoWrapper(Throwable("could not load code: " + "no connection  " + t.message))
                }
            })
            return userPhotoData
        }

    companion object {
        private var instance: UsersRepository? = null
        private var typicodeApi: TypicodeApi? = null

        fun getInstance(): UsersRepository {
            if (instance == null) {
                instance = UsersRepository()
            }
            val retrofit = Retrofit.Builder().baseUrl("https://jsonplaceholder.typicode.com/")
                    .addConverterFactory(GsonConverterFactory.create()).build()



            typicodeApi = retrofit.create(TypicodeApi::class.java)

            return instance as UsersRepository
        }
    }
}
