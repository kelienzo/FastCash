package com.kelly.fastcash.utils

expect val String.handleAmountFormat: String

fun handleAmountInput(text: String, handle: (String) -> Unit) {
    if ((text.toDoubleOrNull() ?: 0.0) <= 1_000_000_000.00) {
        if (text.contains(".")) {
            if (text.substringAfter(".").length <= 2)
                handle(text)
            else return

        } else handle(text)
    }
}

//fun String.formatAmount(showNaira: Boolean = false): String {
//    // Remove commas and treat the input as kobo
//    val kobo = this.let {
//        if (it.isEmpty())
//            "0.0"
//        else if (it.contains(".").not())
//            it.plus(".00")
//        else if (it.contains(".")) {
//            if (it.substringAfter(".").length == 1)
//                it.plus("0")
//            else
//                String.format(Locale.getDefault(), "%,.2f", it.toDouble())
//        } else it
//    }.replace("[-,. ]".toRegex(), "").toLong()
//
//    // Convert cents to naira
//    val naira = kobo / 100.00
//
//    // Format the naira as a currency
//    val formattedAmount = String.format(Locale.getDefault(), "%,.2f", naira)
//    val negative = if (contains("-")) "-" else ""
//    return if (showNaira) "$negative₦$formattedAmount" else "$negative$formattedAmount"
//}