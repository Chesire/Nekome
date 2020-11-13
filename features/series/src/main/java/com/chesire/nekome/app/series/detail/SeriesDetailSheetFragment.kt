package com.chesire.nekome.app.series.detail

import android.content.Context
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.nekome.app.series.R
import com.chesire.nekome.app.series.databinding.FragmentSeriesDetailBinding
import com.chesire.nekome.core.extensions.extraNotNull
import com.chesire.nekome.core.extensions.hide
import com.chesire.nekome.core.extensions.show
import com.chesire.nekome.core.flags.AsyncState
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.models.SeriesModel
import com.chesire.nekome.core.viewmodel.ViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

/**
 * Fragment to display the series detail in a [BottomSheetDialogFragment].
 */
@LogLifecykle
@AndroidEntryPoint
class SeriesDetailSheetFragment : BottomSheetDialogFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by viewModels<SeriesDetailViewModel> { viewModelFactory }
    private val seriesModel by extraNotNull<SeriesModel>(MODEL_BUNDLE_ID, null)
    private var _binding: FragmentSeriesDetailBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding not set" }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentSeriesDetailBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setModel(seriesModel)
        initializeView()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initializeView() {
        observeUpdatingStatus()
        with(viewModel.mutableModel) {
            binding.detailHeader.headerTitle.text = seriesName
            binding.detailHeader.headerType.text = seriesType
            binding.detailHeader.headerSubtype.text = seriesSubType
            binding.detailHeader.headerSeriesStatus.text = seriesStatus
            setupSeriesStatusListener(this)
            setupInitialSeriesStatus(this)
            setupProgress(this)
        }
        setupConfirmation()
    }

    private fun observeUpdatingStatus() {
        fun startInProgressState() {
            binding.detailConfirmation.confirmationConfirm.isEnabled = false
            binding.detailConfirmation.confirmationConfirm.hide(invisible = true)
            binding.detailConfirmation.confirmationProgress.show()
        }

        fun endInProgressState() {
            binding.detailConfirmation.confirmationConfirm.isEnabled = true
            binding.detailConfirmation.confirmationConfirm.show()
            binding.detailConfirmation.confirmationProgress.hide(invisible = true)
        }

        viewModel.updatingStatus.observe(viewLifecycleOwner) { result ->
            when (result) {
                is AsyncState.Loading -> startInProgressState()
                is AsyncState.Error -> {
                    endInProgressState()
                    view?.let { view ->
                        Snackbar.make(
                            view.findViewById(R.id.seriesDetailLayout),
                            getString(R.string.series_detail_failure, result.data?.seriesName),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
                is AsyncState.Success -> dismiss()
            }
        }
    }

    private fun setupSeriesStatusListener(model: MutableSeriesModel) {
        var lastCheckedId = View.NO_ID
        binding.detailStatus.statusGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == View.NO_ID) {
                Timber.w("Tried to uncheck chip, resetting to be checked")
                group.check(lastCheckedId)
                return@setOnCheckedChangeListener
            }
            lastCheckedId = checkedId
            Timber.d("Chip checked, selected is now $checkedId")

            when (checkedId) {
                R.id.statusChipCurrent -> model.userSeriesStatus = UserSeriesStatus.Current
                R.id.statusChipCompleted -> model.userSeriesStatus = UserSeriesStatus.Completed
                R.id.statusChipDropped -> model.userSeriesStatus = UserSeriesStatus.Dropped
                R.id.statusChipOnHold -> model.userSeriesStatus = UserSeriesStatus.OnHold
                R.id.statusChipPlanned -> model.userSeriesStatus = UserSeriesStatus.Planned
            }
        }
    }

    private fun setupInitialSeriesStatus(model: MutableSeriesModel) {
        binding.detailStatus.statusGroup.check(
            when (model.userSeriesStatus) {
                UserSeriesStatus.Current -> R.id.statusChipCurrent
                UserSeriesStatus.Completed -> R.id.statusChipCompleted
                UserSeriesStatus.Dropped -> R.id.statusChipDropped
                UserSeriesStatus.OnHold -> R.id.statusChipOnHold
                UserSeriesStatus.Planned -> R.id.statusChipPlanned
                else -> 0
            }
        )
    }

    private fun setupProgress(model: MutableSeriesModel) {
        binding.detailProgress.progressValue.apply {
            setText("${model.seriesProgress}")
            filters = arrayOf(
                RangeInputFilter(model.seriesLengthValue),
                InputFilter.LengthFilter(4)
            )
            doAfterTextChanged {
                it?.toString()?.toIntOrNull()?.let { newProgress ->
                    model.seriesProgress = newProgress
                }
            }
        }
        binding.detailProgress.progressOutOf.text = getString(
            R.string.series_detail_progress_out_of, model.seriesLength
        )
    }

    private fun setupConfirmation() =
        binding.detailConfirmation.confirmationConfirm.setOnClickListener {
            viewModel.sendUpdate(viewModel.mutableModel)
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
