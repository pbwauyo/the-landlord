package com.peter.thelandlord.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.peter.thelandlord.LandlordActivity
import com.peter.thelandlord.R
import com.peter.thelandlord.databinding.ActivityMainBinding
import com.peter.thelandlord.di.components.LandlordApplication
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var vmFactory: UserLoginVMFactory
    lateinit var viewModel: UserLoginViewModel
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        (applicationContext as LandlordApplication).applicationComponent.inject(this)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this, vmFactory).get(UserLoginViewModel::class.java)

        binding.lifecycleOwner = this
        binding.loginViewModel = viewModel

        viewModel.landlordLiveData.observe(this, Observer{
            it?.let {
                    startActivity(Intent(this, LandlordActivity::class.java))
                }
            }
        )

        viewModel.errorLiveData.observe(this, Observer{
                it?.let {
                    Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                }
            }
        )
    }

}
