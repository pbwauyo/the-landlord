package com.peter.thelandlord.data

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.peter.thelandlord.domain.interfaces.AuthRepositoryInterface
import com.peter.thelandlord.domain.models.Landlord
import com.peter.thelandlord.domain.models.Login
import com.peter.thelandlord.domain.objects.FirestoreCollections
import com.peter.thelandlord.domain.objects.LandlordFields
import com.peter.thelandlord.singleliveevent.SingleLiveEvent
import javax.inject.Inject

class AuthRepository  @Inject constructor (var firebaseAuth: FirebaseAuth, var firestore: FirebaseFirestore) : AuthRepositoryInterface {

    override fun createUser(
        landlord: Landlord,
        liveData: MutableLiveData<Landlord>,
        errorLiveData: MutableLiveData<String>,
        isRegisteringLiveData: MutableLiveData<Boolean>,
        isSignedInLiveData: SingleLiveEvent<Boolean>,
        successLiveData: MutableLiveData<String>
    ) {
        firebaseAuth.createUserWithEmailAndPassword(landlord.email, landlord.password)
            .addOnFailureListener {
                println(it.message)
                errorLiveData.value = it.message
            }
            .addOnSuccessListener {

                firestore.collection(FirestoreCollections.USERS).add(landlord) //save user details
                    .addOnSuccessListener {
                        liveData.value = landlord
                        isSignedInLiveData.value = true
                        successLiveData.value = "Registration successful"
                    }
                    .addOnFailureListener {
                        println(it.message)
                        errorLiveData.value = it.message
                    }
                    .addOnCompleteListener {
                        isRegisteringLiveData.value = false
                    }
            }

    }

    override fun loginUser(
        login: Login,
        liveData: MutableLiveData<Landlord>,
        errorLiveData: MutableLiveData<String>,
        loggingInLiveData: MutableLiveData<Boolean>
    ) {
        firebaseAuth.signInWithEmailAndPassword(login.email, login.password)
            .addOnFailureListener {
                println("Error in login: ${it.message}")
                errorLiveData.value = it.message
            }
            .addOnSuccessListener {
                retrieveLandlord(login.email, liveData, errorLiveData)
            }
            .addOnCompleteListener {
                loggingInLiveData.value = false
            }
    }

    override fun signOut(isSignedInLiveData: SingleLiveEvent<Boolean>) {
        firebaseAuth.signOut()
        isSignedInLiveData.value = false
    }

    override fun isUserLoggedIn(liveData: MutableLiveData<Landlord>, errorLiveData: MutableLiveData<String>, isSignedInLiveData: SingleLiveEvent<Boolean>) {
        if (firebaseAuth.currentUser != null){
            isSignedInLiveData.value = true
            println("USer logged in")

            if (liveData.value == null){
                retrieveLandlord(firebaseAuth.currentUser!!.email!!, liveData, errorLiveData)
            }

        }else{

            isSignedInLiveData.value = false
            println("No USer logged in: ${isSignedInLiveData.value}")
        }
    }

    private fun retrieveLandlord(email: String, liveData: MutableLiveData<Landlord>, errorLiveData: MutableLiveData<String>) {
        firestore.collection(FirestoreCollections.USERS).whereEqualTo(LandlordFields.EMAIL, email).get()
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