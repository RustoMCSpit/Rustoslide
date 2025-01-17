package ltd.ucode.network.lemmy.data.type.webfinger

import kotlinx.serialization.Serializable
import ltd.ucode.network.lemmy.api.response.IResponse

// Some guesswork.
@Serializable
data class NodeInfo (
    val version: String,
    val software: Software,
    val protocols: List<String>? = null,
    val usage: Usage? = null,
    val openRegistrations: Boolean = false,
) : IResponse()
