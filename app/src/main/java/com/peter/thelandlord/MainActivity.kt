package com.peter.thelandlord

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.peter.thelandlord.presentation.landlordactivity.LandlordActivity
import com.peter.thelandlord.databinding.ActivityMainBinding
import com.peter.thelandlord.di.baseapplication.BaseApplication
import com.peter.thelandlord.di.viewmodelproviderfactory.ViewModelProviderFactory
import com.peter.thelandlord.presentation.auth.UserLoginViewModel
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasAndroidInjector{

    @Inject lateinit var vmFactory: ViewModelProviderFactory
    @Inject lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>
    lateinit var viewModel: UserLoginViewModel
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

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

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }

}
