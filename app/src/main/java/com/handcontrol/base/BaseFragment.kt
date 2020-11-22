package com.handcontrol.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

abstract class BaseFragment<DB : ViewDataBinding, VM : ViewModel>(
    protected val viewModelClass: Class<VM>,
    private val layoutRes: Int
) : Fragment() {

    lateinit var binding: DB
    abstract val viewModel: VM
    open var viewModelFactory: ViewModelProvider.Factory? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        viewModel = if (viewModelFactory == null)
//            ViewModelProvider(this).get(viewModelClass)
//        else ViewModelProvider(this, viewModelFactory!!).get(viewModelClass)
        binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        binding.let {
            it.setVariable(BR.viewModel, viewModel)
            it.lifecycleOwner = viewLifecycleOwner
            it.executePendingBindings()
        }
        return binding.root
    }
}