package com.chesire.nekome.app.series.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onCancel
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.chesire.nekome.app.series.R
import com.chesire.nekome.app.series.detail.SeriesDetailSheetFragment
import com.chesire.nekome.app.series.list.anime.AnimeFragment
import com.chesire.nekome.app.series.list.manga.MangaFragment
import com.chesire.nekome.core.DialogHandler
import com.chesire.nekome.core.SharedPref
import com.chesire.nekome.core.flags.AsyncState
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.models.SeriesModel
import com.chesire.nekome.core.viewmodel.ViewModelFactory
import com.chesire.nekome.server.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_series_list.fragmentSeriesListLayout
import kotlinx.android.synthetic.main.fragment_series_list.fragmentSeriesListRecyclerView
import timber.log.Timber
import javax.inject.Inject

/**
 * Provides a base fragment for the [AnimeFragment] & [MangaFragment] to inherit from, performing
 * most of the setup and interaction.
 */
abstract class SeriesListFragment : DaggerFragment(), SeriesInteractionListener {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var dialogHandler: DialogHandler
    @Inject
    lateinit var sharedPref: SharedPref

    /**
     * Flag for which type of series should be displayed in this fragment instance.
     */
    protected abstract val seriesType: SeriesType

    private val viewModel by viewModels<SeriesListViewModel> { viewModelFactory }
    private lateinit var seriesAdapter: SeriesAdapter
    private var seriesDetail: SeriesDetailSheetFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_series_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        seriesAdapter = SeriesAdapter(this, sharedPref)
        fragmentSeriesListRecyclerView.apply {
            adapter = seriesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            val itemTouchHelper = ItemTouchHelper(SwipeToDelete(seriesAdapter))
            itemTouchHelper.attachToRecyclerView(this)
        }

        viewModel.series.observe(
            viewLifecycleOwner,
            Observer { series ->
                val newList = series.filter { it.type == seriesType }
                Timber.d("New list provided, new count [${newList.count()}]")
                seriesAdapter.submitList(newList)
            }
        )
        observeSeriesDeletion()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_series_list, menu)
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
        Timber.i("seriesSelected called with Model ${model.slug}")

        if (seriesDetail?.isVisible == true) {
            Timber.w("Attempt to open series detail while already visible")
        } else {
            seriesDetail = SeriesDetailSheetFragment.newInstance(model).also {
                it.show(childFragmentManager, SeriesDetailSheetFragment.TAG)
            }
        }
    }

    override fun onPlusOne(model: SeriesModel, callback: () -> Unit) {
        Timber.i("Model ${model.slug} onPlusOne called")
        viewModel.updateSeries(model.userId, model.progress.inc(), model.userSeriesStatus) {
            if (it is Resource.Error) {
                Snackbar.make(
                    fragmentSeriesListLayout,
                    getString(R.string.series_list_try_again, model.title),
                    Snackbar.LENGTH_LONG
                ).show()
            }
            callback()
        }
    }

    override fun seriesDelete(model: SeriesModel, callback: (Boolean) -> Unit) {
        MaterialDialog(requireContext()).show {
            title(text = getString(R.string.series_list_delete_title, model.title))
            positiveButton(R.string.series_list_delete_confirm) {
                Timber.d("Deletion confirmed for series ${model.slug}")
                callback(true)
                viewModel.deleteSeries(model)
            }
            negativeButton(R.string.series_list_delete_cancel) {
                callback(false)
            }
            onCancel {
                callback(false)
            }
            lifecycleOwner(viewLifecycleOwner)
        }
    }

    private fun observeSeriesDeletion() {
        viewModel.deletionStatus.observe(viewLifecycleOwner, Observer { state ->
            if (state is AsyncState.Error && state.error == SeriesListDeleteError.DeletionFailure) {
                Snackbar.make(
                    fragmentSeriesListLayout,
                    R.string.series_list_delete_failure,
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(R.string.series_list_delete_retry) {
                    state.data?.let { seriesModel ->
                        viewModel.deleteSeries(seriesModel)
                    }
                }.show()
            }
        })
    }
}
