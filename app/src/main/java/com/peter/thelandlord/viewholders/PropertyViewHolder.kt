package com.peter.thelandlord.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.textview.MaterialTextView
import com.peter.thelandlord.R
import com.peter.thelandlord.domain.models.Property


class PropertyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    var propertyImageView:ImageView = itemView.findViewById(R.id.property_image_view)
    var propertyNameTxt: MaterialTextView = itemView.findViewById(R.id.property_name_txt_view)
    var propertyLocationTxt:MaterialTextView = itemView.findViewById(R.id.property_location_txt_view)

    //private var property: Property? = null


    fun bindTo(property: Property?){

        if (property == null){

            Glide.with(propertyImageView.context)
                .load(R.drawable.apartment_placeholder)
                .into(propertyImageView)

            propertyNameTxt.text = LOADING

            propertyLocationTxt.text = LOADING
        }else{
            showProperty(property)
        }

    }

    fun showProperty(property: Property){
       // this.property = property
        property.imageUrl.let {
            if (it.isNotEmpty()){

                Glide.with(propertyImageView.context)
                     .load(property.imageUrl)
                     .placeholder(R.drawable.apartment_placeholder)
                     .centerCrop()
                     .into(propertyImageView)
            }else{
                Glide.with(propertyImageView.context)
                    .load(R.drawable.apartment_placeholder)
                    .centerCrop()
                    .into(propertyImageView)
            }
        }
        propertyNameTxt.text = property.name
        propertyLocationTxt.text = property.location
    }

    companion object{
        const val LOADING = "loading..."

        fun create(parent: ViewGroup): PropertyViewHolder{
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.property_list_row, parent, false)
            return PropertyViewHolder(view)
        }
    }
}