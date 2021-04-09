package com.interneted.webviewplugin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.interneted.webviewplugin.databinding.ActivityMainBinding
import com.interneted.webviewplugin.library.XWeb

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflate = ActivityMainBinding.inflate(layoutInflater)
        setContentView(inflate.root)

        XWeb.with(this,inflate.root)
            .apply {
                webView.loadUrl("https://google.com")
            }
    }
}