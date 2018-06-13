package com.chesire.malime.view.search

import android.databinding.BaseObservable
import android.databinding.Bindable

class SearchParams : BaseObservable() {
    @Bindable
    var searchText = ""
    @Bindable
    var nsfwEnabled = false
    @Bindable
    var isSearching = false
}