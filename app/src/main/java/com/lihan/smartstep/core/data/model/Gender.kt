package com.lihan.smartstep.core.data.model

enum class Gender {
    Male,Female;
    companion object{
        fun fromName(name: String?): Gender {
            return Gender.entries.find { it.name == name }?: Gender.Female
        }
    }
}