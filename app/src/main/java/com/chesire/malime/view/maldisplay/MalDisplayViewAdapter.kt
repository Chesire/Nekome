package com.chesire.malime.view.maldisplay

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chesire.malime.R
import com.chesire.malime.core.models.MalimeModel
import com.chesire.malime.databinding.ItemMalmodelBinding

class MalDisplayViewAdapter : RecyclerView.Adapter<MalDisplayViewAdapter.ViewHolder>() {
    private val items = ArrayList<MalimeModel>()

    fun addAll(newItems: List<MalimeModel>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun clear(item: MalimeModel) {
        val foundItem = items.find { it.id == item.id }
        items.remove(foundItem)
        notifyDataSetChanged()
    }

    fun clearAll() {
        items.clear()
        notifyDataSetChanged()
    }

    fun update(item: MalimeModel) {
        val foundItem = items.find { it.id == item.id }
        // update the item
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate<ItemMalmodelBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_malmodel,
                parent,
                false
            ).root
        )
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // bind to the model at [position]
        // need the binding instance
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    }
}