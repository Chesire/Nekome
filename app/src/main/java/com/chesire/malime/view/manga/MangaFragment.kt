package com.chesire.malime.view.manga

import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.chesire.malime.MalStates
import com.chesire.malime.R
import com.chesire.malime.mal.MalManager
import com.chesire.malime.models.Manga
import com.chesire.malime.models.UpdateManga
import com.chesire.malime.room.MalimeDatabase
import com.chesire.malime.room.MangaDao
import com.chesire.malime.util.SharedPref
import com.chesire.malime.view.MalModelInteractionListener
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

private const val mangaItemsBundleId = "mangaItems"

class MangaFragment : Fragment(),
    SharedPreferences.OnSharedPreferenceChangeListener,
    MalModelInteractionListener<Manga, UpdateManga> {

    private var disposables = CompositeDisposable()
    private lateinit var sharedPref: SharedPref
    private lateinit var username: String
    private lateinit var malManager: MalManager
    private lateinit var mangaDao: MangaDao

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: MangaViewAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        val requiredContext = requireContext()

        sharedPref = SharedPref(requiredContext)
        username = sharedPref.getUsername()
        malManager = MalManager(sharedPref.getAuth())
        mangaDao = MalimeDatabase.getInstance(requiredContext).mangaDao()

        viewManager = LinearLayoutManager(requiredContext)
        viewAdapter = MangaViewAdapter(sharedPref, this)
        sharedPref.registerOnChangeListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_maldisplay, container, false)

        swipeRefreshLayout = view.findViewById(R.id.maldisplay_swipe_refresh)
        swipeRefreshLayout.setOnRefreshListener {
            executeGetLatestManga()
        }

        recyclerView = view.findViewById<RecyclerView>(R.id.maldisplay_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }

        if (savedInstanceState == null) {
            executeGetLocalManga()
        } else {
            viewAdapter.addAll(savedInstanceState.getParcelableArrayList(mangaItemsBundleId))
        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_options, menu)
        super.onCreateOptionsMenu(menu, inflater)
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
        outState.putParcelableArrayList(mangaItemsBundleId, viewAdapter.getAll())
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        sharedPref.unregisterOnChangeListener(this)
        super.onDestroy()
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
        val states = sharedPref.getAnimeFilter()
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.filter_dialog_title)
            .setMultiChoiceItems(R.array.manga_states, states, { _, which, isChecked ->
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
                    sharedPref.setAnimeFilter(states)
                }
            })
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key != null &&
            (key.contains(sharedPref.preferenceAnimeFilter) || key.contains(sharedPref.preferenceAnimeSortOption))
        ) {
            viewAdapter.filter.filter("")
        }
    }

    override fun onImageClicked(model: Manga) {
        CustomTabsIntent.Builder()
            .build()
            .launchUrl(context, Uri.parse(model.getMalUrl()))
    }

    override fun onLongClick(originalModel: Manga, updateModel: UpdateManga, callback: () -> Unit) {
        var state = MalStates.getMalStateForId(originalModel.myStatus!!)!!.surfaceId
        var executing = false
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.malitem_update_series_state_dialog_title)
            .setSingleChoiceItems(R.array.anime_states, state, { _, which ->
                state = which
            })
            .setPositiveButton(android.R.string.ok, { _, _ ->
                executing = true
                updateModel.setSeriesStatus(state)
                executeUpdateMal(originalModel, updateModel, callback)
            })
            .setNegativeButton(android.R.string.cancel, null)
            .setOnDismissListener {
                Timber.d("Dismissing")
                if (!executing) {
                    callback()
                }
            }
            .show()
    }

    override fun onSeriesUpdate(
        originalModel: Manga,
        updateModel: UpdateManga,
        callback: () -> Unit
    ) {
        var showDialog = false
        val alertBuilder = AlertDialog.Builder(requireContext())
            .setNegativeButton(android.R.string.no, null)
            .setOnDismissListener {
                executeUpdateMal(originalModel, updateModel, callback)
            }

        if (updateModel.chapter < updateModel.totalChapters
            && updateModel.status == MalStates.COMPLETED.id
        ) {
            if (sharedPref.getAutoUpdateSeriesState()) {
                updateModel.setToReadingState()
            } else {
                showDialog = true
                alertBuilder
                    .setTitle(R.string.malitem_update_series_reverted_title)
                    .setMessage(R.string.malitem_update_series_reverted_body)
                    .setPositiveButton(android.R.string.yes, { _, _ ->
                        updateModel.setToReadingState()
                    })
            }
        } else if (updateModel.chapter == updateModel.totalChapters
            && updateModel.status != MalStates.COMPLETED.id
        ) {
            if (sharedPref.getAutoUpdateSeriesState()) {
                updateModel.setToCompleteState()
            } else {
                showDialog = true
                alertBuilder
                    .setTitle(R.string.malitem_update_series_complete_title)
                    .setMessage(R.string.malitem_update_series_complete_body)
                    .setPositiveButton(android.R.string.yes, { _, _ ->
                        updateModel.setToCompleteState()
                    })
            }
        }

        if (showDialog) {
            alertBuilder.show()
        } else {
            executeUpdateMal(originalModel, updateModel, callback)
        }
    }

    private fun executeGetLocalManga() {
        Timber.d("Getting local manga")

        disposables.add(
            mangaDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isEmpty()) {
                        Timber.w("Local manga list was empty")
                    } else {
                        Timber.d("Successfully got local manga, loading into adapter")
                        viewAdapter.addAll(it)
                    }
                })
        )
    }

    private fun executeGetLatestManga() {
        Timber.d("Getting latest manga from MAL")

        disposables.add(malManager.getAllManga(username)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    Timber.d("Successfully got latest manga from MAL")

                    if (result.second == null) {
                        Timber.w("Found 0 series from MAL")

                        // We got a response, but was missing data, likely there are no series
                        executeClearLocalDb()
                        viewAdapter.clearAll()
                    } else {
                        val mangas = result.second as List<Manga>
                        Timber.d("Found ${mangas.count()} series from MAL")

                        // Got everything ok, save it
                        executeSaveToLocalDb(mangas)
                        viewAdapter.addAll(mangas)
                    }

                    swipeRefreshLayout.isRefreshing = false
                },
                { _ ->
                    Timber.e("Failed to get latest manga from MAL")
                    swipeRefreshLayout.isRefreshing = false
                }
            ))
    }

    private fun executeUpdateMal(originalModel: Manga, model: UpdateManga, callback: () -> Unit) {
        Timber.d("Sending update to MAL for manga - [%s]", originalModel)

        disposables.add(malManager.updateManga(model)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { _ ->
                    Timber.d("Successfully updated manga on MAL")

                    callback()
                    executeUpdateInLocalDb(originalModel)
                    viewAdapter.updateItem(model)
                },
                { _ ->
                    Timber.e("Error trying to update manga on MAL")

                    callback()
                    Snackbar.make(
                        recyclerView,
                        String.format(
                            getString(R.string.malitem_update_series_failure),
                            model.title
                        ), Snackbar.LENGTH_LONG
                    ).show()
                }
            ))
    }

    private fun executeClearLocalDb() {
        Timber.d("Clearing local DB for all manga")

        // Looks like this doesn't have to be disposed off
        Completable
            .fromAction({
                mangaDao.clear()
            })
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    private fun executeSaveToLocalDb(mangas: List<Manga>) {
        Timber.d("Updating local DB for all manga")

        // Looks like this doesn't have to be disposed off
        Completable
            .fromAction({
                mangaDao.insertAll(mangas)
            })
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    private fun executeUpdateInLocalDb(manga: Manga) {
        Timber.d("Updating local DB for manga - [%s]", manga)

        // Looks like this doesn't have to be disposed off
        Completable
            .fromAction({
                mangaDao.update(manga)
            })
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    companion object {
        const val tag = "MangaFragment"
        fun newInstance(): MangaFragment {
            val mangaFragment = MangaFragment()
            val args = Bundle()
            mangaFragment.arguments = args
            return mangaFragment
        }
    }
}