package com.problemsolver.event.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.problemsolver.event.R
import com.problemsolver.event.databinding.FragmentFavoriteBinding
import com.problemsolver.event.utils.gone
import com.problemsolver.event.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private val binding by viewBinding(FragmentFavoriteBinding::bind)
    private val viewModel: EventViewModel by viewModels()
    private lateinit var adapter: EventAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initData()
    }

    private fun initData() {
        binding.rvEvents.veil()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.getFavoriteEvents().collect {
                    if (it.isEmpty()) {
                        binding.rvEvents.gone()
                        binding.tvEmpty.visible()
                    } else {
                        binding.rvEvents.visible()
                        binding.tvEmpty.gone()
                    }
                    adapter.setData(it)
                    binding.rvEvents.unVeil()
                }
            }
        }
    }

    private fun initViews() {
        adapter = EventAdapter {
            val intent = Intent(requireContext(), EventDetailActivity::class.java)
            intent.putExtra("id", it.id)
            startActivity(intent)
        }
        with(binding) {
            rvEvents.setAdapter(adapter)
            rvEvents.setLayoutManager(GridLayoutManager(requireContext(), 2))
            rvEvents.addVeiledItems(6)
        }
    }
}