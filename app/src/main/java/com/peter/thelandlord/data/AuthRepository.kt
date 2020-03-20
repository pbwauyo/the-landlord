package com.peter.thelandlord.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.peter.thelandlord.domain.interfaces.AuthRepositoryInterface
import com.peter.thelandlord.domain.models.Landlord
import com.peter.thelandlord.domain.models.Login
import com.peter.thelandlord.domain.objects.FirestoreCollections
import com.peter.thelandlord.domain.objects.LandlordFields
import javax.inject.Inject

class AuthRepository  @Inject constructor (var firebaseAuth: FirebaseAuth, var firestore: FirebaseFirestore) : AuthRepositoryInterface {

    override fun createUser(
        landlord: Landlord,
        liveData: MutableLiveData<Landlord>,
        errorLiveData: MutableLiveData<String>
    ) {
        firebaseAuth.createUserWithEmailAndPassword(landlord.email, landlord.password)
            .addOnFailureListener {
                println(it.message)
                errorLiveData.value = it.message
            }
            .addOnSuccessListener {
                liveData.value = landlord
            }

    }

    override fun loginUser(
        login: Login,
        liveData: MutableLiveData<Landlord>,
        errorLiveData: MutableLiveData<String>
    ) {
        firebaseAuth.signInWithEmailAndPassword(login.email, login.password)
            .addOnFailureListener {
                println("Error in login: ${it.message}")
                errorLiveData.value = it.message
            }
            .addOnSuccessListener {
                retrieveLandlord(login.email, liveData, errorLiveData)
        }
    }

    private fun retrieveLandlord(email: String, liveData: MutableLiveData<Landlord>, errorLiveData: MutableLiveData<String>) {
        firestore.collection(FirestoreCollections.LANDLORDS).whereEqualTo(LandlordFields.EMAIL, email).get()
            .addOnSuccessListener {
                println(it.documents)
                liveData.value = it.documents[0].toObject<Landlord>()
            }
            .addOnFailureListener {
                errorLiveData.value = it.message
                println("Error while retrieving landlord details: ${it.message}")
            }
    }
}