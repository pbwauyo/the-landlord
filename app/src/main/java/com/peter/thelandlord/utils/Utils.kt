package com.peter.thelandlord.utils

import com.google.firebase.Timestamp

object Utils {

    fun getCurrentTimestamp(): String{
        return  Timestamp.now().seconds.toString()
    }

}