package com.peter.thelandlord.boundarycallbacks

import android.util.Log
import androidx.paging.PagedList
import androidx.paging.PagingRequestHelper
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.peter.thelandlord.data.dao.PropertyDao
import com.peter.thelandlord.domain.models.Property
import com.peter.thelandlord.utils.FirestoreCollections
import com.peter.thelandlord.utils.PropertyFields
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class PropertyBoundaryCallback(
    val userEmail: String,
    val propertyDao: PropertyDao,
    val limit: Int
) : PagedList.BoundaryCallback<Property>() {
    private val ioExecutor = Executors.newSingleThreadExecutor()
    private val firestore = Firebase.firestore
    private val helper = PagingRequestHelper(ioExecutor)

    companion object{
        const val TAG = "PROPERTY_BOUND_CALLBACK"
    }

    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()

        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL){
            loadAndCacheProperties()
        }

    }

    override fun onItemAtEndLoaded(itemAtEnd: Property) {
        super.onItemAtEndLoaded(itemAtEnd)

        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER){
            loadAndCacheProperties(itemAtEnd)
        }

    }

    private fun ioThread(f: () -> Unit){
        ioExecutor.execute(f)
        Log.d(TAG, Thread.currentThread().name)
    }

    private fun loadAndCacheProperties(item: Property = Property()){

        if (item.timestamp.isEmpty()){
            firestore.collection(FirestoreCollections.PROPERTIES)
                .whereEqualTo(PropertyFields.OWNER, userEmail)
                .orderBy(PropertyFields.TIMESTAMP, Query.Direction.ASCENDING)
                .limit(limit.toLong())
                .get()
                .addOnSuccessListener {
                    it.documents.forEach { document ->
                        val property = document.toObject(Property::class.java)!!

                        ioThread {
                            propertyDao.saveProperty(property)
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

                        ioThread {
                            propertyDao.saveProperty(property)
                        }
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, "${it.message}")
                }
        }
    }
}