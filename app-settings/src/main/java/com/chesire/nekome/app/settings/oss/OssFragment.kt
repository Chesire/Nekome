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
    // Commented out libraries are pulled in automatically, because the library already includes the
    // xml file required, or it is supplied manually. We only need to specify the libraries that the
    // about libraries plugin provides.
    private val includedLibraries = arrayOf(
        // "AboutLibraries",
        // "androidencryptionhelper",
        "Dagger2",
        "glide",
        // "lifecyklelog",
        // "liveevent",
        "materialdialogs",
        "moshi",
        "Retrofit",
        "Timber"
    )

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
        .withAutoDetect(false)
        .withLicenseShown(false)
        .withLicenseDialog(true)
        .withAboutIconShown(false)
        .withVersionShown(false)
        .withFields(R.string::class.java.fields)
        .withLibraries(*includedLibraries)
        .supportFragment()
}
