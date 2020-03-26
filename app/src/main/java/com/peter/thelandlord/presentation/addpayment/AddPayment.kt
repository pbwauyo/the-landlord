package com.peter.thelandlord.presentation.addpayment

import android.content.Context
import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.peter.thelandlord.R
import com.peter.thelandlord.di.viewmodelproviderfactory.ViewModelProviderFactory
import dagger.android.AndroidInjection
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class AddPayment : Fragment() {
    private val TAG = AddPayment().tag

    @Inject
    lateinit var vmFactory: ViewModelProviderFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_payment, container, false)
    }

    override fun onAttach(context: Context) {
        AndroidInjection.inject(activity)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        println("VM Factory null? $vmFactory")
    }
}
