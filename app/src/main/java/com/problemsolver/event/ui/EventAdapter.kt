package com.problemsolver.event.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.problemsolver.event.data.model.Event
import com.problemsolver.event.databinding.ItemEventBinding

class EventAdapter(
    private var events: List<Event> = emptyList(),
    private val onItemClick: (Event) -> Unit
) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemEventBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = events[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = events.size

    inner class ViewHolder(
        private val binding: ItemEventBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Event) {
            with(binding) {
                ivImage.load(item.imageLogo) {
                    crossfade(true)
                }
                tvTitle.text = item.name
            }

            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newData: List<Event>) {
        events = newData
        notifyDataSetChanged()
    }
}