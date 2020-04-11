package com.peter.thelandlord.di.modules.fragmentinjectors

import com.peter.thelandlord.presentation.addpayment.AddPayment
import com.peter.thelandlord.presentation.addproperty.AddProperty
import com.peter.thelandlord.presentation.addrental.AddRental
import com.peter.thelandlord.presentation.auth.login.LoginFragment
import com.peter.thelandlord.presentation.auth.register.RegisterFragment
import com.peter.thelandlord.presentation.balanceslist.BalancesList
import com.peter.thelandlord.presentation.debtorslist.DebtorsList
import com.peter.thelandlord.presentation.paymentslist.PaymentsListFragment
import com.peter.thelandlord.presentation.profile.ProfileFragment
import com.peter.thelandlord.presentation.propertydetails.PropertyDetails
import com.peter.thelandlord.presentation.propertylist.PropertyList
import com.peter.thelandlord.presentation.rentaldetails.RentalDetails
import com.peter.thelandlord.presentation.rentalslist.RentalsList
import com.peter.thelandlord.presentation.transactionsummary.TransactionSummary
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun addPaymentFragmentInjector(): AddPayment

    @ContributesAndroidInjector
    abstract fun addPropertyFragmentInjector(): AddProperty

    @ContributesAndroidInjector
    abstract fun addRentalFragmentInjector(): AddRental

    @ContributesAndroidInjector
    abstract fun loginFragmentInjector(): LoginFragment

    @ContributesAndroidInjector
    abstract fun registerLandlordFragmentInjector(): RegisterFragment

    @ContributesAndroidInjector
    abstract fun balancesListFragmentInjector(): BalancesList

    @ContributesAndroidInjector
    abstract fun debtorsListFragmentInjector(): DebtorsList

    @ContributesAndroidInjector
    abstract fun paymentsListInjector(): PaymentsListFragment

    @ContributesAndroidInjector
    abstract fun profileFragmentInjector(): ProfileFragment

    @ContributesAndroidInjector
    abstract fun propertyDetailsInjector(): PropertyDetails

    @ContributesAndroidInjector
    abstract fun propertyListInjector(): PropertyList

    @ContributesAndroidInjector
    abstract fun rentalDetailsInjector(): RentalDetails

    @ContributesAndroidInjector
    abstract fun rentalListInjector(): RentalsList

    @ContributesAndroidInjector
    abstract fun transactionsSummaryInjector(): TransactionSummary

}