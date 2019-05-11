package com.chesire.malime.flow.series.list.anime

import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.extensions.visibleIf
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.adapter_item_anime.adapterItemAnimeImage
import kotlinx.android.synthetic.main.adapter_item_anime.adapterItemAnimePlusOne
import kotlinx.android.synthetic.main.adapter_item_anime.adapterItemAnimeProgress
import kotlinx.android.synthetic.main.adapter_item_anime.adapterItemAnimeSubtype
import kotlinx.android.synthetic.main.adapter_item_anime.adapterItemAnimeTitle

class AnimeViewHolder(view: View) : RecyclerView.ViewHolder(view), LayoutContainer {
    private lateinit var seriesModel: SeriesModel
    override val containerView: View?
        get() = itemView

    fun bind(model: SeriesModel) {
        seriesModel = model

        Glide.with(itemView)
            .load(model.posterImage.smallest?.url)
            .into(adapterItemAnimeImage)
        adapterItemAnimeTitle.text = model.title
        adapterItemAnimeSubtype.text = model.subtype.name
        adapterItemAnimeProgress.text = "${model.progress} / ${model.totalLength}"
        adapterItemAnimePlusOne.visibleIf(invisible = true) { model.progress < model.totalLength }
        ViewCompat.setTransitionName(adapterItemAnimeImage, model.title)
    }

    fun bindListener(listener: AnimeInteractionListener) {
        itemView.setOnClickListener { listener.animeSelected(adapterItemAnimeImage, seriesModel) }
        adapterItemAnimePlusOne.setOnClickListener { listener.onPlusOne(seriesModel) }
    }
}
