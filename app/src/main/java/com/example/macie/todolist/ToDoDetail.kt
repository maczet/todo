package com.example.macie.todolist

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View

import kotlinx.android.synthetic.main.activity_to_do_element.*
import android.graphics.Bitmap
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_edit_to_do_element.*
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat

class ToDoDetail : AppCompatActivity() {

    val DEFAULT_INT = -1
    private val CAMERA_REQUEST = 1888

    private var permissionOk = false

    private fun Boolean.toInt() = if (this) 1 else 0
    private fun toBoolean(int: Int): Boolean {
        return int == 1
    }

    private var dataAtt = mutableListOf<Attachments>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do_element)

        val ToDoId = intent.getIntExtra(CustomViewHolder.TODO_ID, DEFAULT_INT)
        supportActionBar?.title = "TODO detail nr: " + ToDoId

        val db = DataBaseHandler(this)
        val data = db.readOneObjectToDO(ToDoId)
        dataAtt = db.readDataAttachments(ToDoId)

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

        attachmentAdd.setOnClickListener {
            requestPermission()
            val options = arrayOf<CharSequence>("Camera", "File")
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Add attachment")
            builder.setItems(options, DialogInterface.OnClickListener { dialog, which ->
                saveAttachment(options[which])
            })
            builder.show()
        }

        attachmentRemove.setOnClickListener {
            val simpleAlert = AlertDialog.Builder(this).create()
            simpleAlert.setTitle("Choose action")
            simpleAlert.setMessage("Delete selected attachments?")

            simpleAlert.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", {
                dialogInterface, i ->
                val delList = mutableListOf<Int>()
                for (i in 0 until grid.getChildCount()) {
                    val v = grid.getChildAt(i)
                    if ((v is CheckBox) && (v as CheckBox).isChecked) {
                        delList.add(v.tag as Int)
                    }
                }
                if (db.deleteAttachments(delList) >= 1) {
                    Toast.makeText(this, "Attachments deleted!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error occured!", Toast.LENGTH_SHORT).show()
                }
                val ToDoId = intent.getIntExtra(CustomViewHolder.TODO_ID, DEFAULT_INT)
                val intent = Intent(this, ToDoDetail::class.java)
                intent.putExtra(CustomViewHolder.TODO_ID, ToDoId)
                startActivity(intent)
            })
            simpleAlert.setButton(AlertDialog.BUTTON_NEGATIVE, "No", {
                dialogInterface, i ->
//                val intent = Intent(this, EditToDoElement::class.java)
//                intent.putExtra("ID", ToDoId)
//                startActivity(intent)
            })
            simpleAlert.show()
        }

        listAttachments()

    }

    private fun listAttachments() {
        for (value in dataAtt) {
            val check = CheckBox(this)
            check.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            check.text = ""
            check.tag = value.id
            grid.addView(check)

            val text = TextView(this)

            text.text = value.filePath.substring(
                    if (value.filePath.lastIndexOf("/") > 0)
                        value.filePath.lastIndexOf("/") + 1 else 0)
            grid.addView(text)

            val stat = TextView(this)

            stat.text = if (value.filePath.lastIndexOf("CAMERA") > -1) "Photo" else "File"
            grid.addView(stat)
        }
    }

    private fun saveAttachment(option: CharSequence) {
        when (option) {
            "Camera" -> openCamera()
            "File" ->  true
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this,
                  arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 101)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            101 -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissionOk = true
            } else {
                permissionOk = false
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            if (!permissionOk) {
                Toast.makeText(this, "Need persmission to proceed", Toast.LENGTH_LONG).show()
            } else {
                val photo = data.extras!!.get("data") as Bitmap
                val imageFileName = Environment.getExternalStorageDirectory().toString() + "/" +
                         "CAMERA_" + SimpleDateFormat("yyyyMMdd_HHmmss")
                        .format(System.currentTimeMillis()) + "_" + "TODO_ATTACHMENT"
                var out: FileOutputStream? = null
                try {
                    out = FileOutputStream( imageFileName)
                    photo.compress(Bitmap.CompressFormat.PNG, 100, out)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    try {
                        if (out != null) {
                            out!!.close()
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    // add image file path to database
                    val ToDoId = intent.getIntExtra(CustomViewHolder.TODO_ID, DEFAULT_INT)
                    val db = DataBaseHandler(this)
                    db.insertAttachment(imageFileName, ToDoId)
                    val intent = Intent(this, ToDoDetail::class.java)
                    intent.putExtra(CustomViewHolder.TODO_ID, ToDoId)
                    startActivity(intent)
                }
            }
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_REQUEST)
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
