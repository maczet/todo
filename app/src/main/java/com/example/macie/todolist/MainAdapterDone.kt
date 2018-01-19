package com.example.macie.todolist

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.main_row.view.*

/**
 * Created by macie on 14.01.2018.
 */

class MainAdapterDone(context: Context): RecyclerView.Adapter<CustomViewHolderDone>() {

    private val mContext: Context

    private fun Boolean.toInt() = if (this) 1 else 0
    private fun toBoolean(int: Int): Boolean {
        return int == 1
    }

    var db = DataBaseHandler(context)
    var data = db.readDataNotDone()

    init {
        mContext = context
    }
    var number = null


    override fun getItemCount(): Int {
        return data.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CustomViewHolderDone {
        // how do we even create a view
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.main_row, parent, false)
        return CustomViewHolderDone(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolderDone?, position: Int) {
        val numberString = (position + 1).toString() + "."
        holder?.view?.textViewNumber?.text = numberString
        holder?.view?.textViewToDo?.text = data[position].toDo
        holder?.view?.checkBoxDone?.isChecked = toBoolean(data[position].done)
        holder?.todo = data[position]
        holder?.view?.textViewPriority?.text = data[position].priority

        holder?.view?.checkBoxDone?.setOnClickListener {
            db.updateDone(data[position].id, holder?.view?.checkBoxDone?.isChecked!!.toInt())

        }
    }
}

class CustomViewHolderDone(val view: View, var todo: ToDoList? = null): RecyclerView.ViewHolder
(view) {

    companion object {
        val TODO_ID = "ID"
    }

    init {
        view.setOnClickListener {
            val intent = Intent(view.context, ToDoDetail::class.java)

            intent.putExtra(TODO_ID, todo?.id)
            view.context.startActivity(intent)

        }
    }
}

