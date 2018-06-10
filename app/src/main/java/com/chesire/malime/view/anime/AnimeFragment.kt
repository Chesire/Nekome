package com.chesire.malime.view.anime

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
import com.chesire.malime.R
import com.chesire.malime.mal.MalStates
import com.chesire.malime.mal.api.MalManager
import com.chesire.malime.mal.api.MalManagerFactory
import com.chesire.malime.mal.models.Anime
import com.chesire.malime.mal.models.UpdateAnime
import com.chesire.malime.mal.room.AnimeDao
import com.chesire.malime.mal.room.MalimeDatabase
import com.chesire.malime.util.SharedPref
import com.chesire.malime.util.preferenceFilter
import com.chesire.malime.util.preferenceSort
import com.chesire.malime.view.MalModelInteractionListener
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

private const val animeItemsBundleId = "animeItems"

class AnimeFragment : Fragment(),
    SharedPreferences.OnSharedPreferenceChangeListener,
    MalModelInteractionListener<Anime, UpdateAnime> {

    private var disposables = CompositeDisposable()
    private lateinit var sharedPref: SharedPref
    private lateinit var malManager: MalManager
    private lateinit var animeDao: AnimeDao

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: AnimeViewAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        val requiredContext = requireContext()

        sharedPref = SharedPref(requiredContext)
        malManager =
                MalManagerFactory().get(
                    sharedPref.getAuth(),
                    sharedPref.getUsername()
                )
        animeDao = MalimeDatabase.getInstance(requiredContext).animeDao()

        viewManager = LinearLayoutManager(requiredContext)
        viewAdapter = AnimeViewAdapter(sharedPref, this)
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
            executeGetLatestAnime()
        }

        recyclerView = view.findViewById<RecyclerView>(R.id.maldisplay_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }

        if (savedInstanceState == null) {
            executeGetLocalAnime()
        } else {
            viewAdapter.addAll(savedInstanceState.getParcelableArrayList(animeItemsBundleId))
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
        outState.putParcelableArrayList(animeItemsBundleId, viewAdapter.getAll())
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
            .setMultiChoiceItems(R.array.anime_states, states, { _, which, isChecked ->
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

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, id: String?) {
        if (id != null &&
            (id.contains(preferenceFilter) || id.contains(preferenceSort))
        ) {
            viewAdapter.filter.filter("")
        }
    }

    override fun onImageClicked(model: Anime) {
        CustomTabsIntent.Builder()
            .build()
            .launchUrl(context, Uri.parse(model.getMalUrl()))
    }

    override fun onLongClick(
        originalModel: Anime,
        updateModel: UpdateAnime,
        callback: () -> Unit
    ) {
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
        originalModel: Anime,
        updateModel: UpdateAnime,
        callback: () -> Unit
    ) {
        var showDialog = false
        val alertBuilder = AlertDialog.Builder(requireContext())
            .setNegativeButton(android.R.string.no, null)
            .setOnDismissListener {
                executeUpdateMal(originalModel, updateModel, callback)
            }

        if (updateModel.episode < updateModel.totalEpisodes
            && updateModel.status == MalStates.COMPLETED.id
        ) {
            if (sharedPref.getAutoUpdateSeriesState()) {
                updateModel.setToWatchingState()
            } else {
                showDialog = true
                alertBuilder
                    .setTitle(R.string.malitem_update_series_reverted_title)
                    .setMessage(R.string.malitem_update_series_reverted_body)
                    .setPositiveButton(android.R.string.yes, { _, _ ->
                        updateModel.setToWatchingState()
                    })
            }
        } else if (updateModel.episode == updateModel.totalEpisodes
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

    private fun executeGetLocalAnime() {
        Timber.d("Getting local anime")

        disposables.add(
            animeDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isEmpty()) {
                        Timber.w("Local anime list was empty")
                    } else {
                        Timber.d("Successfully got local anime, loading into adapter")
                        viewAdapter.addAll(it)
                    }
                })
        )
    }

    private fun executeGetLatestAnime() {
        Timber.d("Getting latest anime from MAL")

        disposables.add(malManager.getAllAnime()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    Timber.d("Successfully got latest anime from MAL")

                    if (result.second == null) {
                        Timber.w("Found 0 series from MAL")

                        // We got a response, but was missing data, likely there are no series
                        executeClearLocalDb()
                        viewAdapter.clearAll()
                    } else {
                        val animes = result.second as List<Anime>
                        Timber.d("Found ${animes.count()} series from MAL")

                        // Got everything ok, save it
                        executeSaveToLocalDb(animes)
                        viewAdapter.addAll(animes)
                    }

                    swipeRefreshLayout.isRefreshing = false
                },
                { _ ->
                    Timber.e("Failed to get latest anime from MAL")
                    swipeRefreshLayout.isRefreshing = false
                }
            ))
    }

    private fun executeUpdateMal(originalModel: Anime, model: UpdateAnime, callback: () -> Unit) {
        Timber.d("Sending update to MAL for anime - [%s]", originalModel)

        disposables.add(malManager.updateAnime(model)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { _ ->
                    Timber.d("Successfully updated anime on MAL")

                    callback()
                    executeUpdateInLocalDb(originalModel)
                    viewAdapter.updateItem(model)
                },
                { _ ->
                    Timber.e("Error trying to update anime on MAL")

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
        Timber.d("Clearing local DB for all anime")

        // Looks like this doesn't have to be disposed off
        Completable
            .fromAction({
                animeDao.clear()
            })
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    private fun executeSaveToLocalDb(animes: List<Anime>) {
        Timber.d("Updating local DB for all anime")

        // Looks like this doesn't have to be disposed off
        Completable
            .fromAction({
                animeDao.insertAll(animes)
            })
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    private fun executeUpdateInLocalDb(anime: Anime) {
        Timber.d("Updating local DB for anime - [%s]", anime)

        // Looks like this doesn't have to be disposed off
        Completable
            .fromAction({
                animeDao.update(anime)
            })
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    companion object {
        const val tag = "AnimeFragment"
        fun newInstance(): AnimeFragment {
            val animeFragment = AnimeFragment()
            val args = Bundle()
            animeFragment.arguments = args
            return animeFragment
        }
    }
}