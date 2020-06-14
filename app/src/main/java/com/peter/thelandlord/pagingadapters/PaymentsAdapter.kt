package com.peter.thelandlord.pagingadapters

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.github.ybq.android.spinkit.SpinKitView
import com.peter.thelandlord.domain.models.Payment
import com.peter.thelandlord.viewholders.PaymentsViewHolder

class PaymentsAdapter(val progressBar: SpinKitView) : PagedListAdapter<Payment, PaymentsViewHolder>(DIFF_CALL_BACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentsViewHolder {
        return PaymentsViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: PaymentsViewHolder, position: Int) {
        progressBar.visibility = View.GONE
        val payment = getItem(position)
        holder.bindTo(payment)
    }

    companion object {
        private val DIFF_CALL_BACK = object : DiffUtil.ItemCallback<Payment>(){
            override fun areItemsTheSame(oldItem: Payment, newItem: Payment): Boolean {
                return oldItem.paymentId == newItem.paymentId
            }

            override fun areContentsTheSame(oldItem: Payment, newItem: Payment): Boolean {
                return oldItem == newItem
            }

        }
    }
}