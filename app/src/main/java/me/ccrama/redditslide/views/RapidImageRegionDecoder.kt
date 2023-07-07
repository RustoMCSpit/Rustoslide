package me.ccrama.redditslide.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.Rect
import android.net.Uri
import com.davemorrissey.labs.subscaleview.decoder.ImageRegionDecoder
import org.acra.ktx.sendSilentlyWithAcra
import rapid.decoder.BitmapDecoder

/**
 * A very simple implementation of [com.davemorrissey.labs.subscaleview.decoder.ImageRegionDecoder]
 * using the RapidDecoder library (https://github.com/suckgamony/RapidDecoder). For PNGs, this can
 * give more reliable decoding and better performance. For JPGs, it is slower and can run out of
 * memory with large images, but has better support for grayscale and CMYK images.
 *
 * This is an incomplete and untested implementation provided as an example only.
 */
class RapidImageRegionDecoder : ImageRegionDecoder {
    private var decoder: BitmapDecoder? = null

    @Throws(Exception::class)
    override fun init(context: Context, uri: Uri): Point {
        decoder = BitmapDecoder.from(context, uri)
        decoder!!.useBuiltInDecoder(true)
        return Point(decoder!!.sourceWidth(), decoder!!.sourceHeight())
    }

    override val isReady: Boolean
        get() = decoder != null

    @Synchronized
    override fun decodeRegion(sRect: Rect, sampleSize: Int): Bitmap {
        return try {
            decoder!!.reset()
                .region(sRect)
                .scale(sRect.width() / sampleSize, sRect.height() / sampleSize)
                .decode()!!
        } catch (e: Exception) {
            e.sendSilentlyWithAcra()
            Bitmap.createBitmap(sRect.width() / sampleSize, sRect.height() / sampleSize,
                Bitmap.Config.ALPHA_8)
        }
    }

    override fun recycle() {
        BitmapDecoder.destroyMemoryCache()
        BitmapDecoder.destroyDiskCache()
        decoder!!.reset()
        decoder = null
    }
}
