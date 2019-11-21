package com.chesire.malime.app.search.results

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chesire.malime.core.extensions.hide
import com.chesire.malime.core.extensions.show
import com.chesire.malime.core.extensions.visibleIf
import com.chesire.malime.core.models.SeriesModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_result.resultImage
import kotlinx.android.synthetic.main.item_result.resultProgressBar
import kotlinx.android.synthetic.main.item_result.resultSubType
import kotlinx.android.synthetic.main.item_result.resultTitle
import kotlinx.android.synthetic.main.item_result.resultTrack

/**
 * ViewHolder to be used with the [ResultsAdapter].
 */
class ResultsViewHolder(view: View) : RecyclerView.ViewHolder(view), LayoutContainer {
    private lateinit var seriesModel: SeriesModel
    override val containerView: View?
        get() = itemView

    /**
     * Bind the [model] to the view to display data.
     */
    fun bind(model: SeriesModel, exists: Boolean) {
        seriesModel = model

        Glide.with(itemView)
            .load(model.posterImage.smallest?.url)
            .into(resultImage)
        resultTitle.text = model.title
        resultSubType.text = model.subtype.name

        resultTrack.visibleIf { !exists }
        containerView?.alpha = if (exists) 0.3f else 1f
    }

    /**
     * Bind the [ResultsListener] to the [ResultsViewHolder] so click events can be collected.
     */
    fun bindListener(resultsListener: ResultsListener) {
        resultTrack.setOnClickListener {
            startTrackingSeries()
            resultsListener.onTrack(seriesModel) {
                finishTrackingSeries()
            }
        }
    }

    private fun startTrackingSeries() {
        resultTrack.hide()
        resultProgressBar.show()
    }

    private fun finishTrackingSeries() {
        resultProgressBar.hide()
    }
}
