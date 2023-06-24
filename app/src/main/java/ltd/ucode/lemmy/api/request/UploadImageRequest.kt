package ltd.ucode.lemmy.api.request

// Not serialized!
class UploadImageRequest (
    override var auth: String? = null,
    val filename: String,
    val image: ByteArray
) : Authenticated
