package com.peter.thelandlord.domain.interfaces

import com.peter.thelandlord.domain.models.Rental
import io.reactivex.Completable

interface RentalManagementRepo {

    fun saveRental(rental: Rental): Completable

}