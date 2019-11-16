package com.simferopol.app.utils.dataBindings

import androidx.databinding.BindingAdapter
import com.budiyev.android.codescanner.*
import com.simferopol.app.ui.infoMarks.QrCodeVM

@BindingAdapter("initQrCamera")
fun initQrCamera(view: CodeScannerView, vm: QrCodeVM) {
    val codeScanner = CodeScanner(view.context, view)

    codeScanner.camera = CodeScanner.CAMERA_BACK
    codeScanner.formats = CodeScanner.ALL_FORMATS

    codeScanner.autoFocusMode = AutoFocusMode.SAFE
    codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
    codeScanner.isAutoFocusEnabled = true
    codeScanner.isFlashEnabled = false

    codeScanner.decodeCallback = DecodeCallback {
        vm.scannerResult(it)
    }
    codeScanner.errorCallback = ErrorCallback {
        vm.errorCallback(it)
    }

    view.setOnClickListener {
        codeScanner.startPreview()
    }

    vm.resumeAction = {
        codeScanner.startPreview()
    }
    vm.pauseAction = {
        codeScanner.stopPreview()
    }
}
