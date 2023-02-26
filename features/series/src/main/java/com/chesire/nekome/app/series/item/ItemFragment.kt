package com.chesire.nekome.app.series.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import com.chesire.nekome.app.series.R
import com.chesire.nekome.app.series.item.ui.ItemScreen
import com.chesire.nekome.app.series.item.ui.ItemViewModel
import com.chesire.nekome.app.series.item.ui.ViewAction
import com.chesire.nekome.core.compose.theme.NekomeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemFragment : Fragment(), MenuProvider {

    private val viewModel by viewModels<ItemViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            NekomeTheme {
                ItemScreen(
                    viewModel = viewModel,
                    finishScreen = { findNavController().popBackStack() }
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost = requireActivity() as MenuHost
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        // This should be removed when we move to fully compose
        menuInflater.inflate(R.menu.menu_item, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        // This should be removed when we move to fully compose
        return when (menuItem.itemId) {
            R.id.menuDelete -> {
                viewModel.execute(ViewAction.DeletePressed)
                true
            }
            else -> false
        }
    }
}
