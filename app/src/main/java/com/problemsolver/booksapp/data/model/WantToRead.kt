package com.problemsolver.booksapp.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class WantToRead(
    @Json(name = "reading_log_entries")
    val readingLogEntries: List<Work>?
) {

    @JsonClass(generateAdapter = true)
    data class Work(

        @Json(name = "work")
        val work: WorkDetail?
    ) {

        @JsonClass(generateAdapter = true)
        data class WorkDetail(

            @Json(name = "title")
            val title: String?,

            @Json(name = "key")
            val key: String?,

            @Json(name = "cover_edition_key")
            val coverEditionKey: String?,

            @Json(name = "author_names")
            val authorNames: List<String>?,

            @Json(name = "first_publish_year")
            val firstPublishYear: Int?,
        ) {
            fun getCoverMediumUrl() = "https://covers.openlibrary.org/b/olid/${coverEditionKey}-M.jpg"

            fun getCoverLargeUrl() = "https://covers.openlibrary.org/b/olid/${coverEditionKey}-L.jpg"
        }
    }
}
