package com.example.macie.todolist

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_edit_to_do_element.*

class EditToDoElement : AppCompatActivity() {

    val DEFAULT_INT = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_to_do_element)

        val ToDoId = intent.getIntExtra(CustomViewHolder.TODO_ID, DEFAULT_INT)
        supportActionBar?.title = "TODO edit nr: " + ToDoId

        val db = DataBaseHandler(this)
        val data = db.readOneObjectToDO(ToDoId)

        editTextEdit.setText(data.toDo)

        buttonEditOk.setOnClickListener {
            db.updateToDo(ToDoId, editTextEdit.text.toString())
            val intent = Intent(this, ToDoDetail::class.java)
            intent.putExtra("ID", ToDoId)
            startActivity(intent)
        }
    }
}
