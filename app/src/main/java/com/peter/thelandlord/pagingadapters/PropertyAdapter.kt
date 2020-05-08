package com.peter.thelandlord.pagingadapters

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.github.ybq.android.spinkit.SpinKitView
import com.peter.thelandlord.domain.models.Property
import com.peter.thelandlord.viewholders.PropertyViewHolder

class PropertyAdapter(val progressBar: SpinKitView): PagedListAdapter<Property, PropertyViewHolder>(DIFF_CALL_BACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        return PropertyViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        val property: Property? = getItem(position)
        progressBar.visibility = View.GONE
        holder.bindTo(property)
    }

//    override fun getItemCount(): Int {
//        return super.getItemCount()
//    }

    companion object{
        val DIFF_CALL_BACK = object : DiffUtil.ItemCallback<Property>(){
            override fun areItemsTheSame(oldItem: Property, newItem: Property): Boolean
                = oldItem.propertyID == newItem.propertyID

            override fun areContentsTheSame(oldItem: Property, newItem: Property): Boolean
                = oldItem == newItem
        }
    }
}