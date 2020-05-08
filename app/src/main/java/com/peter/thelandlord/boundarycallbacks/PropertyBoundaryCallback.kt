package com.peter.thelandlord.boundarycallbacks

import android.util.Log
import androidx.paging.PagedList
import androidx.paging.PagingRequestHelper
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.peter.thelandlord.data.dao.PropertyDao
import com.peter.thelandlord.domain.models.Property
import com.peter.thelandlord.utils.FirestoreCollections
import com.peter.thelandlord.utils.PropertyFields
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class PropertyBoundaryCallback(
    val userEmail: String,
    val propertyDao: PropertyDao,
    val limit: Int
) : PagedList.BoundaryCallback<Property>() {

    private val ioExecutor = Executors.newSingleThreadExecutor()
    private val firestore = Firebase.firestore
    private val helper = PagingRequestHelper(ioExecutor)
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    companion object{
        const val TAG = "PROPERTY_BOUND_CALLBACK"
    }

    override fun onZeroItemsLoaded() {
//        super.onZeroItemsLoaded()

        Log.d(TAG, "ZERO_ITEMS_LOADED")

        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL){
            loadAndCacheProperties()
            Log.d(TAG, Thread.currentThread().name)
        }

    }

    override fun onItemAtEndLoaded(itemAtEnd: Property) {
 //       super.onItemAtEndLoaded(itemAtEnd)

        Log.d(TAG, "ITEM_AT_END_LOADED")

        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER){
            loadAndCacheProperties(itemAtEnd)
            Log.d(TAG, Thread.currentThread().name)
        }

    }

    private fun ioThread(f: () -> Unit){
        ioExecutor.execute(f)
    }

    private fun loadAndCacheProperties(item: Property? = null){

        if (item == null){
            firestore.collection(FirestoreCollections.PROPERTIES)
                .whereEqualTo(PropertyFields.OWNER, userEmail)
                .orderBy(PropertyFields.TIMESTAMP, Query.Direction.ASCENDING)
                .limit(limit.toLong())
                .get()
                .addOnSuccessListener {

                    it.documents.forEach { document ->
                        val property = document.toObject(Property::class.java)!!

                        try {
                            coroutineScope.launch {
                                propertyDao.insertProperty(property)
                                Log.d(TAG, Thread.currentThread().name)
                            }

                        }catch (e: Exception){
                            Log.d(TAG, "${e.message}")
                        }

                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, "${it.message}")
                }
        }else {

            firestore.collection(FirestoreCollections.PROPERTIES)
                .whereEqualTo(PropertyFields.OWNER, userEmail)
                .whereGreaterThan(PropertyFields.TIMESTAMP, item.timestamp)
                .orderBy(PropertyFields.TIMESTAMP, Query.Direction.ASCENDING)
                .limit(limit.toLong())
                .get()
                .addOnSuccessListener {
                    it.documents.forEach { document ->
                        val property = document.toObject(Property::class.java)!!

                        try{
                            coroutineScope.launch {
                                Log.d(TAG, Thread.currentThread().name)
                                propertyDao.insertProperty(property)
                            }
                        }catch (e: Exception){
                            Log.d(TAG, "${e.message}")
                        }
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, "${it.message}")
                }
        }
    }
}