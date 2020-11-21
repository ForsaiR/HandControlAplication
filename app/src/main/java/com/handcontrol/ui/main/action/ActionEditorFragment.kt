package com.handcontrol.ui.main.action

import com.handcontrol.R
import com.handcontrol.base.BaseFragment
import com.handcontrol.databinding.FragmentActionDetailsEditorBinding

class ActionEditorFragment : BaseFragment<FragmentActionDetailsEditorBinding, ActionViewModel>(
    ActionViewModel::class.java,
    R.layout.fragment_action_details_editor
) {
}