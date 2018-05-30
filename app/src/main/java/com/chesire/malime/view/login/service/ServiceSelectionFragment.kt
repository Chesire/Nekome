package com.chesire.malime.view.login.service

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.chesire.malime.R
import com.chesire.malime.view.login.LoginInteractor

class ServiceSelectionFragment : Fragment() {
    private lateinit var loginInteractor: LoginInteractor

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_serviceselection, container, false)
        view.findViewById<Button>(R.id.button_1).setOnClickListener {
            loginInteractor.serviceSelected()
        }
        view.findViewById<Button>(R.id.button_2).setOnClickListener {
            loginInteractor.serviceSelected()
        }

        return view
    }

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