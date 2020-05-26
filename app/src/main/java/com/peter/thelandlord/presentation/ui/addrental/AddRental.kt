package com.peter.thelandlord.presentation.ui.addrental

import android.annotation.SuppressLint
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
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker

import com.peter.thelandlord.R
import com.peter.thelandlord.databinding.FragmentAddRentalBinding
import com.peter.thelandlord.di.viewmodelproviderfactory.ViewModelProviderFactory
import com.peter.thelandlord.presentation.viewmodels.RentalViewModel
import dagger.android.support.AndroidSupportInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class AddRental : Fragment() {

    companion object {
        const val TAG = "ADD_RENTAL"
        const val EMPTY_TENANT_NAME_ERROR = "Please fill in tenant name first"
    }

    val args: AddRentalArgs by navArgs()
    var binding: FragmentAddRentalBinding? = null
    lateinit var rentalViewModel: RentalViewModel
    @Inject lateinit var vmFactory: ViewModelProviderFactory
    lateinit var propertyID: String
    lateinit var compositeDisposable: CompositeDisposable
    lateinit var datePicker: MaterialDatePicker<Long>
    lateinit var dateFormat: SimpleDateFormat
    lateinit var calendar: Calendar
    lateinit var monthYearPicker: MonthYearPickerDialogFragment

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rentalViewModel = activity?.let { ViewModelProvider(it, vmFactory).get(RentalViewModel::class.java)}!!
        compositeDisposable = CompositeDisposable()
        propertyID = args.propertyId

        dateFormat = SimpleDateFormat("dd/MM/yyyy")

        calendar = Calendar.getInstance()

        //init material date picker
        val datePickerBuilder = MaterialDatePicker.Builder.datePicker()
        val currentTime = calendar.timeInMillis
        datePickerBuilder
            .setSelection(currentTime)
            .setTitleText("SELECT TENANCY START DATE")
        datePicker = datePickerBuilder.build()

        //init month-year picker
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        monthYearPicker = MonthYearPickerDialogFragment.getInstance(currentMonth, currentYear)

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

        initFieldErrorObservers()
        initTenantFieldsActivation()
        initTenantFieldsListeners()
        initPickers()

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

    private fun initPickers(){
        datePicker.addOnPositiveButtonClickListener {

            val date = dateFormat.format(Date(it))
            Log.d(TAG, date)
            rentalViewModel.setTenacyStartDate(date)
        }

        monthYearPicker.setOnDateSetListener {
                year, monthOfYear ->
            rentalViewModel.setRentStartMonth((monthOfYear+1).toString().padStart(2, '0')) //add trailing zero
            rentalViewModel.setRentStartYear(year.toString())

            Log.d(TAG, "${monthOfYear+1}")
        }
    }

    private fun initTenantFieldsActivation(){
        rentalViewModel.tenantNameLiveData.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, it)
                if(it.isNullOrBlank()){
                    binding!!.tenantContactEditTxt.isEnabled = false
                    binding!!.tenancyStartDateEditTxt.isEnabled = false
                    binding!!.monthTxt.isEnabled = false
                    binding!!.yearTxt.isEnabled = false
                    binding!!.setRentStartBtn.isEnabled = false
                    binding!!.setTenancyStartDateBtn.isEnabled = false
                    binding!!.tenantContactCardView.isActivated = false
                    binding!!.tenancyStartDateCardView.isActivated = false
                    binding!!.rentComputationStartCardVw.isActivated = false
                }else {
                    binding!!.tenantContactEditTxt.isEnabled = true
                    binding!!.tenancyStartDateEditTxt.isEnabled = true
                    binding!!.monthTxt.isEnabled = true
                    binding!!.yearTxt.isEnabled = true
                    binding!!.setRentStartBtn.isEnabled = true
                    binding!!.setTenancyStartDateBtn.isEnabled = true
                    binding!!.tenantContactCardView.isActivated = true
                    binding!!.tenancyStartDateCardView.isActivated = true
                    binding!!.rentComputationStartCardVw.isActivated = true
                }
            }
        )
    }

    private fun initTenantFieldsListeners(){
        binding!!.tenantContactCardView.setOnClickListener {
            if (!it.isActivated){
                Toast.makeText(activity,
                    EMPTY_TENANT_NAME_ERROR, Toast.LENGTH_LONG).show()
            }
        }

        binding!!.tenancyStartDateCardView.setOnClickListener {
            if (!it.isActivated){
                Toast.makeText(activity,
                    EMPTY_TENANT_NAME_ERROR, Toast.LENGTH_LONG).show()
            }
        }

        binding!!.rentComputationStartCardVw.setOnClickListener {
            if (!it.isActivated){
                Toast.makeText(activity,
                    EMPTY_TENANT_NAME_ERROR, Toast.LENGTH_LONG).show()
            }
        }

        binding!!.setTenancyStartDateBtn.setOnClickListener {
                Log.d(TAG, "Tenancy strt btn enabled, ${it.isEnabled}")
                activity?.let { activity -> datePicker.show(activity.supportFragmentManager, datePicker.toString())  }
        }

        binding!!.setRentStartBtn.setOnClickListener {
            Log.d(TAG, "Rent strt btn enabled, ${it.isEnabled}")
            activity?.let { activity -> monthYearPicker.show(activity.supportFragmentManager, monthYearPicker.toString()) }
        }
    }

    private fun initFieldErrorObservers(){
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

        rentalViewModel.rentComputStartMonthErrorLiveData.observe(viewLifecycleOwner, Observer {
            binding!!.monthTxt.error = it
        })

        rentalViewModel.rentComputStartYearErrorLiveData.observe(viewLifecycleOwner, Observer {
            binding!!.yearTxt.error = it
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        compositeDisposable.dispose()
    }
}
