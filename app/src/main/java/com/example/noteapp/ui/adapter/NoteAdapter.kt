package com.example.noteapp.ui.adapter

import android.graphics.Color
import android.provider.ContactsContract.CommonDataKinds.Note
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.data.models.NoteModel
import com.example.noteapp.R
import com.example.noteapp.databinding.ItemNoteBinding
import com.example.noteapp.interfaces.OnClickItem

class NoteAdapter(
    private val onLongClick: OnClickItem,
    private val onClick: OnClickItem

) : ListAdapter<NoteModel, NoteAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: NoteModel) {
            binding.itemTitle.text = item.title
            binding.itemDescription.text = item.description
            binding.itemDate.text = item.date
            binding.itemTime.text = item.time

            val context = binding.root.context
            binding.containerNote.background = ContextCompat.getDrawable(context, item.color)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemNoteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position))

        holder.itemView.setOnLongClickListener {
            onLongClick.onLongClick(getItem(position))
            true
        }

        holder.itemView.setOnClickListener {
            onClick.onClick(getItem(position))
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<NoteModel>() {
        override fun areItemsTheSame(oldItem: NoteModel, newItem: NoteModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NoteModel, newItem: NoteModel): Boolean {
            return oldItem == newItem
        }
    }
}
