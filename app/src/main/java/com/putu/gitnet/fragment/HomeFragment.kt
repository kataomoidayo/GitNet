package com.putu.gitnet.fragment

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.putu.gitnet.R
import com.putu.gitnet.adapter.UserListAdapter
import com.putu.gitnet.databinding.FragmentHomeBinding
import com.putu.gitnet.extra.Helper
import com.putu.gitnet.extra.SettingPreferences
import com.putu.gitnet.model.UserModel
import com.putu.gitnet.viewmodel.MainViewModel
import com.putu.gitnet.viewmodel.SettingsViewModel
import com.putu.gitnet.viewmodel.SettingsViewModelFactory


class HomeFragment : Fragment(), MenuProvider {

    private var _fragmentHomeBinding: FragmentHomeBinding? = null
    private val fragmentHomeBinding get() = _fragmentHomeBinding

    private val homeViewModel by viewModels<MainViewModel>()

    private val helper = Helper()

    private var listUser = ArrayList<UserModel>()

    private lateinit var settingsViewModel : SettingsViewModel

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): RelativeLayout? {
        _fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return fragmentHomeBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewModel()
        checkDarkMode()

        context?.let {
            searchUsername()
        }

        homeViewModel.listUser.observe(viewLifecycleOwner) { listUser ->
            setUser(listUser)
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            fragmentHomeBinding?.let { it1 ->
                helper.isLoading(
                    it,
                    it1.homeProgressBar
                )
            }
        }

        homeViewModel.snackbarText.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { snackBarText ->
                fragmentHomeBinding?.let { it1 ->
                    Snackbar.make(
                        it1.root,
                        snackBarText,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }

        val binding = fragmentHomeBinding?.rvUserList
        binding?.layoutManager = LinearLayoutManager(activity)

        (activity as AppCompatActivity).supportActionBar?.title = "GitNet"

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

    private fun searchUsername() {
        val searchView = fragmentHomeBinding?.homeSearch
        searchView?.queryHint = resources.getString(R.string.search_hint)
        searchView?.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                homeViewModel.searchUser(query)
                query.let {
                    fragmentHomeBinding?.rvUserList?.visibility = View.VISIBLE
                    homeViewModel.searchUser(it)
                    setUser(listUser)
                }
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    private fun setUser(users: List<UserModel>) {
        val listUser = ArrayList<UserModel>()

        for (user in users) {
            listUser.clear()
            listUser.addAll(users)
        }

        val userListAdapter = UserListAdapter(listUser)
        fragmentHomeBinding?.rvUserList?.adapter = userListAdapter

        userListAdapter.setOnItemClickCallback(object : UserListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserModel) {
                showUser(data)
            }
        })
    }

    private fun showUser(data: UserModel) {
        val moveDataUser = Bundle()
        moveDataUser.putParcelable(EXTRA_USER, data)
        NavHostFragment
            .findNavController(this)
            .navigate(R.id.action_homeFragment_to_userDetailFragment, moveDataUser)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_list, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.favoriteBtn -> {
                NavHostFragment
                    .findNavController(this@HomeFragment)
                    .navigate(R.id.action_homeFragment_to_favoriteFragment)
                true
            }

            R.id.settingsBtn -> {
                NavHostFragment
                    .findNavController(this@HomeFragment)
                    .navigate(R.id.action_homeFragment_to_settingsFragment)
                true
            }
            else -> false
        }
    }

    private fun setViewModel() {
        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        settingsViewModel = ViewModelProvider(this, SettingsViewModelFactory(pref))[SettingsViewModel::class.java]
    }

    private fun checkDarkMode() {
        settingsViewModel.getThemeSettings().observe(viewLifecycleOwner) {isDarkTheme ->
            if (isDarkTheme) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _fragmentHomeBinding = null
    }

    companion object {
        const val EXTRA_USER = "extra_user"
    }
}