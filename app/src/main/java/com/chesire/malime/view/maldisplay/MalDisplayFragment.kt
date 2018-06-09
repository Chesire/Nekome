package com.chesire.malime.view.maldisplay

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import com.chesire.malime.R
import com.chesire.malime.core.repositories.Library
import com.chesire.malime.databinding.FragmentMaldisplayBinding
import com.chesire.malime.kitsu.api.KitsuManagerFactory
import com.chesire.malime.util.SharedPref
import kotlinx.android.synthetic.main.fragment_maldisplay.maldisplay_recycler_view
import kotlinx.android.synthetic.main.fragment_maldisplay.maldisplay_swipe_refresh

class MalDisplayFragment : Fragment() {
    private lateinit var viewModel: MalDisplayViewModel
    private lateinit var viewAdapter: MalDisplayViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = SharedPref(requireContext())
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

        viewAdapter = MalDisplayViewAdapter()

        viewModel.series.observe(this,
            Observer {
                if (it != null) {
                    viewAdapter.addAll(it)
                }
            })
        viewModel.updatingStatus.observe(this,
            Observer {
                if (it != null) {
                    when (it) {
                        UpdatingSeriesStatus.Updating -> {
                            // nothing for now
                        }
                        UpdatingSeriesStatus.Finished -> {
                            maldisplay_swipe_refresh.isRefreshing = false
                        }
                        UpdatingSeriesStatus.Error -> {
                            maldisplay_swipe_refresh.isRefreshing = false
                            Snackbar.make(
                                maldisplay_swipe_refresh,
                                "Failed",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }

                    }
                }
            })
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
        }.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_options, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    companion object {
        const val tag = "MalDisplayFragment"
        fun newInstance(): MalDisplayFragment {
            val malDisplayFragment = MalDisplayFragment()
            val args = Bundle()
            malDisplayFragment.arguments = args
            return malDisplayFragment
        }
    }
}