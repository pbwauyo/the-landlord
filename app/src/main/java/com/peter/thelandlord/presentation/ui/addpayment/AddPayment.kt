package com.peter.thelandlord.presentation.ui.addpayment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.Timestamp
import com.peter.thelandlord.R

import com.peter.thelandlord.databinding.FragmentAddPaymentBinding
import com.peter.thelandlord.di.viewmodelproviderfactory.ViewModelProviderFactory
import com.peter.thelandlord.domain.models.Debt
import com.peter.thelandlord.domain.models.Payment
import com.peter.thelandlord.presentation.viewmodels.RentalAccountViewModel
import com.peter.thelandlord.presentation.viewmodels.RentalViewModel
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class AddPayment : Fragment() {

    @Inject lateinit var vmFactory: ViewModelProviderFactory
    private var binding: FragmentAddPaymentBinding? = null
    private lateinit var rentalViewModel: RentalViewModel
    private lateinit var rentalAccountViewModel: RentalAccountViewModel
    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var materialDatePicker: MaterialDatePicker<Long>
    private lateinit var dateFormat: SimpleDateFormat

    companion object{
        const val TAG = "ADD_PAYMENT"
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val materialDatePickerBuilder = MaterialDatePicker.Builder.datePicker()
        val calender = Calendar.getInstance()
        materialDatePickerBuilder.setSelection(calender.timeInMillis)
        materialDatePickerBuilder.setTitleText("SELECT DATE OF PAYMENT")
        compositeDisposable = CompositeDisposable()
        materialDatePicker = materialDatePickerBuilder.build()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddPaymentBinding.inflate(inflater, container, false)
        rentalViewModel = activity?.let { ViewModelProvider(it, vmFactory).get(RentalViewModel::class.java) }!!
        rentalAccountViewModel = activity?.let { ViewModelProvider(it, vmFactory).get(RentalAccountViewModel::class.java) }!!

        rentalAccountViewModel.setRentalId(rentalViewModel.getCurrentRentalId())
        rentalAccountViewModel.setPropertyId(rentalViewModel.getCurrentPropertyId())

        rentalAccountViewModel.rentalDebtsLiveData.observe(viewLifecycleOwner, Observer {
            if(it.isNotEmpty()){
                binding?.noDebtsTxt?.visibility = View.GONE
            }
            setUpDebtTextViews(it)
        })

        rentalAccountViewModel.dateOfPaymentLiveData.observe(viewLifecycleOwner, Observer {
            binding?.datePaymentTxtVw?.text = it
        })

        setUpDatePickerListener()

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.submitPaymentBtn?.setOnClickListener {
            setUpAddPaymentBtn()
        }

        binding?.setPaymentDateBtn?.setOnClickListener {
            showDatePicker()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
        compositeDisposable.dispose()
    }

    private fun showDatePicker(){
        activity?.supportFragmentManager?.let {
            materialDatePicker.show(it, materialDatePicker.toString())
        }
    }

    private fun setUpAddPaymentBtn(){
        val handlePaytsDisposable = handlePaymentsInput()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                {
                    Toast.makeText(activity, "Payments saved successfully", Toast.LENGTH_SHORT).show()
                },
                {
                    Toast.makeText(activity, "ERROR, ${it.message}", Toast.LENGTH_LONG).show()
                }
            )

        compositeDisposable.add(handlePaytsDisposable)
    }

    private fun setUpDatePickerListener(){
        materialDatePicker.addOnPositiveButtonClickListener {
            val date = dateFormat.format(Date(it))
            rentalAccountViewModel.setDateOfPayment(date)
        }
    }

    private fun setUpDebtTextViews(debts: List<Debt>){

        val parentView: ViewGroup = binding!!.addPaymentFieldsLayout

        debts.forEach {

            val paymentRowView: View = layoutInflater.inflate(R.layout.payment_month_row, parentView, false)

            if(paymentRowView.parent != null){
                (paymentRowView.parent as ViewGroup).removeView(paymentRowView)
            }
            paymentRowView.findViewById<MaterialTextView>(R.id.month_txt).text = it.month
            paymentRowView.findViewById<MaterialTextView>(R.id.year_txt).text = it.year
            paymentRowView.findViewById<EditText>(R.id.amount_edit_txt_vw).setText("0")
            parentView.addView(paymentRowView)

            val monthYearKey = "${it.month}_${it.year}"
            val debtMap = hashMapOf(
                monthYearKey to paymentRowView.findViewById<EditText>(R.id.amount_edit_txt_vw) //add EditText to map
            )
            rentalAccountViewModel.debtsMapList.add(debtMap)

        }

    }

    /*
    * Get Input from EditTexts while excluding those with invalid values as the amount
    * Create a Payment object of the remaining values and save it to the database
    * */
    private fun handlePaymentsInput(): Completable{
        val dateOfPayment = rentalAccountViewModel.getDateOfPayment()!!
        val payments = ArrayList<Payment>()
        val timestamp = Timestamp.now().seconds.toString()

        rentalAccountViewModel.debtsMapList.filter {
            val key = it.keys.toList()[0]
            val amount = it[key]?.text.toString().trim()
            amount.isNotEmpty()  && amount.toInt() > 0 // allow only keys for valid values
        }.forEach {
            map ->
            val monthYearKey = map.keys.toList()[0]
            val splitKey = splitMonthYearKey(monthYearKey)
            val month = splitKey[0]
            val year = splitKey[1]
            val amountPaid = map[monthYearKey]?.text.toString().trim()

            val payment = Payment(
                dateOfPayment = dateOfPayment,
                amount = amountPaid,
                timestamp = timestamp,
                month = month,
                year = year,
                rentalId = rentalAccountViewModel.getRentalId(),
                propertyId = rentalAccountViewModel.getPropertyId()
            )
            payments.add(payment)
        }

        return  rentalAccountViewModel.savePayments(payments)
    }

    private fun splitMonthYearKey(monthYearKey: String): Array<String>{
        val splitKey = monthYearKey.split("_")
        return arrayOf(splitKey[0], splitKey[1])
    }


}
