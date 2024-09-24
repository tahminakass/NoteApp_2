package com.example.noteapp.ui.fragments.note

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.data.models.NoteModel
import com.example.noteapp.App
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentNoteDetailBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.properties.Delegates

class NoteDetailFragment : Fragment() {

    private lateinit var binding: FragmentNoteDetailBinding
    private var savedColor by Delegates.notNull<Int>()
    private var colorResourse by Delegates.notNull<Int>()
    private var noteId: Int = -1
    private val args by navArgs<NoteDetailFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        upDateNote()
        setUpListeners()
        displayCurrentDateTime()
    }

    private fun upDateNote() {
        noteId = args.noteID
        if (noteId != -1) {
            val argsNote = App.appDatabase?.noteDao()?.getNoteById(noteId)
            argsNote?.let { item ->
                binding.etTitle.setText(item.title)
                binding.etAddDescription.setText(item.description)

                savedColor = item.color
                setCheckedRadioButton(item.color)
            }
        }
    }

    private fun setCheckedRadioButton(color: Int) {
        when (color) {
            R.drawable.ic_style_gray -> binding.rb1.isChecked = true
            R.drawable.ic_style_yellow -> binding.rb2.isChecked = true
            R.drawable.ic_style_brown -> binding.rb3.isChecked = true
        }
    }

    private fun setUpListeners() = with(binding) {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.btnAdd.visibility =
                    if (binding.etTitle.text.isNotEmpty() || binding.etAddDescription.text.isNotEmpty()) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        binding.etTitle.addTextChangedListener(textWatcher)
        binding.etAddDescription.addTextChangedListener(textWatcher)
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            colorResourse = when (checkedId) {
                binding.rb1.id -> R.drawable.ic_style_gray
                binding.rb2.id -> R.drawable.ic_style_yellow
                binding.rb3.id -> R.drawable.ic_style_brown
                else -> R.drawable.ic_style_yellow // default value
            }
        }

        binding.btnAdd.setOnClickListener {
            val etTitle = binding.etTitle.text.toString()
            val etDescription = binding.etAddDescription.text.toString()
            val itemDate = binding.date.text.toString()
            val itemTime = binding.time.text.toString()

            if (noteId != -1) {
                val updateNote = NoteModel(etTitle, etDescription, itemDate, itemTime, savedColor)
                updateNote.id = noteId
                App.appDatabase?.noteDao()?.updateNote(updateNote)
            } else {
                App().getInstance()?.noteDao()?.insertNote(
                    NoteModel(
                        title = etTitle,
                        description = etDescription,
                        date = itemDate,
                        time = itemTime,
                        color = colorResourse
                    )
                )
            }
            findNavController().navigateUp()
        }

        binding.returnBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnAdd.visibility = View.GONE
    }

    private fun displayCurrentDateTime() {
        val currentDateTime = LocalDateTime.now()
        val formatterDate = DateTimeFormatter.ofPattern("dd MMMM")
        val formatterTime = DateTimeFormatter.ofPattern("HH:mm")
        val formattedDate = currentDateTime.format(formatterDate)
        val formattedTime = currentDateTime.format(formatterTime)
        binding.date.text = formattedDate
        binding.time.text = formattedTime
    }
}