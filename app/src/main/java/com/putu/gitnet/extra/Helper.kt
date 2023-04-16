package com.putu.gitnet.extra

import android.view.View

class Helper {
    fun isLoading(status: Boolean, view: View) {
        if (status) view.visibility = View.VISIBLE
        else view.visibility = View.INVISIBLE
    }
}