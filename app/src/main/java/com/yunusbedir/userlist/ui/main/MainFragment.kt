package com.yunusbedir.userlist.ui.main

import com.yunusbedir.userlist.data.FetchResponse
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
class MainFragment : Fragment() {

    private val mainViewModel by viewModels<MainViewModel>()
    private val userListAdapter = UserListAdapter()

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
        binding.mainSwipeRefreshLayout.setOnRefreshListener {
            userListAdapter.submitList(null)
            mainViewModel.nextUserList()
        }
        binding.userListRecyclerView.adapter = userListAdapter
        initObserver()
    }

    private fun initObserver() = lifecycleScope.launch {
        mainViewModel.userListLiveData
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .collect {
                when (it) {
                    is UiState.Success<*> -> {
                        val userList = ArrayList<Person>()
                        userList.addAll(userListAdapter.currentList)
                        userList.addAll((it.data as FetchResponse).people)
                        userListAdapter.submitList(
                            userList
                        )
                        progressVisibility(false)
                    }
                    is UiState.Fail -> {
                        progressVisibility(false)
                    }
                    UiState.Loading -> {
                        progressVisibility(true)
                    }
                }
            }
    }

    fun progressVisibility(isVisible: Boolean) {
        binding.mainSwipeRefreshLayout.isRefreshing = isVisible
    }

}