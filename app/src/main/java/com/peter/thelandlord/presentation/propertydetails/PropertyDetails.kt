package com.peter.thelandlord.presentation.propertydetails

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

import com.peter.thelandlord.R
import com.peter.thelandlord.databinding.FragmentPropertyDetailsBinding
import com.peter.thelandlord.di.viewmodelproviderfactory.ViewModelProviderFactory
import com.peter.thelandlord.presentation.addproperty.PropertyViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class PropertyDetails : Fragment() {

    val args: PropertyDetailsArgs by navArgs()
    lateinit var propertyID:String
    var binding: FragmentPropertyDetailsBinding?  = null
    lateinit var propertyViewModel: PropertyViewModel
    @Inject lateinit var vmFactory: ViewModelProviderFactory
    lateinit var navController: NavController

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        propertyID = args.propertyID
        propertyViewModel = activity?.let { ViewModelProvider(it, vmFactory).get(PropertyViewModel::class.java) }!!

        propertyViewModel.setPropertyID(propertyID)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPropertyDetailsBinding.inflate(inflater, container, false)
        navController = findNavController()

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        propertyViewModel.propertyLiveData.observe(viewLifecycleOwner, Observer {
            binding?.propertyNameTxtView?.text = it.name
            binding?.propertyLocationTxtView?.text = it.location
            if(it.imageUrl.isNotEmpty()){
                Glide.with(this)
                    .load(it.imageUrl)
                    .centerCrop()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.default_apartment)
                    .into(binding!!.propertyImgView)
            }else{
                Glide.with(this)
                    .load(R.drawable.default_apartment)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop()
                    .into(binding!!.propertyImgView)
            }
        })

        binding?.addRentalCardView?.setOnClickListener{
            val action = PropertyDetailsDirections.actionPropertyDetailsToAddRental(propertyID)
            navController.navigate(action)
        }


        binding?.balancesListCardView?.setOnClickListener {
            navController.navigate(R.id.action_propertyDetails_to_balancesList)
        }

        binding?.debtorsListCardView?.setOnClickListener {
            navController.navigate(R.id.action_propertyDetails_to_debtorsList)
        }

        binding?.rentalListCardView?.setOnClickListener {

            val action = PropertyDetailsDirections.actionPropertyDetailsToRentalsList(propertyID)
            navController.navigate(action)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
