package com.simferopol.app.utils

import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.view.Display
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.simferopol.app.R


class CustomToolbar : Toolbar {

    private val location = IntArray(2)
    private var screenWidth = 0

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        screenWidth = getScreenSize().x
    }

    override fun setTitle(resId: Int) {
        super.setTitle(resId)
        findLabel()?.setText(resId)
    }

    override fun setTitle(title: CharSequence?) {
        super.setTitle(title)
        findLabel()?.text = title
    }

    /*** Нахождение центра View */
    override fun onLayout(
        changed: Boolean,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        super.onLayout(changed, left, top, right, bottom)
        val textView = findLabel()
        textView?.let {
            textView.getLocationOnScreen(location)
            textView.x = textView.x + (-location.get(0) + screenWidth / 2 - textView.width / 2)
        }
    }

    private fun findLabel(): TextView? = findViewById(R.id.title)

    /*** Получение размера экрана */
    private fun getScreenSize(): Point {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display: Display = windowManager.defaultDisplay
        val screenSize = Point()
        display.getSize(screenSize)
        return screenSize
    }

}


