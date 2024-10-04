package com.problemsolver.booksapp.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BookDetail(
    val title: String?,
    val description: Description?,
    val subjects: List<String>?
) {

    @JsonClass(generateAdapter = true)
    data class Description(
        val value: String?,
    )

    fun getAllContent(): String {
        return "$title\n\n${description?.value}\n\n${subjects?.joinToString(", ")}"
    }
}
