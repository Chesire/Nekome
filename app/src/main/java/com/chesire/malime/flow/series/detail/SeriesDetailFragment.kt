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
import com.chesire.malime.core.flags.AsyncState
import com.chesire.malime.flow.ViewModelFactory
import com.chesire.malime.flow.series.list.SheetController
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.view_bottom_sheet_actions.bottomSheetDelete
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
        observeDelete()
    }

    private fun observeModel() {
        viewModel.model.observe(viewLifecycleOwner, Observer { model ->
            viewBottomSheetTitle.setText(model.title)
            viewBottomSheetSubtitle.setText(model.userSeriesStatus.name)
            viewBottomSheetProgress.setText("${model.progress} / ${model.totalLength}")
        })
    }

    private fun observeDelete() {
        viewModel.deletionStatus.observe(viewLifecycleOwner, Observer { status ->
            when (status) {
                is AsyncState.Loading -> {
                    // do something to disable the button maybe?
                }
                is AsyncState.Error -> {
                    val parent = parentFragment
                    val parentView = parent?.view
                    if (parent == null || parentView == null) {
                        Timber.w("Parent fragment was null, not displaying snackbar")
                        return@Observer
                    }

                    (parent as? SheetController)?.closeSheet()
                    Snackbar.make(
                        parentView,
                        getString(
                            R.string.series_detail_delete_failure_message,
                            status.data?.title
                        ),
                        Snackbar.LENGTH_INDEFINITE
                    ).apply {
                        status.data?.let { seriesModel ->
                            setAction(R.string.series_detail_delete_failure_retry) {
                                viewModel.deleteModel(seriesModel)
                            }
                        }
                    }.show()
                }
                is AsyncState.Success -> {
                    // TODO: show a snackbar with undo button
                    (parentFragment as? SheetController)?.closeSheet()
                }
            }
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
