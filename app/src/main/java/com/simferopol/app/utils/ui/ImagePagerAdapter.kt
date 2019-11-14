package com.simferopol.app.utils.ui

import android.view.View
import androidx.viewpager.widget.PagerAdapter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.simferopol.app.R
import com.squareup.picasso.Picasso
import android.widget.LinearLayout

open class ImagePagerAdapter : PagerAdapter() {

    private val imagesList = ArrayList<String>()

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`
    override fun getCount(): Int = imagesList.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = LayoutInflater.from(container.context)
        val itemView = layoutInflater.inflate(R.layout.image_pager_item, container, false)
        val imageView = itemView.findViewById(R.id.imageView) as ImageView

        Picasso.get().load(imagesList[position]).into(imageView)
        container.addView(itemView)

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }

    fun setItems(newItems: ArrayList<String>) {
        imagesList.clear()
        imagesList.addAll(newItems)
        notifyDataSetChanged()
    }
}