package com.peter.thelandlord.presentation.ui.profile

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
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.peter.thelandlord.R
import com.peter.thelandlord.databinding.FragmentProfileBinding
import com.peter.thelandlord.di.viewmodelproviderfactory.ViewModelProviderFactory
import com.peter.thelandlord.presentation.viewmodels.AuthViewModel
import com.peter.thelandlord.presentation.viewmodels.ProfileViewModel
import com.peter.thelandlord.utils.Utils
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ProfileFragment : Fragment() {
    var binding: FragmentProfileBinding? = null
    var navController: NavController? = null
    @Inject lateinit var vmFactory: ViewModelProviderFactory
    lateinit var profileViewModel: ProfileViewModel

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        profileViewModel = activity?.let { ViewModelProvider(it, vmFactory)}?.get(ProfileViewModel::class.java)!!

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(inflater, container, false)
        navController = findNavController()

        initCurrentUserEmail()
        loadNameAndEmail()
        loadProfileImage()

        // Inflate the layout for this fragment
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCardViewListeners()
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }

    private fun loadNameAndEmail(){
        profileViewModel.landlordLiveData.observe(viewLifecycleOwner, Observer {
            binding?.name?.text = String.format("%s %s", it.firstName, it.lastName)
            binding?.email?.text = it.email
        })
    }

    private fun loadProfileImage(){
        profileViewModel.landlordLiveData.observe(viewLifecycleOwner, Observer {

            if(!it.imageUrl.isBlank()){
                activity?.let { activityCtxt ->
                    Glide.with(activityCtxt)
                        .load(it.imageUrl)
                        .centerCrop()
                        .into(binding?.profileImage!!)
                }
            }else{
                activity?.let { activityCtxt ->
                    Glide.with(activityCtxt)
                        .load(R.drawable.ic_user)
                        .into(binding?.profileImage!!)
                }
            }

        })
    }

    private fun initCurrentUserEmail(){
        val email = Utils.getSignedInUser()

        Log.d(TAG, "Profile email: $email")
        profileViewModel.setlandlordEmail(email)
    }

    private fun initCardViewListeners(){
        binding?.aboutUs?.setOnClickListener {
            navController?.navigate(R.id.action_profileFragment_to_aboutUs)
        }

        binding?.donate?.setOnClickListener {
            navController?.navigate(R.id.action_profileFragment_to_donate)
        }
    }

    companion object {
        const val TAG = "PROFILE_FRAG"
    }
}
