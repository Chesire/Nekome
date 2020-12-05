package com.chesire.nekome.app.search.results

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.chesire.nekome.app.search.R
import com.chesire.nekome.app.search.domain.SearchModel
import com.chesire.nekome.core.extensions.hide
import com.chesire.nekome.core.extensions.show
import com.chesire.nekome.core.extensions.visibleIf
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_result.resultDescription
import kotlinx.android.synthetic.main.item_result.resultImage
import kotlinx.android.synthetic.main.item_result.resultProgressBar
import kotlinx.android.synthetic.main.item_result.resultSubType
import kotlinx.android.synthetic.main.item_result.resultTitle
import kotlinx.android.synthetic.main.item_result.resultTrack

/**
 * ViewHolder to be used with the [ResultsAdapter].
 */
class ResultsViewHolder(view: View) : RecyclerView.ViewHolder(view), LayoutContainer {
    private lateinit var searchModel: SearchModel
    override val containerView: View
        get() = itemView

    /**
     * Bind the [model] to the view to display data.
     */
    fun bind(model: SearchModel, exists: Boolean) {
        searchModel = model

        resultImage.load(model.posterImage.smallest?.url) {
            placeholder(R.drawable.ic_insert_photo)
            error(R.drawable.ic_insert_photo)
        }
        resultTitle.text = model.canonicalTitle
        resultDescription.text = model.synopsis
        resultSubType.text = model.subtype.name

        resultTrack.visibleIf { !exists }
        containerView.alpha = if (exists) 0.3f else 1f
    }

    /**
     * Bind the [ResultsListener] to the [ResultsViewHolder] so click events can be collected.
     */
    fun bindListener(resultsListener: ResultsListener) {
        resultTrack.setOnClickListener {
            startTrackingSeries()
            resultsListener.onTrack(searchModel) {
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
