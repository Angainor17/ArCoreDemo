package com.simferopol.app.navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.simferopol.app.R

class ContainerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val extras = intent?.extras!!

        val fragment = Class.forName(extras.getString(FRAGMENT_TAG, "")).newInstance() as Fragment
        fragment.arguments = extras.getBundle(BUNDLE_TAG) ?: Bundle()

        setContentView(R.layout.activity_container)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}