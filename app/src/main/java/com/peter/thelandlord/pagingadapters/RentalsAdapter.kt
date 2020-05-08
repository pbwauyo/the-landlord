package com.peter.thelandlord.pagingadapters

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.github.ybq.android.spinkit.SpinKitView
import com.peter.thelandlord.domain.models.Rental
import com.peter.thelandlord.viewholders.RentalViewHolder

class RentalsAdapter(val progressBar: SpinKitView): PagedListAdapter<Rental, RentalViewHolder>(DIFF_CALL_BACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RentalViewHolder {
        return RentalViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RentalViewHolder, position: Int) {

        progressBar.visibility = View.GONE
        val rental = getItem(position)
        holder.bindTo(rental)

    }

    companion object {
        val DIFF_CALL_BACK = object : DiffUtil.ItemCallback<Rental>(){
            override fun areItemsTheSame(oldItem: Rental, newItem: Rental): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Rental, newItem: Rental): Boolean {
                return  oldItem == newItem
            }

        }

    }
}