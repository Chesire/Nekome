package com.chesire.nekome

import android.widget.ImageView
import android.widget.TextView
import coil.load
import coil.transform.CircleCropTransformation
import com.chesire.nekome.core.models.UserModel
import com.google.android.material.navigation.NavigationView
import timber.log.Timber

/**
 * Updates the [view] with new data from [model].
 */
fun updateAvatar(view: NavigationView, model: UserModel) {
    Timber.d("Updating Activity avatar with new model $model")
    view.getHeaderView(0)
        ?.apply {
            findViewById<TextView>(R.id.viewNavHeaderTitle).text = model.name
            findViewById<TextView>(R.id.viewNavHeaderSubtitle).text = model.service.name
            findViewById<ImageView>(R.id.viewNavHeaderImage)
                .load(model.avatar.medium.url) {
                    transformations(CircleCropTransformation())
                    placeholder(R.drawable.ic_account_circle)
                    error(R.drawable.ic_account_circle)
                }
        }
}
