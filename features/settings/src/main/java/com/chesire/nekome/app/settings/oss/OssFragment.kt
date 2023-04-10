package com.chesire.nekome.app.settings.oss

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chesire.nekome.app.settings.R
import dagger.hilt.android.AndroidEntryPoint
import java.lang.reflect.Field
import javax.inject.Inject

/**
 * Fragment that displays information about open source licenses that are used.
 */
@AndroidEntryPoint
class OssFragment : Fragment() {

    @Inject
    lateinit var fields: Array<Field>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_oss, container, false).also {
    }
}
