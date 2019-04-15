package kampukter.myWebSocketProject.Data

sealed class ResultLocation {
    data class Success(val location: String) : ResultLocation()
    object EmptyResponse: ResultLocation()
    data class OtherError( val tError: String ) : ResultLocation()
}