package com.example.macie.todolist

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_to_do_element.*


class AddToDoElement : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_to_do_element)

        var priority = "Normal"


        spinnerPriority.adapter =
                ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item,
                        resources.getStringArray(R.array.priority_array))
        spinnerPriority.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                priority = spinnerPriority.selectedItem.toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        val context = this

        buttonAdd.setOnClickListener {
            var success: Boolean = false
            if (newToDO.text.toString().length > 0){
                var toDoList = ToDoList(newToDO.text.toString(), done = 0, priority = priority)
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
