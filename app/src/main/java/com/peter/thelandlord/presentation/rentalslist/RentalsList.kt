package com.peter.thelandlord.presentation.rentalslist

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

import com.peter.thelandlord.R
import com.peter.thelandlord.data.networkstate.NetworkState
import com.peter.thelandlord.databinding.FragmentRentalsListBinding
import com.peter.thelandlord.di.viewmodelproviderfactory.ViewModelProviderFactory
import com.peter.thelandlord.pagingadapters.RentalsAdapter
import com.peter.thelandlord.presentation.addrental.RentalViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class RentalsList : Fragment() {
    lateinit var rentalViewModel: RentalViewModel
    @Inject lateinit var vmFactory: ViewModelProviderFactory
    var binding: FragmentRentalsListBinding? = null
    lateinit var navController: NavController
    lateinit var adapter: RentalsAdapter
    val args: RentalsListArgs by navArgs()
    lateinit var properyId: String

    companion object{
        const val TAG = "RENTALS_LIST"
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        properyId = args.propertyId
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        rentalViewModel = activity?.let{ViewModelProvider(it, vmFactory).get(RentalViewModel::class.java)}!!
        rentalViewModel.setPropertyId(properyId)

        navController = findNavController()
        binding = FragmentRentalsListBinding.inflate(inflater, container, false)
        adapter = RentalsAdapter(binding!!.rentalsListProgressBar)
        binding!!.rentalsListRv.adapter = adapter

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        initSwipeToRefresh()
    }

    override fun onDestroy() {
        super.onDestroy()
         binding = null
    }

    private fun initAdapter(){
        rentalViewModel.rentalsPagedList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        rentalViewModel.networkState.observe(viewLifecycleOwner, Observer {
            if(it.message != null){
                Log.d(TAG, it.message)
                Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                rentalViewModel.retry()
            }
        })
    }

    private fun initSwipeToRefresh(){
        rentalViewModel.refreshState.observe(viewLifecycleOwner, Observer {
            binding?.rentalsListSwipeRefreshLayout?.isRefreshing = it == NetworkState.LOADING

            if (it.message != null){
                Log.d(TAG, it.message)
                Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
            }
        })

        binding?.rentalsListSwipeRefreshLayout?.setOnRefreshListener {
            rentalViewModel.refresh()
        }
    }

}
