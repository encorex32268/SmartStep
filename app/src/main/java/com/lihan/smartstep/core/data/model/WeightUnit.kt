package com.lihan.smartstep.core.data.model

enum class WeightUnit {
    Kg, Lbs;

    companion object {

        val WeightUnit.formattedName: String
            get() = when(this){
                WeightUnit.Kg -> "kg"
                WeightUnit.Lbs -> "lbs"
            }

        val lowercaseUnits: List<String>
            get() = WeightUnit.entries.map { it.formattedName.lowercase() }

        fun fromName(name: String?): WeightUnit{
            return WeightUnit.entries
                .find { it.formattedName == name }?: Kg
        }


    }
    fun fromKg(kg: Double): Double = when (this) {
        Kg -> kg
        Lbs -> kg * 2.20462
    }

    fun toKg(value: Double): Double = when (this) {
        Kg -> value
        Lbs -> value / 2.20462
    }


}