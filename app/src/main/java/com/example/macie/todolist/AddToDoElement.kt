package com.example.macie.todolist

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_to_do_element.*


class AddToDoElement : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_to_do_element)

        val context = this

        buttonAdd.setOnClickListener {
            var success: Boolean = false
            if (newToDO.text.toString().length > 0){
                var toDoList = ToDoList(newToDO.text.toString(), done = false)
                var db = DataBaseHandler(context)
                db.insertData(toDoList)
                success = db.success
            }
            else{
                Toast.makeText(context, "Please Fill Data", Toast.LENGTH_SHORT).show()
            }

            if (success){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
