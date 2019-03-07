package com.chesire.malime.flow.overview.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.chesire.malime.databinding.FragmentProfileBinding
import dagger.android.support.DaggerFragment

class ProfileFragment : DaggerFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentProfileBinding.inflate(inflater, container, false).root

    companion object {
        const val TAG = "ProfileFragment"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ProfileFragment.
         */
        fun newInstance() = ProfileFragment()
    }
}
