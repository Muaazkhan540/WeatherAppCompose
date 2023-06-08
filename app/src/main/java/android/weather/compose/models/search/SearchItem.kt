package android.weather.compose.models.search


import androidx.annotation.Keep
import androidx.annotation.NonNull
import androidx.room.Embedded
import androidx.room.Entity

@Keep
@Entity(tableName = "city_weather", primaryKeys = ["primaryKey", "name", "region"])
data class SearchItem(
    @Embedded
    var current: Current,
    @Embedded
    var location: Location,
    var primaryKey: Long,
)

data class SearchState(
    var success: SearchItem? = null,
    var isLoading: Boolean = false,
    var error: String? = null,
)