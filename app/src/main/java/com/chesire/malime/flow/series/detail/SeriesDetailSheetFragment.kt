package com.chesire.malime.flow.series.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chesire.malime.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.android.support.DaggerDialogFragment

class SeriesDetailSheetFragment: BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_series_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        const val TAG = "SeriesDetailSheetFragment"

        // maybe pass in the model?
        fun newInstance(): SeriesDetailSheetFragment = SeriesDetailSheetFragment()
    }
}
