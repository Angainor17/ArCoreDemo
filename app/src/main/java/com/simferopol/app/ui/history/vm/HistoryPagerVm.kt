package com.simferopol.app.ui.history.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HistoryPagerVm(list: ArrayList<HistoryVm>) : ViewModel() {

    val list = MutableLiveData(ArrayList<HistoryVm>())

    init {

        }
    }
