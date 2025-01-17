package ltd.ucode.network.lemmy.api.request

import kotlinx.serialization.Serializable

@Serializable
data class GetUnreadCountRequest (
    override var auth: String? = null
) : Authenticated
