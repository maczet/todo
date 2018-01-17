package com.example.macie.todolist

/**
 * Created by macie on 13.01.2018.
 */

class ToDoList{

    var id : Int = 0
    var toDo : String = ""
    var done : Int = 0
    var priority: String = "Normal"

    constructor(toDo: String, done: Int, priority: String){
        this.toDo = toDo
        this.done = done
        this.priority = priority
    }

    constructor()
}