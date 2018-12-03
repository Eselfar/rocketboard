package com.remiboulier.rocketboard.network

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.remiboulier.rocketboard.network.Tls12SocketFactory.Companion.enableTls12
import okhttp3.OkHttpClient
import java.io.InputStream

/**
 * Required to generate the GlideApp implementation
 */
@GlideModule
class GlobalGlideModule : AppGlideModule() {
    /**
     *  We need to override this method to add support to TLS 1.2 for Android 19
     *  @see [This blog post](https://quizlet.com/blog/working-with-tls-1-2-on-android-4-4-and-lower)
     *
     */
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)

        val okHttpClient = OkHttpClient.Builder()
                .enableTls12()
                .build()

        registry.replace(
                GlideUrl::class.java,
                InputStream::class.java,
                OkHttpUrlLoader.Factory(okHttpClient)
        )
    }

    override fun isManifestParsingEnabled() = false
}