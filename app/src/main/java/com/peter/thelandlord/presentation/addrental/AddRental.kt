package com.peter.thelandlord.presentation.addrental

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs

import com.peter.thelandlord.R
import com.peter.thelandlord.databinding.FragmentAddRentalBinding
import com.peter.thelandlord.di.viewmodelproviderfactory.ViewModelProviderFactory
import dagger.android.support.AndroidSupportInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class AddRental : Fragment() {

    companion object {
        const val TAG = "ADD_RENTAL"
    }

    val args: AddRentalArgs by navArgs()
    var binding: FragmentAddRentalBinding? = null
    lateinit var rentalViewModel: RentalViewModel
    @Inject lateinit var vmFactory: ViewModelProviderFactory
    lateinit var propertyID: String
    lateinit var compositeDisposable: CompositeDisposable

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rentalViewModel = activity?.let { ViewModelProvider(it, vmFactory).get(RentalViewModel::class.java)}!!
        compositeDisposable = CompositeDisposable()
        propertyID = args.propertyId
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_rental, container, false)

        binding!!.lifecycleOwner = viewLifecycleOwner
        binding!!.rentalViewModel = rentalViewModel

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rentalViewModel.rentalNumberErrorLiveData.observe(viewLifecycleOwner, Observer {
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
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        })

        binding!!.addRentalBtn.setOnClickListener {
            val saveRentalDisposable = rentalViewModel.saveRental(propertyID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (
                    {
                        rentalViewModel.setSavingStatus(false)
                        rentalViewModel.setSuccessValue("Rental saved successfully")
                        rentalViewModel.clearFields()
                    },
                    {
                        rentalViewModel.setSavingStatus(false)
                        rentalViewModel.setErrorValue(it.message!!)
                        Log.d(TAG, "${it.message}")
                    }
                )

            compositeDisposable.add(saveRentalDisposable)

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        compositeDisposable.dispose()
    }
}
