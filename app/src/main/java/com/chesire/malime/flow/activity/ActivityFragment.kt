package com.chesire.malime.flow.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.databinding.FragmentActivityBinding
import dagger.android.support.DaggerFragment

@LogLifecykle
class ActivityFragment : DaggerFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentActivityBinding.inflate(inflater, container, false).root
}
