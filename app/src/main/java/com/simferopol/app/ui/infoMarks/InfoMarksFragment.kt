package com.simferopol.app.ui.infoMarks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.simferopol.app.R

class InfoMarksFragment : Fragment() {

    private lateinit var infoMarksVM: InfoMarksVM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        infoMarksVM =
            ViewModelProviders.of(this).get(InfoMarksVM::class.java)
        return inflater.inflate(R.layout.fragment_info_marks, container, false)
    }
}