package com.chesire.malime.view.maldisplay

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.chesire.malime.R
import com.chesire.malime.core.flags.ItemType
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.MalimeModel
import com.chesire.malime.databinding.FragmentMaldisplayBinding
import com.chesire.malime.util.SharedPref
import com.chesire.malime.util.UrlLoader
import com.chesire.malime.util.autoCleared
import com.chesire.malime.util.extension.getSeriesStatusStrings
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_maldisplay.maldisplay_swipe_refresh
import timber.log.Timber
import javax.inject.Inject

private const val ITEM_TYPE_BUNDLE_ID = "ITEM_TYPE_BUNDLE_ID"

class MalDisplayFragment : DaggerFragment(), ModelInteractionListener {
    private var binding by autoCleared<FragmentMaldisplayBinding>()
    private var viewAdapter by autoCleared<MalDisplayViewAdapter>()
    private lateinit var viewModel: MalDisplayViewModel
    private lateinit var type: ItemType
    private lateinit var recyclerView: RecyclerView

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var sharedPref: SharedPref
    @Inject
    lateinit var urlLoader: UrlLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = ItemType.getTypeForInternalId(arguments!!.getInt(ITEM_TYPE_BUNDLE_ID))

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return DataBindingUtil.inflate<FragmentMaldisplayBinding>(
            inflater,
            R.layout.fragment_maldisplay,
            container,
            false
        ).apply {
            binding = this
            maldisplayRecyclerView.apply {
                recyclerView = this
                setEmptyView(maldisplayEmptyView)
                setHasFixedSize(true)
                layoutManager =
                        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            GridLayoutManager(
                                requireContext(),
                                2,
                                OrientationHelper.VERTICAL,
                                false
                            )
                        } else {
                            LinearLayoutManager(requireContext())
                        }
            }
        }.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders
            .of(this, viewModelFactory)
            .get(MalDisplayViewModel::class.java)
            .apply {
                series.observe(this@MalDisplayFragment,
                    Observer {
                        it?.let {
                            viewAdapter.addAll(it.filter { it.type == type })
                        }
                    })
                updateAllStatus.observe(this@MalDisplayFragment,
                    Observer {
                        it?.let {
                            onUpdateAllStatusChange(it)
                        }
                    })
            }

        viewAdapter = MalDisplayViewAdapter(requireContext(), this, sharedPref)
        recyclerView.adapter = viewAdapter
        binding.vm = viewModel
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_options, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == R.id.menu_options_filter) {
            spawnFilterDialog()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun spawnFilterDialog() {
        val states = sharedPref.getFilter()

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.filter_dialog_title)
            .setMultiChoiceItems(
                UserSeriesStatus.getSeriesStatusStrings(requireContext()),
                states
            ) { _, which, isChecked ->
                states[which] = isChecked
            }
            .setPositiveButton(android.R.string.ok) { _, _ ->
                if (states.all { !it }) {
                    Timber.w("User tried to set all filter states to false")
                    Snackbar.make(
                        view!!.findViewById(R.id.maldisplay_layout),
                        R.string.filter_must_select,
                        Snackbar.LENGTH_LONG
                    ).show()
                } else {
                    sharedPref.setFilter(states)
                }
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun onUpdateAllStatusChange(status: UpdatingSeriesStatus) {
        when (status) {
            UpdatingSeriesStatus.Finished -> maldisplay_swipe_refresh.isRefreshing = false
            UpdatingSeriesStatus.Error -> {
                maldisplay_swipe_refresh.isRefreshing = false
                Snackbar.make(
                    maldisplay_swipe_refresh,
                    R.string.list_update_failure,
                    Snackbar.LENGTH_LONG
                ).show()
            }
            else -> {
                // nothing for now
            }
        }
    }

    override fun showSeriesProfile(model: MalimeModel) {
        urlLoader.loadSeries(requireContext(), sharedPref.getPrimaryService(), model)
    }

    override fun deleteSeries(model: MalimeModel, callback: (success: Boolean) -> Unit) {
        viewModel.deleteSeries(model, callback)
    }

    override fun updateSeries(
        model: MalimeModel,
        newProgress: Int,
        newStatus: UserSeriesStatus,
        callback: (success: Boolean) -> Unit
    ) {
        viewModel.updateSeries(model, newProgress, newStatus, callback)
    }

    companion object {
        const val tag = "MalDisplayFragment"
        const val malDisplayAnime = "MalDisplayFragmentAnime"
        const val malDisplayManga = "MalDisplayFragmentFragment"
        fun newInstance(type: ItemType): MalDisplayFragment {
            val malDisplayFragment = MalDisplayFragment()
            val args = Bundle()
            args.putInt(ITEM_TYPE_BUNDLE_ID, type.internalId)
            malDisplayFragment.arguments = args
            return malDisplayFragment
        }
    }
}
