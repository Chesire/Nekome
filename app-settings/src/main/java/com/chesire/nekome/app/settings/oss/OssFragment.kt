package com.chesire.nekome.app.settings.oss

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.nekome.app.settings.R
import com.mikepenz.aboutlibraries.LibsBuilder
import dagger.android.support.DaggerFragment

/**
 * Fragment that displays information about open source licenses that are used.
 */
@LogLifecykle
class OssFragment : DaggerFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_oss, container, false).also {
        childFragmentManager.commit {
            replace(R.id.fragmentOssLayout, createLicensePage())
        }
    }

    private fun createLicensePage() = LibsBuilder()
        .withLicenseShown(false)
        .withLicenseDialog(true)
        .withAboutIconShown(false)
        .withVersionShown(false)
        .supportFragment()
}
