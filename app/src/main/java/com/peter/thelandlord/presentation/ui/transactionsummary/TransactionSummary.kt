package com.peter.thelandlord.presentation.ui.transactionsummary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.peter.thelandlord.R

/**
 * A simple [Fragment] subclass.
 */
class TransactionSummary : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction_summary, container, false)
    }

}
