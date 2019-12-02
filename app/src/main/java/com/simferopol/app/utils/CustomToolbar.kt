package com.simferopol.app.utils

import android.content.Context
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Display
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.simferopol.app.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.app_bar_nav_drawer.view.*

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

    override fun setLogo(resId: Int) {
        findLogo()?.setImageResource(resId)
    }

    override fun setLogo(drawable: Drawable?) {
        findLogo()?.setImageDrawable(drawable)
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
        val imageView = findLogo()
        imageView.let {
            imageView!!.getLocationOnScreen(location)
            imageView!!.x = imageView.x + (-location.get(0) + screenWidth / 2 - imageView.width / 2)
        }
    }

    private fun findLabel(): TextView? = findViewById(R.id.title)
    private fun findLogo(): ImageView? = findViewById(R.id.logo)
    private fun findWeather(): LinearLayout = findViewById(R.id.weather)

    /*** Получение размера экрана */
    private fun getScreenSize(): Point {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display: Display = windowManager.defaultDisplay
        val screenSize = Point()
        display.getSize(screenSize)
        return screenSize
    }

    fun setWeather(temperature: String, icon: String?) {
        val weatherView = findWeather()
        weatherView.visibility = View.VISIBLE
        val text =
            temperature + ' ' + String(Character.toChars(0x00B0)) + context.resources.getString(R.string.temperature)
        weatherView.weatherTemperature.text = text
        val iconUrl = "http://openweathermap.org/img/w/$icon.png"
        if (!icon.isNullOrEmpty()) {
            Picasso.get()
                .load(iconUrl)
                .into(weatherView.weatherIcon)
        }
    }

    fun hideWeather() {
        findWeather()!!.visibility = View.GONE
    }
}


