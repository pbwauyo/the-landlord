package com.peter.thelandlord.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.peter.thelandlord.R
import com.peter.thelandlord.domain.models.Rental
import de.hdodenhof.circleimageview.CircleImageView

class RentalViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    val tenantImageView: CircleImageView = itemView.findViewById(R.id.tenant_image)
    val rentalNumberTxtView: MaterialTextView = itemView.findViewById(R.id.rental_number_txt_vw)
    val tenantNameTxtView: MaterialTextView = itemView.findViewById(R.id.tenant_name_txt_vw)
    val tenantContactTxtView: MaterialTextView = itemView.findViewById(R.id.tenant_contact_txt_vw)
    val monthlyAmountTxtView: MaterialTextView = itemView.findViewById(R.id.monthly_amount_txt_vw)

    fun bindTo(rental: Rental?){
        if (rental == null){
            showPlaceholders()
        }else{
            showRental(rental)
        }
    }

    private fun showRental(rental: Rental){
        rentalNumberTxtView.text = rental.rentalNumber
        tenantNameTxtView.text = if (rental.tenantName.isNotEmpty()) rental.tenantName else NO_TENANT_MSG
        if (rental.tenantContact.isEmpty()){
            tenantContactTxtView.visibility = View.GONE
        }else{
            tenantContactTxtView.text = rental.tenantContact
        }
        monthlyAmountTxtView.text  = rental.monthlyAmount
    }

    private fun showPlaceholders(){
        rentalNumberTxtView.text = LOADING
        tenantNameTxtView.text = LOADING
        tenantContactTxtView.text = LOADING
        monthlyAmountTxtView.text = LOADING
    }

    companion object{

        private const val NO_TENANT_MSG = "No tenant available"
        private const val LOADING  = "Loading..."

        fun create(parent: ViewGroup): RentalViewHolder{
            return RentalViewHolder(LayoutInflater
                .from(parent.context)
                .inflate(R.layout.rentals_list_row, parent, false)
            )
        }
    }

}