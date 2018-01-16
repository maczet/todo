package com.example.macie.todolist

import android.app.Activity
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowId
import kotlinx.android.synthetic.main.activity_to_do_element.*
import kotlinx.android.synthetic.main.main_row.*

class ToDoDetail : AppCompatActivity() {

    val DEFAULT_INT = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do_element)

        val ToDoId = intent.getIntExtra(CustomViewHolder.TODO_ID, DEFAULT_INT)
        supportActionBar?.title = "TODO details nr: " + ToDoId


        val db = DataBaseHandler(this)
        val data = db.readOneObjectToDO(ToDoId)

        checkBoxToDoDetail.isChecked = data.done
        textViewTitleToDoDetail.text = data.toDo


        checkBoxToDoDetail.setOnClickListener {
            var check = checkBoxToDoDetail.isChecked
            db.updateDone(ToDoId, checkBoxToDoDetail.isChecked)

            updateContext(this, ToDoId)
        }
    }

    private fun updateContext(context: Context, ToDoId: Int){
        val db = DataBaseHandler(this)
        val data = db.readOneObjectToDO(ToDoId)
        checkBoxToDoDetail.isChecked = data.done
    }
}
