package com.peter.thelandlord.presentation.rentaldetails

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavArgs
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.datepicker.MaterialDatePicker

import com.peter.thelandlord.R
import com.peter.thelandlord.databinding.FragmentRentalDetailsBinding
import com.peter.thelandlord.di.viewmodelproviderfactory.ViewModelProviderFactory
import com.peter.thelandlord.presentation.addrental.RentalViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class RentalDetails : Fragment() {
    @Inject lateinit var vmFactory: ViewModelProviderFactory
    lateinit var rentalViewModel: RentalViewModel
    var binding: FragmentRentalDetailsBinding? = null
    val args: RentalDetailsArgs by navArgs()
    lateinit var rentalId: String
    lateinit var navController: NavController

    companion object {
        const val TAG = "RENTAL_DETAILS"
        const val NOT_AVAILABLE = "N/A"
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rentalId = args.rentalID
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRentalDetailsBinding.inflate(inflater, container, false)
        rentalViewModel = activity?.let { ViewModelProvider(it, vmFactory).get(RentalViewModel::class.java)}!!
        navController = findNavController()

        setUpCurrentRental()

        try {
            initRentalObservers()
        }catch (e: Exception){
            Log.d(TAG, "${e.message}")
        }

        // Inflate the layout for this fragment
        return binding!!.root
    }

    fun setUpOnClickListeners(){
        binding!!.addPaymentCardVw.setOnClickListener {
            navController.navigate(R.id.action_rentalDetails_to_addPayment)
        }
    }

    fun setUpCurrentRental(){
        rentalViewModel.setCurrentRentalId(rentalId)
    }

    fun initRentalObservers(){
        rentalViewModel.currentRentalLiveData.observe(viewLifecycleOwner, Observer {

            Log.d(TAG, it.toString())
            binding?.tenantNameTxtVw?.text = if (it.tenantName.isEmpty()) NOT_AVAILABLE else it.tenantName
            binding?.tenantContactTxtVw?.text = if (it.tenantContact.isEmpty()) NOT_AVAILABLE else it.tenantContact
            binding?.monthlyAmountTxtVw?.text = it.monthlyAmount

            Glide.with(this)
                .load(R.drawable.ic_user)
                .centerCrop()
                .into(binding!!.tenantImageImgVw)

            rentalViewModel.setCurrentPropertyIdForRental(it.propertyID)
        })

        rentalViewModel.currentPropertyDetailsForRental.observe(viewLifecycleOwner, Observer {
            binding?.propertyNameTxtVw?.text = it.name
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpOnClickListeners()
    }

    override fun onDestroy() {
        super.onDestroy()

         binding = null
    }
}
