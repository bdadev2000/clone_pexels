package com.example.clonepexel.data.response

data class PexelsApiResponse(
    val page: Int,
    val perPage: Int,
    val photos: List<Photo>,
    val totalResults: Int,
    val nextPage: String?,
    val prevPage: String?
)

data class Photo(
    val id: Long,
    val width: Int,
    val height: Int,
    val url: String,
    val photographer: String,
    val photographerUrl: String,
    val photographerId: Long,
    val avgColor: String,
    val src: ImageSource,
    val liked: Boolean,
    val alt: String
)

data class ImageSource(
    val original: String,
    val large2x: String,
    val large: String,
    val medium: String,
    val small: String,
    val portrait: String,
    val landscape: String,
    val tiny: String
)