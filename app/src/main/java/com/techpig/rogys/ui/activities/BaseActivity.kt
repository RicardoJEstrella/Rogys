package com.techpig.rogys.ui.activities

import android.app.Dialog
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tapadoo.alerter.Alerter
import com.techpig.rogys.R
import kotlinx.android.synthetic.main.dialog_progress.*

open class BaseActivity : AppCompatActivity() {

    private var doubleBackPressed = false
    lateinit var mProgressDialog: Dialog

    fun showAlerter(title: String, message: String, isErrorAlert: Boolean, duration: Long) {
        val alert = Alerter.create(this)
        alert.setTitle(title)
        alert.setText(message)
        if (isErrorAlert) {
            alert.setIcon(R.drawable.ic_error)
            alert.setBackgroundColorRes(R.color.crimson)
        } else {
            alert.setBackgroundColorRes(R.color.green_hard)
            alert.setIcon(R.drawable.alerter_ic_face)
        }
        alert.setDuration(duration)
        alert.enableSwipeToDismiss()
        alert.show()
    }

    fun showProgressDialog(text: String) {
        mProgressDialog = Dialog(this)
        mProgressDialog.setContentView(R.layout.dialog_progress)
        mProgressDialog.tv_progress_text.text = text
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()
    }

    fun makeLongToast(message: String) {
        Toast.makeText(this@BaseActivity, message, Toast.LENGTH_LONG).show()
    }

    fun makeShortToast(message: String) {
        Toast.makeText(this@BaseActivity, message, Toast.LENGTH_SHORT).show()
    }

    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }

    fun doubleBackToExit() {
        if (doubleBackPressed) {
//            super.onBackPressed()
            finish()
            return
        }

        this.doubleBackPressed = true
        makeShortToast(getString(R.string.press_back_again_to_exit))

        @Suppress("DEPRECATION")
        Handler().postDelayed({ doubleBackPressed = false }, 2000)
    }
}