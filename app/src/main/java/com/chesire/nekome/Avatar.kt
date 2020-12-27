package com.chesire.nekome

import android.widget.ImageView
import android.widget.TextView
import coil.load
import coil.transform.CircleCropTransformation
import com.chesire.nekome.account.UserDomain
import com.google.android.material.navigation.NavigationView
import timber.log.Timber

/**
 * Updates the [view] with new data from [model].
 */
fun updateAvatar(view: NavigationView, model: UserDomain) {
    Timber.d("Updating Activity avatar with new model $model")
    view.getHeaderView(0)
        ?.apply {
            findViewById<TextView>(R.id.viewNavHeaderTitle).text = model.name
            // TODO: change subtitle
            findViewById<TextView>(R.id.viewNavHeaderSubtitle).text = "Kitsu"
            findViewById<ImageView>(R.id.viewNavHeaderImage)
                .load(model.avatar.medium.url) {
                    transformations(CircleCropTransformation())
                    placeholder(R.drawable.ic_account_circle)
                    error(R.drawable.ic_account_circle)
                }
        }
}
