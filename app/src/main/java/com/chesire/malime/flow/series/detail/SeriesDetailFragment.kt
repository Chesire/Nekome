package com.chesire.malime.flow.series.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.databinding.FragmentSeriesDetailBinding
import com.chesire.malime.flow.ViewModelFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_series_detail.fragmentSeriesDetailHeaderImage
import kotlinx.android.synthetic.main.fragment_series_detail.fragmentSeriesDetailImageView
import kotlinx.android.synthetic.main.fragment_series_detail.fragmentSeriesDetailToolbar
import javax.inject.Inject

@LogLifecykle
class SeriesDetailFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val args by navArgs<SeriesDetailFragmentArgs>()
    private val viewModel by lazy {
        ViewModelProviders
            .of(this, viewModelFactory)
            .get(SeriesDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentSeriesDetailBinding.inflate(inflater, container, false)
        .apply {
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? AppCompatActivity)?.let { activity ->
            activity.setSupportActionBar(fragmentSeriesDetailToolbar)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        fragmentSeriesDetailImageView.transitionName = args.series.title
        Glide.with(this)
            .load(args.series.posterImage.smallest?.url)
            .into(fragmentSeriesDetailImageView)
        Glide.with(this)
            .load(args.series.coverImage.smallest?.url)
            .into(fragmentSeriesDetailHeaderImage)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.model = args.series
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> findNavController().navigateUp()
        }
        return super.onOptionsItemSelected(item)
    }
}
