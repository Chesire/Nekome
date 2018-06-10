package com.chesire.malime.view.maldisplay

import android.databinding.ViewDataBinding
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chesire.malime.BR
import com.chesire.malime.R
import com.chesire.malime.core.models.MalimeModel
import com.chesire.malime.databinding.ItemMalmodelBinding
import com.chesire.malime.util.GlideApp
import kotlinx.android.synthetic.main.item_malmodel.view.item_malmodel_content_layout
import kotlinx.android.synthetic.main.item_malmodel.view.item_malmodel_image
import kotlinx.android.synthetic.main.item_malmodel.view.item_malmodel_loading_layout
import kotlinx.android.synthetic.main.item_malmodel.view.item_malmodel_neg_one
import kotlinx.android.synthetic.main.item_malmodel.view.item_malmodel_plus_one
import timber.log.Timber

class MalDisplayViewAdapter(
    private val listener: ModelInteractionListener
) : RecyclerView.Adapter<MalDisplayViewAdapter.ViewHolder>() {
    private val items = ArrayList<MalimeModel>()

    fun addAll(newItems: List<MalimeModel>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun clear(item: MalimeModel) {
        val foundItem = items.find { it.seriesId == item.seriesId }
        items.remove(foundItem)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (items.count() == 0) {
            // we should show a screen with no items
            // for now return the same item
            return ViewHolder(
                ItemMalmodelBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            return ViewHolder(
                ItemMalmodelBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(
        private val binding: ViewDataBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private val loadingLayout = binding.root.item_malmodel_loading_layout
        private val contentLayout = binding.root.item_malmodel_content_layout

        fun bind(item: MalimeModel?) {
            if (item == null) {
                Timber.w("Empty list found, not performing binding")
                return
            }

            binding.setVariable(BR.model, item)
            binding.executePendingBindings()

            GlideApp.with(binding.root)
                .load(
                    if (item.posterImage == "") {
                        item.coverImage
                    } else {
                        item.posterImage
                    }
                )
                .into(binding.root.item_malmodel_image)


            binding.root.apply {
                item_malmodel_neg_one.setOnClickListener {
                    updateSeriesProgress(item, item.progress - 1)
                }
                item_malmodel_plus_one.setOnClickListener {
                    updateSeriesProgress(item, item.progress + 1)
                }
                item_malmodel_image.setOnClickListener {
                    listener.onImageClicked(item)
                }
            }
        }

        private fun updateSeriesProgress(item: MalimeModel, newProgress: Int) {
            setLayoutState(false)

            listener.onSeriesSetProgress(item, newProgress, { success ->
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
            })
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