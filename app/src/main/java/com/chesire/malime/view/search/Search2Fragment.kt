package com.chesire.malime.view.search

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.chesire.malime.R
import com.chesire.malime.core.api.MalimeApi
import com.chesire.malime.core.api.SearchApi
import com.chesire.malime.core.flags.SupportedService
import com.chesire.malime.core.repositories.Library
import com.chesire.malime.databinding.FragmentSearchBinding
import com.chesire.malime.kitsu.api.KitsuManagerFactory
import com.chesire.malime.mal.api.MalManagerFactory
import com.chesire.malime.util.SharedPref
import timber.log.Timber

class Search2Fragment : Fragment() {
    private lateinit var sharedPref: SharedPref
    private lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        sharedPref = SharedPref(requireContext())

        val api = if (sharedPref.getPrimaryService() == SupportedService.Kitsu) {
            Timber.i("Found Kitsu as supported service")
            KitsuManagerFactory().get(sharedPref.getAuth(), sharedPref.getUserId())
        } else {
            Timber.i("Found Mal as supported service")
            MalManagerFactory().get(sharedPref.getAuth(), sharedPref.getUsername())
        }

        viewModel = ViewModelProviders
            .of(
                this,
                SearchViewModelFactory(
                    requireActivity().application,
                    api as SearchApi,
                    Library(requireContext(), api as MalimeApi)
                )
            )
            .get(SearchViewModel::class.java)
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
        const val tag = "Search2Fragment"
        fun newInstance(): Search2Fragment {
            val search2Fragment = Search2Fragment()
            val args = Bundle()
            search2Fragment.arguments = args
            return search2Fragment
        }
    }
}