package com.peter.thelandlord.presentation.auth.login

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
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.peter.thelandlord.R

import com.peter.thelandlord.databinding.FragmentLoginBinding
import com.peter.thelandlord.di.viewmodelproviderfactory.ViewModelProviderFactory
import com.peter.thelandlord.presentation.LandlordActivity
import com.peter.thelandlord.presentation.auth.AuthViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class LoginFragment : Fragment() {
    private var binding: FragmentLoginBinding? = null
    @Inject lateinit var vmFactory: ViewModelProviderFactory
    private lateinit var authViewModel: AuthViewModel
    private lateinit var navController: NavController

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

//    @Suppress("DEPRECATION")    //for APIs < 23
//    override fun onAttach(activity: Activity) {
//        AndroidInjection.inject(activity)
//        super.onAttach(activity)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authViewModel = activity?.let { ViewModelProvider(it, vmFactory).get(AuthViewModel::class.java) }!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding?.authViewModel = authViewModel
        navController = findNavController()

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel.errorLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
            }
        })

        authViewModel.isSignedInLiveData.observe(viewLifecycleOwner, Observer {

            it?.let {
                if (it && (navController.currentDestination!!.id == R.id.loginFragment)){
                    navController.navigate(R.id.action_loginFragment_to_propertyList)
                }
            }

        })

        authViewModel.loggingInLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it){
                    binding?.loginProgressBar?.visibility = View.VISIBLE
                }else{
                    binding?.loginProgressBar?.visibility = View.GONE
                }
            }
        })

        authViewModel.emailErrorLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding?.loginEmailLayout?.error = it
            }
        })

        authViewModel.pswdErrorLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding?.loginPswdEditTxt?.error = it
            }
        })

        binding?.gotoSignupBtn?.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
