package com.chesire.malime.view.search

import android.os.Build
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.chesire.malime.R
import com.chesire.malime.models.Entry
import com.chesire.malime.util.GlideApp

class SearchViewAdapter(
    private val items: ArrayList<Entry>
) : RecyclerView.Adapter<SearchViewAdapter.ViewHolder>() {

    fun update(newItems: List<Entry>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun getAll(): ArrayList<Entry> {
        return items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindModel(items[position])
    }

    inner class ViewHolder(
        private val searchView: View
    ) : RecyclerView.ViewHolder(searchView) {
        private val loadingLayout = searchView.findViewById<View>(R.id.item_search_loading_layout)
        private val contentLayout =
            searchView.findViewById<ViewGroup>(R.id.item_search_content_layout)

        fun bindModel(entryModel: Entry) {
            val image = searchView.findViewById<ImageView>(R.id.item_search_image)

            // Setup the image
            GlideApp.with(searchView)
                .load(entryModel.image)
                .into(image)

            // Setup the text
            searchView.findViewById<TextView>(R.id.item_search_title).text = entryModel.title
            searchView.findViewById<TextView>(R.id.item_search_progress).text =
                    fromHtml(entryModel.synopsis!!)
        }

        private fun fromHtml(input: String): Spanned {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(input, Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(input)
            }
        }
    }
}