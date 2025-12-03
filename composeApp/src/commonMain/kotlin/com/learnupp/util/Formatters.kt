package com.learnupp.util

import kotlin.math.abs
import kotlin.math.roundToInt

fun Double.formatPrice(
    includeSymbol: Boolean = true,
    showSign: Boolean = false,
    currencySymbol: String = "$"
): String {
    val rounded = (this * 100).roundToInt() / 100.0
    val absValue = abs(rounded)
    val scaled = (absValue * 100).roundToInt()
    val integerPart = scaled / 100
    val decimalPart = scaled % 100
    val decimalText = when {
        decimalPart == 0 -> ""
        decimalPart % 10 == 0 -> ".${decimalPart / 10}"
        else -> ".${decimalPart.toString().padStart(2, '0')}"
    }
    val numberPart = integerPart.toString() + decimalText
    val sign = when {
        rounded < 0 -> "-"
        showSign -> "+"
        else -> ""
    }
    val symbol = if (includeSymbol) currencySymbol else ""
    return "$sign$symbol$numberPart"
}

fun Number.formatPrice(
    includeSymbol: Boolean = true,
    showSign: Boolean = false,
    currencySymbol: String = "$"
): String = toDouble().formatPrice(includeSymbol, showSign, currencySymbol)

