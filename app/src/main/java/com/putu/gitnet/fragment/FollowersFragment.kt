package com.putu.gitnet.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.putu.gitnet.adapter.FollowersAdapter
import com.putu.gitnet.databinding.FragmentFollowersBinding
import com.putu.gitnet.extra.Helper
import com.putu.gitnet.model.UserModel
import com.putu.gitnet.viewmodel.FollowersViewModel


class FollowersFragment : Fragment() {

    private var _fragmentFollowersBinding: FragmentFollowersBinding? = null
    private val fragmentFollowersBinding get() = _fragmentFollowersBinding

    private val followersViewModel by viewModels<FollowersViewModel>()

    private val helper = Helper()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): RelativeLayout? {
        _fragmentFollowersBinding = FragmentFollowersBinding.inflate(inflater, container, false)
        return fragmentFollowersBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followersViewModel.loading.observe(viewLifecycleOwner) {
            fragmentFollowersBinding?.let { it1 ->
                helper.isLoading(
                    it,
                    it1.followersProgressBar
                )}
        }

        followersViewModel.followers.observe(viewLifecycleOwner) { followerList ->
            setRecyclerData(followerList)
        }

        followersViewModel.getFollowers(
            arguments?.getString(UserDetailFragment.EXTRA_USERNAME).toString()
        )

        followersViewModel.snackbarText.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { snackBarText ->
                fragmentFollowersBinding?.root?.let { it1 ->
                    Snackbar.make(
                        it1,
                        snackBarText,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setRecyclerData(list: List<UserModel>) {
        val followersList = ArrayList<UserModel>()
        for (user in list) {
            followersList.clear()
            followersList.addAll(list)
        }
        fragmentFollowersBinding?.rvFollowersList?.layoutManager = LinearLayoutManager(context)

        val followersAdapter = FollowersAdapter(followersList)
        fragmentFollowersBinding?.rvFollowersList?.adapter = followersAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _fragmentFollowersBinding= null
    }
}