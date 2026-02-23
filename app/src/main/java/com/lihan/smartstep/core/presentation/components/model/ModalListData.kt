package com.lihan.smartstep.core.presentation.components.model

data class ModalListData(
    val title: String,
    val description: String,
    val value: String,
    val firstUnitType: UnitType,
    val secondUnitType: UnitType,
    val firstUnitTypeItems: List<String>,
    val secondUnitTypeItems: List<String>
)