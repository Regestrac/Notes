package com.regestrac.notes.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.regestrac.notes.Modals.Note
import com.regestrac.notes.R
import kotlin.random.Random

class NotesAdapter(private val context: Context, private val listener: NotesClickListener) :
    RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private val notesList = ArrayList<Note>()
    private val fullList = ArrayList<Note>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = notesList[position]

        val colorMap: Map<String, Int> = mapOf(
            "Black" to R.color.noteListDefaultColor,
            "Red" to R.color.NoteColor1,
            "Cyan" to R.color.NoteColor2,
            "Green" to R.color.NoteColor3,
            "Yellow" to R.color.NoteColor4,
            "Blue" to R.color.NoteColor5,
            "Pink" to R.color.NoteColor6,
            "Purple" to R.color.NoteColor7,
        )

        holder.title.text = currentNote.title
        holder.noteTv.text = currentNote.note
        holder.date.text = currentNote.date

        holder.title.isSelected = true
        holder.date.isSelected = true

        val selectedColor = colorMap[currentNote.bgColor]?.let { ContextCompat.getColor(context, it) }
        if (selectedColor != null && currentNote.bgColor != "Black") {
            holder.notesLayout.setCardBackgroundColor(selectedColor)
        } else {
            holder.notesLayout.setCardBackgroundColor(ContextCompat.getColor(context, R.color.noteListDefaultColor))
            holder.title.setTextColor(ContextCompat.getColor(context, R.color.white))
            holder.noteTv.setTextColor(ContextCompat.getColor(context, R.color.white))
            holder.date.setTextColor(ContextCompat.getColor(context, R.color.white))
        }

        holder.notesLayout.setOnClickListener {
            listener.onItemClicked(notesList[holder.adapterPosition])
        }

        holder.notesLayout.setOnLongClickListener {
            listener.onLongItemClicked(notesList[holder.adapterPosition], holder.notesLayout)
            true
        }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    fun updateList(newList: List<Note>) {
        fullList.clear()
        fullList.addAll(newList)

        notesList.clear()
        notesList.addAll(fullList)

        notifyDataSetChanged()
    }

    fun filterList(search: String) {
        notesList.clear()

        for (item in fullList) {
            if (item.title?.lowercase()?.contains(search.lowercase()) == true ||
                item.note?.lowercase()?.contains(search.lowercase()) == true
            ) {
                notesList.add(item)
            }
        }

        notifyDataSetChanged()
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notesLayout: CardView = itemView.findViewById(R.id.card_layout)
        val title: TextView = itemView.findViewById(R.id.tv_title)
        val noteTv: TextView = itemView.findViewById(R.id.tv_note)
        val date: TextView = itemView.findViewById(R.id.tv_date)
    }

    interface NotesClickListener {
        fun onItemClicked(note: Note)
        fun onLongItemClicked(note: Note, cardView: CardView)
    }
}