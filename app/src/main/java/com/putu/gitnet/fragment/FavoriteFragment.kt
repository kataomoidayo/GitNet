package com.putu.gitnet.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.putu.gitnet.R
import com.putu.gitnet.adapter.FavoriteAdapter
import com.putu.gitnet.database.UserEntity
import com.putu.gitnet.databinding.FragmentFavoriteBinding
import com.putu.gitnet.model.UserModel
import com.putu.gitnet.viewmodel.FavoriteViewModel
import com.putu.gitnet.viewmodel.ViewModelFactory

class FavoriteFragment : Fragment(), MenuProvider {

    private var _fragmentFavoriteBinding : FragmentFavoriteBinding? = null
    private val fragmentFavoriteBinding get() = _fragmentFavoriteBinding

    private lateinit var favoriteViewModel: FavoriteViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _fragmentFavoriteBinding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return fragmentFavoriteBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteViewModel = obtainViewModel(this)

        favoriteViewModel.getAllFavoriteUser().observe(viewLifecycleOwner) { favoriteList ->
            setRecyclerData(favoriteList)
        }

        (activity as AppCompatActivity).supportActionBar?.title = "Favorite"

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setRecyclerData(list: List<UserEntity>) {
        val favoriteList = ArrayList<UserEntity>()

        for (user in list) {
            favoriteList.clear()
            favoriteList.addAll(list)
        }

        fragmentFavoriteBinding?.rvFavorite?.layoutManager = LinearLayoutManager(context)

        val favoriteAdapter = FavoriteAdapter(favoriteList)
        fragmentFavoriteBinding?.rvFavorite?.adapter = favoriteAdapter

        favoriteAdapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserModel) {
                showUser(data)
            }
        })
    }

    private fun showUser(data: UserModel) {
        val moveDataUser = Bundle()
        moveDataUser.putParcelable(HomeFragment.EXTRA_USER, data)
        NavHostFragment
            .findNavController(this)
            .navigate(R.id.action_favoriteFragment_to_userDetailFragment, moveDataUser)
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

    private fun obtainViewModel(activity : Fragment) : FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.requireActivity().application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    override fun onDestroy() {
        super.onDestroy()
        _fragmentFavoriteBinding = null
    }
}