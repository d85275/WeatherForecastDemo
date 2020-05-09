package ch.protonmail.android.protonmailtest

import android.content.Context
import android.widget.Toast

class ToastHelper(private val ctx: Context) {
    fun show(msg: String) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
    }
}