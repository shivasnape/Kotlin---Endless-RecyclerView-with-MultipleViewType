package com.shivichu.recyclerviewwithviewtype.model

class Model {

    var type = 0
    var data = 0
    var text: String? = null

    constructor() {}
    constructor(type: Int, text: String?, data: Int) {
        this.type = type
        this.data = data
        this.text = text
    }

    companion object {
        const val TYPE_LOADING = 0
        const val TYPE_1 = 1
        const val TYPE_2 = 2
        const val TYPE_3 = 3
    }
}