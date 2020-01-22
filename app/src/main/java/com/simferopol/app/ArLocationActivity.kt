package com.simferopol.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.simferopol.app.ui.arLocation.view.ARLocationFragment

class ArLocationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        val fragment = ARLocationFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commitAllowingStateLoss()
    }
}