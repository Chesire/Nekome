package com.chesire.malime.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.chesire.malime.util.SharedPref

class Receiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != Intent.ACTION_BOOT_COMPLETED) {
            return
        }

        context?.let {
            val pref = SharedPref(it)
            PeriodicUpdateHelper().schedule(it, pref, true)
            RefreshTokenHelper().schedule(it, pref, true)
        }
    }
}