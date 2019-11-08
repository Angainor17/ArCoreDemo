package com.simferopol.app.ui.monuments

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.simferopol.app.R
import com.simferopol.app.ui.monuments.vm.MonumentVm
import com.simferopol.app.utils.ui.FixedListAdapter
import kotlinx.android.synthetic.main.monument_list_item.view.*

@BindingAdapter("app:initMonumentsList")
fun initMonumentsList(view: RecyclerView, list: ArrayList<MonumentVm>) {
    val adapter = object : FixedListAdapter<MonumentVm>(R.layout.monument_list_item) {
        override fun onBindViewHolder(holder: VH<MonumentVm>, position: Int) {
            super.onBindViewHolder(holder, position)
            val item = list[position]
            holder.bind(item)
            holder.itemView.monumentImage.setOnTouchListener { view, motionEvent ->
                item.onItemTouch(view, motionEvent)
            }
        }
    }
    adapter.setItems(list)

    view.adapter = adapter
}



