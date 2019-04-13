package com.chesire.malime.flow.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.databinding.FragmentSettingsBinding
import dagger.android.support.DaggerFragment

@LogLifecykle
class SettingsFragment : DaggerFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentSettingsBinding.inflate(inflater, container, false).root
}
