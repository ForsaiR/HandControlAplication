package com.handcontrol.ui.main.action

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.handcontrol.R
import com.handcontrol.base.BaseFragment
import com.handcontrol.databinding.FragmentActionDetailsBinding
import com.handcontrol.model.Action

class ActionFragment : BaseFragment<FragmentActionDetailsBinding, ActionViewModel>(
    ActionViewModel::class.java,
    R.layout.fragment_action_details
) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModelFactory = ActionViewModelFactory(
            arguments?.getSerializable(ARG_ACTION_KEY) as? Action
        )
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    companion object {
        const val ARG_ACTION_KEY = "action"
    }

}