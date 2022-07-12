package gentle.hilt.learnunittests2.remote.reponses

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageResponse(
    val hits: List<ImageResult>,
    val total: Int,
    val totalHits: Int
):Parcelable