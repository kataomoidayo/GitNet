package com.putu.gitnet.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.putu.gitnet.R
import com.putu.gitnet.adapter.SectionsPagerAdapter
import com.putu.gitnet.database.UserEntity
import com.putu.gitnet.databinding.FragmentUserDetailBinding
import com.putu.gitnet.extra.Helper
import com.putu.gitnet.model.UserModel
import com.putu.gitnet.viewmodel.UserDetailViewModel
import com.putu.gitnet.viewmodel.ViewModelFactory

class UserDetailFragment : Fragment(), MenuProvider {

    private var _fragmentUserDetailBinding: FragmentUserDetailBinding? = null
    private val fragmentUserDetailBinding get() = _fragmentUserDetailBinding

    private lateinit var username: String

    private val helper = Helper()

    private lateinit var userDetailViewModel : UserDetailViewModel

    private var buttonState : Boolean = false

    private var userEntity : UserEntity? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _fragmentUserDetailBinding = FragmentUserDetailBinding.inflate(inflater, container, false)
        return fragmentUserDetailBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUsername()
        setViewPager()

        userDetailViewModel = obtainViewModel(this)

        (activity as AppCompatActivity).supportActionBar?.title = username

        userDetailViewModel.getUserDetail(username)

        userDetailViewModel.checkFavoriteStatus(username).observe(viewLifecycleOwner) { favoriteList ->
            buttonState = favoriteList.isNotEmpty()
            if (buttonState) {
                fragmentUserDetailBinding?.actionFavorite?.setImageResource(R.drawable.ic_favorite)
            } else {
                fragmentUserDetailBinding?.actionFavorite?.setImageResource(R.drawable.ic_favorite_border)
            }
        }

        userDetailViewModel.userDetail.observe(viewLifecycleOwner) { user ->
            setUserData(user)

            fragmentUserDetailBinding?.actionFavorite?.setOnClickListener {
                if (!buttonState) {
                    buttonState = true
                    fragmentUserDetailBinding?.actionFavorite?.setImageResource(R.drawable.ic_favorite)
                    userEntity = UserEntity(
                        user.id,
                        user.bio,
                        user.login,
                        user.company,
                        user.publicRepos,
                        user.followers,
                        user.avatarUrl,
                        user.following,
                        user.name,
                        user.htmlUrl
                    )

                    userDetailViewModel.addUserToFavorite(userEntity as UserEntity)

                    fragmentUserDetailBinding?.root?.let { it1 ->
                        Snackbar.make(it1, "User Added to Favorite", Snackbar.LENGTH_SHORT).show()
                    }

                } else {
                    buttonState = false

                    fragmentUserDetailBinding?.actionFavorite?.setImageResource(R.drawable.ic_favorite_border)
                    userDetailViewModel.deleteUserFromFavorite(user.id)

                    fragmentUserDetailBinding?.root?.let { it1 ->
                        Snackbar.make(it1, "User Removed From Favorite", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }

        fragmentUserDetailBinding?.actionFavorite?.setOnClickListener {
            fragmentUserDetailBinding?.actionFavorite?.setImageResource(R.drawable.ic_favorite)
        }

        userDetailViewModel.isLoading.observe(viewLifecycleOwner) {
            fragmentUserDetailBinding?.profileProgressBar?.let { it1 ->
                helper.isLoading(
                    it,
                    it1
                )
            }
        }

        userDetailViewModel.snackbarText.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { snackBarText ->
                fragmentUserDetailBinding?.let { it1 ->
                    Snackbar.make(
                        it1.root,
                        snackBarText,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setUsername() {
        val user = arguments?.getParcelable<UserModel>(HomeFragment.EXTRA_USER) as UserModel
        this.username = user.login.toString()
    }

    private fun setUserData(userItem: UserModel) {
        fragmentUserDetailBinding?.apply {
            Glide.with(this@UserDetailFragment)
                .load(userItem.avatarUrl)
                .circleCrop()
                .into(detailImgAvatar)

            if (userItem.name != null) {
                fragmentUserDetailBinding?.profileName?.text = userItem.name
                fragmentUserDetailBinding?.profileName?.visibility = View.VISIBLE
            } else {
                fragmentUserDetailBinding?.profileName?.visibility = View.GONE
            }

            if (userItem.bio != null) {
                fragmentUserDetailBinding?.profileBio?.text = userItem.bio.toString()
                fragmentUserDetailBinding?.relativeBio?.visibility = View.VISIBLE
            } else {
                fragmentUserDetailBinding?.relativeBio?.visibility = View.GONE
            }

            if (userItem.company != null) {
                fragmentUserDetailBinding?.profileCompanyName?.text = userItem.company
                fragmentUserDetailBinding?.relativeCompany?.visibility = View.VISIBLE
            } else {
                fragmentUserDetailBinding?.relativeCompany?.visibility = View.GONE
            }

            profileLink.text = userItem.htmlUrl
            repositoryCounter.text = userItem.publicRepos.toString()
            followersCounter.text = userItem.followers.toString()
            followingCounter.text = userItem.following.toString()

            actionShare.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                intent.putExtra(Intent.EXTRA_TEXT, "${userItem.login}" + "\n\n${userItem.htmlUrl}")
                intent.type = "text/plain"
                startActivity(Intent.createChooser(intent, "Share to"))
            }
        }
    }

    private fun setViewPager() {

        val viewPager: ViewPager2? = view?.findViewById(R.id.profile_view_pager)

        val tabs: TabLayout? = view?.findViewById(R.id.profile_tabs)

        val username = Bundle()
        username.putString(EXTRA_USERNAME, this.username)

        val sectionsPagerAdapter = SectionsPagerAdapter(childFragmentManager, lifecycle, username)

        viewPager?.adapter = sectionsPagerAdapter
        if (viewPager != null && tabs != null) {
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            android.R.id.home -> {
                activity?.onBackPressedDispatcher?.onBackPressed()
                true
            }
            else -> false
        }
    }

    private fun obtainViewModel(activity: Fragment) : UserDetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.requireActivity().application)
        return ViewModelProvider(activity, factory)[UserDetailViewModel::class.java]
    }

    override fun onDestroy() {
        super.onDestroy()
        _fragmentUserDetailBinding = null
    }

    companion object{
        const val EXTRA_USERNAME = "extra_username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers_tab,
            R.string.following_tab
        )
    }
}