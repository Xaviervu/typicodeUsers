package ru.vegax.xavier.a3test.models

class UserWrapper {


    var list:  List<User>? = null
    var throwable: Throwable? = null

    constructor(list: List<User>?) {
        this.list = list
    }

    constructor(throwable: Throwable) {
       this.throwable = throwable
    }
}


