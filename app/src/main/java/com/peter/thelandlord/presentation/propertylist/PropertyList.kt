package com.peter.thelandlord.presentation.propertylist

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
import com.google.firebase.auth.FirebaseAuth
import com.peter.thelandlord.R
import com.peter.thelandlord.databinding.FragmentPropertyListBinding
import com.peter.thelandlord.di.viewmodelproviderfactory.ViewModelProviderFactory
import com.peter.thelandlord.presentation.auth.AuthViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class PropertyList : Fragment() {
    private var binding: FragmentPropertyListBinding? = null
    @Inject lateinit var vmFactory: ViewModelProviderFactory
    lateinit var authViewModel: AuthViewModel
    lateinit var navController: NavController

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }
//
//    @Suppress("DEPRECATION")    //for APIs < 23
//    override fun onAttach(activity: Activity) {
//        AndroidSupportInjection.inject(this)
//        super.onAttach(activity)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authViewModel = activity?.let { ViewModelProvider(it, vmFactory).get(AuthViewModel::class.java) }!!
        authViewModel.checkedSignedInUser()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPropertyListBinding.inflate(inflater, container, false)
        navController = findNavController()
        // Inflate the layout for this fragment
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel.isSignedInLiveData.observe(viewLifecycleOwner, Observer { //responsible for logging in and out

            if(!it){
                navController.navigate(R.id.action_propertyList_to_loginFragment)
            }

        })

        binding?.signOut?.setOnClickListener {
            authViewModel.signOutUser()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}

