package com.peter.thelandlord.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.firestore.ktx.toObject
import com.peter.thelandlord.R
import com.peter.thelandlord.data.apis.RentalsApi
import com.peter.thelandlord.data.db.AppDatabase
import com.peter.thelandlord.domain.models.Payment
import com.peter.thelandlord.domain.models.Rental
import com.peter.thelandlord.utils.Executors
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PaymentsViewHolder constructor(val view: View) : RecyclerView.ViewHolder(view) {

    private val tenantImageImgView = view.findViewById<CircleImageView>(R.id.tenant_image_img_vw)
    private val tenantNameTxt = view.findViewById<MaterialTextView>(R.id.tenant_name_txt_vw)
    private val amountPaidTxt = view.findViewById<MaterialTextView>(R.id.amount_paid_txt_vw)
    private val paymentDateTxt = view.findViewById<MaterialTextView>(R.id.payment_date_txt_vw)
    private val paymentForTxt = view.findViewById<MaterialTextView>(R.id.payment_for_txt_vw)
    private val appDb = AppDatabase.getInstance(view.context)
    private val rentalDao = appDb.rentalDao()

    fun bindTo(payment:Payment?){
        if (payment != null){
            showPayment(payment)
        }
    }

    private fun showPayment(payment: Payment){

        amountPaidTxt.text = payment.amount
        paymentDateTxt.text = payment.dateOfPayment
        paymentForTxt.text = String.format("%s/%s", payment.month, payment.year)

        var dbRental:Rental? = null
        Executors.ioExecutor {
            dbRental = rentalDao.findRentalById(payment.rentalId) //TODO("ISSUE HERE CONCERNING DB")
            displayName(payment.rentalId, dbRental)
        }
        loadImage(payment.rentalId, dbRental)

    }

    private fun displayName(rentalId: String, dbRental: Rental?){
        if (dbRental != null){
            tenantNameTxt.text = dbRental.tenantName
        }else{
            RentalsApi.getRental(rentalId)
                .addOnSuccessListener {
                    val rental = it.documents[0].toObject<Rental>()
                    tenantNameTxt.text = rental?.tenantName
                }
        }
    }

    private fun loadImage(rentalId: String, dbRental: Rental?){

        if(dbRental != null){  //if the rental is in the database, load the image directly
            glideImage(dbRental.tenantImage)
        }else{                          //else download rental and save in the database
            RentalsApi.getRental(rentalId)
                .addOnSuccessListener {
                    val rental = it.documents[0].toObject<Rental>()
                    glideImage(rental?.tenantImage)

                    Executors.ioExecutor {
                        rentalDao.insertRental(rental!!)
                    }

                }
        }
    }

    private fun glideImage(imageUrl: String?){
        if (!imageUrl.isNullOrBlank()){
            Glide.with(view.context)
                .load(imageUrl)
                .centerCrop()
                .into(tenantImageImgView)
        }else{
            Glide.with(view.context)
                .load(R.drawable.ic_user)
                .centerCrop()
                .into(tenantImageImgView)
        }
    }

    companion object{
        fun create(parent: ViewGroup): PaymentsViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.payments_row, parent, false)
            return PaymentsViewHolder(view)
        }
    }
}