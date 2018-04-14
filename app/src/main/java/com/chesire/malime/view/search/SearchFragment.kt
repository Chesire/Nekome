package com.chesire.malime.view.search

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioGroup
import com.chesire.malime.R

class SearchFragment : Fragment() {
    private val nsfwAllowedKey = "nsfwAllowed"
    private val checkedOptionKey = "checkedOption"

    private var nsfwAllowed = false
    private var checkedOption = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        val searchRadioGroup = view.findViewById<RadioGroup>(R.id.search_option_choices)
        searchRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            checkedOption = checkedId
            searchOptionChanged()
        }

        val nsfwCheckbox = view.findViewById<CheckBox>(R.id.search_option_nsfw_allowed)
        nsfwCheckbox.setOnClickListener {
            nsfwAllowed = (it as CheckBox).isChecked
            searchOptionChanged()
        }

        if (savedInstanceState == null) {
            checkedOption = searchRadioGroup.checkedRadioButtonId
            nsfwAllowed = nsfwCheckbox.isChecked
        } else {
            checkedOption = savedInstanceState.getInt(checkedOptionKey)
            nsfwAllowed = savedInstanceState.getBoolean(nsfwAllowedKey)
        }

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(nsfwAllowedKey, nsfwAllowed)
        outState.putInt(checkedOptionKey, checkedOption)
        super.onSaveInstanceState(outState)
    }

    private fun searchOptionChanged() {
        // perform search again
    }

    companion object {
        const val tag = "SearchFragment"
        fun newInstance(): SearchFragment {
            val searchFragment = SearchFragment()
            val args = Bundle()
            searchFragment.arguments = args
            return searchFragment
        }
    }
}