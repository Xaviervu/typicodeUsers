package ru.vegax.xavier.a3test.models

class UserAlbumWrapper {


    var list: List<UserAlbum>? = null
    var throwable: Throwable? = null

    constructor(list: List<UserAlbum>?) {
        this.list = list
    }

    constructor(throwable: Throwable) {
        this.throwable = throwable
    }
}


