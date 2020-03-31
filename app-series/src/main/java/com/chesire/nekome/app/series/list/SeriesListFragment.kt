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
import com.chesire.nekome.app.series.SeriesPreferences
import com.chesire.nekome.app.series.databinding.FragmentSeriesListBinding
import com.chesire.nekome.app.series.detail.SeriesDetailSheetFragment
import com.chesire.nekome.app.series.list.anime.AnimeFragment
import com.chesire.nekome.app.series.list.manga.MangaFragment
import com.chesire.nekome.app.series.list.view.SeriesAdapter
import com.chesire.nekome.app.series.list.view.SwipeToDelete
import com.chesire.nekome.core.flags.AsyncState
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.models.SeriesModel
import com.chesire.nekome.core.viewmodel.ViewModelFactory
import com.chesire.nekome.server.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
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
    lateinit var seriesPreferences: SeriesPreferences

    /**
     * Flag for which type of series should be displayed in this fragment instance.
     */
    protected abstract val seriesType: SeriesType

    private var _binding: FragmentSeriesListBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding not set" }
    private val viewModel by viewModels<SeriesListViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentSeriesListBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val seriesAdapter = SeriesAdapter(this, seriesPreferences)
        binding.listContent.apply {
            adapter = seriesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            val itemTouchHelper = ItemTouchHelper(SwipeToDelete(seriesAdapter))
            itemTouchHelper.attachToRecyclerView(this)
        }

        binding.refreshLayout.setOnRefreshListener { startRefreshingSeries() }

        viewModel.series.observe(
            viewLifecycleOwner,
            Observer { series ->
                val newList = series.filter { it.type == seriesType }
                Timber.d("New list provided, new count [${newList.count()}]")
                seriesAdapter.submitList(newList)
                if (binding.listContent.emptyView == null) {
                    // Set the empty view here so it doesn't show on load before we get series
                    Timber.d("Setting in the RecyclerViews empty view")
                    binding.listContent.emptyView = binding.listEmpty.root
                }
            }
        )

        observeSeriesDeletion()
        observeSeriesRefresh()
    }

    override fun onDestroyView() {
        // If this isn't removed here it can still display when we come back to this view
        binding.listContent.emptyView = null
        _binding = null
        super.onDestroyView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_series_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuFilter -> dialogHandler.showFilterDialog(requireContext(), viewLifecycleOwner)
            R.id.menuSort -> dialogHandler.showSortDialog(requireContext(), viewLifecycleOwner)
            R.id.menuRefresh -> {
                binding.refreshLayout.isRefreshing = true
                startRefreshingSeries()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun seriesSelected(imageView: ImageView, model: SeriesModel) {
        Timber.i("seriesSelected called with Model ${model.slug}")

        SeriesDetailSheetFragment
            .newInstance(model)
            .show(parentFragmentManager, SeriesDetailSheetFragment.TAG)
    }

    override fun onPlusOne(model: SeriesModel, callback: () -> Unit) {
        Timber.i("Model ${model.slug} onPlusOne called")
        viewModel.updateSeries(model.userId, model.progress.inc(), model.userSeriesStatus) {
            if (it is Resource.Error) {
                Snackbar.make(
                    binding.seriesListLayout,
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
                    binding.seriesListLayout,
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

    private fun startRefreshingSeries() {
        viewModel.refreshAllSeries()
    }

    private fun endRefreshingSeries() {
        binding.refreshLayout.isRefreshing = false
    }

    private fun observeSeriesRefresh() {
        viewModel.refreshStatus.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is AsyncState.Success -> endRefreshingSeries()
                is AsyncState.Error -> {
                    Timber.w("Error trying to refresh series - ${state.error}")
                    endRefreshingSeries()
                    Snackbar.make(
                        binding.seriesListLayout,
                        R.string.series_list_refresh_error,
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        })
    }
}
