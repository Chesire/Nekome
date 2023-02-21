package com.chesire.nekome.app.series.collection

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
import com.chesire.nekome.app.series.R
import com.chesire.nekome.app.series.collection.ui.CollectionScreen
import com.chesire.nekome.app.series.collection.ui.CollectionViewModel
import com.chesire.nekome.app.series.collection.ui.ViewAction
import com.chesire.nekome.core.compose.theme.NekomeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CollectionFragment : Fragment(), MenuProvider {

    private val viewModel by viewModels<CollectionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            NekomeTheme {
                CollectionScreen(viewModel = viewModel)
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
        menuInflater.inflate(R.menu.menu_series_list, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        // This should be removed when we move to fully compose
        return when (menuItem.itemId) {
            R.id.menuFilter -> true // showFilterDialog(seriesPreferences)
            R.id.menuSort -> {
                viewModel.execute(ViewAction.SortPressed)
                true
            }
            R.id.menuRefresh -> {
                viewModel.execute(ViewAction.PerformSeriesRefresh)
                true
            }
            else -> false
        }
    }
}
