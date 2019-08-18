package com.chesire.malime.flow.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.R
import com.mikepenz.aboutlibraries.LibsBuilder
import dagger.android.support.DaggerFragment

@LogLifecykle
class OssFragment : DaggerFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_oss, container, false).also {
        childFragmentManager.commit {
            replace(R.id.fragmentOssLayout, LibsBuilder().supportFragment())
        }
    }
}
