package com.handcontrol.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.handcontrol.BR

class BaseRecyclerAdapter<D, T : BaseRecyclerAdapter.BaseAdapterListener>(
    @LayoutRes val layout: Int,
    private val dataSet: MutableList<D>,
    listener: T
) : RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        BaseViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                layout,
                parent,
                false
            )
        )

    override fun onBindViewHolder(viewHolder: BaseViewHolder, position: Int) {
        viewHolder.binding.setVariable(BR.item, getItem(position))
    }

    override fun getItemCount() = dataSet.size

    fun getItem(position: Int): D = dataSet[position]

    interface BaseAdapterListener

}

class BaseViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)