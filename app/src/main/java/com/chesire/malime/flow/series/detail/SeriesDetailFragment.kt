package com.chesire.malime.flow.series.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.R
import com.chesire.malime.flow.ViewModelFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.view_bottom_sheet_actions.bottomSheetDelete
import kotlinx.android.synthetic.main.view_bottom_sheet_header.viewBottomSheetProgress
import kotlinx.android.synthetic.main.view_bottom_sheet_header.viewBottomSheetSubtitle
import kotlinx.android.synthetic.main.view_bottom_sheet_header.viewBottomSheetTitle
import javax.inject.Inject

/**
 * A fragment to display details about a series, and provide buttons to interact with the series.
 */
@LogLifecykle
class SeriesDetailFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get<SeriesDetailViewModel>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_series_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheetDelete.setOnClickListener {
            viewModel.model.value?.let { model ->
                MaterialDialog(requireContext()).show {
                    message(text = getString(R.string.series_detail_delete_message, model.title))
                    positiveButton(R.string.series_detail_delete_confirm) {
                        viewModel.deleteModel(model)
                    }
                    negativeButton(R.string.series_detail_delete_cancel)
                    lifecycleOwner(viewLifecycleOwner)
                }
            }
        }

        viewModel.model.observe(viewLifecycleOwner, Observer { model ->
            viewBottomSheetTitle.setText(model.title)
            viewBottomSheetSubtitle.setText(model.userSeriesStatus.name)
            viewBottomSheetProgress.setText("${model.progress} / ${model.totalLength}")
        })
        viewModel.deletionStatus.observe(viewLifecycleOwner, Observer { status ->
            // show a snackbar for success or error?
            // maybe add an undo button?
        })
    }

    companion object {
        /**
         * Identifying tag for the fragment.
         */
        const val TAG = "SeriesDetailFragment"

        /**
         * Constructs a new instance of [SeriesDetailFragment].
         */
        fun newInstance() = SeriesDetailFragment()
    }
}
