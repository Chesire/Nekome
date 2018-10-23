package com.chesire.malime.view.maldisplay

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.databinding.ViewDataBinding
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Filter
import android.widget.Filterable
import android.widget.PopupMenu
import com.chesire.malime.BR
import com.chesire.malime.R
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.MalimeModel
import com.chesire.malime.databinding.ItemMalmodelBinding
import com.chesire.malime.util.GlideApp
import com.chesire.malime.util.SharedPref
import com.chesire.malime.util.extension.getString
import com.chesire.malime.view.preferences.SortOption
import kotlinx.android.synthetic.main.item_malmodel.view.item_malmodel_content_layout
import kotlinx.android.synthetic.main.item_malmodel.view.item_malmodel_image
import kotlinx.android.synthetic.main.item_malmodel.view.item_malmodel_loading_layout
import kotlinx.android.synthetic.main.item_malmodel.view.item_malmodel_more
import kotlinx.android.synthetic.main.item_malmodel.view.item_malmodel_plus_one
import timber.log.Timber
import java.util.Locale

class MalDisplayViewAdapter(
    context: Context,
    private val listener: ModelInteractionListener,
    private val sharedPref: SharedPref
) : RecyclerView.Adapter<MalDisplayViewAdapter.ViewHolder>(), Filterable,
    SharedPreferences.OnSharedPreferenceChangeListener {
    private val sortPref = context.getString(R.string.key_sort)
    private val filterPref = context.getString(R.string.key_filter)

    private val items = ArrayList<MalimeModel>()
    private val filteredItems = ArrayList<MalimeModel>()
    private val filter = MalDisplayFilter()

    init {
        sharedPref.registerOnChangeListener(this)
    }

    fun addAll(newItems: List<MalimeModel>) {
        items.clear()
        items.addAll(newItems)
        filter.filter("")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMalmodelBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = filteredItems.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filteredItems[position])
    }

    override fun getFilter(): Filter = filter

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        key?.let {
            if (it.contains(sortPref) || it.contains(filterPref)) {
                filter.filter("")
            }
        }
    }

    inner class ViewHolder(
        private val binding: ViewDataBinding
    ) : RecyclerView.ViewHolder(binding.root), PopupMenu.OnMenuItemClickListener {
        private val loadingLayout = binding.root.item_malmodel_loading_layout
        private val contentLayout = binding.root.item_malmodel_content_layout
        private lateinit var malItem: MalimeModel

        fun bind(item: MalimeModel?) {
            if (item == null) {
                Timber.w("Empty list found, not performing binding")
                return
            }
            malItem = item

            binding.setVariable(BR.model, item)
            binding.executePendingBindings()

            GlideApp.with(binding.root)
                .load(if (item.posterImage.isEmpty()) item.coverImage else item.posterImage)
                .placeholder(R.drawable.ic_image_black)
                .error(R.drawable.ic_broken_image_black)
                .into(binding.root.item_malmodel_image)

            binding.root.apply {
                item_malmodel_plus_one.setOnClickListener {
                    updateSeries(item, item.progress + 1, item.userSeriesStatus)
                }
                item_malmodel_more.setOnClickListener { showPopupMenu() }
            }
        }

        private fun showPopupMenu() {
            val popup = PopupMenu(binding.root.context, binding.root.item_malmodel_more)
            popup.inflate(R.menu.menu_maldisplay_item)
            popup.setOnMenuItemClickListener(this)
            popup.show()
        }

        @Suppress("ComplexMethod")
        override fun onMenuItemClick(item: MenuItem?): Boolean {
            when (item?.itemId) {
                R.id.menu_maldisplay_series_profile -> listener.showSeriesProfile(malItem)
                R.id.menu_maldisplay_series_progress -> showProgressDialog()
                R.id.menu_maldisplay_series_delete -> confirmDelete()
                R.id.menu_maldisplay_state_complete -> confirmStateChange(UserSeriesStatus.Completed)
                R.id.menu_maldisplay_state_current -> confirmStateChange(UserSeriesStatus.Current)
                R.id.menu_maldisplay_state_dropped -> confirmStateChange(UserSeriesStatus.Dropped)
                R.id.menu_maldisplay_state_on_hold -> confirmStateChange(UserSeriesStatus.OnHold)
                R.id.menu_maldisplay_state_planned -> confirmStateChange(UserSeriesStatus.Planned)
                else -> return false
            }
            return true
        }

        private fun showProgressDialog() {
            Timber.d("Trying to update series progress for ${malItem.title}")
            val context = binding.root.context

            val input = EditText(context)
                .apply {
                    setText(malItem.progress.toString())
                    inputType = InputType.TYPE_CLASS_NUMBER
                    setSelection(text.length)
                }

            AlertDialog.Builder(context)
                .setTitle(R.string.maldisplay_menu_set_progress_title)
                .setView(input)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    val parsedInt = input.text.toString().toIntOrNull()
                    when {
                        parsedInt == null -> Snackbar.make(
                            loadingLayout,
                            R.string.maldisplay_progress_invalid,
                            Snackbar.LENGTH_LONG
                        ).show()

                        parsedInt > malItem.totalLength && malItem.totalLength != 0 -> Snackbar.make(
                            loadingLayout,
                            R.string.maldisplay_progress_invalid,
                            Snackbar.LENGTH_LONG
                        ).show()

                        parsedInt == malItem.progress -> {
                            // no change
                        }

                        else -> updateSeries(malItem, parsedInt, malItem.userSeriesStatus)
                    }
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
        }

        private fun confirmDelete() {
            Timber.d("Trying to delete series ${malItem.title}")
            val context = binding.root.context

            AlertDialog.Builder(context)
                .setTitle(R.string.maldisplay_confirm_status_change_title)
                .setMessage(
                    String.format(
                        Locale.getDefault(),
                        context.getString(R.string.maldisplay_confirm_delete_body),
                        malItem.title
                    )
                )
                .setPositiveButton(android.R.string.yes) { _, _ ->
                    deleteSeries(malItem)
                }
                .setNegativeButton(android.R.string.no, null)
                .show()
        }

        private fun deleteSeries(item: MalimeModel) {
            setLayoutState(false)

            listener.deleteSeries(item) { success ->
                setLayoutState(true)

                val toastBody = if (success) {
                    R.string.maldisplay_delete_successful
                } else {
                    R.string.maldisplay_delete_failure
                }

                Snackbar.make(
                    loadingLayout,
                    String.format(
                        Locale.getDefault(),
                        binding.root.context.getString(toastBody),
                        item.title
                    ),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        private fun confirmStateChange(newStatus: UserSeriesStatus) {
            Timber.d("Trying to set series ${malItem.title} to state $newStatus")
            val context = binding.root.context

            AlertDialog.Builder(context)
                .setTitle(R.string.maldisplay_confirm_status_change_title)
                .setMessage(
                    String.format(
                        Locale.getDefault(),
                        context.getString(R.string.maldisplay_confirm_status_change_body),
                        malItem.title,
                        newStatus.getString(context)
                    )
                )
                .setPositiveButton(android.R.string.yes) { _, _ ->
                    updateSeries(malItem, malItem.progress, newStatus)
                }
                .setNegativeButton(android.R.string.no, null)
                .show()
        }

        private fun updateSeries(item: MalimeModel, newProgress: Int, newStatus: UserSeriesStatus) {
            setLayoutState(false)

            listener.updateSeries(item, newProgress, newStatus) { success ->
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
            }
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

    private inner class MalDisplayFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            val filterOption = sharedPref.getFilter()
            val sortOption = sharedPref.sortOption

            val tempList = items
                .filter {
                    // Internal id starts at 0 for Unknown,
                    // but we can't filter on that, so change it to current
                    val filterId = if (it.userSeriesStatus == UserSeriesStatus.Unknown) {
                        UserSeriesStatus.Current.internalId
                    } else {
                        it.userSeriesStatus.internalId
                    }
                    filterOption[filterId - 1]
                }
                .sortedWith(
                    when (sortOption) {
                        SortOption.Default -> compareBy { it.userSeriesId }
                        SortOption.Title -> compareBy { it.title }
                        SortOption.StartDate -> compareBy { it.startDate }
                        SortOption.EndDate -> compareBy { it.endDate }
                    }
                )

            results.values = tempList
            results.count = tempList.count()
            return results
        }

        @Suppress("UNCHECKED_CAST", "UnsafeCast")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredItems.clear()
            if (results?.values is List<*>) {
                filteredItems.addAll(results.values as List<MalimeModel>)
            }
            notifyDataSetChanged()
        }
    }
}