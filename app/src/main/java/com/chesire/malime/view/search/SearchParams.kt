package com.chesire.malime.view.search

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.chesire.malime.BR

class SearchParams : BaseObservable() {
    @Bindable
    var searchText = ""

    @Bindable
    var nsfwEnabled = false

    @Bindable
    var searching = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.searching)
        }
}
