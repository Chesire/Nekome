package com.chesire.malime.view.login.service

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.chesire.malime.R
import com.chesire.malime.core.flags.SupportedService
import com.chesire.malime.view.login.LoginInteractor
import dagger.android.support.DaggerFragment

class ServiceSelectionFragment : DaggerFragment() {
    private lateinit var loginInteractor: LoginInteractor

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_service_selection, container, false)
        view.findViewById<Button>(R.id.fragmentServiceSelectionMalButton).setOnClickListener {
            loginInteractor.serviceSelected(SupportedService.MyAnimeList)
        }
        view.findViewById<Button>(R.id.fragmentServiceSelectionKitsuButton).setOnClickListener {
            loginInteractor.serviceSelected(SupportedService.Kitsu)
        }

        return view
    }

    @Suppress("UnsafeCast")
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        loginInteractor = context as LoginInteractor
    }

    companion object {
        const val tag = "ServiceSelectionFragment"
        fun newInstance(): ServiceSelectionFragment {
            val serviceSelectionFragment = ServiceSelectionFragment()
            val args = Bundle()
            serviceSelectionFragment.arguments = args
            return serviceSelectionFragment
        }
    }
}
