package com.chesire.malime.app.search.results

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.chesire.malime.core.models.SeriesModel
import kotlinx.android.extensions.LayoutContainer

/**
 * ViewHolder to be used with the [ResultsAdapter].
 */
class ResultsViewHolder(view: View) : RecyclerView.ViewHolder(view), LayoutContainer {
    private lateinit var seriesModel: SeriesModel
    override val containerView: View?
        get() = itemView

    fun bind(series: SeriesModel) {

    }
}
