package com.chesire.malime.flow.series.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.R
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.extensions.extraNotNull
import com.chesire.malime.flow.ViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.view_series_detail_header.seriesDetailHeaderSubtype
import kotlinx.android.synthetic.main.view_series_detail_header.seriesDetailHeaderTitle
import kotlinx.android.synthetic.main.view_series_detail_header.seriesDetailHeaderType
import javax.inject.Inject

@LogLifecykle
class SeriesDetailSheetFragment : BottomSheetDialogFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)
            .get<SeriesDetailViewModel>()
            .also { viewModel ->
                viewModel.setModel(seriesModel)
            }
    }
    private val seriesModel by extraNotNull<SeriesModel>(MODEL_BUNDLE_ID, null)

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_series_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
    }

    private fun initializeView() {
        with(viewModel.mutableModel) {
            seriesDetailHeaderTitle.text = seriesName
            seriesDetailHeaderType.text = seriesType
            seriesDetailHeaderSubtype.text = seriesSubType
        }
    }

    companion object {
        const val TAG = "SeriesDetailSheetFragment"
        private const val MODEL_BUNDLE_ID = "SeriesDetailSheetFragment_model"

        /**
         * Creates a new instance of the [SeriesDetailSheetFragment], using the [seriesModel] for
         * data.
         */
        fun newInstance(seriesModel: SeriesModel): SeriesDetailSheetFragment {
            return SeriesDetailSheetFragment().apply {
                arguments = bundleOf(MODEL_BUNDLE_ID to seriesModel)
            }
        }
    }
}
