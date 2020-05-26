package com.peter.thelandlord.di.modules.fragmentinjectors

import com.peter.thelandlord.presentation.ui.addpayment.AddPayment
import com.peter.thelandlord.presentation.ui.addproperty.AddProperty
import com.peter.thelandlord.presentation.ui.addrental.AddRental
import com.peter.thelandlord.presentation.ui.auth.login.LoginFragment
import com.peter.thelandlord.presentation.ui.auth.register.RegisterFragment
import com.peter.thelandlord.presentation.ui.balanceslist.BalancesList
import com.peter.thelandlord.presentation.ui.debtorslist.DebtorsList
import com.peter.thelandlord.presentation.ui.edittenantdetails.EditTenantDetails
import com.peter.thelandlord.presentation.ui.paymentslist.PaymentsListFragment
import com.peter.thelandlord.presentation.ui.profile.ProfileFragment
import com.peter.thelandlord.presentation.ui.propertydetails.PropertyDetails
import com.peter.thelandlord.presentation.ui.propertylist.PropertyList
import com.peter.thelandlord.presentation.ui.rentaldetails.RentalDetails
import com.peter.thelandlord.presentation.ui.rentalslist.RentalsList
import com.peter.thelandlord.presentation.ui.transactionsummary.TransactionSummary
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

    @ContributesAndroidInjector
    abstract fun editTenantDetailsInjector(): EditTenantDetails
}