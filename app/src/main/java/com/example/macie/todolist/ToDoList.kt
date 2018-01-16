package com.example.macie.todolist

/**
 * Created by macie on 13.01.2018.
 */

class ToDoList{

    var id : Int = 0
    var toDo : String = ""
    var done : Boolean = false

    constructor(toDo: String, done: Boolean){
//        this.id = id
        this.toDo = toDo
        this.done = done
    }

    constructor()
}