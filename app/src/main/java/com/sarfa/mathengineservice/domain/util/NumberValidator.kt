package com.sarfa.mathengineservice.domain.util

object NumberValidator {
    fun validate(number: String): String? {
        return if (number.isEmpty()) {
            "Empty"
        } else if (number.toDoubleOrNull() == null) {
            "Not number"
        } else {
            null
        }
    }
}