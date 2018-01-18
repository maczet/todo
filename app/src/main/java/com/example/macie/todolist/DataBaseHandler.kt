package com.example.macie.todolist


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.renderscript.Sampler
import android.util.Log
import java.util.concurrent.CountedCompleter

/**
 * Created by macie on 13.01.2018.
 */

val dbName = "ToDoListDB"
val dbTable = "ToDO"
val colId = "id"
val colToDo = "toDo"
val colDone = "done"
val colPriority = "priority"


class DataBaseHandler(var context: Context) : SQLiteOpenHelper(context, dbName, null,1) {
    var success: Boolean = false


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) { }

    override fun onCreate(db: SQLiteDatabase?) {

        val createTable = "CREATE TABLE IF NOT EXISTS " + dbTable + " (" +
                colId + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                colToDo + " TEXT," +
                colPriority + " TEXT," +
                colDone + " BOOLEAN)"

        db?.execSQL(createTable)
    }

    fun insertData(toDoList: ToDoList) {
        val db = this.writableDatabase
        var cv = ContentValues()

        cv.put(colToDo, toDoList.toDo)
        cv.put(colPriority, toDoList.priority)
        cv.put(colDone, toDoList.done)
        var result = db.insert(dbTable, null, cv)
        if (result == -1.toLong())
            Toast.makeText(context, "Failed add new todo", Toast.LENGTH_SHORT).show()

        else
            Toast.makeText(context, "Success add new todo", Toast.LENGTH_SHORT).show()

        success = true
    }

    fun readData(): MutableList<ToDoList>{
        var list : MutableList<ToDoList> = ArrayList()
        var cursor: Cursor? = null
        val db = this.readableDatabase
        val query = "Select * from " + dbTable

        try {
            cursor = db.rawQuery(query, null)
        } catch (e: SQLiteException) {
            onCreate(db)
            return ArrayList()
        }

        if (cursor.moveToFirst()){
            do {
                var toDoList = ToDoList()

                toDoList.toDo = cursor.getString(cursor.getColumnIndex(colToDo))
                toDoList.done = cursor.getInt(cursor.getColumnIndex(colDone))
                toDoList.priority = cursor.getString(cursor.getColumnIndex(colPriority))
                toDoList.id = cursor.getInt(cursor.getColumnIndex(colId))
                list.add(toDoList)
            }while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return list
    }

    fun readOneObjectToDO(id: Int): ToDoList{
        var cursor: Cursor? = null
        val db = this.readableDatabase
        val query = "Select * from " + dbTable + " where " + colId + " = " + id
        cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        var toDoList = ToDoList()

        toDoList.toDo = cursor.getString(cursor.getColumnIndex(colToDo))
        toDoList.done = cursor.getInt(cursor.getColumnIndex(colDone))
        toDoList.priority = cursor.getString(cursor.getColumnIndex(colPriority))
        toDoList.id = cursor.getInt(cursor.getColumnIndex(colId))


        cursor.close()
        db.close()
        return toDoList
    }

    fun updateDone(id: Int, done: Int){

        val values = ContentValues()
        values.put(colDone, done)
        val db = this.writableDatabase

        val retVal =  db.update(dbTable, values, colId + " = " + id, null)

        if (retVal >= 1)
            Log.v("Update done", " Record updated")
        else
            Toast.makeText(context, "Failed update todo", Toast.LENGTH_SHORT).show()

        db.close()

    }

    fun updateToDo(id: Int, todo: String){

        val values = ContentValues()
        values.put(colToDo, todo)

        val db = this.writableDatabase
        val retVal =  db.update(dbTable, values, colId + " = " + id, null)

        if (retVal >= 1) {
            Toast.makeText(context, "Record was updated", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Record wasn't updated", Toast.LENGTH_SHORT).show()
        }
        db.close()
    }

    fun deleteToDO(id: Int){
        val db = this.writableDatabase
        val retVal = db.delete(dbTable, colId + " = " + id.toString(), null)
        if (retVal >= 1) {
            Toast.makeText(context, "Record deleted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Record not deleted", Toast.LENGTH_SHORT).show()
        }
        db.close()
    }

}
