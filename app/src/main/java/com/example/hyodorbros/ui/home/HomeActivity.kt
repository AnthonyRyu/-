package com.example.hyodorbros.ui.home

import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.hyodorbros.R
import com.example.hyodorbros.base.BaseActivity
import com.example.hyodorbros.databinding.ActivityHomeBinding
import com.example.hyodorbros.ui.adapter.FragmentPagerAdapter
import com.example.hyodorbros.ui.community.CommunityFragment
import com.example.hyodorbros.ui.dday.DDayFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel, HomeViewState>() {
    override val bindLayout: Int
        get() = R.layout.activity_home
    override val viewModel: HomeViewModel by viewModels()

    private val pageTriple: List<Triple<Fragment, String, Int>> =
        listOf(
            Triple(DDayFragment(), "D-Day", R.drawable.ic_calendar),
            Triple(CommunityFragment(), "Community", R.drawable.ic_community),
        )

    private val tabConfigurationStrategy =
        TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            tab.text = pageTriple[position].second
            tab.icon = getDrawable(pageTriple[position].third)
        }

    override fun initUi() {
        val pagerAdapter = FragmentPagerAdapter(pageTriple.map { it.first }, this)

        with(binding) {
            viewpager.adapter = pagerAdapter
            viewpager.offscreenPageLimit = 3
            viewpager.isUserInputEnabled = false
            TabLayoutMediator(tab, viewpager, tabConfigurationStrategy).attach()
        }
    }


    override fun onChangedViewState(viewState: HomeViewState) {
        when (viewState) {
            else -> {}
        }
    }

    override fun onBackPressed() {
        if (binding.viewpager.currentItem == 0) {
            val navHostFragment =
                (supportFragmentManager.findFragmentByTag("f0")?.childFragmentManager?.findFragmentById(
                    R.id.nav_dday
                ) as NavHostFragment)
            if (navHostFragment.childFragmentManager.backStackEntryCount == 0) {
                super.onBackPressed()
            } else {
                navHostFragment.findNavController().popBackStack()
            }
        } else if (binding.viewpager.currentItem == 1) {
            val navHostFragment =
                (supportFragmentManager.findFragmentByTag("f1")?.childFragmentManager?.findFragmentById(
                    R.id.nav_home
                ) as NavHostFragment)
            if (navHostFragment.childFragmentManager.backStackEntryCount == 0) {
                super.onBackPressed()
            } else {
                navHostFragment.findNavController().popBackStack()
            }
        } else {
            super.onBackPressed()
        }
    }
}