package search

import com.example.pl_market.domain.models.Track
import com.google.gson.annotations.SerializedName

class SearchResponse(
    @SerializedName("resultCount") val resultCount: Int,
    @SerializedName("results") val results: List<Track>
)