package com.problemsolver.booksapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.problemsolver.booksapp.R
import com.problemsolver.booksapp.data.model.Status
import com.problemsolver.booksapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: BooksViewModel by viewModels()
    private val binding by viewBinding(FragmentHomeBinding::bind)
    private lateinit var adapter: BooksAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initData()
        initViews()
    }

    private fun initData() {
        viewModel.getWantToRead()
    }

    private fun initViews() {
        adapter = BooksAdapter {
            val intent = Intent(requireContext(), BookDetailActivity::class.java)
            intent.putExtra("key", it.work?.key)
            intent.putExtra("imageUrl", it.work?.getCoverLargeUrl())
            startActivity(intent)
        }
        with(binding) {
            rvBooks.setAdapter(adapter)
            rvBooks.setLayoutManager(GridLayoutManager(requireContext(), 2))
            rvBooks.addVeiledItems(6)
        }
    }

    private fun initObserver() {
        viewModel.wantToRead.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    binding.rvBooks.veil()
                }

                Status.SUCCESS -> {
                    binding.rvBooks.unVeil()
                    it.data?.readingLogEntries?.let { books ->
                        adapter.setBooks(books)
                    }
                }

                Status.ERROR -> {
                    binding.rvBooks.unVeil()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}