package com.peter.thelandlord.presentation.addpayment

import android.content.Context
import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

import com.peter.thelandlord.R
import com.peter.thelandlord.databinding.FragmentAddPaymentBinding
import com.peter.thelandlord.di.viewmodelproviderfactory.ViewModelProviderFactory
import com.peter.thelandlord.presentation.addrental.RentalViewModel
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class AddPayment : Fragment() {

    @Inject lateinit var vmFactory: ViewModelProviderFactory
    private var binding: FragmentAddPaymentBinding? = null
    private lateinit var rentalViewModel: RentalViewModel

    companion object{
        const val TAG = "ADD_PAYMENT"
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddPaymentBinding.inflate(inflater, container, false)
        rentalViewModel = activity?.let { ViewModelProvider(it, vmFactory).get(RentalViewModel::class.java) }!!

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }
}
