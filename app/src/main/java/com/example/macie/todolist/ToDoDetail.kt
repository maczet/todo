package com.example.macie.todolist

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter

import kotlinx.android.synthetic.main.activity_to_do_element.*

class ToDoDetail : AppCompatActivity() {

    val DEFAULT_INT = -1

    private fun Boolean.toInt() = if (this) 1 else 0
    private fun toBoolean(int: Int): Boolean {
        return int == 1
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do_element)

        val ToDoId = intent.getIntExtra(CustomViewHolder.TODO_ID, DEFAULT_INT)
        supportActionBar?.title = "TODO detail nr: " + ToDoId


        val db = DataBaseHandler(this)
        val data = db.readOneObjectToDO(ToDoId)

        checkBoxToDoDetail.isChecked = toBoolean(data.done)
        textViewTitleToDoDetail.text = data.toDo

        var priority = checkPriority(data.priority)

        spinnerPriorityDetail.adapter = ArrayAdapter(this,
                R.layout.support_simple_spinner_dropdown_item, priority)
        spinnerPriorityDetail.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                db.updatePriority(ToDoId, spinnerPriorityDetail.selectedItem.toString())
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        checkBoxToDoDetail.setOnClickListener {
            db.updateDone(ToDoId, checkBoxToDoDetail.isChecked.toInt())
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

    private fun updatePriority(ToDoId: Int){
        val db = DataBaseHandler(this)


    }

    private fun checkPriority(priority: String): Array<out String>? {
        val normal = resources.getStringArray(R.array.priority_array)
        val high = resources.getStringArray(R.array.priority_high)
        val low = resources.getStringArray(R.array.priority_low)

        return when (priority) {
            normal[0] -> normal
            high[0] -> high
            else -> low
        }
    }

}
