package com.lihan.smartstep.core.presentation.components.model

import com.lihan.smartstep.R


sealed interface UnitType{
    data object Kg: UnitType
    data object Lbs: UnitType
    data object Cm: UnitType
    data object FtIn: UnitType

    fun toKey(): String = when(this) {
        Kg -> "KG"
        Lbs -> "LBS"
        Cm -> "CM"
        FtIn -> "FtIn"
    }

    companion object{
        fun fromKey(key: String?): UnitType = when(key) {
            "KG" -> Kg
            "LBS" -> Lbs
            "CM" -> Cm
            "FtIn" -> FtIn
            else -> Kg
        }

        fun UnitType.toStringResId(): Int{
            return when(this){
                Cm -> R.string.cm
                FtIn -> R.string.ft_in
                Kg -> R.string.kg
                Lbs -> R.string.lbs
            }
        }

        val UnitType.isNeedTwoColumn
            get() = this == UnitType.FtIn
    }
}


