package com.example.noteapp.ui.fragments.note

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.example.data.models.NoteModel
import com.example.noteapp.App
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentNoteDetailBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NoteDetailFragment : Fragment() {

    private lateinit var binding: FragmentNoteDetailBinding
    private lateinit var savedColor: String
    private var colorResourse: Int = R.drawable.ic_style
    private var noteId: Int = -1


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
        arguments?.let { args ->
            noteId = args.getInt("noteId", -1)
        }
        if (noteId != -1) {
            val argsNote = App.appDatabase?.noteDao()?.getNoteById(noteId)
            argsNote?.let { item ->
                binding.etTitle.setText(item.title)
                binding.etAddDescription.setText(item.description)
                savedColor = item.color
            }
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
                App.appDatabase?.noteDao()
                    ?.insertNote(NoteModel(etTitle, etDescription, itemDate, itemTime, savedColor))

                App().getInstance()?.noteDao()?.insertNote(
                    NoteModel(

                        title = etTitle,
                        description = etDescription,
                        date = itemDate,
                        time = itemTime,
                        color = colorResourse.toString()
                    )
                )
            }
            findNavController().navigateUp()
        }

        binding.returnBtn.setOnClickListener {
            findNavController().navigate(R.id.action_noteFragment_to_noteDetailFragment)
        }

        binding.btnAdd.visibility = View.GONE
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            colorResourse = when (checkedId) {
                binding.rb1.id -> R.drawable.ic_style
                binding.rb2.id -> R.drawable.ic_style_et
                binding.rb3.id -> R.drawable.ic_style
                else -> R.drawable.ic_style_et
            }
        }
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