package com.CoolPeppers.android.data.model

data class Symptom(
    val id: Int,
    val name: String,
    val description: String? = null
)

data class SymptomInput(
    val symptoms: List<String>
)

data class MatchResult(
    val services: String
) 