package com.example.macie.todolist

/**
 * Created by realy on 31.01.2018.
 */


class Attachments{

    var id : Int = 0
    var taskID : Int = 0
    var dateAdded : Int = 0
    var filePath: String = ""

    constructor(id: Int, taskID: Int, dateAdded: Int, filePath: String){
           this.id = id
           this.taskID = taskID
           this.dateAdded = dateAdded
           this.filePath = filePath
        }

}