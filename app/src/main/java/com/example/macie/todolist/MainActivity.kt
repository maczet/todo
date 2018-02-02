package com.example.macie.todolist

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import kotlinx.android.synthetic.main.activity_main.*
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.widget.Toast
import android.widget.ArrayAdapter




class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView_main.layoutManager = LinearLayoutManager(this)
        recyclerView_main.adapter = MainAdapter(this)

        recyclerView_done.layoutManager = LinearLayoutManager(this)
        recyclerView_done.adapter = MainAdapterDone(this)

        fab.setOnClickListener {
            val intent = Intent(this, AddToDoElement::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    private fun sort(option: CharSequence, viewDone: Boolean) {
        val adapter = if (viewDone) recyclerView_main.adapter as MainAdapter else recyclerView_main.adapter as MainAdapter
        when (option) {
            "Name asc" ->  adapter.sortByName()
            "Name desc" ->  adapter.sortByName(true)
//            "Date added asc" ->  adapter.sortByDateAdded()
//            "Date added desc" ->  adapter.sortByDateAdded(true)
//            "Date done asc" ->  adapter.sortByDateDone()
//            "Date done desc" ->  adapter.sortByDateDone(true)
            "Priority asc" ->  adapter.sortByPriority()
            "Priority desc" ->  adapter.sortByPriority(true)
        }
        adapter.notifyDataSetChanged()
    }

    // mbak: task sorting
    fun sort(option: CharSequence): Boolean {
        sort(option, false)
        sort(option, true)
        return true
    }

    // mbak: displaying sort options to the user
    fun openSortOptions(): Boolean {

        val options = arrayOf<CharSequence>("Name asc", "Name desc"/*, "Date added asc", "Date added desc",
                "Date done asc", "Date done desc"*/, "Priority asc", "Priority desc")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Sort by")
        builder.setItems(options, DialogInterface.OnClickListener { dialog, which ->
            sort(options[which])
        })
        builder.show()

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_sort -> openSortOptions()
            else -> super.onOptionsItemSelected(item)
        }
    }



}
