package com.chesire.malime.flow.series.list

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.chesire.malime.R
import com.chesire.malime.SharedPref
import com.chesire.malime.core.Resource
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.databinding.FragmentSeriesListBinding
import com.chesire.malime.flow.DialogHandler
import com.chesire.malime.flow.ViewModelFactory
import com.chesire.malime.flow.series.list.anime.AnimeFragment
import com.chesire.malime.flow.series.list.manga.MangaFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_series_list.fragmentSeriesListLayout
import kotlinx.android.synthetic.main.view_bottom_sheet.viewBottomSheetLayout
import kotlinx.android.synthetic.main.view_bottom_sheet.viewBottomSheetProgress
import kotlinx.android.synthetic.main.view_bottom_sheet.viewBottomSheetSubtitle
import kotlinx.android.synthetic.main.view_bottom_sheet.viewBottomSheetTitle
import timber.log.Timber
import javax.inject.Inject

/**
 * Provides a base fragment for the [AnimeFragment] & [MangaFragment] to inherit from, performing
 * most of the setup and interaction.
 */
@Suppress("TooManyFunctions")
abstract class SeriesListFragment :
    DaggerFragment(),
    SeriesInteractionListener,
    SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var dialogHandler: DialogHandler
    @Inject
    lateinit var sharedPref: SharedPref

    protected val viewModel by lazy {
        ViewModelProviders
            .of(this, viewModelFactory)
            .get(SeriesListViewModel::class.java)
    }

    private lateinit var seriesAdapter: SeriesAdapter
    private lateinit var sheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentSeriesListBinding.inflate(
        inflater,
        container,
        false
    ).apply {
        seriesAdapter = SeriesAdapter(this@SeriesListFragment, sharedPref)
        fragmentSeriesListRecyclerView.apply {
            adapter = seriesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        fragmentSeriesListFab.setOnClickListener {
            toSearch()
        }
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sheetBehavior = BottomSheetBehavior.from(viewBottomSheetLayout).also {
            it.state = STATE_HIDDEN
        }
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

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            SharedPref.FILTER_PREFERENCE -> seriesAdapter.performFilter()
            SharedPref.SORT_PREFERENCE -> seriesAdapter.performSort()
        }
    }

    override fun seriesSelected(imageView: ImageView, model: SeriesModel) {
        Timber.i("seriesSelected called with Model ${model.slug}")
        loadBottomSheet(model)
    }

    private fun loadBottomSheet(model: SeriesModel) {
        sheetBehavior.state = STATE_COLLAPSED
        viewBottomSheetTitle.text = model.title
        viewBottomSheetSubtitle.text = model.userSeriesStatus.name
        viewBottomSheetProgress.text = "${model.progress} / ${model.totalLength}"
    }

    override fun onPlusOne(model: SeriesModel, callback: () -> Unit) {
        Timber.i("Model ${model.slug} onPlusOne called")
        viewModel.updateSeries(model.userId, model.progress.inc(), model.userSeriesStatus) {
            if (it is Resource.Error) {
                Snackbar
                    .make(
                        fragmentSeriesListLayout,
                        getString(R.string.list_try_again, model.title),
                        Snackbar.LENGTH_LONG
                    )
                    .show()
            }
            callback()
        }
    }

    /**
     * Tell the current fragment to navigate to its search screen.
     */
    abstract fun toSearch()

    /**
     * Inform the adapter that a new series list has been provided.
     */
    fun newSeriesListProvided(newList: List<SeriesModel>) {
        Timber.d("New list provided, new count [${newList.count()}]")
        seriesAdapter.allItems = newList
    }
}
