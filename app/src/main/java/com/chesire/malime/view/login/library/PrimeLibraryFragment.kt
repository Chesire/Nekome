package com.chesire.malime.view.login.library

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chesire.malime.R
import com.chesire.malime.core.models.MalimeModel
import com.chesire.malime.core.repositories.Library
import com.chesire.malime.databinding.FragmentPrimeLibraryBinding
import com.chesire.malime.kitsu.api.KitsuApi
import com.chesire.malime.kitsu.api.KitsuManager
import com.chesire.malime.util.SharedPref

class PrimeLibraryFragment : Fragment() {
    private lateinit var viewModel: PrimeLibraryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = SharedPref(requireContext())
        viewModel = ViewModelProviders
            .of(
                this,
                PrimeLibraryViewModelFactory(
                    requireActivity().application,
                    Library(
                        requireContext(),
                        KitsuManager(
                            KitsuApi(sharedPref.getAuth()),
                            sharedPref.getUserId()
                        )
                    )
                )
            )
            .get(PrimeLibraryViewModel::class.java)

        viewModel.myLibrary.observe(this,
            Observer {
                if (it != null) {
                    listUpdated(it)
                }
            }
        )

        viewModel.updateLibrary()
    }

    private fun listUpdated(items: List<MalimeModel>) {
        val s = ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return DataBindingUtil
            .inflate<FragmentPrimeLibraryBinding>(
                inflater,
                R.layout.fragment_prime_library,
                container,
                false
            ).apply {
                vm = viewModel
            }.root
    }

    companion object {
        const val tag = "PrimeLibraryFragment"
        fun newInstance(): PrimeLibraryFragment {
            val getLibraryFragment = PrimeLibraryFragment()
            val args = Bundle()
            getLibraryFragment.arguments = args
            return getLibraryFragment
        }
    }
}