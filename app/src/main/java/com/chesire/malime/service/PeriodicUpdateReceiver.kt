package com.chesire.malime.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.chesire.malime.util.SharedPref

class PeriodicUpdateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        PeriodicUpdateHelper().schedule(context!!, SharedPref(context))
    }
}