package com.techpig.rogys.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.techpig.rogys.R
import com.techpig.rogys.firestore.FirestoreClass
import com.techpig.rogys.models.User
import com.techpig.rogys.utils.Constants
import com.techpig.rogys.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.io.IOException

class UserProfileActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mUserDetails: User
    private var mSelectedImageFileUri: Uri? = null
    private var mUserProfileImageUrl: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        mUserDetails = User()

        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            //Get the user details from intent as ParcelableExtra.
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        et_first_name.setText(mUserDetails.firstName)
        et_last_name.setText(mUserDetails.lastName)
        et_email.setText(mUserDetails.email)
        et_email.isEnabled = false

        if (mUserDetails.profileCompleted == 0) {
            tv_title.text = resources.getString(R.string.completa_tu_perfil)
            et_first_name.isEnabled = false
            et_last_name.isEnabled = false
        } else {
            setupActionBar()
            tv_title.text = getString(R.string.edit_profile_title)
            GlideLoader(this@UserProfileActivity).loadUserPicture(mUserDetails.image, iv_user_photo)

            if (mUserDetails.mobile != 0L) {
                et_phone.text = SpannableStringBuilder(mUserDetails.mobile.toString())
            }

            if (mUserDetails.gender == Constants.MALE) {
                rb_male.isChecked = true
            } else {
                rb_female.isChecked = true
            }
        }

        iv_user_photo.setOnClickListener(this)
        btn_submit.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.iv_user_photo -> {
                    //Check if the permission is already allowed or need to request for it.
                    //Check the READ_EXTERNAL_STORAGE permission
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        Constants.showImageChooser(this)
                    } else {
                        /**
                         * Request permissions to be granted to this application. These permissions
                         * must be requested in the manifest, they should not be granted to your app
                         * and they should have protection level.
                         */
                        /**
                         * Request permissions to be granted to this application. These permissions
                         * must be requested in the manifest, they should not be granted to your app
                         * and they should have protection level.
                         */
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }
                R.id.btn_submit -> {
                    if (validateUserProfileDetails()) {
                        showProgressDialog("Actualizando datos...")
                        if (mSelectedImageFileUri != null) {
                            FirestoreClass().uploadImageToCloudStorage(this, mSelectedImageFileUri, Constants.USER_PROFILE_IMAGE)
                        } else {
                            updateUserProfileDetails()
                        }
                    }
                }
            }
        }
    }

    private fun updateUserProfileDetails() {
        val userHashMap = HashMap<String, Any>()

        val firstName = et_first_name.text.toString().trim { it <= ' ' }
        if (firstName != mUserDetails.firstName) {
            userHashMap[Constants.FIRST_NAME] = firstName
        }

        val lastName = et_last_name.text.toString().trim { it <= ' ' }
        if (lastName != mUserDetails.firstName) {
            userHashMap[Constants.LAST_NAME] = lastName
        }

        val mobileNumber =
            et_phone.text.toString().trim { it <= ' ' }
        val gender = if (rb_male.isChecked) {
            Constants.MALE
        } else {
            Constants.FEMALE
        }

        if (mUserProfileImageUrl!!.isNotEmpty()) {
            userHashMap[Constants.IMAGE] = mUserProfileImageUrl!!
        }

        if (mobileNumber.isNotEmpty() && mobileNumber != mUserDetails.mobile.toString()) {
            userHashMap[Constants.MOBILE] = 0 + mobileNumber.toLong()
        }

        if (gender.isNotEmpty() && gender != mUserDetails.gender) {
            userHashMap[Constants.GENDER] = gender
        }

        userHashMap[Constants.GENDER] = gender
        userHashMap[Constants.COMPLETE_PROFILE] = 1
        FirestoreClass().updateUserProfileData(this, userHashMap)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this)
            } else {
                makeLongToast("Read permission storage denied")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST__CODE) {
                if (data != null) {
                    try {
                        mSelectedImageFileUri = data.data!!

                        GlideLoader(this).loadUserPicture(mSelectedImageFileUri!!, iv_user_photo)
                    } catch (e: IOException) {

                        makeShortToast("No pudimos cargar tu imagen")

                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            makeLongToast("No has seleccionado ninguna imagen, se mantendrá el avatar por defecto")
        }
    }

    private fun validateUserProfileDetails(): Boolean {
        return when {
            TextUtils.isEmpty(
                et_phone.text.toString()
                    .trim { it <= ' ' }) || et_phone.text!!.length < 9 || et_phone.text!!.length > 10 -> {
                showAlerter(
                    "Número inválido",
                    "Debes ingresar tu número de teléfono celular para poder contactarte cuando realicemos tu pedido",
                    true,
                    4500
                )
                false
            }
            else -> true
        }
    }

    fun userProfileUpdateSuccess() {
        hideProgressDialog()
        makeLongToast(getString(R.string.profile_updated))
        startActivity(Intent(this@UserProfileActivity, DashboardActivity::class.java))
        finish()
    }

    fun imageUploadSuccess(imageURL: String) {
        mUserProfileImageUrl = imageURL
        updateUserProfileDetails()
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_user_profile_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = " "
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white)
        }
        toolbar_user_profile_activity.setNavigationOnClickListener { onBackPressed() }
    }
}