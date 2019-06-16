package com.chesire.malime.flow.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.R
import com.chesire.malime.core.models.UserModel
import com.chesire.malime.databinding.FragmentProfileBinding
import com.chesire.malime.flow.ViewModelFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_profile.fragmentProfileAvatar
import kotlinx.android.synthetic.main.fragment_profile.fragmentProfileCollapsingToolbar
import kotlinx.android.synthetic.main.fragment_profile.fragmentProfileHeaderImage
import kotlinx.android.synthetic.main.fragment_profile.fragmentProfileToolbar
import javax.inject.Inject

@LogLifecykle
class ProfileFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProviders
            .of(this, viewModelFactory)
            .get(ProfileViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentProfileBinding
        .inflate(inflater, container, false)
        .apply {
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
            fragmentProfileAnimeProgress.viewSeriesProgressTitle.text =
                getString(R.string.nav_anime)
            fragmentProfileMangaProgress.viewSeriesProgressTitle.text =
                getString(R.string.nav_manga)
        }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.setSupportActionBar(fragmentProfileToolbar)

        viewModel.user.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                fragmentProfileCollapsingToolbar.title = it.name
                setImagery(it)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_profile, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuProfileSettings -> findNavController().navigate(ProfileFragmentDirections.toSettings())
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setImagery(user: UserModel) {
        Glide.with(requireContext())
            .load(user.coverImage.smallest?.url)
            .into(fragmentProfileHeaderImage)
        Glide.with(requireContext())
            .load(user.avatar.largest?.url)
            .apply(RequestOptions.circleCropTransform())
            .into(fragmentProfileAvatar)
    }
}
