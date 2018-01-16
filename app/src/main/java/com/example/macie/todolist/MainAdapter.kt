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

class MainAdapter(context: Context): RecyclerView.Adapter<CustomViewHolder>() {

    private val mContext: Context

    var db = DataBaseHandler(context)
    var data = db.readData()

    init {
        mContext = context
    }
    var number = null


    override fun getItemCount(): Int {
        return data.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CustomViewHolder {
        // how do we even create a view
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.main_row, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder?, position: Int) {
        val numberString = (position + 1).toString() + "."
        holder?.view?.textViewNumber?.text = numberString
        holder?.view?.textViewToDo?.text = data.get(position).toDo

        holder?.todo = data.get(position)
    }
}

class CustomViewHolder(val view: View, var todo: ToDoList? = null): RecyclerView.ViewHolder(view) {

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

