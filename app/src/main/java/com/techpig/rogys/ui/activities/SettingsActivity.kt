package com.techpig.rogys.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.techpig.rogys.R
import com.techpig.rogys.firestore.FirestoreClass
import com.techpig.rogys.models.User
import com.techpig.rogys.utils.Constants
import com.techpig.rogys.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mUserDetails: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setupActionBar()
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getUserDetails(this)

        tv_edit.setOnClickListener(this)
        logout_button.setOnClickListener(this)
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_settings_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = " "
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white)
        }
        toolbar_settings_activity.setNavigationOnClickListener { onBackPressed() }
    }

    fun userDetailsSuccess(user: User) {

        mUserDetails = user

        hideProgressDialog()
        GlideLoader(this@SettingsActivity).loadUserPicture(user.image, iv_user_photo)
        tv_name.text = "${user.firstName} ${user.lastName}"
        tv_gender.text = "${user.gender}"
        tv_email.text = "${user.email}"
        tv_mobile_number.text = "${user.mobile}"
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.logout_button -> {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this@SettingsActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                R.id.tv_edit -> {
                    val intent = Intent(this@SettingsActivity, UserProfileActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS, mUserDetails)
                    startActivity(intent)
                }
            }
        }
    }

}
