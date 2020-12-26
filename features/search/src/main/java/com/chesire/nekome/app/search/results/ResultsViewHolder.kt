package com.chesire.nekome.app.search.results

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.chesire.nekome.app.search.R
import com.chesire.nekome.app.search.databinding.ItemResultBinding
import com.chesire.nekome.app.search.domain.SearchModel
import com.chesire.nekome.core.extensions.hide
import com.chesire.nekome.core.extensions.show
import com.chesire.nekome.core.extensions.visibleIf

/**
 * ViewHolder to be used with the [ResultsAdapter].
 */
class ResultsViewHolder(
    private val binding: ItemResultBinding
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var searchModel: SearchModel

    /**
     * Bind the [model] to the view to display data.
     */
    fun bind(model: SearchModel, exists: Boolean) {
        searchModel = model

        binding.apply {
            resultTitle.text = model.canonicalTitle
            resultDescription.text = model.synopsis
            resultSubType.text = model.subtype.name

            resultTrack.visibleIf { !exists }
            resultLayout.alpha = if (exists) 0.3f else 1f
        }

        loadResultImage(model)
    }

    private fun loadResultImage(model: SearchModel) = binding.apply {
        resultImage.load(model.posterImage.smallest?.url) {
            placeholder(R.drawable.ic_insert_photo)
            error(R.drawable.ic_insert_photo)
        }
    }

    /**
     * Bind the [ResultsListener] to the [ResultsViewHolder] so click events can be collected.
     */
    fun bindListener(resultsListener: ResultsListener) = binding.apply {
        resultTrack.setOnClickListener {
            startTrackingSeries()
            resultsListener.onTrack(searchModel) {
                finishTrackingSeries()
            }
        }
    }

    private fun startTrackingSeries() = binding.apply {
        resultTrack.hide()
        resultProgressBar.show()
    }

    private fun finishTrackingSeries() = binding.apply {
        resultProgressBar.hide()
    }
}
