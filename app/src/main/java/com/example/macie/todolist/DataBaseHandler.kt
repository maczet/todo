package com.example.macie.todolist


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.renderscript.Sampler
import android.text.TextUtils
import android.util.Log
import java.text.SimpleDateFormat
import java.util.concurrent.CountedCompleter

/**
 * Created by macie on 13.01.2018.
 */

val dbName = "ToDoListDB"
val dbTable = "ToDO"
val dbTableAttachments = "ToDOAtt"
val colId = "id"
val colTaskID = "taskid"
val colToDo = "toDo"
val colDone = "done"
val colPriority = "priority"
val colDateAdded = "dateadded"
val colDateDone = "datedone"
val colFilePath = "filepath"


class DataBaseHandler(var context: Context) : SQLiteOpenHelper(context, dbName, null,1) {
    var success: Boolean = false


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) { }

    override fun onCreate(db: SQLiteDatabase?) {

        val createTable = "CREATE TABLE IF NOT EXISTS " + dbTable + " (" +
                colId + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                colToDo + " TEXT," +
                colPriority + " TEXT," +
                colDateAdded + " INTEGER," +
                colDateDone + " INTEGER," +
                colDone + " BOOLEAN)"

        db?.execSQL(createTable)

        // attachments table
        db?.execSQL("CREATE TABLE IF NOT EXISTS " + dbTableAttachments + " (" +
                colId + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                colTaskID + " INTEGER REFERENCES " + dbTable + " ( " + colId + " )," +
                colDateAdded + " Integer," +
                colFilePath + " TEXT)")
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

    fun readDataDone(): MutableList<ToDoList>{
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

                var done:Boolean

                toDoList.toDo = cursor.getString(cursor.getColumnIndex(colToDo))
                toDoList.done = cursor.getInt(cursor.getColumnIndex(colDone))
                toDoList.priority = cursor.getString(cursor.getColumnIndex(colPriority))
                toDoList.id = cursor.getInt(cursor.getColumnIndex(colId))

                if (toDoList.done == 0) {
                    list.add(toDoList)
                }
            }while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return list
    }

    fun readDataNotDone(): MutableList<ToDoList>{
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

                var done:Boolean

                toDoList.toDo = cursor.getString(cursor.getColumnIndex(colToDo))
                toDoList.done = cursor.getInt(cursor.getColumnIndex(colDone))
                toDoList.priority = cursor.getString(cursor.getColumnIndex(colPriority))
                toDoList.id = cursor.getInt(cursor.getColumnIndex(colId))

                if (toDoList.done == 1) {
                    list.add(toDoList)
                }
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

    fun updatePriority(id: Int, priority: String){

        val values = ContentValues()
        values.put(colPriority, priority)
        val db = this.writableDatabase

        val retVal =  db.update(dbTable, values, colId + " = " + id, null)

        if (retVal >= 1)
            Log.v("Update done", " Record updated")
        else
            Toast.makeText(context, "Failed update todo", Toast.LENGTH_SHORT).show()

        db.close()
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

    // Attachments db methods
    fun insertAttachment(filepath: String, taskID: Int) {
        val db = this.writableDatabase
        var cv = ContentValues()

        cv.put(colTaskID, taskID)
        cv.put(colDateAdded, System.currentTimeMillis())
        cv.put(colFilePath, filepath)

        var result = db.insert(dbTableAttachments, null, cv)
        if (result == -1.toLong())
            Toast.makeText(context, "Failed add new attachment", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context, "Success add new attachment", Toast.LENGTH_SHORT).show()
        db.close()
        success = true;
    }

    fun readDataAttachments(taskID: Int): MutableList<Attachments>{
        var list : MutableList<Attachments> = ArrayList()
        var cursor: Cursor? = null
        val db = this.readableDatabase
        val query = "Select * from " + dbTableAttachments +
                " where " + colTaskID + " = " + Integer.toString(taskID) + " order by " + colId

        try {
            cursor = db.rawQuery(query, null)
        } catch (e: SQLiteException) {
            onCreate(db)
            return ArrayList()
        }

        if (cursor.moveToFirst()){
            do {
                var toDoList = Attachments(
                        cursor.getInt(cursor.getColumnIndex(colId)),
                        cursor.getInt(cursor.getColumnIndex(colTaskID)),
                                cursor.getInt(cursor.getColumnIndex(colDateAdded)),
                                cursor.getString(cursor.getColumnIndex(colFilePath))
                )
                list.add(toDoList)

            }while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return list
    }

    fun deleteAttachments(list: MutableList<Int>): Int {
        val db = this.readableDatabase

        val retVal = db.delete(dbTableAttachments, colId + " in (" +
                android.text.TextUtils.join(",", list) + ")", null)

        db.close()
        return retVal
    }

}
