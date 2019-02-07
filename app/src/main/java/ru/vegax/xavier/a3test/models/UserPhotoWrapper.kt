package ru.vegax.xavier.a3test.models

class UserPhotoWrapper {


    var list: List<UserPhoto>? = null
    var throwable: Throwable? = null

    constructor(list: List<UserPhoto>?) {
        this.list = list
    }

    constructor(throwable: Throwable) {
        this.throwable = throwable
    }
}


