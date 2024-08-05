package com.example.tasktrack.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tasktrack.fragment.CompletedTasksFragment
import com.example.tasktrack.fragment.SearchableFragment
import com.example.tasktrack.fragment.UncompletedTasksFragment
import java.lang.IllegalStateException

class ViewPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    private val fragments = listOf<Fragment>(
        UncompletedTasksFragment(),
        CompletedTasksFragment()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

    fun getCurrentFragment(position: Int): SearchableFragment? {
        return fragments.getOrNull(position) as? SearchableFragment
    }

}