package com.peter.thelandlord.presentation.ui.aboutus

import android.content.ComponentName
import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.peter.thelandlord.R
import com.peter.thelandlord.databinding.FragmentAboutUsBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AboutUs.newInstance] factory method to
 * create an instance of this fragment.
 */
class AboutUs : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var binding: FragmentAboutUsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAboutUsBinding.inflate(inflater, container, false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInstaFollowButtonClick()
        initFbFollowBtnClick()
        initTwitterFollowBtnClick()
        initEmailBtnClick()
        initCallBtnsClick()
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }

    private fun initInstaFollowButtonClick(){
        binding?.instagramFollowBtn?.setOnClickListener {
            openInstragramProfile()
        }
    }

    private fun initFbFollowBtnClick(){
        binding?.facebookFollowBtn?.setOnClickListener {
            openFacebookProfile()
        }
    }

    private fun initTwitterFollowBtnClick(){
        binding?.twitterFollowBtn?.setOnClickListener {
            openTwitterProfile()
        }
    }

    private fun initEmailBtnClick(){
        binding?.sendEmailBtn?.setOnClickListener {
            sendEmail()
        }
    }

    private fun initCallBtnsClick(){
        binding?.callBtn1?.setOnClickListener {
            call("0785271004")
        }

        binding?.callBtn2?.setOnClickListener {
            call("0700908030")
        }
    }

    private fun openInstragramProfile(){
        val instagramPackage = "com.instagram.android"
        val instagramMobileUri = Uri.parse("https://instagram.com/_u/peter_wauyo.this")
        val instagramBrowserUri = Uri.parse("https://instagram.com/peter_wauyo.this")

        try{
            val mobileAppIntent = activity?.packageManager?.getLaunchIntentForPackage(instagramPackage)

            if (mobileAppIntent != null){
                mobileAppIntent.action = Intent.ACTION_VIEW
                mobileAppIntent.data = instagramMobileUri
                startActivity(mobileAppIntent)
            }

        }catch (e: Exception){
            Log.d(TAG, "Instagram Exception: ${e.message}")
            val browserIntent = Intent(Intent.ACTION_VIEW, instagramBrowserUri)
            startActivity(browserIntent)
        }
    }

    private fun openFacebookProfile(){
        val uri = Uri.parse("https://www.facebook.com/wauyo")
        val fbIntent = Intent(Intent.ACTION_VIEW, uri)
        try {
            startActivity(fbIntent)
        }catch (e: Exception){
            Log.d(TAG, "Facebook Exception: ${e.message}")
        }

    }

    private fun openTwitterProfile(){
        val twitterPackage = "com.twitter.android"
        val twitterUri = Uri.parse("https://twitter.com/pbwauyo")

        try {
            val twitterIntent = Intent(Intent.ACTION_VIEW, twitterUri)
            twitterIntent.setPackage(twitterPackage)
            startActivity(twitterIntent)
        }catch (e: Exception){
            Log.d(TAG, "Twitter Exception: ${e.message}")
            val twitterIntent = Intent(Intent.ACTION_VIEW, twitterUri)
            startActivity(twitterIntent)
        }

    }

    private fun sendEmail(){
        val emailAddresses = arrayOf("pbwauyo@gmail.com")

        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, emailAddresses)
        }

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email"))
        }catch (e: Exception){
            Log.d(TAG, "Email Exception: ${e.message}")
        }
    }

    private fun call(number: String){
        val uri = Uri.parse("tel:$number")
        val callIntent = Intent(Intent.ACTION_DIAL, uri)
        try {
            startActivity(callIntent)
        }catch (e: Exception){
            Log.d(TAG, "Call Exception: ${e.message}")
        }
    }

    companion object {

        const val TAG = "ABOUT_US"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AboutUs.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AboutUs().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}