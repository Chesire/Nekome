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
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.chesire.malime.R
import com.chesire.malime.SharedPref
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.databinding.FragmentSeriesListBinding
import com.chesire.malime.flow.DialogHandler
import com.chesire.malime.flow.ViewModelFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_series_list.fragmentSeriesListToolbar
import timber.log.Timber
import javax.inject.Inject

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

    private lateinit var seriesAdapter: SeriesAdapter

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

        return FragmentSeriesListBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            fragmentSeriesListRecyclerView.apply {
                adapter = seriesAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
            fragmentSeriesListFab.setOnClickListener {
                toSearch()
            }
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? AppCompatActivity)?.setSupportActionBar(fragmentSeriesListToolbar)
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
        toDetails(model, imageView to model.title)
    }

    abstract fun toDetails(model: SeriesModel, navigatorExtras: Pair<View, String>)
    abstract fun toSearch()

    fun newSeriesListProvided(newList: List<SeriesModel>) {
        Timber.d("New list provided, new count [${newList.count()}]")
        seriesAdapter.allItems = newList
    }
}
