package com.peter.thelandlord.presentation.ui.debtorslist

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
import androidx.navigation.fragment.navArgs

import com.peter.thelandlord.R
import com.peter.thelandlord.data.networkstate.NetworkState
import com.peter.thelandlord.databinding.FragmentDebtorsListBinding
import com.peter.thelandlord.di.viewmodelproviderfactory.ViewModelProviderFactory
import com.peter.thelandlord.pagingadapters.DebtsAdapter
import com.peter.thelandlord.presentation.viewmodels.RentalAccountViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class DebtorsList : Fragment() {

    lateinit var binding: FragmentDebtorsListBinding
    lateinit var rentalAccountViewModel: RentalAccountViewModel
    @Inject lateinit var vmFactory: ViewModelProviderFactory
    lateinit var adapter: DebtsAdapter
    val args: DebtorsListArgs by navArgs()

    companion object {
        const val TAG = "DEBTORS_LIST"
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rentalAccountViewModel = activity?.let{
            ViewModelProvider(it, vmFactory).get(RentalAccountViewModel::class.java)
        }!!

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_debtors_list,
            container, false)

        binding.lifecycleOwner = viewLifecycleOwner

        initAdapter()
        initRecyclerView()
        initData()
        initSwipeRefresh()

        // Inflate the layout for this fragment
        return binding.root
    }

    fun initRecyclerView(){
        binding.debtorsRecyclerVw.adapter = adapter
    }

    fun initAdapter(){
        val progressBar = binding.debtorsListProgressBar
        adapter = DebtsAdapter(progressBar)
    }

    fun initData(){
        rentalAccountViewModel.propertyDebtsLiveData.observe(viewLifecycleOwner, Observer {
//            val progressBar = binding.debtorsListProgressBar
//            adapter = DebtsAdapter(progressBar)
//            initRecyclerView()
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        })

        val propertyid = args.propertyId
        rentalAccountViewModel.setPropertyId(propertyid)
    }

    fun initSwipeRefresh(){

        binding.debtorsSwipeLayout.setOnRefreshListener {

            Log.d(TAG, "Refresh in progress")
            rentalAccountViewModel.refreshPropertyDebtsList()
        }

        rentalAccountViewModel.propertyDebtsRefreshState.observe(viewLifecycleOwner, Observer {
            binding.debtorsSwipeLayout.isRefreshing = it == NetworkState.LOADING
        })
    }

}
