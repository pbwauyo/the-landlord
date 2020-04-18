package com.peter.thelandlord.presentation.addproperty

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.peter.thelandlord.R
import com.peter.thelandlord.databinding.FragmentAddPropertyBinding
import com.peter.thelandlord.di.viewmodelproviderfactory.ViewModelProviderFactory
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class AddProperty : Fragment() {

    private var binding: FragmentAddPropertyBinding? = null
    private lateinit var propertyViewModel: PropertyViewModel
    @Inject lateinit var vmFactory: ViewModelProviderFactory

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        propertyViewModel = ViewModelProvider(this, vmFactory).get(PropertyViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_property, container, false)
        binding!!.propertyViewModel = propertyViewModel


        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        propertyViewModel.propertyNameErrorLiveData.observe(viewLifecycleOwner, Observer {
            binding?.propertyNameEditTxt?.error = it
        })

        propertyViewModel.propertyLocationErrorLiveData.observe(viewLifecycleOwner, Observer {
            binding?.propertyLocationEditTxt?.error = it
        })

        propertyViewModel.isSavingLiveData.observe(viewLifecycleOwner, Observer {

            if (it){
                binding?.savePropertyProgressBar?.visibility = View.VISIBLE
            }else{
                binding?.savePropertyProgressBar?.visibility = View.GONE
            }

        })

        propertyViewModel.errorLiveData.observe(viewLifecycleOwner, Observer {
            Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
        })

        propertyViewModel.successLiveData.observe(viewLifecycleOwner, Observer {
            Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
            propertyViewModel.clearFields()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
