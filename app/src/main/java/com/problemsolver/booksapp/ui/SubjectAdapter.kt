package com.problemsolver.booksapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.problemsolver.booksapp.databinding.ItemSubjectBinding

class SubjectAdapter(
    private val subjects: List<String>
): RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        val binding = ItemSubjectBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SubjectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        holder.bind(subjects[position])
    }

    override fun getItemCount() = subjects.size

    inner class SubjectViewHolder(
        private val binding: ItemSubjectBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(subject: String) {
            binding.tvSubject.text = subject
        }
    }
}