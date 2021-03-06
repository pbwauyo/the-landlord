package com.peter.thelandlord.presentation.ui.auth.register

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
import androidx.work.*
import com.google.android.material.textview.MaterialTextView
import com.peter.thelandlord.R
import com.peter.thelandlord.databinding.FragmentRegisterBinding
import com.peter.thelandlord.di.viewmodelproviderfactory.ViewModelProviderFactory
import com.peter.thelandlord.presentation.viewmodels.AuthViewModel
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class RegisterFragment : Fragment() {
    private var binding: FragmentRegisterBinding? = null
    private lateinit var authViewModel: AuthViewModel
    @Inject lateinit var vmFactory: ViewModelProviderFactory
    @Inject lateinit var workManager: WorkManager
    private lateinit var navController: NavController
    private lateinit var logoImgView: ImageView
    private lateinit var logoTxtView: MaterialTextView
    private lateinit var compositeDisposable: CompositeDisposable

    private companion object{
        val TAG = this::class.java.simpleName
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authViewModel = activity?.let { ViewModelProvider(it, vmFactory).get(AuthViewModel::class.java) }!!
        compositeDisposable = CompositeDisposable()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_register,container, false
        )

        binding?.lifecycleOwner = viewLifecycleOwner
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
                authViewModel.clearSignUpFields()
//                val landlord = authViewModel.landlordLiveData.value!!
//                val disposable = authViewModel.saveLandlordToDB(landlord)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(
//                        {
//                            Log.d(TAG, "SAVE_SUCCESS")
//                        },
//                        {
//                           er -> Log.d(TAG, "${er.message}")
//                        }
//                    )
//                compositeDisposable.add(disposable)
//
//                val inputData = workDataOf(Constants.KEY_LANDLORD_EMAIL to landlord.email)
//                val constraints = Constraints.Builder()
//                    .setRequiredNetworkType(NetworkType.CONNECTED)
//                    .build()
//
//                val uploadRequest = OneTimeWorkRequestBuilder<UploadLandlordDetailsWorker>()
//                    .setInputData(inputData)
//                    .setConstraints(constraints)
//                    .build()
//
//                workManager.enqueue(uploadRequest)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        compositeDisposable.dispose()
    }
}
