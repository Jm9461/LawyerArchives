package com.lawyer_archives.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lawyer_archives.fragments.DailyTasksFragment
import com.lawyer_archives.fragments.ClientCaseTasksFragment // این خط باید باشه

class OfficeManagementPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DailyTasksFragment()
            1 -> ClientCaseTasksFragment()   // حالا کاملاً شناخته میشه
            else -> DailyTasksFragment()
        }
    }
}