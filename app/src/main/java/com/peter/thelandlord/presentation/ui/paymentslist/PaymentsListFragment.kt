package com.peter.thelandlord.presentation.ui.paymentslist

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.peter.thelandlord.R
import com.peter.thelandlord.data.networkstate.NetworkState
import com.peter.thelandlord.databinding.FragmentPaymentsListBinding
import com.peter.thelandlord.di.viewmodelproviderfactory.ViewModelProviderFactory
import com.peter.thelandlord.pagingadapters.PaymentsAdapter
import com.peter.thelandlord.presentation.viewmodels.PropertyViewModel
import com.peter.thelandlord.presentation.viewmodels.RentalAccountViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class PaymentsListFragment : Fragment() {
    lateinit var binding: FragmentPaymentsListBinding
    @Inject lateinit var vmFactory: ViewModelProviderFactory
    lateinit var rentalAccountViewModel: RentalAccountViewModel
    lateinit var propertyViewModel: PropertyViewModel
    lateinit var adapter: PaymentsAdapter

    companion object {
        const val TAG = "PAYTS_LIST_FRAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rentalAccountViewModel = activity?.let { ViewModelProvider(it, vmFactory).get(RentalAccountViewModel::class.java) }!!
        propertyViewModel = activity?.let {ViewModelProvider(it, vmFactory).get(PropertyViewModel::class.java)}!!
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_payments_list, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.rentalAccountViewModel = rentalAccountViewModel

        initPropertyId()
        initAdapter()
        initRecyclerView()
        initSwipeRefresh()

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun initPropertyId(){
        val propertyId = propertyViewModel.propertyIDLLiveData.value
        rentalAccountViewModel.setPropertyId(propertyId)
    }

    private fun initAdapter(){
        adapter = PaymentsAdapter(binding.paymentsListProgressBar)
        rentalAccountViewModel.paymentsPagedList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    private fun initRecyclerView(){
        binding.paymentsRecyclerView.adapter = adapter
    }

    private fun initSwipeRefresh(){
        rentalAccountViewModel.refreshState.observe(viewLifecycleOwner, Observer {
            binding.paymentsSwipeLayout.isRefreshing = it == NetworkState.LOADING
        })

        binding.paymentsSwipeLayout.setOnRefreshListener {
            rentalAccountViewModel.refresh()
        }
    }

}
