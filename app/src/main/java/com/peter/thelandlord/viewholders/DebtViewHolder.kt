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
import com.peter.thelandlord.domain.models.Debt
import com.peter.thelandlord.domain.models.Rental
import com.peter.thelandlord.utils.Executors
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.debts_row.view.*

class DebtViewHolder (val view: View) : RecyclerView.ViewHolder(view) {
    val tenantNameTxtVw: MaterialTextView = view.tenant_name_txt_vw
    val debtAmountTxtVw: MaterialTextView = view.debt_amount_txt_vw
    val paymentForTxtVw: MaterialTextView = view.payment_for_txt_vw
    val tenantImageVw: CircleImageView = view.tenant_image_img_vw

    private val appDb = AppDatabase.getInstance(view.context)
    private val rentalDao = appDb.rentalDao()

    fun bind(debt: Debt){
        debtAmountTxtVw.text = debt.amount
        paymentForTxtVw.text = String.format("%s", "${debt.month}/${debt.year}")

        var dbRental:Rental? = null
        Executors.ioExecutor {
            dbRental = rentalDao.findRentalById(debt.rentalId) //TODO("ISSUE HERE CONCERNING DB")

        }
        displayTenantName(debt.rentalId, dbRental)
        displayTenantImage(debt.rentalId, dbRental)
    }

    private fun displayTenantName(rentalId: String, dbRental: Rental?){
        if (dbRental != null){
            tenantNameTxtVw.text = dbRental.tenantName
        }else{
            RentalsApi.getRental(rentalId)
                .addOnSuccessListener {
                    val rental = it.documents[0].toObject<Rental>()
                    tenantNameTxtVw.text = rental?.tenantName
                }
        }
    }

    private fun displayTenantImage(rentalId: String, dbRental: Rental?){

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

    private fun glideImage(imageurl: String?){

        if(!imageurl.isNullOrBlank()){
            Glide.with(view.context)
                .load(imageurl)
                .centerCrop()
                .into(tenantImageVw)
        }else {
            Glide.with(view.context)
                .load(R.drawable.ic_user)
                .centerCrop()
                .into(tenantImageVw)
        }

    }

    companion object {
        fun create(parent: ViewGroup): DebtViewHolder{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.debts_row, parent, false)
            return DebtViewHolder(view)
        }
    }
}