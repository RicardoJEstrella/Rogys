package com.techpig.rogys.ui.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.techpig.rogys.R
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        setupActionBar()

        btn_submit.setOnClickListener {
            sendMail()
        }
    }

    private fun sendMail() {
        val email: String = til_email.editText!!.text.toString().trim { it <= ' ' }

        if (email.isEmpty()) {
            showAlerter(
                "Debes llenar este campo",
                "Debes ingresar tu correo electrónico para poder reestablecer la contraseña",
                true,
                3200
            )
        } else {
            showProgressDialog("Por favor, espera...")
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    hideProgressDialog()
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this@ForgotPasswordActivity,
                            "Se ha enviado un correo electrónico a $email",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    } else {
                        try {
                            throw task.exception!!
                        } catch (e: FirebaseAuthInvalidCredentialsException) {
                            showAlerter(
                                "Correo no válido",
                                "Por favor, revisa que el correo esté bien escrito",
                                true,
                                4000
                            )
                        } catch (e: FirebaseAuthInvalidUserException) {
                            Log.e("Exception", e.toString())
                            showAlerter(
                                "Correo no válido",
                                "No existe el correo electrónico \n$email en nuestra base de datos",
                                true,
                                4500
                            )
                        } catch (e: FirebaseAuthEmailException) {
                            showAlerter(
                                "Correo no válido",
                                "Por favor, revisa que el correo esté bien escrito",
                                true,
                                4000
                            )
                        }
                    }
                }
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_forgot_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white)
            actionBar.title = null
        }
        toolbar_forgot_activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}