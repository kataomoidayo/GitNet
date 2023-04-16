package com.putu.gitnet.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.putu.gitnet.adapter.FollowingAdapter
import com.putu.gitnet.databinding.FragmentFollowingBinding
import com.putu.gitnet.extra.Helper
import com.putu.gitnet.model.UserModel
import com.putu.gitnet.viewmodel.FollowingViewModel

class FollowingFragment : Fragment() {

    private var _fragmentFollowingBinding: FragmentFollowingBinding? = null
    private val fragmentFollowingBinding get() = _fragmentFollowingBinding

    private val followingViewModel by viewModels<FollowingViewModel>()

    private val helper = Helper()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): RelativeLayout? {
        _fragmentFollowingBinding = FragmentFollowingBinding.inflate(inflater, container, false)
        return fragmentFollowingBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followingViewModel.loading.observe(viewLifecycleOwner) {
            fragmentFollowingBinding?.let { it1 ->
                helper.isLoading(
                    it,
                    it1.followingProgressBar
                )}
        }

        followingViewModel.following.observe(viewLifecycleOwner) { followingList ->
            setRecyclerData(followingList)
        }

        followingViewModel.getFollowing(
            arguments?.getString(UserDetailFragment.EXTRA_USERNAME).toString()
        )

        followingViewModel.snackbarText.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { snackBarText ->
                fragmentFollowingBinding?.root?.let { it1 ->
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
        val followingList = ArrayList<UserModel>()
        for (user in list) {
            followingList.clear()
            followingList.addAll(list)
        }
        fragmentFollowingBinding?.rvFollowingList?.layoutManager = LinearLayoutManager(context)

        val followingAdapter = FollowingAdapter(followingList)
        fragmentFollowingBinding?.rvFollowingList?.adapter = followingAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _fragmentFollowingBinding = null
    }
}