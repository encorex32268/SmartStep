package com.lihan.smartstep.onboarding.presentation

import com.lihan.smartstep.onboarding.presentation.model.Gender
import com.lihan.smartstep.onboarding.presentation.model.HeightUnit
import com.lihan.smartstep.onboarding.presentation.model.WeightUnit

data class ProfileState(
    val gender: Gender = Gender.FEMALE,
    val isExpandGender: Boolean = false,
    val height: String = "170",
    val heightUnit: HeightUnit = HeightUnit.CM,
    val isShowHeightModal: Boolean = false,
    val weight: String = "60",
    val weightUnit: WeightUnit = WeightUnit.KG,
    val isShowWeightModal: Boolean = false
){

    val heightItems: List<String>
        get() = if (heightUnit == HeightUnit.CM){
            (120..230).map { it.toString() }
        }else{
            (120..230).map { it.toString() }
        }

    //lbs = kg × 2.20462
    //kg = lbs ÷ 2.20462
    val weightItems: List<String>
        get() = if (weightUnit == WeightUnit.KG){
            (45..220).map { it.toString() }
        }else{
            (45..220).map { (it * 2.20462).toString()}
        }
}
