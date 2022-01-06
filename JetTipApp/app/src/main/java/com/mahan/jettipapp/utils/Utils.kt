package com.mahan.jettipapp.utils

fun calculateTotalTip(billAmount: Int, tipPercentage: Int): Float {
    if (billAmount <= 1) return 0.0f
    return (billAmount * tipPercentage / 100).toFloat()
}

fun calculateTotalPerPerson(
    billValue: String,
    tip: Float,
    splitBy: Int
): Float {
    if (billValue.isEmpty())
        return 0.0f
    return (billValue.toFloat() + tip).div(splitBy)
}