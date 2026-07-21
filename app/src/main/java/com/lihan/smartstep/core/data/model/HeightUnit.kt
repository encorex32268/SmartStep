package com.lihan.smartstep.core.data.model

enum class HeightUnit {
    Cm, FtIn;

    companion object {

        val HeightUnit.formattedName: String
            get() = when(this){
                Cm -> "cm"
                FtIn -> "ft/in"
            }

        val lowercaseUnits: List<String>
            get() = HeightUnit.entries.map { it.formattedName.lowercase() }

        fun fromName(name: String?): HeightUnit{
            return HeightUnit.entries
                .find { it.formattedName == name }?: HeightUnit.Cm
        }

    }

    fun fromCm(cm: Double): Double = when (this) {
        Cm -> cm
        FtIn -> cm / 2.54
    }

}