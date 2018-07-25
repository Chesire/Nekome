package com.chesire.malime.view.search

import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import com.chesire.malime.BR
import com.chesire.malime.R
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.MalimeModel
import com.chesire.malime.databinding.ItemSearchBinding
import com.chesire.malime.util.GlideApp
import com.chesire.malime.util.extension.getString
import kotlinx.android.synthetic.main.item_search.view.item_search_content_layout
import kotlinx.android.synthetic.main.item_search.view.item_search_image
import kotlinx.android.synthetic.main.item_search.view.item_search_loading_layout
import kotlinx.android.synthetic.main.item_search.view.item_search_status_text
import kotlinx.android.synthetic.main.item_search.view.item_search_type_text
import kotlinx.android.synthetic.main.item_search.view.search_image_add_button

class SearchViewAdapter(
    private val interactor: SearchInteractionListener
) : RecyclerView.Adapter<SearchViewAdapter.ViewHolder>() {
    private val searchItems = ArrayList<MalimeModel>()
    private val currentItems = ArrayList<MalimeModel>()

    fun addSearchItems(newItems: List<MalimeModel>) {
        searchItems.clear()
        searchItems.addAll(newItems)
        notifyDataSetChanged()
    }

    fun setCurrentItems(newItems: List<MalimeModel>) {
        currentItems.clear()
        currentItems.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = searchItems.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(searchItems[position])
    }

    inner class ViewHolder(
        private val searchView: ItemSearchBinding
    ) : RecyclerView.ViewHolder(searchView.root), PopupMenu.OnMenuItemClickListener {
        private val loadingLayout = searchView.root.item_search_loading_layout
        private val contentLayout = searchView.root.item_search_content_layout
        private lateinit var malItem: MalimeModel

        fun bind(item: MalimeModel) {
            malItem = item

            searchView.setVariable(BR.model, item)
            searchView.executePendingBindings()

            GlideApp.with(searchView.root)
                .load(if (item.posterImage.isEmpty()) item.coverImage else item.posterImage)
                .placeholder(R.drawable.ic_image_black)
                .error(R.drawable.ic_broken_image_black)
                .into(searchView.itemSearchImage)

            if (currentItems.find { it.seriesId == item.seriesId } == null) {
                searchView.searchImageAddButton.visibility = View.VISIBLE
                contentLayout.alpha = 1f
            } else {
                searchView.searchImageAddButton.visibility = View.INVISIBLE
                contentLayout.alpha = 0.3f
            }

            searchView.root.apply {
                item_search_type_text.text = item.subtype.getString(context)
                item_search_status_text.text = item.seriesStatus.getString(context)
                item_search_image.setOnClickListener { interactor.navigateToSeries(item) }
                search_image_add_button.setOnClickListener { showAddMenu() }
            }
        }

        private fun showAddMenu() {
            val popup = PopupMenu(searchView.root.context, searchView.root.search_image_add_button)
            popup.inflate(R.menu.menu_possible_states)
            popup.setOnMenuItemClickListener(this)
            popup.show()
        }

        @Suppress("ComplexMethod")
        override fun onMenuItemClick(item: MenuItem?): Boolean {
            when (item?.itemId) {
                R.id.menu_maldisplay_state_complete -> addSeries(UserSeriesStatus.Completed)
                R.id.menu_maldisplay_state_current -> addSeries(UserSeriesStatus.Current)
                R.id.menu_maldisplay_state_dropped -> addSeries(UserSeriesStatus.Dropped)
                R.id.menu_maldisplay_state_on_hold -> addSeries(UserSeriesStatus.OnHold)
                R.id.menu_maldisplay_state_planned -> addSeries(UserSeriesStatus.Planned)
                else -> return false
            }
            return true
        }

        private fun addSeries(status: UserSeriesStatus) {
            setLayoutState(false)

            malItem.userSeriesStatus = status

            interactor.addNewSeries(malItem) { success ->
                setLayoutState(true)
                Snackbar.make(
                    loadingLayout,
                    String.format(
                        if (success) {
                            searchView.root.context.getString(R.string.search_add_success)
                        } else {
                            searchView.root.context.getString(R.string.search_add_failed)
                        },
                        malItem.title
                    ), Snackbar.LENGTH_LONG
                ).show()
            }
        }

        private fun setLayoutState(enabled: Boolean) {
            loadingLayout.visibility = if (enabled) {
                View.GONE
            } else {
                View.VISIBLE
            }
            contentLayout.isEnabled = enabled
            for (i in 0 until contentLayout.childCount) {
                contentLayout.getChildAt(i).isEnabled = enabled
            }
        }
    }
}