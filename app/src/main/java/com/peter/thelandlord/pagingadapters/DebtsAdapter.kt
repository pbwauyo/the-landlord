package com.peter.thelandlord.pagingadapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.ybq.android.spinkit.SpinKitView
import com.peter.thelandlord.domain.models.Debt
import com.peter.thelandlord.viewholders.DebtViewHolder

class DebtsAdapter(val progressBar: SpinKitView) : RecyclerView.Adapter<DebtViewHolder>(){

    private var debtsList: List<Debt> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DebtViewHolder {
        return DebtViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: DebtViewHolder, position: Int) {
        progressBar.visibility = View.GONE
        holder.bind(debtsList[position])
    }

    override fun getItemCount(): Int {
        return debtsList.size
    }

    fun submitList(list: List<Debt>){
        debtsList = list
    }
}