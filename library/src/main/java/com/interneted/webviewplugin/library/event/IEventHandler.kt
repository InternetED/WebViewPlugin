package com.interneted.webviewplugin.library.event

import android.view.KeyEvent

/**
 * Creator: ED
 * Date: 2020/11/17 10:41 AM
 * Mail: salahayo3192@gmail.com
 *
 * **/
interface IEventHandler {
    fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean

    fun back(): Boolean
}