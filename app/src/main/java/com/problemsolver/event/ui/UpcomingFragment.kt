package com.problemsolver.event.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.problemsolver.event.R
import com.problemsolver.event.data.model.Status
import com.problemsolver.event.databinding.FragmentUpcomingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpcomingFragment : Fragment(R.layout.fragment_upcoming) {

    private val viewModel: EventViewModel by viewModels()
    private val binding by viewBinding(FragmentUpcomingBinding::bind)
    private lateinit var adapter: EventAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initData()
        initViews()
    }

    private fun initData() {
        viewModel.getEvents(active = 1)
    }

    private fun initViews() {
        adapter = EventAdapter {
            val intent = Intent(requireContext(), EventDetailActivity::class.java)
            intent.putExtra("id", it.id)
            startActivity(intent)
        }
        with(binding) {
            rvEvents.setAdapter(adapter)
            rvEvents.setLayoutManager(LinearLayoutManager(requireContext()))
            rvEvents.addVeiledItems(6)
        }
    }

    private fun initObserver() {
        viewModel.events.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    binding.rvEvents.veil()
                }

                Status.SUCCESS -> {
                    binding.rvEvents.unVeil()
                    it.data?.let { data ->
                        adapter.setBooks(data)
                    }
                }

                Status.ERROR -> {
                    binding.rvEvents.unVeil()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}