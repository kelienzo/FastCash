package com.kelly.fastcash.utils

import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterDecimalStyle

actual val String.handleAmountFormat: String
    get() {
        if (this.isEmpty()) return "0.00"
        val numberFormat = NSNumberFormatter().apply {
            numberStyle = NSNumberFormatterDecimalStyle
            usesGroupingSeparator = true   // 👈 enables commas
            maximumFractionDigits = 0u      // 👈 removes decimals
        }
        return if (this.contains(".")) {
            val (numberBeforeDecimal, numberAfterDecimal) = this.split(".")
            "${numberFormat.stringFromNumber(NSNumber(long = numberBeforeDecimal.toLong()))}.${numberAfterDecimal}"
        } else numberFormat.stringFromNumber(NSNumber(long = this.toLong())) ?: "0"
    }