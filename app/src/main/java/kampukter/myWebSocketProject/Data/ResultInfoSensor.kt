package kampukter.myWebSocketProject.Data

sealed class ResultInfoSensor {
    data class Success(val infoSensor: List<InfoSensor>) : ResultInfoSensor()
    object EmptyResponse: ResultInfoSensor()
    data class OtherError( val tError: String ) : ResultInfoSensor()
}