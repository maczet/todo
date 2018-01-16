package com.example.macie.todolist

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_to_do_element.*

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
            db.updateDone(ToDoId, checkBoxToDoDetail.isChecked)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        buttonDelete.setOnClickListener {
            val db = DataBaseHandler(this)
            db.deleteToDO(ToDoId)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        buttonEdit.setOnClickListener {
            val intent = Intent(this, EditToDoElement::class.java)
            intent.putExtra("ID", ToDoId)
            startActivity(intent)
        }
    }

    private fun updateContext(context: Context, ToDoId: Int){
        val db = DataBaseHandler(this)
        val data = db.readOneObjectToDO(ToDoId)
        checkBoxToDoDetail.isChecked = data.done
    }

}
