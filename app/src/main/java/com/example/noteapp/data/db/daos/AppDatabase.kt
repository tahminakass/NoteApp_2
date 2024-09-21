package com.example.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.db.daos.NoteDao
import com.example.data.models.NoteModel

@Database(entities = [NoteModel:: class], version = 3)
abstract class AppDatabase: RoomDatabase() {

    abstract fun noteDao(): NoteDao

}