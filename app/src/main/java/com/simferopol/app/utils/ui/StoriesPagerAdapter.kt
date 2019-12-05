package com.simferopol.app.utils.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.simferopol.app.App
import com.simferopol.app.databinding.StoryPagerItemBinding
import com.simferopol.app.providers.audio.IAudioProvider
import com.simferopol.app.ui.history.vm.HistoryVm
import kotlinx.android.synthetic.main.audio_player_element.view.*
import org.kodein.di.generic.instance

open class StoriesPagerAdapter : PagerAdapter() {

    private val list = ArrayList<HistoryVm>()
    private val audioProvider by App.kodein.instance<IAudioProvider>()

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`
    override fun getCount(): Int = list.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = LayoutInflater.from(container.context)
        val binding = StoryPagerItemBinding.inflate(layoutInflater, container, false)
        binding.vm = list[position]
        val itemVm = list[position]
        binding.executePendingBindings()
        binding.pagerTab.setupWithViewPager(container as ViewPager)
        binding.pagerTab.isTabIndicatorFullWidth = true

        binding.player.play_button.isActivated = false
        audioProvider.stopAudio()
        var audioUrl = itemVm.audio
        if (!audioUrl.isNullOrEmpty()) {
            binding.player.visibility = View.VISIBLE
            audioProvider.progressBar(binding.player.progressBar)
            binding.player.play_button.setOnClickListener {
                binding.player.play_button.isActivated = !binding.player.play_button.isActivated
                audioProvider.playClickListener(audioUrl)
            }
        }
        container.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
        audioProvider.stopAudio()
    }

    fun setItems(newItems: ArrayList<HistoryVm>) {
        list.clear()
        list.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return list[position].name
    }

    fun onPageChanged() {
        audioProvider.stopAudio()
    }
}