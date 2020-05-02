package com.peter.thelandlord.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.peter.thelandlord.data.dao.LandlordDao
import com.peter.thelandlord.domain.interfaces.AuthRepositoryInterface
import com.peter.thelandlord.domain.models.Landlord
import com.peter.thelandlord.domain.models.Login
import com.peter.thelandlord.objects.Constants
import com.peter.thelandlord.objects.FirestoreCollections
import com.peter.thelandlord.objects.LandlordFields
import com.peter.thelandlord.singleliveevent.SingleLiveEvent
import com.peter.thelandlord.work.UploadLandlordDetailsWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthRepository  @Inject constructor (
    val firebaseAuth: FirebaseAuth,
    val firestore: FirebaseFirestore,
    val landlordDao: LandlordDao,
    val workManager: WorkManager
) : AuthRepositoryInterface {

    private companion object{
        const val TAG = "AUTH_REPO"
        val coroutineScope = CoroutineScope(Dispatchers.IO)
    }

    override suspend fun createUser(
        landlord: Landlord,
        errorLiveData: MutableLiveData<String>,
        emailLiveData: MutableLiveData<String>,
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
                emailLiveData.postValue(landlord.email)
                isSignedInLiveData.postValue(true)
                successLiveData.postValue("Registration successful")

                coroutineScope.launch {
                    try {
                        Log.d(TAG, "$landlord")
                        landlordDao.saveLandlord(landlord).also {
                            Log.d(TAG, Thread.currentThread().name)
                        }

                        val inputData = workDataOf(Constants.KEY_LANDLORD_EMAIL to landlord.email)
                        val constraints = Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build()

                        val uploadRequest = OneTimeWorkRequestBuilder<UploadLandlordDetailsWorker>()
                            .setInputData(inputData)
                            .setConstraints(constraints)
                            .build()

                        workManager.enqueue(uploadRequest)

                    }catch (e: Exception){
                        Log.d(TAG, "${e.message}")
                    }
                }

            }
            .addOnCompleteListener {
                isRegisteringLiveData.postValue(false)
            }

    }

    fun getLandlordByEmail(email: String, errorLiveData: MutableLiveData<String>): LiveData<Landlord>{

        if (landlordDao.getLandlordByEmail(email).value == null){
            fetchLandlordDetails(email,errorLiveData)
        }

        return landlordDao.getLandlordByEmail(email)
    }

    fun fetchLandlordDetails(email: String, errorLiveData: MutableLiveData<String> = MutableLiveData<String>()){
        firestore.collection(FirestoreCollections.USERS)
            .whereEqualTo(LandlordFields.EMAIL, email)
            .get()
            .addOnSuccessListener {
                val landlord = it.documents[0].toObject<Landlord>()!!

                coroutineScope.launch {
                    landlordDao.saveLandlord(landlord)
                }

            }
            .addOnFailureListener {
                Log.d("FETCH LANDLORD ERROR", "${it.message}")
                errorLiveData.postValue(it.message)
            }
    }

    override fun loginUser(
        login: Login,
        emailLiveData: MutableLiveData<String>,
        errorLiveData: MutableLiveData<String>,
        loggingInLiveData: MutableLiveData<Boolean>,
        isSignedInLiveData: SingleLiveEvent<Boolean>
    ) {
        firebaseAuth.signInWithEmailAndPassword(login.email, login.password)
            .addOnFailureListener {
                println("Error in login: ${it.message}")
                errorLiveData.value = it.message
            }
            .addOnSuccessListener {
                emailLiveData.postValue(firebaseAuth.currentUser?.email)
                isSignedInLiveData.postValue(true)
            }
            .addOnCompleteListener {
                loggingInLiveData.value = false
            }
    }

    override fun signOut(isSignedInLiveData: SingleLiveEvent<Boolean>) {
        firebaseAuth.signOut()
        isSignedInLiveData.value = false
    }

    override fun isUserLoggedIn(errorLiveData: MutableLiveData<String>, isSignedInLiveData: SingleLiveEvent<Boolean>, emailLiveData: MutableLiveData<String>) {
        if (firebaseAuth.currentUser != null){
            isSignedInLiveData.value = true
            Log.d("Logged in User", "${firebaseAuth.currentUser?.email}")
            emailLiveData.postValue(firebaseAuth.currentUser?.email)

        }else{

            isSignedInLiveData.value = false
            println("No USer logged in: ${isSignedInLiveData.value}")
        }
    }

    private fun retrieveLandlord(email: String, liveData: MutableLiveData<Landlord>, errorLiveData: MutableLiveData<String>) {
        firestore.collection(FirestoreCollections.USERS).whereEqualTo(
            LandlordFields.EMAIL, email).get()
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