package com.lihan.smartstep.onboarding.presentation

import androidx.compose.runtime.retain.retain
import com.lihan.smartstep.core.presentation.components.model.UnitType
import com.lihan.smartstep.onboarding.presentation.model.Gender
import kotlin.math.roundToInt
import kotlin.text.toFloat


data class ProfileState(
    val gender: Gender = Gender.FEMALE,
    val isExpandGender: Boolean = false,
    val height: Int = 170,
    val heightModalValue: Int = 170,
    val selectHeightUnitType: UnitType = UnitType.Cm,
    val isShowHeightModal: Boolean = false,
    val weight: Int = 60,
    val weightModalValue: Int = 60,
    val selectWeightUnitType: UnitType = UnitType.Kg,
    val isShowWeightModal: Boolean = false
){

    val heightItems: List<String> = (120..230).map { it.toString() }
    val heightFtItems: List<String> = (0..9).map{ "$it" }
    val heightInchItems: List<String> = (0..9).map{ "$it" }

    val heightFtIn: Pair<Int,Int>
        get(){
            val totalInches = height / 2.54
            val heightFt = (totalInches / 12).toInt()
            val heightIn = (totalInches % 12).roundToInt()

            return Pair(heightFt,heightIn)
        }

    val heightModalValueFtIn: Pair<Int,Int>
        get(){
            val totalInches = heightModalValue / 2.54
            val heightFt = (totalInches / 12).toInt()
            val heightIn = (totalInches % 12).roundToInt()

            return Pair(heightFt,heightIn)
        }



    //lbs = kg × 2.20462
    //kg = lbs ÷ 2.20462
    val weightItems: List<String>
        get() = if (selectWeightUnitType == UnitType.Kg){
            (45..220).map { it.toString() }
        }else{
            (45..220).map { (it * 2.20462).roundToInt().toString()}
        }

    val weightModalValueLbs: Int
        get() = (weightModalValue * 2.20462).roundToInt()

    val weightLbs: Int
        get() = (weight * 2.20462).roundToInt()
}
