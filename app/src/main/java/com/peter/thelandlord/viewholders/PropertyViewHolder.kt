package com.peter.thelandlord.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.textview.MaterialTextView
import com.peter.thelandlord.R
import com.peter.thelandlord.domain.models.Property
import com.peter.thelandlord.presentation.ui.propertylist.PropertyListDirections

class PropertyViewHolder(val itemVw: View): RecyclerView.ViewHolder(itemVw) {

    var propertyImageView:ImageView = itemVw.findViewById(R.id.property_image_view)
    var propertyNameTxt: MaterialTextView = itemVw.findViewById(R.id.property_name_txt_view)
    var propertyLocationTxt:MaterialTextView = itemVw.findViewById(R.id.property_location_txt_view)

    //private var property: Property? = null


    fun bindTo(property: Property?){

        if (property == null){

            Glide.with(propertyImageView.context)
                .load(R.drawable.default_apartment)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .into(propertyImageView)

            propertyNameTxt.text = LOADING

            propertyLocationTxt.text = LOADING
        }else{
            showProperty(property)
            val propertyID = property.propertyID
            val action = PropertyListDirections.actionPropertyListToPropertyDetails(propertyID)
            itemVw.setOnClickListener (
                Navigation.createNavigateOnClickListener(action)
            )
        }

    }

    fun showProperty(property: Property){
       // this.property = property
        property.imageUrl.let {
            if (it.isNotEmpty()){

                Glide.with(propertyImageView.context)
                     .load(property.imageUrl)
                     .placeholder(R.drawable.default_apartment)
                     .centerCrop()
                     .skipMemoryCache(true)
                     .diskCacheStrategy(DiskCacheStrategy.NONE)
                     .into(propertyImageView)
            }else{
                Glide.with(propertyImageView.context)
                    .load(R.drawable.default_apartment)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
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