package com.yunusbedir.userlist.ui.main

import com.yunusbedir.userlist.data.FetchResponse
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.yunusbedir.userlist.data.Person
import com.yunusbedir.userlist.databinding.FragmentMainBinding
import com.yunusbedir.userlist.domain.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment(),
    UserListAdapterCallback {

    private val mainViewModel by viewModels<MainViewModel>()
    private val userListAdapter = UserListAdapter(this)

    private lateinit var binding: FragmentMainBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.userListRecyclerView.adapter = userListAdapter
        initObserver()
        binding.mainSwipeRefreshLayout.setOnRefreshListener {
            userListAdapter.submitList(listOf())
            mainViewModel.getUserList()
        }
        mainViewModel.getUserList()
    }

    private fun initObserver() {
        lifecycleScope.launch {
            mainViewModel.userListLiveData
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    when (it) {
                        is UiState.Success<*> -> {
                            userListAdapter.submitList(
                                (it.data as List<Person>)
                            )
                            progressVisibility(false)
                        }
                        is UiState.Fail -> {
                            Toast.makeText(requireContext(), it.failureMessage, Toast.LENGTH_LONG)
                                .show()
                            progressVisibility(false)
                        }
                    }
                }
        }
    }

    private fun progressVisibility(isVisible: Boolean) {
        binding.mainSwipeRefreshLayout.isRefreshing = isVisible
        binding.mainBottomProgressBar.visibility = if (isVisible.not()) View.GONE else View.VISIBLE
    }

    override fun onRefresh() {
        binding.mainBottomProgressBar.visibility = View.VISIBLE
        mainViewModel.nextUserList(userListAdapter.currentList)
    }

}