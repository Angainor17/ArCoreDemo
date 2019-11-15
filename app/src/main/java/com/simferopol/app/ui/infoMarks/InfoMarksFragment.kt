package com.simferopol.app.ui.infoMarks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.simferopol.app.databinding.FragmentInfoMarksBinding

const val CAMERA_REQUEST_CODE = 1

class InfoMarksFragment : Fragment() {

    private lateinit var vm: InfoMarksVM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vm = ViewModelProviders.of(this).get(InfoMarksVM::class.java)

        val binding = FragmentInfoMarksBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.vm = vm

        return binding.root
    }

    override fun onResume() {
        vm.checkPermission(activity!!)
        vm.resumeAction?.invoke()
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        vm.pauseAction?.invoke()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST_CODE) {
            vm.resumeAction?.invoke()
        }
    }
}