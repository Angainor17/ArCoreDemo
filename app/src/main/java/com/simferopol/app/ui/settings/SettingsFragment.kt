package com.simferopol.app.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.simferopol.app.databinding.FragmentSettingsBinding
import com.simferopol.app.ui.settings.vm.SettingsVm
import com.simferopol.app.ui.splashScreen.SplashScreenActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SettingsFragment : Fragment(), SettingsView {

    private val settingsVm = SettingsVm(this)

    override fun recreate() {
        GlobalScope.launch(Dispatchers.Main) {
            val intent = Intent(activity, SplashScreenActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            activity?.startActivity(intent)
            activity?.finish()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSettingsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.vm = settingsVm

        return binding.root
    }
}