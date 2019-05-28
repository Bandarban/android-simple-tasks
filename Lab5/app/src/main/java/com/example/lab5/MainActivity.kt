package com.example.lab5

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.TextView


object FeedReaderContract {
    // Table contents are grouped together in an anonymous object.
    object FeedEntry : BaseColumns {
        const val TABLE_NAME = "dictionary"
        const val COLUMN_NAME_WORD = "word"
        const val COLUMN_NAME_TRANSLATION = "translation"
        const val COLUMN_NAME_WORD_TYPE = "wordType"
    }

    private const val SQL_CREATE_ENTRIES =
        "CREATE TABLE ${FeedEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${FeedEntry.COLUMN_NAME_WORD} TEXT," +
                "${FeedEntry.COLUMN_NAME_TRANSLATION} TEXT," +
                "${FeedEntry.COLUMN_NAME_WORD_TYPE} TEXT)"

    private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${FeedEntry.TABLE_NAME}"

    class FeedReaderDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(SQL_CREATE_ENTRIES)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES)
            onCreate(db)
        }

        override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            onUpgrade(db, oldVersion, newVersion)
        }

        companion object {
            // If you change the database schema, you must increment the database version.
            const val DATABASE_VERSION = 1
            const val DATABASE_NAME = "FeedReader.db"
        }
    }

}


class MainActivity : AppCompatActivity() {
    private var button: Button? = null
    private var wordField: TextView? = null
    private var translationField: TextView? = null
    private var wordTypeField: TextView? = null
    private val dbHelper = FeedReaderContract.FeedReaderDbHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.button)
        wordField = findViewById(R.id.word)
        translationField = findViewById(R.id.translation)
        wordTypeField = findViewById(R.id.wordType)
    }


    fun getFromDb(searchWord: String): MutableList<Map<String, String>> {
        val db = dbHelper.readableDatabase

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        val projection = arrayOf(
            BaseColumns._ID,
            FeedReaderContract.FeedEntry.COLUMN_NAME_WORD,
            FeedReaderContract.FeedEntry.COLUMN_NAME_TRANSLATION,
            FeedReaderContract.FeedEntry.COLUMN_NAME_WORD_TYPE
        )

        // Filter results WHERE "title" = 'My Title'
        val selection = "${FeedReaderContract.FeedEntry.COLUMN_NAME_WORD} = ?"
        val selectionArgs = arrayOf(searchWord)

        // How you want the results sorted in the resulting Cursor
        val sortOrder = "${FeedReaderContract.FeedEntry.COLUMN_NAME_TRANSLATION} DESC"

        val cursor = db.query(
            FeedReaderContract.FeedEntry.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            sortOrder               // The sort order
        )
        val itemIds = mutableListOf<Map<String, String>>()
        with(cursor) {
            while (moveToNext()) {
                val word = getString(getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_WORD))
                val translation =
                    getString(getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TRANSLATION))
                val wordType =
                    getString(getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_WORD_TYPE))
                val dict = mapOf<String, String>(
                    "searchWord" to word.toString(),
                    "translation" to translation.toString(),
                    "wordType" to wordType.toString()
                )
                itemIds.add(dict)
            }
        }
        return itemIds
    }

    fun putToDb(word: String, translation: String, wordType: String) {

        // Gets the data repository in write mode
        val db = dbHelper.writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues().apply {
            put(FeedReaderContract.FeedEntry.COLUMN_NAME_WORD, word)
            put(FeedReaderContract.FeedEntry.COLUMN_NAME_TRANSLATION, translation)
            put(FeedReaderContract.FeedEntry.COLUMN_NAME_WORD_TYPE, wordType)
        }

        // Insert the new row, returning the primary key value of the new row
        val newRowId = db?.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values)
    }

    fun findWord(view: View) {
        val word: String = wordField?.text.toString()
        val idList: MutableList<Map<String, String>> = getFromDb(word)
        if (idList.size == 0) {
            translationField?.inputType = InputType.TYPE_CLASS_TEXT
            translationField?.setTextIsSelectable(true)

            wordTypeField?.inputType = InputType.TYPE_CLASS_TEXT
            wordTypeField?.setTextIsSelectable(true)

            button?.setText(R.string.add)
            button?.setOnClickListener { v -> addWord(v) }
        } else {
            translationField?.text = idList[0]["translation"]
            wordTypeField?.text = idList[0]["wordType"]


        }
    }

    fun addWord(view: View) {
        val word: String = wordField?.text.toString()
        val translation: String = translationField?.text.toString()
        val wordType: String = wordTypeField?.text.toString()
        putToDb(word, translation, wordType)

        translationField?.inputType = InputType.TYPE_NULL
        translationField?.setTextIsSelectable(false)

        wordTypeField?.inputType = InputType.TYPE_NULL
        wordTypeField?.setTextIsSelectable(false)

        button?.setText(R.string.find)
        button?.setOnClickListener { v -> findWord(v) }

    }
}



