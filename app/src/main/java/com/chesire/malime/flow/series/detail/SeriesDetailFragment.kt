package com.chesire.malime.flow.series.detail

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.R
import com.chesire.malime.core.flags.AsyncState
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.flow.ViewModelFactory
import com.chesire.malime.flow.series.list.SheetController
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.view_bottom_sheet_actions.bottomSheetDelete
import kotlinx.android.synthetic.main.view_bottom_sheet_actions.bottomSheetProgress
import kotlinx.android.synthetic.main.view_bottom_sheet_header.viewBottomSheetProgress
import kotlinx.android.synthetic.main.view_bottom_sheet_header.viewBottomSheetSubtitle
import kotlinx.android.synthetic.main.view_bottom_sheet_header.viewBottomSheetTitle
import timber.log.Timber
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

        bottomSheetProgress.setOnClickListener {
            viewModel.model.value?.let { model ->
                MaterialDialog(requireContext()).show {
                    input(
                        inputType = InputType.TYPE_CLASS_NUMBER,
                        prefill = model.progress.toString(),
                        hint = getString(R.string.series_detail_progress_hint, model.totalLength)
                    ) { _, charSequence ->
                        charSequence.toString().toIntOrNull()?.let {
                            viewModel.updateProgress(model, it)
                        }
                    }
                    message(R.string.series_detail_progress_message)
                    positiveButton(R.string.series_detail_progress_confirm)
                    negativeButton(R.string.series_detail_progress_cancel)
                    lifecycleOwner(viewLifecycleOwner)
                }
            }
        }
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

        observeModel()
        observeProgress()
        observeDelete()
    }

    private fun observeModel() {
        viewModel.model.observe(viewLifecycleOwner, Observer { model ->
            viewBottomSheetTitle.setText(model.title)
            viewBottomSheetSubtitle.setText(model.userSeriesStatus.name)
            viewBottomSheetProgress.setText("${model.progress} / ${model.totalLength}")
        })
    }

    private fun observeProgress() {
        viewModel.progressStatus.observe(viewLifecycleOwner, Observer { status ->
            // handle states
        })
    }

    private fun observeDelete() {
        viewModel.deletionStatus.observe(viewLifecycleOwner, Observer { status ->
            when (status) {
                is AsyncState.Loading -> {
                    // do something to disable the button maybe?
                }
                is AsyncState.Error -> onDeleteError(status.data)
                is AsyncState.Success -> {
                    // TODO: show a snackbar with undo button
                    (parentFragment as? SheetController)?.closeSheet()
                }
            }
        })
    }

    private fun onDeleteError(series: SeriesModel?) {
        val parent = parentFragment
        val parentView = parent?.view
        if (series == null || parent == null || parentView == null) {
            Timber.w("Could not run onDeleteError, a param was null")
            return
        }

        (parent as? SheetController)?.closeSheet()
        Snackbar.make(
            parentView,
            getString(R.string.series_detail_delete_failure_message, series.title),
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction(R.string.series_detail_delete_failure_retry) {
                viewModel.deleteModel(series)
            }
        }.show()
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
