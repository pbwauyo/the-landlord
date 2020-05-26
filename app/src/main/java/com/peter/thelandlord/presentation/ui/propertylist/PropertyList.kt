package com.peter.thelandlord.presentation.ui.propertylist

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.peter.thelandlord.R
import com.peter.thelandlord.databinding.FragmentPropertyListBinding
import com.peter.thelandlord.di.viewmodelproviderfactory.ViewModelProviderFactory
import com.peter.thelandlord.pagingadapters.PropertyAdapter
import com.peter.thelandlord.presentation.viewmodels.PropertyViewModel
import com.peter.thelandlord.presentation.viewmodels.AuthViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class PropertyList : Fragment() {
    private var binding: FragmentPropertyListBinding? = null
    @Inject lateinit var vmFactory: ViewModelProviderFactory
    @Inject lateinit var firebaseAuth: FirebaseAuth
    lateinit var authViewModel: AuthViewModel
    lateinit var propertyViewModel: PropertyViewModel
    lateinit var navController: NavController
    lateinit var propertyAdapter: PropertyAdapter

    private companion object{
        const val TAG = "PROPERTY_LIST"
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authViewModel = activity?.let { ViewModelProvider(it, vmFactory).get(AuthViewModel::class.java) }!!
        propertyViewModel = activity?.let { ViewModelProvider(it, vmFactory).get(PropertyViewModel::class.java) }!!

        authViewModel.checkedSignedInUser()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPropertyListBinding.inflate(inflater, container, false)

        propertyAdapter = PropertyAdapter(binding!!.propertyListProgressBar)

        navController = findNavController()

        binding?.propertyListRv?.adapter = propertyAdapter

        authViewModel.isSignedInLiveData.observe(viewLifecycleOwner, Observer { //responsible for logging in and out

            if(it){
               setSignedInEmail()
            }else{
                navController.navigate(R.id.action_propertyList_to_loginFragment)
            }

        })

        authViewModel.landlordLiveData.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "$it")
        })

        propertyViewModel.propertyListLiveData.observe(viewLifecycleOwner, Observer {
            propertyAdapter.submitList(it)
        })


        // Inflate the layout for this fragment
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        binding?.signOutBtn?.setOnClickListener {
//            authViewModel.signOutUser()
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setSignedInEmail(){
        val email = FirebaseAuth.getInstance().currentUser?.email!!
        propertyViewModel.setPropertyEmail(email)
    }

}

