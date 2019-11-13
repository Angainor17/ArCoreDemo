package com.simferopol.app.utils.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.simferopol.app.BR

open class FixedListAdapter<T>(
    @LayoutRes private val layoutRes: Int
) : RecyclerView.Adapter<FixedListAdapter<T>.VH<T>>() {

    private val list = ArrayList<T>()

    fun setItems(newItems: ArrayList<T>) {
        list.clear()
        list.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH<T> {
        val layoutInflater = LayoutInflater.from(parent.context)
        return VH(DataBindingUtil.inflate(layoutInflater, layoutRes, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: VH<T>, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    inner class VH<T>(
        private val binding: ViewDataBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: T) {
            binding.setVariable(BR.vm, viewModel)
            binding.executePendingBindings()
        }
    }
}