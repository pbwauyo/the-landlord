package com.peter.thelandlord.utils

import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import de.hdodenhof.circleimageview.CircleImageView

object Utils {

    fun getCurrentTimestamp(): String{
        return  Timestamp.now().seconds.toString()
    }

}