package com.peter.thelandlord.presentation.addrental

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.peter.thelandlord.R
import com.peter.thelandlord.databinding.FragmentAddRentalBinding
import com.peter.thelandlord.di.viewmodelproviderfactory.ViewModelProviderFactory
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class AddRental : Fragment() {

    var binding: FragmentAddRentalBinding? = null
    lateinit var rentalViewModel: RentalViewModel
    @Inject lateinit var vmFactory: ViewModelProviderFactory
    lateinit var propertyID: String

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rentalViewModel = ViewModelProvider(this, vmFactory).get(RentalViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_rental, container, false)
        binding!!.rentalViewModel = rentalViewModel

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rentalViewModel.rentalIDErrorLiveData.observe(viewLifecycleOwner, Observer {
            binding!!.rentalIdEditTxt.error = it
        })

        rentalViewModel.monthlyAmountErrorLiveData.observe(viewLifecycleOwner, Observer {
            binding!!.rentalAmountEditTxt.error = it
        })

        rentalViewModel.tenantNameErrorLiveData.observe(viewLifecycleOwner, Observer {
            binding!!.tenantNameEditTxt.error = it
        })

        rentalViewModel.tenantContactErrorLiveData.observe(viewLifecycleOwner, Observer {
            binding!!.tenantContactEditTxt.error = it
        })

        rentalViewModel.tenancyStartDateErrorLiveData.observe(viewLifecycleOwner, Observer {
            binding!!.tenancyStartDateEditTxt.error = it
        })

        rentalViewModel.isSavingLiveData.observe(viewLifecycleOwner, Observer {
            if (it){
                binding!!.saveRentalProgressBar.visibility = View.VISIBLE
            }else{
                binding!!.saveRentalProgressBar.visibility = View.GONE
            }
        })

        rentalViewModel.errorLiveData.observe(viewLifecycleOwner, Observer {
            Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
        })

        rentalViewModel.successLiveData.observe(viewLifecycleOwner, Observer {
            Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
        })

        binding!!.addRentalBtn.setOnClickListener {
            rentalViewModel.saveRentalDetails(propertyID)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
