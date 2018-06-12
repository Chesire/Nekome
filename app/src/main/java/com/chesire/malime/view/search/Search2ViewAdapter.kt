package com.chesire.malime.view.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chesire.malime.BR
import com.chesire.malime.core.models.MalimeModel
import com.chesire.malime.databinding.ItemSearchBinding
import com.chesire.malime.util.GlideApp
import kotlinx.android.synthetic.main.item_search.view.item_search_content_layout
import kotlinx.android.synthetic.main.item_search.view.item_search_image
import kotlinx.android.synthetic.main.item_search.view.item_search_loading_layout
import kotlinx.android.synthetic.main.item_search.view.search_image_add_button

class Search2ViewAdapter : RecyclerView.Adapter<Search2ViewAdapter.ViewHolder>() {
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

    override fun getItemCount(): Int {
        return searchItems.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(searchItems[position])
    }

    inner class ViewHolder(
        private val searchView: ItemSearchBinding
    ) : RecyclerView.ViewHolder(searchView.root) {
        private val loadingLayout = searchView.root.item_search_loading_layout
        private val contentLayout = searchView.root.item_search_content_layout

        fun bind(item: MalimeModel) {
            val image = searchView.itemSearchImage

            searchView.setVariable(BR.model, item)
            searchView.executePendingBindings()

            GlideApp.with(searchView.root)
                .load(
                    if (item.posterImage == "") {
                        item.coverImage
                    } else {
                        item.posterImage
                    }
                )
                .into(image)

            if (currentItems.find { it.seriesId == item.seriesId } == null) {
                searchView.searchImageAddButton.visibility = View.VISIBLE
            } else {
                searchView.searchImageAddButton.visibility = View.INVISIBLE
            }

            searchView.root.apply {
                item_search_image.setOnClickListener {
                    // on image clicked
                }
                search_image_add_button.setOnClickListener {
                    addSeries(item)
                }
            }

        }

        private fun addSeries(item: MalimeModel) {
            setLayoutState(false)

            /*listener.updateSeries(item, newProgress, newStatus, { success ->
                setLayoutState(true)
                if (!success) {
                    Snackbar.make(
                        loadingLayout,
                        String.format(
                            binding.root.context.getString(R.string.malitem_update_series_failure),
                            item.title
                        ), Snackbar.LENGTH_LONG
                    ).show()
                }
            })*/
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