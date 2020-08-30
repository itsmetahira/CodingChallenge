package com.axxess.cavista

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class MyDBHandler(context: Context, name: String?,
                  factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    companion object {

        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "CodingChallengeDB.db"
        val TABLE_COMMENTS = "Comments"

        val COLUMN_ID = "image_id"
        val COLUMN_COMMENT = "comment"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_COMMENTS_TABLE = ("CREATE TABLE " +
                TABLE_COMMENTS + "("
                + COLUMN_ID + " TEXT, " +
                COLUMN_COMMENT
                + " TEXT" + ")")
        db.execSQL(CREATE_COMMENTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int,
                           newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS)
        onCreate(db)
    }

    fun addComment(id: String, comment: String) {

        val values = ContentValues()
        values.put(COLUMN_ID, id)
        values.put(COLUMN_COMMENT, comment)

        val db = this.writableDatabase

        db.insert(TABLE_COMMENTS, null, values)
        db.close()
    }

    fun loadImageComments(imageId: String): ArrayList<String> {
        val query =
                "SELECT * FROM $TABLE_COMMENTS WHERE $COLUMN_ID=  \"$imageId\""

        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)

        var comments : ArrayList<String> = ArrayList()

        if (cursor != null) {
            Log.i("Cursor count: ", cursor.count.toString())
            if(cursor.count > 0) {
                cursor.moveToFirst()
                for (x in 0 until cursor.count) {
                    val comment = cursor.getString(1)
                    comments.add(comment)
                    cursor.moveToNext()
                }
                Log.i("Comments count: ", cursor.count.toString())
            }
        }

        db.close()
        return comments
    }
}