package com.chesire.malime.view.maldisplay

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.chesire.malime.R
import com.chesire.malime.core.api.MalimeApi
import com.chesire.malime.core.flags.ItemType
import com.chesire.malime.core.flags.SupportedService
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.repositories.Library
import com.chesire.malime.databinding.FragmentMaldisplayBinding
import com.chesire.malime.kitsu.api.KitsuManagerFactory
import com.chesire.malime.mal.api.MalManagerFactory
import com.chesire.malime.util.SharedPref
import com.chesire.malime.util.extension.getSeriesStatusStrings
import kotlinx.android.synthetic.main.fragment_maldisplay.maldisplay_swipe_refresh
import timber.log.Timber

private const val itemTypeBundleId = "itemTypeBundleId"

class MalDisplayFragment : Fragment() {
    private lateinit var viewModel: MalDisplayViewModel
    private lateinit var viewAdapter: MalDisplayViewAdapter
    private lateinit var sharedPref: SharedPref
    private lateinit var type: ItemType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        type = ItemType.getTypeForInternalId(arguments!!.getInt(itemTypeBundleId))
        sharedPref = SharedPref(requireContext())

        val api: MalimeApi = if (sharedPref.getPrimaryService() == SupportedService.Kitsu) {
            Timber.i("Found Kitsu as supported service")
            KitsuManagerFactory().get(sharedPref, sharedPref.getUserId())
        } else {
            Timber.i("Found Mal as supported service")
            MalManagerFactory().get(sharedPref, sharedPref.getUsername())
        }

        viewModel = ViewModelProviders
            .of(
                this,
                MalDisplayViewModelFactory(
                    requireActivity().application,
                    Library(requireContext(), api)
                )
            )
            .get(MalDisplayViewModel::class.java)

        viewAdapter = MalDisplayViewAdapter(viewModel, SharedPref(requireContext()))
        viewModel.apply {
            series.observe(this@MalDisplayFragment,
                Observer {
                    if (it != null) {
                        viewAdapter.addAll(it.filter { it.type == type })
                    }
                })
            updateAllStatus.observe(this@MalDisplayFragment,
                Observer {
                    if (it != null) {
                        onUpdateAllStatusChange(it)
                    }
                })
        }
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
            vm = viewModel
            maldisplayRecyclerView.apply {
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
                adapter = viewAdapter
            }
        }.root
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
                states,
                { _, which, isChecked ->
                    states[which] = isChecked
                })
            .setPositiveButton(android.R.string.ok, { _, _ ->
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
            })
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun onUpdateAllStatusChange(status: UpdatingSeriesStatus) {
        when (status) {
            UpdatingSeriesStatus.Finished -> {
                maldisplay_swipe_refresh.isRefreshing = false
            }
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

    companion object {
        const val tag = "MalDisplayFragment"
        const val malDisplayAnime = "MalDisplayFragmentAnime"
        const val malDisplayManga = "MalDisplayFragmentFragment"
        fun newInstance(type: ItemType): MalDisplayFragment {
            val malDisplayFragment = MalDisplayFragment()
            val args = Bundle()
            args.putInt(itemTypeBundleId, type.internalId)
            malDisplayFragment.arguments = args
            return malDisplayFragment
        }
    }
}