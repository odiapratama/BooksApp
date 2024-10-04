package com.problemsolver.booksapp.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.problemsolver.booksapp.data.model.WantToRead
import com.problemsolver.booksapp.databinding.ItemBookBinding

class BooksAdapter(
    private var books: List<WantToRead.Work> = emptyList(),
    private val onItemClick: (WantToRead.Work) -> Unit
) : RecyclerView.Adapter<BooksAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemBookBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = books[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = books.size

    inner class ViewHolder(
        private val binding: ItemBookBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: WantToRead.Work) {
            with(binding) {
                ivImage.load(item.work?.getCoverMediumUrl()) {
                    crossfade(true)
                }
                tvTitle.text = item.work?.title
                tvAuthor.text = item.work?.authorNames?.joinToString()
            }

            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setBooks(newDataBooks: List<WantToRead.Work>) {
        books = newDataBooks
        notifyDataSetChanged()
    }
}