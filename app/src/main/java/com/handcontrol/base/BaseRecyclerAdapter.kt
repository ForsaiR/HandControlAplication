package com.handcontrol.base

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.handcontrol.BR
import com.handcontrol.model.ExecutableItem

class BaseRecyclerAdapter<D, T : BaseAdapterListener>(
    @LayoutRes val layout: Int,
    data: List<D>,
    val listener: T
) : RecyclerView.Adapter<BaseViewHolder>() {

    var dataSet: List<D>
        get() = differ.currentList
        set(value) {
            differ.submitList(value.toList())
        }

    private var differCallback: DiffUtil.ItemCallback<D> = object : DiffUtil.ItemCallback<D>() {
        override fun areItemsTheSame(oldItem: D, newItem: D): Boolean =
            if (oldItem is ExecutableItem && newItem is ExecutableItem) {
                oldItem.id == newItem.id
            } else false


        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: D, newItem: D): Boolean {
            return newItem == oldItem
        }
    }

    private val differ: AsyncListDiffer<D> = AsyncListDiffer(this, differCallback)

    init {
        dataSet = data
    }

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
        viewHolder.binding.setVariable(BR.listener, listener)
        viewHolder.binding.setVariable(BR.position, position)
    }

    override fun getItemCount() = dataSet.size

    fun getItem(position: Int): D = dataSet[position]

}

interface BaseAdapterListener

class BaseViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)