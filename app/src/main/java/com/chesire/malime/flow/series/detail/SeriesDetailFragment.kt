package com.chesire.malime.flow.series.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.R
import com.chesire.malime.flow.ViewModelFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.view_bottom_sheet_header.viewBottomSheetProgress
import kotlinx.android.synthetic.main.view_bottom_sheet_header.viewBottomSheetSubtitle
import kotlinx.android.synthetic.main.view_bottom_sheet_header.viewBottomSheetTitle
import javax.inject.Inject

@LogLifecykle
class SeriesDetailFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProviders
            .of(requireActivity(), viewModelFactory)
            .get(SeriesDetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_series_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.model.observe(viewLifecycleOwner, Observer { model ->
            viewBottomSheetTitle.text = model.title
            viewBottomSheetSubtitle.text = model.userSeriesStatus.name
            viewBottomSheetProgress.text = "${model.progress} / ${model.totalLength}"
        })
    }

    companion object {
        const val TAG = "SeriesDetailFragment"
        fun newInstance() = SeriesDetailFragment()
    }
}
