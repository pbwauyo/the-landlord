package com.peter.thelandlord.presentation.auth.register

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.textview.MaterialTextView
import com.peter.thelandlord.R
import com.peter.thelandlord.databinding.FragmentRegisterBinding
import com.peter.thelandlord.di.viewmodelproviderfactory.ViewModelProviderFactory
import com.peter.thelandlord.presentation.LandlordActivity
import com.peter.thelandlord.presentation.auth.AuthViewModel
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class RegisterFragment : Fragment() {
    private var binding: FragmentRegisterBinding? = null
    private lateinit var authViewModel: AuthViewModel
    @Inject lateinit var vmFactory: ViewModelProviderFactory
    private lateinit var navController: NavController
    private lateinit var logoImgView: ImageView
    private lateinit var logoTxtView: MaterialTextView

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

//    @Suppress("DEPRECATION")
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

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_register,container, false
        )
        binding?.authViewModel = authViewModel
        logoImgView = binding!!.logo
        logoTxtView = binding!!.txtView

        logoImgView.bringToFront()
        logoTxtView.bringToFront()

        logoTxtView.parent.requestLayout()
        (logoTxtView.parent as View).invalidate()

        navController = findNavController()
        // Inflate the layout for this fragment
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.gotoLoginBtn?.setOnClickListener {
            navController.navigate(R.id.action_registerFragment_to_loginFragment)
        }

        authViewModel.isRegisteringLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it){
                    binding?.registerProgressBar?.visibility = View.VISIBLE
                }else{
                    binding?.registerProgressBar?.visibility = View.GONE
                }
            }
        })

        authViewModel.isSignedInLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it && (navController.currentDestination!!.id == R.id.registerFragment)){
                    navController.navigate(R.id.action_registerFragment_to_propertyList)
                }
            }
        })

        authViewModel.errorLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
            }
        })

        authViewModel.signUpEmailErrorLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding?.emailEditTxt?.error = it
            }
        })

        authViewModel.signUpFNameErrorLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding?.firstNameEditTxt?.error = it
            }
        })

        authViewModel.signUpLNameErrorLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding?.lastNameEditTxt?.error = it
            }
        })

        authViewModel.signUpPswdErrorLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding?.pswdEditTxt?.error = it
            }
        })

        authViewModel.signUpConfPswdErrorLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding?.confirmPswdEditTxt?.error = it
            }
        })

        authViewModel.signUpPswdMatchErrorLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding?.pswdEditTxt?.error = it
                binding?.confirmPswdEditTxt?.error = it
            }
        })

        authViewModel.signUpSuccessLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
