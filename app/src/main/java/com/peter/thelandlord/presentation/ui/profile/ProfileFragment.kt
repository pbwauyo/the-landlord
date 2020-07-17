package com.peter.thelandlord.presentation.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController
import com.peter.thelandlord.R
import com.peter.thelandlord.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    var binding: FragmentProfileBinding? = null
    var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(inflater, container, false)
        navController = findNavController()

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

    private fun initCardViewListeners(){
        binding?.aboutUs?.setOnClickListener {
            navController?.navigate(R.id.action_profileFragment_to_aboutUs)
        }

        binding?.donate?.setOnClickListener {
            navController?.navigate(R.id.action_profileFragment_to_donate)
        }
    }
}
