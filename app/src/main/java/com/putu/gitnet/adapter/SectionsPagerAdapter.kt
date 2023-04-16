package com.putu.gitnet.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.putu.gitnet.fragment.FollowersFragment
import com.putu.gitnet.fragment.FollowingFragment

class SectionsPagerAdapter(activity: FragmentManager, lifecycle: Lifecycle, private val login: Bundle) : FragmentStateAdapter(activity, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> { fragment = FollowersFragment() }
            1 -> { fragment = FollowingFragment() }
        }

        fragment?.arguments = login
        return fragment as Fragment
    }

    override fun getItemCount(): Int = 2
}