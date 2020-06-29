package com.peter.thelandlord.domain.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.peter.thelandlord.data.listing.Listing
import com.peter.thelandlord.data.listing.NormalListing
import com.peter.thelandlord.data.networkstate.NetworkState
import com.peter.thelandlord.domain.models.Debt
import com.peter.thelandlord.domain.models.Payment
import io.reactivex.Completable

interface RentalAccountRepo {

    fun refreshPropertyDebts(propertyId: String): LiveData<NetworkState>

    fun handlePropertyDebtsFetching(propertyId: String): NormalListing<Debt>

    fun getAllDebtsForRental(rentalId: String): LiveData<List<Debt>>

    fun getAllPayments(propertyId: String): Listing<Payment>

    fun handlePaymentsFetching(propertyId: String): Listing<Payment>

    fun getAllPaymentsByTimestamp(timestamp: String): List<Payment>

    fun saveDebts(debts: List<Debt>)

    fun savePayments(payments: List<Payment>): Completable

}