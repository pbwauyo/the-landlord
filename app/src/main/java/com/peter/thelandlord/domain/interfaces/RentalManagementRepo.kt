package com.peter.thelandlord.domain.interfaces

import androidx.lifecycle.LiveData
import com.peter.thelandlord.data.listing.Listing
import com.peter.thelandlord.domain.models.Property
import com.peter.thelandlord.domain.models.Rental
import io.reactivex.Completable

interface RentalManagementRepo {

    fun saveRental(rental: Rental): Completable

    fun rentalsFromProperty(propertyId: String): Listing<Rental>

    fun getRentalLiveData(rentalId: String): LiveData<Rental>

    fun getPropertyDetailsForRental(propertyId: String): LiveData<Property>
}