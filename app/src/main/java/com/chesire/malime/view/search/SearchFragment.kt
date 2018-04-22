package com.chesire.malime.view.search

import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioGroup
import com.chesire.malime.R
import com.chesire.malime.mal.MalManager
import com.chesire.malime.util.SharedPref
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class SearchFragment : Fragment() {
    private val nsfwAllowedKey = "nsfwAllowed"
    private val checkedOptionKey = "checkedOption"
    private val searchTextKey = "searchText"

    private var disposables = CompositeDisposable()
    private var nsfwAllowed = false
    private var checkedOption = 0

    private lateinit var malManager: MalManager
    private lateinit var searchText: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPref = SharedPref(context!!)
        malManager = MalManager(sharedPref.getAuth())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        val searchRadioGroup = view.findViewById<RadioGroup>(R.id.search_option_choices)
        searchRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            checkedOption = checkedId
            executeSearch()
        }

        val nsfwCheckbox = view.findViewById<CheckBox>(R.id.search_option_nsfw_allowed)
        nsfwCheckbox.setOnClickListener {
            nsfwAllowed = (it as CheckBox).isChecked
            executeSearch()
        }

        searchText = view.findViewById(R.id.search_search_term_edit_text)
        searchText.setOnEditorActionListener { _, _, _ ->
            executeSearch()
            true
        }

        if (savedInstanceState == null) {
            checkedOption = searchRadioGroup.checkedRadioButtonId
            nsfwAllowed = nsfwCheckbox.isChecked
        } else {
            checkedOption = savedInstanceState.getInt(checkedOptionKey)
            nsfwAllowed = savedInstanceState.getBoolean(nsfwAllowedKey)
            searchText.setText(savedInstanceState.getString(searchTextKey))
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        disposables = CompositeDisposable()
    }

    override fun onPause() {
        super.onPause()
        disposables.clear()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(nsfwAllowedKey, nsfwAllowed)
        outState.putInt(checkedOptionKey, checkedOption)
        outState.putString(searchTextKey, searchText.text.toString())
        super.onSaveInstanceState(outState)
    }

    private fun executeSearch() {
        if (searchText.text.isNullOrBlank()) {
            Timber.w("No text entered to search for")
            return
        }

        val searchMethod = when (checkedOption) {
            R.id.search_option_anime_choice -> malManager.searchForAnime(searchText.text.toString())
            R.id.search_option_manga_choice -> malManager.searchForManga(searchText.text.toString())
            else -> {
                Timber.e("Unknown search method selected")
                return
            }
        }

        disposables.add(searchMethod
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Timber.i(it.toString())
                    // Add to the view adapter
                },
                {
                    Timber.e(it, "Error performing the search")
                }
            ))
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