package com.chesire.malime.flow.series.list.anime

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.R
import com.chesire.malime.SharedPref
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.databinding.FragmentAnimeBinding
import com.chesire.malime.flow.DialogHandler
import com.chesire.malime.flow.ViewModelFactory
import com.chesire.malime.flow.series.list.SeriesAdapter
import com.chesire.malime.flow.series.list.SeriesInteractionListener
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_anime.fragmentAnimeToolbar
import timber.log.Timber
import javax.inject.Inject

@Suppress("TooManyFunctions")
@LogLifecykle
class AnimeFragment :
    DaggerFragment(),
    SeriesInteractionListener,
    SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var dialogHandler: DialogHandler
    @Inject
    lateinit var sharedPref: SharedPref
    private lateinit var seriesAdapter: SeriesAdapter

    private val viewModel by lazy {
        ViewModelProviders
            .of(this, viewModelFactory)
            .get(AnimeViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        seriesAdapter = SeriesAdapter(this, sharedPref)

        return FragmentAnimeBinding.inflate(inflater, container, false)
            .apply {
                fragmentAnimeRecyclerView.apply {
                    adapter = seriesAdapter
                    layoutManager = LinearLayoutManager(requireContext())
                    setHasFixedSize(true)
                }
                fragmentAnimeFab.setOnClickListener {
                    findNavController().navigate(AnimeFragmentDirections.toSearchFragment())
                }
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? AppCompatActivity)?.setSupportActionBar(fragmentAnimeToolbar)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.animeSeries.observe(
            viewLifecycleOwner,
            Observer {
                Timber.d("Anime has been updated, new count [${it.count()}]")
                seriesAdapter.allItems = it
            }
        )
    }

    override fun onStart() {
        super.onStart()
        sharedPref.subscribeToChanges(this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_series_list, menu)
    }

    override fun onStop() {
        sharedPref.unsubscribeFromChanges(this)
        super.onStop()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuSeriesListFilter ->
                dialogHandler.showFilterDialog(requireContext(), viewLifecycleOwner)
            R.id.menuSeriesListSort ->
                dialogHandler.showSortDialog(requireContext(), viewLifecycleOwner)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun seriesSelected(imageView: ImageView, model: SeriesModel) {
        Timber.i("Model ${model.slug} animeSelected called")

        findNavController().navigate(
            AnimeFragmentDirections.toSeriesDetailFragment(model),
            FragmentNavigatorExtras(imageView to model.title)
        )
    }

    override fun onPlusOne(model: SeriesModel) {
        Timber.i("Model ${model.slug} onPlusOne called")
        viewModel.updateSeries(model.userId, model.progress.inc(), model.userSeriesStatus)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            SharedPref.FILTER_PREFERENCE -> seriesAdapter.performFilter()
            SharedPref.SORT_PREFERENCE -> seriesAdapter.performSort()
        }
    }
}
