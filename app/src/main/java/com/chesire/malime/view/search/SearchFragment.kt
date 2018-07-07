package com.chesire.malime.view.search

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
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
import com.chesire.malime.core.api.MalimeApi
import com.chesire.malime.core.api.SearchApi
import com.chesire.malime.core.flags.ItemType
import com.chesire.malime.core.flags.SupportedService
import com.chesire.malime.core.repositories.Library
import com.chesire.malime.core.room.MalimeDatabase
import com.chesire.malime.databinding.FragmentSearchBinding
import com.chesire.malime.injection.Injectable
import com.chesire.malime.kitsu.api.KitsuAuthorizer
import com.chesire.malime.kitsu.api.KitsuManagerFactory
import com.chesire.malime.mal.api.MalAuthorizer
import com.chesire.malime.mal.api.MalManagerFactory
import com.chesire.malime.util.SharedPref
import com.chesire.malime.util.autoCleared
import com.chesire.malime.util.extension.hideSystemKeyboard
import timber.log.Timber
import javax.inject.Inject

class SearchFragment : Fragment(), Injectable {
    private var checkedOption = R.id.search_option_anime_choice
    private var binding by autoCleared<FragmentSearchBinding>()
    private lateinit var viewModel: SearchViewModel
    private lateinit var viewAdapter: SearchViewAdapter
    private lateinit var recyclerView: RecyclerView

    @Inject
    lateinit var sharedPref: SharedPref

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val api: MalimeApi = if (sharedPref.getPrimaryService() == SupportedService.Kitsu) {
            Timber.i("Found Kitsu as supported service")
            KitsuManagerFactory().get(KitsuAuthorizer(requireContext()))
        } else {
            Timber.i("Found Mal as supported service")
            MalManagerFactory().get(MalAuthorizer(requireContext()))
        }

        viewModel = ViewModelProviders
            .of(
                this,
                SearchViewModelFactory(
                    requireActivity().application,
                    api as SearchApi,
                    Library(api, MalimeDatabase.getInstance(requireContext()).malimeDao())
                )
            )
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

        viewAdapter = SearchViewAdapter(viewModel)
        recyclerView.adapter = viewAdapter
        binding.vm = viewModel
    }

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
            searchSearchTermEditText.setOnEditorActionListener { _, _, _ ->
                requireActivity().hideSystemKeyboard(requireContext())
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