package com.regestrac.notes

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.regestrac.notes.Modals.Note
import com.regestrac.notes.databinding.ActivityAddNoteBinding
import java.text.SimpleDateFormat
import java.util.Date


class AddNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var note: Note
    private lateinit var oldNote: Note
    private var isUpdate = false

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
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
            oldNote = intent.getSerializableExtra("current_note") as Note
            binding.etTitle.setText(oldNote.title)
            binding.etNote.setText(oldNote.note)
            binding.selectColor.text = oldNote.bgColor
            val selectedColor = colorMap[oldNote.bgColor]?.let { ContextCompat.getColor(this, it) }
            if (selectedColor != null && oldNote.bgColor != "Black") {
                binding.selectColor.setBackgroundColor(selectedColor)
                binding.selectColor.setTextColor(ContextCompat.getColor(this, R.color.black))

                val selectColorBackground = ContextCompat.getDrawable(this, R.drawable.select_color_background)
                selectColorBackground?.setTint(selectedColor)
                binding.selectColor.background = selectColorBackground
            }
            isUpdate = true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding.imgCheck.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val noteDescription = binding.etNote.text.toString()
            val bgColor = binding.selectColor.text.toString()

            if (title.isNotEmpty() || noteDescription.isNotEmpty()) {
                val formatter = SimpleDateFormat("EEE d MMM yyyy hh:mm a")

                note = if (isUpdate) {
                    Note(oldNote.id, title, noteDescription, formatter.format(Date()), bgColor)
                } else {
                    Note(null, title, noteDescription, formatter.format(Date()), bgColor)
                }

                val intent = Intent()
                intent.putExtra("note", note)
                setResult(Activity.RESULT_OK, intent)
            } else {
                Toast.makeText(this@AddNoteActivity, "Please enter some data", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
        }

        binding.imgBackArrow.setOnClickListener {
            onBackPressed()
        }

        val colors = arrayOf(
            R.color.noteListDefaultColor,
            R.color.NoteColor1,
            R.color.NoteColor2,
            R.color.NoteColor3,
            R.color.NoteColor4,
            R.color.NoteColor5,
            R.color.NoteColor6,
            R.color.NoteColor7,
        )

        binding.selectColor.setOnClickListener {
            val colorNames = arrayOf("Black", "Red", "Cyan", "Green", "Yellow", "Blue", "Pink", "Purple")
            val selectColorDialog = AlertDialog.Builder(this@AddNoteActivity)
            selectColorDialog.setTitle("Select a Color")
            selectColorDialog.setCancelable(true)

            val adapter = ColorAdapter(this@AddNoteActivity, R.layout.color_item, colors, colorNames)
            selectColorDialog.setAdapter(adapter) { _, which ->
                binding.selectColor.text = colorNames[which]
                val selectedColor = ContextCompat.getColor(this, colors[which])

                binding.selectColor.setBackgroundColor(selectedColor)
                if (colorNames[which] != "Black") {
                    binding.selectColor.setTextColor(ContextCompat.getColor(this, R.color.black))
                }

                val selectColorBackground = ContextCompat.getDrawable(this, R.drawable.select_color_background)
                selectColorBackground?.setTint(selectedColor)
                binding.selectColor.background = selectColorBackground

            }
            selectColorDialog.setNeutralButton("Cancel"){_, _ ->
            }

            val alertBox = selectColorDialog.create()
            alertBox.show()
        }

    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("SimpleDateFormat")
    override fun onBackPressed() {
        val title = binding.etTitle.text.toString()
        val noteDescription = binding.etNote.text.toString()
        val bgColor = binding.selectColor.text.toString()

        if (title.isNotEmpty() || noteDescription.isNotEmpty()) {
            val formatter = SimpleDateFormat("EEE d MMM yyyy hh:mm a")

            note = if (isUpdate) {
                Note(oldNote.id, title, noteDescription, formatter.format(Date()), bgColor)
            } else {
                Note(null, title, noteDescription, formatter.format(Date()), bgColor)
            }

            val intent = Intent()
            intent.putExtra("note", note)
            setResult(Activity.RESULT_OK, intent)
        }
        super.onBackPressed()
    }
    class ColorAdapter(context: Context, resource: Int, private val colors: Array<Int>, private val colorNames: Array<String>) :
        ArrayAdapter<String>(context, resource, colorNames) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.color_item, parent, false)

            val colorCircle = view.findViewById<View>(R.id.colorCircle)
            val colorResId = colors[position]

            // Set background color of the circle
            val circularDrawable = ContextCompat.getDrawable(context, R.drawable.circle_background)
            colorCircle.background = circularDrawable
            circularDrawable?.setTint(ContextCompat.getColor(context, colorResId))

            // Set color name
            val colorNameTextView = view.findViewById<TextView>(R.id.colorName)
            colorNameTextView.text = colorNames[position]

            return view
        }
    }

}
