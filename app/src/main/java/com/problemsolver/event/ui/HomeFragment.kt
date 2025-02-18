package com.problemsolver.event.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.problemsolver.event.R
import com.problemsolver.event.data.model.Status
import com.problemsolver.event.databinding.FragmentHomeBinding
import com.problemsolver.event.utils.gone
import com.problemsolver.event.utils.onQueryTextChanged
import com.problemsolver.event.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: EventViewModel by viewModels()
    private val binding by viewBinding(FragmentHomeBinding::bind)
    private lateinit var upcomingAdapter: EventAdapter
    private lateinit var finishedAdapter: EventAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData("")
        initView()
    }

    private fun getData(search: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getAllEvents(search).collect {
                    when (it.status) {
                        Status.LOADING -> {
                            binding.rvUpcoming.veil()
                            binding.rvFinished.veil()
                            binding.tvUpcoming.visible()
                            binding.tvFinished.visible()
                        }
                        Status.SUCCESS -> {
                            upcomingAdapter.setData(it.data?.finished?.take(5) ?: emptyList())
                            finishedAdapter.setData(it.data?.finished?.take(5) ?: emptyList())
                            if (it.data?.upcoming.isNullOrEmpty()) binding.tvUpcoming.gone()
                            else binding.tvUpcoming.visible()
                            if (it.data?.finished.isNullOrEmpty()) binding.tvFinished.gone()
                            else binding.tvFinished.visible()
                            binding.rvUpcoming.unVeil()
                            binding.rvFinished.unVeil()
                        }
                        Status.ERROR -> {
                            binding.rvUpcoming.unVeil()
                            binding.rvFinished.unVeil()
                            binding.tvUpcoming.gone()
                            binding.tvFinished.gone()
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun initView() {
        upcomingAdapter = EventAdapter {
            val intent = Intent(requireContext(), EventDetailActivity::class.java)
            intent.putExtra("id", it.id)
            startActivity(intent)
        }
        finishedAdapter = EventAdapter {
            val intent = Intent(requireContext(), EventDetailActivity::class.java)
            intent.putExtra("id", it.id)
            startActivity(intent)
        }
        with(binding) {
            rvUpcoming.setAdapter(upcomingAdapter)
            rvUpcoming.setLayoutManager(LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false))
            rvUpcoming.addVeiledItems(3)

            rvFinished.setAdapter(finishedAdapter)
            rvFinished.setLayoutManager(LinearLayoutManager(requireContext()))
            rvFinished.addVeiledItems(3)
        }
        binding.searchView.onQueryTextChanged(viewLifecycleOwner.lifecycleScope) {
            getData(it)
        }
    }
}