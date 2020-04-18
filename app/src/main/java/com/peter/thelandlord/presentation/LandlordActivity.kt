package com.peter.thelandlord.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomappbar.BottomAppBarTopEdgeTreatment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.peter.thelandlord.R
import com.peter.thelandlord.databinding.ActivityLandlordBinding
import com.peter.thelandlord.di.viewmodelproviderfactory.ViewModelProviderFactory
import com.peter.thelandlord.presentation.auth.AuthViewModel
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class LandlordActivity : AppCompatActivity(), HasAndroidInjector {

    lateinit var binding:ActivityLandlordBinding
    @Inject lateinit var vmFactory: ViewModelProviderFactory
    @Inject lateinit var androidInjector: DispatchingAndroidInjector<Any>
    @Inject lateinit var firebaseAuth: FirebaseAuth
    lateinit var authViewModel: AuthViewModel
    lateinit var bottomNavBar: BottomNavigationView
    lateinit var navController: NavController
    lateinit var navControllerListener: NavController.OnDestinationChangedListener
    lateinit var floatingActionButton: FloatingActionButton

    companion object{
        val TAG = this::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        binding = ActivityLandlordBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        bottomNavBar = binding.navBar
        floatingActionButton = binding.floatingButton

        navController = findNavController(R.id.nav_host)
        bottomNavBar.setupWithNavController(navController)

        navControllerListener = NavController.OnDestinationChangedListener {    //hide or show bottomNavBar depending on Fragment
                _, destination, _ ->

                when(destination.id){
                    R.id.loginFragment, R.id.registerFragment -> bottomNavBar.visibility = View.GONE
                    else -> bottomNavBar.visibility = View.VISIBLE
                }

                when(destination.id){
                    R.id.loginFragment, R.id.registerFragment, R.id.addProperty -> binding.floatingButton.visibility = View.GONE
                    else -> binding.floatingButton.visibility = View.VISIBLE
                }

        }

        authViewModel = ViewModelProvider(this, vmFactory).get(AuthViewModel::class.java)

        floatingActionButton.setOnClickListener {
            navController.navigate(R.id.addProperty)
        }

    }

    override fun onSupportNavigateUp() = navController.navigateUp()

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }

    override fun onResume() {
        super.onResume()
        navController.addOnDestinationChangedListener(navControllerListener)
    }

    override fun onPause() {
        super.onPause()
        navController.removeOnDestinationChangedListener(navControllerListener)
    }


}

