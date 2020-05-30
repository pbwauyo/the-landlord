package com.peter.thelandlord.presentation.ui.rentaldetails

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder

import com.peter.thelandlord.R
import com.peter.thelandlord.databinding.FragmentRentalDetailsBinding
import com.peter.thelandlord.di.viewmodelproviderfactory.ViewModelProviderFactory
import com.peter.thelandlord.presentation.viewmodels.RentalViewModel
import dagger.android.support.AndroidSupportInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RentalDetails : Fragment() {
    @Inject lateinit var vmFactory: ViewModelProviderFactory
    lateinit var rentalViewModel: RentalViewModel
    var binding: FragmentRentalDetailsBinding? = null
    val args: RentalDetailsArgs by navArgs()
    lateinit var rentalId: String
    lateinit var navController: NavController
    lateinit var alertDialogBuilder: MaterialAlertDialogBuilder
    lateinit var compositeDisposable: CompositeDisposable

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
        compositeDisposable = CompositeDisposable()
        alertDialogBuilder = activity?.let { MaterialAlertDialogBuilder(it) }!!
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

        binding!!.editTenantCardVw.setOnClickListener {
            navController.navigate(R.id.action_rentalDetails_to_editTenantDetails)
        }

        binding!!.removeTenantCardVw.setOnClickListener {
            alertDialogBuilder
                .setTitle("Are you sure?")
                .setMessage("Tenant Details will be deleted permanently")
                .setNegativeButton("No"){
                        dialog, _ -> dialog.dismiss()
                }
                .setPositiveButton("Yes"){
                        dialog, _ ->
                    dialog.dismiss()
                    val removeTenantDisposable = rentalViewModel.removeTenantDetails()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe (
                            {
                                Toast.makeText(activity, "Details deleted successfully", Toast.LENGTH_SHORT).show()
                            },
                            {
                              Log.d(TAG, "${it.message}")
                            }
                        )

                    compositeDisposable.add(removeTenantDisposable)
                }
                .show()
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
        compositeDisposable.dispose()
        binding = null
    }
}
