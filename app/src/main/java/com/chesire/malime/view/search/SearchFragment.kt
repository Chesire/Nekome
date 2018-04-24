package com.chesire.malime.view.search

import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ProgressBar
import android.widget.RadioGroup
import com.chesire.malime.R
import com.chesire.malime.mal.MalManager
import com.chesire.malime.models.Entry
import com.chesire.malime.room.AnimeDao
import com.chesire.malime.room.MalimeDatabase
import com.chesire.malime.util.SharedPref
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

private const val nsfwAllowedKey = "nsfwAllowed"
private const val checkedOptionKey = "checkedOption"
private const val searchTextKey = "searchText"
private const val searchItemsBundleId = "searchItems"
private const val currentAnimeKey = "currentAnime"
private const val currentMangaKey = "currentManga"

class SearchFragment : Fragment(), SearchInteractionListener {

    private var disposables = CompositeDisposable()
    private var nsfwAllowed = false
    private var checkedOption = 0

    private lateinit var malManager: MalManager
    private lateinit var animeDao: AnimeDao

    private lateinit var searchText: TextInputEditText
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: SearchViewAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPref = SharedPref(context!!)
        malManager = MalManager(sharedPref.getAuth())
        animeDao = MalimeDatabase.getInstance(context!!).animeDao()

        viewManager = LinearLayoutManager(context!!)
        viewAdapter = SearchViewAdapter(this)
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

        progressBar = view.findViewById(R.id.search_loading_indicator)

        recyclerView = view.findViewById<RecyclerView>(R.id.search_all_items).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }

        if (savedInstanceState == null) {
            checkedOption = searchRadioGroup.checkedRadioButtonId
            nsfwAllowed = nsfwCheckbox.isChecked
            executeGetLocalAnime()
            // executeGetLocalManga()
        } else {
            checkedOption = savedInstanceState.getInt(checkedOptionKey)
            nsfwAllowed = savedInstanceState.getBoolean(nsfwAllowedKey)
            searchText.setText(savedInstanceState.getString(searchTextKey))
            viewAdapter.update(savedInstanceState.getParcelableArrayList(searchItemsBundleId))
            viewAdapter.setCurrentAnime(savedInstanceState.getParcelableArrayList(currentAnimeKey))
            viewAdapter.setCurrentManga(savedInstanceState.getParcelableArrayList(currentMangaKey))
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
        outState.putParcelableArrayList(searchItemsBundleId, viewAdapter.getAll())
        outState.putParcelableArrayList(currentAnimeKey, viewAdapter.getCurrentAnime())
        outState.putParcelableArrayList(currentMangaKey, viewAdapter.getCurrentManga())

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

        progressBar.visibility = View.VISIBLE
        disposables.add(searchMethod
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Timber.i("Found ${it.count()} items")
                    viewAdapter.update(it)
                    progressBar.visibility = View.INVISIBLE
                },
                {
                    Timber.e(it, "Error performing the search")
                    progressBar.visibility = View.INVISIBLE
                }
            ))
    }

    private fun executeGetLocalAnime() {
        Timber.d("Getting local anime")
        disposables.add(
            animeDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.d("Successfully got local anime, loading into adapter")
                    viewAdapter.setCurrentAnime(it)
                })
        )
    }

    override fun onAddPressed(selectedEntry: Entry) {
        if (selectedEntry.chapters == null) {
            Timber.d("Anime - [${selectedEntry.title}] selected")
        } else {
            Timber.d("Manga - [${selectedEntry.title}] selected")
        }
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