package com.kelly.fastcash.presentation

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.kelly.fastcash.utils.handleAmountFormat

object TextFieldAmountFormat : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            text = AnnotatedString(
                text = if (text.isEmpty()) text.text else text.text.handleAmountFormat
            ),
            offsetMapping = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    return if (text.isEmpty()) offset else text.text.handleAmountFormat.length
                }

                override fun transformedToOriginal(offset: Int): Int {
                    return if (text.isEmpty()) offset else text.length
                }
            }
        )
    }
}