package com.example.storyapp.util

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import com.example.storyapp.R

class LoadingDialog(context: Context) : Dialog(context) {
    override fun show() {
        super.show()
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        window?.setContentView(R.layout.dialog_progress)
        setCancelable(false)
    }
    
    override fun dismiss() {
        super.dismiss()
    }
}