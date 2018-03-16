package com.chesire.malime

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chesire.malime.mal.MalManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AnimeFragment : Fragment() {
    private var disposables = CompositeDisposable()
    private lateinit var username: String
    private lateinit var malManager: MalManager
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: AnimeViewAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = SharedPref(context!!)
        username = sharedPref.getUsername()
        malManager = MalManager(sharedPref.getAuth())

        viewManager = LinearLayoutManager(context!!)
        viewAdapter = AnimeViewAdapter(ArrayList(), ArrayList(), sharedPref)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_maldisplay, container, false)

        swipeRefreshLayout = view.findViewById(R.id.maldisplay_swipe_refresh)
        swipeRefreshLayout.setOnRefreshListener {
            executeLoadAnime()
        }

        recyclerView = view.findViewById<RecyclerView>(R.id.maldisplay_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        executeLoadAnime()

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

    private fun executeLoadAnime() {
        disposables.add(malManager.getAllAnime(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            viewAdapter.addAll(result.second)
                            swipeRefreshLayout.isRefreshing = false
                        },
                        { _ ->
                            swipeRefreshLayout.isRefreshing = false
                        }
                ))
    }

    companion object {
        fun newInstance(): AnimeFragment {
            val animeFragment = AnimeFragment()
            val args = Bundle()
            animeFragment.arguments = args
            return animeFragment
        }
    }
}