package com.lihan.smartstep.profile_setup.presentation

import com.lihan.smartstep.core.data.model.Gender
import com.lihan.smartstep.core.data.model.HeightUnit
import com.lihan.smartstep.core.data.model.HeightUnit.Companion.formattedName
import com.lihan.smartstep.core.data.model.WeightUnit
import com.lihan.smartstep.core.presentation.components.WheelPickerData
import kotlin.math.roundToInt

data class ProfileSetupState(
    val gender: Gender = Gender.Female,
    val isShowGenderDropdownMenu: Boolean = false,
    val isShowHeightDialog: Boolean = false,
    val isShowWeightDialog: Boolean = false,
    val heightUnitOption: HeightUnit = HeightUnit.Cm,
    val heightValue: Double = 175.0,
    val heightItems: List<WheelPickerData> = emptyList(),
    val weightUnitOption: WeightUnit = WeightUnit.Kg,
    val weightValue: Double = 65.0,
    val weightItems: List<WheelPickerData> = emptyList()

){
    val displayHeight: Double
        get() = heightUnitOption.fromCm(heightValue)

    val displayHeightString: String
        get() = when(heightUnitOption){
            HeightUnit.Cm ->  displayHeight.roundToInt().toString()
            HeightUnit.FtIn -> {
                val roundToIntHeight = displayHeight.roundToInt()
                val feet = roundToIntHeight / 12
                val inches = roundToIntHeight % 12
                "$feet'$inches\""
            }
        }

    val displayWeight: Double
        get() = weightUnitOption.fromKg(weightValue)

    val displayWeightString: String
        get() = displayWeight.roundToInt().toString()


}




