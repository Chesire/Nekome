package com.chesire.malime.view.search

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import com.chesire.malime.R
import com.chesire.malime.core.flags.ItemType
import com.chesire.malime.core.models.MalimeModel
import com.chesire.malime.databinding.FragmentSearchBinding
import com.chesire.malime.util.SharedPref
import com.chesire.malime.util.UrlLoader
import com.chesire.malime.util.autoCleared
import com.chesire.malime.util.extension.hideSystemKeyboard
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_search.search_search_term_edit_text
import javax.inject.Inject

class SearchFragment : DaggerFragment(), SearchInteractionListener {
    private var checkedOption = R.id.search_option_anime_choice
    private var binding by autoCleared<FragmentSearchBinding>()
    private lateinit var viewModel: SearchViewModel
    private lateinit var viewAdapter: SearchViewAdapter
    private lateinit var recyclerView: RecyclerView

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var sharedPref: SharedPref
    @Inject
    lateinit var urlLoader: UrlLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return DataBindingUtil.inflate<FragmentSearchBinding>(
            inflater,
            R.layout.fragment_search,
            container,
            false
        ).apply {
            binding = this
            searchAllItems.apply {
                recyclerView = this
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
            searchOptionChoices.setOnCheckedChangeListener { _, checkedId ->
                checkedOption = checkedId
            }
        }.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders
            .of(this, viewModelFactory)
            .get(SearchViewModel::class.java)
            .apply {
                series.observe(this@SearchFragment,
                    Observer {
                        it?.let {
                            viewAdapter.setCurrentItems(it)
                        }
                    })
                searchItems.observe(this@SearchFragment,
                    Observer {
                        it?.let {
                            viewAdapter.addSearchItems(it)
                        }
                    })
            }
        search_search_term_edit_text.setOnEditorActionListener { _, _, _ ->
            requireActivity().hideSystemKeyboard(requireContext())
            viewModel.searchForSeries(
                when (checkedOption) {
                    R.id.search_option_anime_choice -> ItemType.Anime
                    R.id.search_option_manga_choice -> ItemType.Manga
                    else -> ItemType.Unknown
                }
            ) {
                Snackbar.make(binding.root, getString(it), Snackbar.LENGTH_LONG).show()
            }
            true
        }

        viewAdapter = SearchViewAdapter(this)
        recyclerView.adapter = viewAdapter
        binding.vm = viewModel
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_search, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun addNewSeries(selectedSeries: MalimeModel, callback: (Boolean) -> Unit) {
        viewModel.addNewSeries(selectedSeries, callback)
    }

    override fun showSeriesProfile(selectedSeries: MalimeModel) {
        urlLoader.loadSeries(requireContext(), sharedPref.primaryService, selectedSeries)
    }

    companion object {
        const val tag = "SearchFragment"
        fun newInstance(): SearchFragment {
            val searchFragment = SearchFragment()
            val args = Bundle()
            searchFragment.arguments = args
            return searchFragment
        }
    }
}
