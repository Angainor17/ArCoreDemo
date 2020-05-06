package com.ar.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ar.app.databinding.ActivityDemoBinding
import com.ar.app.ui.ar.ArFlyerActivity

class DemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)

        val binding: ActivityDemoBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_demo
        )
        binding.activity = this
        binding.lifecycleOwner = this
    }

    fun flyerAr() {
        val intent = Intent(this, ArFlyerActivity::class.java)
        startActivity(intent)
    }
}