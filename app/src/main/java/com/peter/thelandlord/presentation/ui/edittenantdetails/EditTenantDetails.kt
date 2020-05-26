package com.peter.thelandlord.presentation.ui.edittenantdetails

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider

import com.peter.thelandlord.R
import com.peter.thelandlord.databinding.FragmentEditTenantDetailsBinding
import com.peter.thelandlord.di.viewmodelproviderfactory.ViewModelProviderFactory
import com.peter.thelandlord.domain.models.Tenant
import com.peter.thelandlord.presentation.viewmodels.RentalViewModel
import dagger.android.support.AndroidSupportInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class EditTenantDetails : DialogFragment() {

    @Inject lateinit var vmFactory: ViewModelProviderFactory
    lateinit var rentalViewModel: RentalViewModel
    var binding: FragmentEditTenantDetailsBinding? = null
    var width: Int? = null
    var height: Int? = null
    lateinit var compositeDisposable: CompositeDisposable

    companion object {
        const val TAG = "EDIT_TENANT"
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setStyle(STYLE_NORMAL, R.style.DialogStyle)
        super.onCreate(savedInstanceState)

        compositeDisposable = CompositeDisposable()

        val metrics = resources.displayMetrics
        width = metrics.widthPixels
        height = metrics.heightPixels

        Log.d(TAG, "Device Width, $width")
        Log.d(TAG, "Device Height, $height")
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout((6 * width!!)/7, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentEditTenantDetailsBinding.inflate(inflater, container, false)
        rentalViewModel = activity?.let { ViewModelProvider(it, vmFactory).get(RentalViewModel::class.java) }!!
        dialog?.setCancelable(true)

        // Inflate the layout for this fragment
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showCurrentTenantDetailsInInputFields()

        binding?.confirmEditBtn?.setOnClickListener {
            binding?.progressBar?.visibility = View.VISIBLE

            val tenant = Tenant(
                name = binding!!.tenantNameEditTxt.text.toString().trim(),
                contact = binding!!.tenantContactEditTxt.text.toString().trim(),
                startDate = binding!!.tenantStartDateEditTxt.text.toString().trim()
            )

            val updateTenantDisposable = rentalViewModel.updateTenantDetails(tenant)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (
                    {
                        dialog?.dismiss()
                        Toast.makeText(activity, "Details changed successfully", Toast.LENGTH_SHORT).show()
                    },
                    {
                        dialog?.dismiss()
                        Log.d(TAG, "${it.message}")
                        Toast.makeText(activity, "${it.message}", Toast.LENGTH_LONG).show()
                    }
                )

            compositeDisposable.add(updateTenantDisposable)
        }

        binding?.cancelButton?.setOnClickListener {
            dialog?.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
        compositeDisposable.dispose()
    }

    private fun showCurrentTenantDetailsInInputFields(){

        Log.d(TAG, "Current Rental, ${rentalViewModel.currentRentalLiveData.value}")

        val rental = rentalViewModel.currentRentalLiveData.value!!

        binding?.tenantNameEditTxt?.setText(rental.tenantName)
        binding?.tenantContactEditTxt?.setText(rental.tenantContact)
        binding?.tenantStartDateEditTxt?.setText(rental.tenancyStartDate)
    }
}
