package com.techpig.rogys.ui.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.techpig.rogys.R
import com.techpig.rogys.models.User
import com.techpig.rogys.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences(Constants.ROGYS_PREFERENCES, Context.MODE_PRIVATE)
        val username = sharedPreferences.getString(Constants.LOGGED_IN_FIRSTNAME, "")

        if (User().gender == "Hombre") {
            tv_main.text = "¡Hola $username, bienvenido!"
        } else {
            tv_main.text = "¡Hola $username, bienvenida!"
        }
    }
}