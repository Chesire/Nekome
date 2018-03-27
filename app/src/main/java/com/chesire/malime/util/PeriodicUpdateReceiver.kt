package com.chesire.malime.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class PeriodicUpdateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        PeriodicUpdateHelper().schedule(context!!)
    }
}