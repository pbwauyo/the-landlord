package com.peter.thelandlord.utils

import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView

object Utils {
    private val firebaseAuth = FirebaseAuth.getInstance()

    fun getCurrentTimestamp(): String{
        return  Timestamp.now().seconds.toString()
    }

    fun getSignedInUser(): String{
        return firebaseAuth.currentUser?.email!!
    }
}