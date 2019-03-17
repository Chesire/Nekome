package com.chesire.malime.flow.series.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProviders
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.databinding.FragmentSeriesDetailBinding
import com.chesire.malime.extensions.extraNotNull
import com.chesire.malime.flow.ViewModelFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class SeriesDetailFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val model by extraNotNull<SeriesModel>(MODEL_KEY)
    private val viewModel by lazy {
        ViewModelProviders
            .of(this, viewModelFactory)
            .get(SeriesDetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSeriesDetailBinding.inflate(inflater, container, false)
            .apply {
                vm = viewModel
                lifecycleOwner = viewLifecycleOwner
            }
            .root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.model = model
    }

    companion object {
        const val TAG = "SeriesDetailFragment"
        private const val MODEL_KEY = "SeriesDetailFragment#MODEL_KEY"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment SeriesDetailFragment.
         */
        fun newInstance(seriesModel: SeriesModel) = SeriesDetailFragment().apply {
            arguments = bundleOf(MODEL_KEY to seriesModel)
        }
    }
}
