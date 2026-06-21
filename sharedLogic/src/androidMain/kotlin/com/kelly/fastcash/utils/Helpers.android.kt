package com.kelly.fastcash.utils

import java.text.NumberFormat

actual val String.handleAmountFormat: String
    get() {
        if (this.isEmpty()) return "0.00"
        val numberFormat = NumberFormat.getNumberInstance()
        return if (this.contains(".")) {
            val (numberBeforeDecimal, numberAfterDecimal) = this.split(".")
            "${numberFormat.format(numberBeforeDecimal.toLong())}.${numberAfterDecimal}"
        } else numberFormat.format(this.toLong())
    }