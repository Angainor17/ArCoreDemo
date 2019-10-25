package com.simferopol.app.navigation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

const val BUNDLE_TAG = "bundle"
const val FRAGMENT_TAG = "fragment"

fun showScreen(context: AppCompatActivity, clazz: Class<out Fragment>, bundle: Bundle = Bundle()) {
    val intent = Intent(context, ContainerActivity::class.java)
    intent.putExtra(BUNDLE_TAG, bundle)
    intent.putExtra(FRAGMENT_TAG, clazz.canonicalName)

    context.startActivityForResult(intent, 0)
}