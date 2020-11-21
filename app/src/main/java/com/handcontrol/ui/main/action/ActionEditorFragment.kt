package com.handcontrol.ui.main.action

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.handcontrol.R
import com.handcontrol.base.BaseFragment
import com.handcontrol.databinding.FragmentActionDetailsEditorBinding
import com.handcontrol.model.Action
import com.handcontrol.ui.main.action.ActionFragment.Companion.ARG_ACTION_KEY

class ActionEditorFragment : BaseFragment<FragmentActionDetailsEditorBinding, ActionViewModel>(
    ActionViewModel::class.java,
    R.layout.fragment_action_details_editor
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
}