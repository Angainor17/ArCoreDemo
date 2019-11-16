package com.simferopol.app.ui.infoMarks

import com.google.zxing.Result

class InfoMarksVM : QrCodeVM() {

    override fun scannerResult(result: Result) {
        //TODO implement
    }

    override fun errorCallback(exception: Exception) {
        //TODO implement
    }
}
