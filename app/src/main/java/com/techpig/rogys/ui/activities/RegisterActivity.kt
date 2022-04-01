package com.techpig.rogys.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import com.google.firebase.auth.*
import com.techpig.rogys.R
import com.techpig.rogys.firestore.FirestoreClass
import com.techpig.rogys.models.User
import com.techpig.rogys.utils.RogysTextViewBold
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : BaseActivity() {

    lateinit var tv_login: RogysTextViewBold

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        tv_login = findViewById(R.id.tv_login)

        setupActionBar()

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        tv_login.setOnClickListener {
            val login = Intent(this, LoginActivity::class.java)
            startActivity(login)
            finish()
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_register_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white)
            actionBar.title = null
        }
        toolbar_register_activity.setNavigationOnClickListener {
            onBackPressed()
        }

        btn_register.setOnClickListener {
            registerUser()
        }
    }

    private fun validateFields(): Boolean {
        return when {
            TextUtils.isEmpty(
                et_name.editableText.toString()
                    .trim { it <= ' ' }) || et_name.length() <= 2 -> {
                showAlerter(
                    "¿Cuál es tu nombre?", "Introduce tu nombre", true,
                    3500
                )
                false
            }
            TextUtils.isEmpty(
                et_last_name.text.toString()
                    .trim { it <= ' ' }) || et_last_name.length() <= 2 -> {
                showAlerter(
                    "¿Cuál es tu apellido?", "Introduce tu apellido", true,
                    3500
                )
                false
            }
            TextUtils.isEmpty(et_email.text.toString()) && !android.util.Patterns.EMAIL_ADDRESS.matcher(
                et_email.text.toString()
            ).matches() -> {
                showAlerter(
                    "Error", "Introduce un correo válido", true,
                    3500
                )
                false
            }
            TextUtils.isEmpty(et_password.text.toString().trim { it <= ' ' }) -> {
                showAlerter(
                    "Contraseña inválida", "Introduce una contraseña", true,
                    3500
                )
                false
            }
            TextUtils.isEmpty(
                et_confirm_password.text.toString()
                    .trim { it <= ' ' }) || (et_password.text.toString() != et_confirm_password.text.toString()) -> {
                showAlerter(
                    "Las contraseñas no coinciden",
                    "Verifica que hayas escrito bien las contraseñas",
                    true,
                    4000
                )
                false
            }
            !cb_terms_and_conditions.isChecked -> {
                showAlerter(
                    "¿Leíste los T&C?",
                    "Debes aceptar nuestros términos y condiciones",
                    true,
                    3500
                )
                false
            }
            else -> {
                true
            }
        }
    }

    private fun registerUser() {
        if (validateFields()) {

            showProgressDialog(resources.getString(R.string.please_wait))

            val email: String = et_email.text.toString().trim { it <= ' ' }
            val password: String = et_password.text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    //If the registration is successfully done
                    if (task.isSuccessful) {
                        //Firebase registered user
                        val firebaseUser: FirebaseUser = task.result!!.user!!

                        val user = User(
                            firebaseUser.uid,
                            et_name.text.toString().trim { it <= ' ' },
                            et_last_name.text.toString().trim { it <= ' ' },
                            et_email.text.toString().trim { it <= ' ' },
                        )

                        FirestoreClass().registerUser(this@RegisterActivity, user)

//                        FirebaseAuth.getInstance().signOut()
//                        finish()

                    } else {
                        try {
                            throw task.exception!!
                        } catch (e: FirebaseAuthWeakPasswordException) {
                            showAlerter(
                                getString(R.string.weak_password),
                                "Intenta con una contraseña más segura",
                                true,
                                3500
                            )
                            til_password.requestFocus()
                        } catch (e: FirebaseAuthInvalidCredentialsException) {
                            showAlerter(
                                getString(R.string.invalid_email),
                                "Por favor, ingresa un correo válido",
                                true,
                                3500
                            )
                            til_email.requestFocus()
                        } catch (e: FirebaseAuthUserCollisionException) {
                            showAlerter(
                                getString(R.string.email_already_in_use),
                                getString(R.string.login_or_change_email),
                                true,
                                4300
                            )
                        } catch (e: Exception) {
                            Log.e("New exception", e.message!!)
                        }
                    }
                }
        }
    }

    fun userRegisterSuccess() {
        hideProgressDialog()
        showAlerter(
            getString(R.string.registered),
            getString(R.string.welcome_to_rogys),
            false,
            3500
        )
        //TODO( Borrar)
        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
    }
}