package com.simferopol.app.ui.splashScreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.simferopol.app.R
import com.simferopol.app.databinding.ActivitySplashBinding
import com.simferopol.app.navDrawer.NavDrawer

class SplashScreenActivity : AppCompatActivity(), ISplashScreenVIew {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)

        val binding: ActivitySplashBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_splash
        )
        binding.vm = SplashScreenVm(this)
        binding.lifecycleOwner = this
    }


    override fun startBaseApp() {
        val intent = Intent(this, NavDrawer::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        finish()
        application.startActivity(intent)
    }

    override fun onBackPressed() {

    }
}