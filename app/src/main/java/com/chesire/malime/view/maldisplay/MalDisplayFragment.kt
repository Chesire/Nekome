package com.chesire.malime.view.maldisplay

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.chesire.malime.R
import com.chesire.malime.core.flags.ItemType
import com.chesire.malime.core.repositories.Library
import com.chesire.malime.databinding.FragmentMaldisplayBinding
import com.chesire.malime.kitsu.api.KitsuManagerFactory
import com.chesire.malime.util.SharedPref
import kotlinx.android.synthetic.main.fragment_maldisplay.maldisplay_swipe_refresh

private const val itemTypeBundleId = "itemTypeBundleId"

class MalDisplayFragment : Fragment() {
    private lateinit var viewModel: MalDisplayViewModel
    private lateinit var viewAdapter: MalDisplayViewAdapter
    private lateinit var type: ItemType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        type = ItemType.getTypeForInternalId(arguments!!.getInt(itemTypeBundleId))

        val sharedPref = SharedPref(requireContext())
        viewAdapter = MalDisplayViewAdapter()

        viewModel = ViewModelProviders
            .of(
                this,
                MalDisplayViewModelFactory(
                    requireActivity().application,
                    Library(
                        requireContext(),
                        KitsuManagerFactory().get(
                            sharedPref.getAuth(),
                            sharedPref.getUserId()
                        )
                    )
                )
            )
            .get(MalDisplayViewModel::class.java)
            .apply {
                series.observe(this@MalDisplayFragment,
                    Observer {
                        if (it != null) {
                            viewAdapter.addAll(it.filter { it.type == type })
                        }
                    })
                updatingStatus.observe(this@MalDisplayFragment,
                    Observer {
                        if (it != null) {
                            onUpdateStatusChange(it)
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
                layoutManager = LinearLayoutManager(requireContext())
                adapter = viewAdapter
                //(itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            }
        }.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_options, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == R.id.menu_options_filter) {
            //spawnFilterDialog()
            // when this is pressed, we need to handle it
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun onUpdateStatusChange(status: UpdatingSeriesStatus) {
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