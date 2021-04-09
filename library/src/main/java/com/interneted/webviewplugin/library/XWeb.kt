package com.interneted.webviewplugin.library

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.interneted.webviewplugin.library.client.DefaultWebChromeClient
import com.interneted.webviewplugin.library.client.DefaultWebViewClient
import com.interneted.webviewplugin.library.container.WebViewParentLayout
import com.interneted.webviewplugin.library.indicator.IndicatorController
import com.interneted.webviewplugin.library.indicator.IndicatorHandler
import com.interneted.webviewplugin.library.indicator.WebIndicator
import com.interneted.webviewplugin.library.settings.XWebSettings
import com.interneted.webviewplugin.library.video.IVideo
import com.interneted.webviewplugin.library.video.VideoImpl

/**
 * Creator: ED
 * Date: 2020/11/16 5:02 PM
 * Mail: salahayo3192@gmail.com
 *
 * **/

class XWeb private constructor(
    private val mActivity: FragmentActivity,
    viewGroup: ViewGroup
) {

    companion object {
        fun with(context: Context, viewGroup: ViewGroup): XWeb {

            val activity =
                when (context) {
                    is AppCompatActivity -> {
                        context
                    }
                    is Fragment -> {
                        context.requireActivity()
                    }
                    else -> {
                        throw IllegalAccessError("不支援除 Activity｜Fragment 的 Context")
                    }
                }



            return XWeb(activity, viewGroup)
        }
    }


    val webView: WebView by lazy(LazyThreadSafetyMode.NONE) {
        NestedScrollAgentWebView(mActivity).apply {
            id = View.generateViewId()
        }
    }
    private val mIndicatorController: IndicatorController

    private val mIVideo: IVideo by lazy(LazyThreadSafetyMode.NONE) {
        VideoImpl(mActivity, webView)
    }

    private val xWebSettings = XWebSettings()


    init {
        val webIndicator = WebIndicator(mActivity)
        webIndicator.id = View.generateViewId()

        mIndicatorController = IndicatorHandler(webIndicator)

        createLayout(viewGroup, webIndicator)

        xWebSettings.toSetting(webView)

        mActivity.lifecycle.addObserver(WebViewLifecycleObserver(webView))
    }

    private fun createLayout(viewGroup: ViewGroup, webIndicator: WebIndicator) {


        val webViewParentLayout = WebViewParentLayout(mActivity)
        webViewParentLayout.addView(
            webView,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        webViewParentLayout.addView(
            webIndicator,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
        webIndicator.visibility = View.GONE

        viewGroup.addView(
            webViewParentLayout, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
    }


    fun setWebSettings(settings: (WebSettings) -> Unit): XWeb {
        webView.settings.apply(settings)
        return this
    }


    fun setWebViewClient(webViewClient: WebViewClient): XWeb {
        val defaultWebViewClient =
            DefaultWebViewClient(webViewClient)

        xWebSettings.setWebViewClient(webView, defaultWebViewClient)
        return this
    }

    fun setWebChromeClient(webChromeClient: WebChromeClient): XWeb {
        val defaultWebChromeClient =
            DefaultWebChromeClient(webChromeClient, mIndicatorController, mIVideo)

        xWebSettings.setWebViewChromeClient(webView, defaultWebChromeClient)
        return this
    }


}
