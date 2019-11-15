package com.simferopol.app.utils.ui

import android.view.View
import androidx.viewpager.widget.PagerAdapter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import com.simferopol.app.databinding.StoryPagerItemBinding
import com.simferopol.app.ui.history.vm.HistoryVm

open class StoriesPagerAdapter : PagerAdapter() {

    private val list = ArrayList<HistoryVm>()

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`
    override fun getCount(): Int = list.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = LayoutInflater.from(container.context)
        val binding = StoryPagerItemBinding.inflate(layoutInflater, container, false)

        binding.vm = list[position]
        binding.executePendingBindings()
        binding.pagerTab.setupWithViewPager(container as ViewPager)
        binding.pagerTab.isTabIndicatorFullWidth = true
        container.addView(binding.root)

        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }

    fun setItems(newItems: ArrayList<HistoryVm>) {
        list.clear()
        list.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return list[position].name
    }
}