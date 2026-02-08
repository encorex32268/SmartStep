package com.lihan.smartstep.onboarding.presentation.model

import androidx.annotation.IdRes
import com.lihan.smartstep.R

enum class Gender(
    @param:IdRes val id: Int
) {
    FEMALE(R.string.female),
    MALE(R.string.male);

}