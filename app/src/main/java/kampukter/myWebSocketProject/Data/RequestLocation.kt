package kampukter.myWebSocketProject.Data

import android.content.Context
import android.location.Location

data class RequestLocation(
    val location: Location,
    val context: Context
)