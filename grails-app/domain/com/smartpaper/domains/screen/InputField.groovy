package com.smartpaper.domains.screen

class InputField {

    boolean required = false
    int order = 1
    String label = ""

    static belongsTo = [
            form:FormScreen
    ]

    static constraints = {
        label blank: true

    }

    static mapping = {
        cache true
        order column: '`orderNumber`'
        table 'sp_input_field'
    }
}
