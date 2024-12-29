package com.example.samojlov_av_homework_module_16_number_8_1_koala.models

data class Contact(val name: String? = null, val phone: String? = null) {
    override fun toString(): String {
        return "$name, $phone"
    }
}