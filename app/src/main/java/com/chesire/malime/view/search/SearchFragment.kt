package com.chesire.malime.view.search

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.chesire.malime.R
import com.chesire.malime.core.api.MalimeApi
import com.chesire.malime.core.api.SearchApi
import com.chesire.malime.core.flags.ItemType
import com.chesire.malime.core.flags.SupportedService
import com.chesire.malime.core.repositories.Library
import com.chesire.malime.databinding.FragmentSearchBinding
import com.chesire.malime.kitsu.api.KitsuManagerFactory
import com.chesire.malime.mal.api.MalManagerFactory
import com.chesire.malime.util.SharedPref
import timber.log.Timber

class SearchFragment : Fragment() {
    private var checkedOption = R.id.search_option_anime_choice
    private lateinit var viewModel: SearchViewModel
    private lateinit var viewAdapter: SearchViewAdapter
    private lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

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
                SearchViewModelFactory(
                    requireActivity().application,
                    api as SearchApi,
                    Library(requireContext(), api)
                )
            )
            .get(SearchViewModel::class.java)
        viewAdapter = SearchViewAdapter(viewModel)

        viewModel.apply {
            series.observe(this@SearchFragment,
                Observer {
                    if (it != null) {
                        viewAdapter.setCurrentItems(it)
                    }
                })
            searchItems.observe(this@SearchFragment,
                Observer {
                    if (it != null) {
                        viewAdapter.addSearchItems(it)
                    }
                })
        }
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
            vm = viewModel
            searchAllItems.apply {
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
            searchSearchTermEditText.setOnEditorActionListener { _, _, _ ->
                hideSystemKeyboard()
                viewModel.searchForSeries(
                    when (checkedOption) {
                        R.id.search_option_anime_choice -> ItemType.Anime
                        R.id.search_option_manga_choice -> ItemType.Manga
                        else -> {
                            Timber.e("Unknown search method selected")
                            ItemType.Unknown
                        }
                    }
                )
                true
            }
            searchOptionChoices.setOnCheckedChangeListener { _, checkedId ->
                checkedOption = checkedId
            }
        }.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_search, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun hideSystemKeyboard() {
        requireActivity().currentFocus?.let {
            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    companion object {
        const val tag = "SearchFragment"
        fun newInstance(): SearchFragment {
            val search2Fragment = SearchFragment()
            val args = Bundle()
            search2Fragment.arguments = args
            return search2Fragment
        }
    }
}