package com.chesire.malime.app.search.results

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chesire.malime.core.models.SeriesModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_result.resultImage
import kotlinx.android.synthetic.main.item_result.resultSubType
import kotlinx.android.synthetic.main.item_result.resultTitle

/**
 * ViewHolder to be used with the [ResultsAdapter].
 */
class ResultsViewHolder(view: View) : RecyclerView.ViewHolder(view), LayoutContainer {
    private lateinit var seriesModel: SeriesModel
    override val containerView: View?
        get() = itemView

    fun bind(model: SeriesModel) {
        seriesModel = model

        Glide.with(itemView)
            .load(model.posterImage.smallest?.url)
            .into(resultImage)
        resultTitle.text = model.title
        resultSubType.text = model.subtype.name
    }
}
