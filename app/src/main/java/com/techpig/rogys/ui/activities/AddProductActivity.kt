package com.techpig.rogys.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.techpig.rogys.R
import com.techpig.rogys.firestore.FirestoreClass
import com.techpig.rogys.models.Product
import com.techpig.rogys.utils.Constants
import com.techpig.rogys.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_add_product.*
import java.io.IOException

class AddProductActivity : BaseActivity(), View.OnClickListener {

    private var mSelectedImageFileURI: Uri? = null
    private var mProductImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        setupActionBar()
        add_update_product.setOnClickListener(this)
        btn_submit.setOnClickListener(this)
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_add_product_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = " "
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white)
        }
        toolbar_add_product_activity.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.add_update_product -> {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        Constants.showImageChooser(this@AddProductActivity)
                    } else {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }
                R.id.btn_submit -> {
                    if (validateProductDetails()) {
                        uploadProductImage()
                    }
                }
            }
        }
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
                showAlerter(
                    getString(R.string.access_denied),
                    getString(R.string.need_to_grant_access_to_perform_this_action),
                    true,
                    5000
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST__CODE) {
                if (data != null) {
                    add_update_product.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.ic_edit
                        )
                    ) //Change camera icon

                    mSelectedImageFileURI = data.data!!
                    try {
                        GlideLoader(this).loadUserPicture(mSelectedImageFileURI!!, iv_product_image)
                    } catch (e: IOException) {
                        makeShortToast(getString(R.string.couldnt_load_image_try_again))
                        makeLongToast(e.localizedMessage!!)
                        e.printStackTrace()
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            makeLongToast(getString(R.string.image_not_selected))
        }
    }

    private fun validateProductDetails(): Boolean {
        return when {
            mSelectedImageFileURI == null -> {
                showAlerter(
                    getString(R.string.what_are_you_selling),
                    getString(R.string.select_image),
                    true,
                    3500
                )
                false
            }
            TextUtils.isEmpty(et_product_title.text.toString().trim { it <= ' ' }) -> {
                showAlerter(
                    getString(R.string.what_is_the_product_name),
                    getString(R.string.write_a_title),
                    true,
                    3000
                )
                false
            }
            TextUtils.isEmpty(et_product_price.text.toString().trim { it <= ' ' }) -> {
                showAlerter(
                    getString(R.string.is_it_free),
                    getString(R.string.put_the_price),
                    true,
                    3000
                )
                false
            }
            TextUtils.isEmpty(et_product_description.text.toString().trim { it <= ' ' }) -> {
                showAlerter(
                    getString(R.string.put_description),
                    getString(R.string.product_must_have_description),
                    true,
                    3000
                )
                false
            }
            else -> true
        }
    }

    fun productUploadSuccess() {
        hideProgressDialog()
        Toast.makeText(
            this@AddProductActivity,
            getString(R.string.product_added),
            Toast.LENGTH_SHORT
        ).show()
        finish()
    }

    fun imageUploadSuccess(imageURL: String) {
        mProductImageURL = imageURL
        uploadProductDetails()
    }

    private fun uploadProductDetails() {
        val username = this.getSharedPreferences(Constants.ROGYS_PREFERENCES, Context.MODE_PRIVATE)
            .getString(Constants.LOGGED_IN_USERNAME, "")!!

        fun dosDecimales(precio: String): String {
            val prec = precio.toFloat()
            return String.format("%.2f", prec)
        }

        val product = Product(
            FirestoreClass().getCurrentUserId(),
            username,
            et_product_title.text.toString().trim { it <= ' ' },
            dosDecimales(et_product_price.text.toString().trim { it <= ' ' }),
            et_product_description.text.toString().trim { it <= ' ' },
            mProductImageURL
        )

        FirestoreClass().uploadProductDetails(this, product)
    }

    private fun uploadProductImage() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().uploadImageToCloudStorage(
            this,
            mSelectedImageFileURI,
            Constants.PRODUCT_IMAGE
        )
    }

    fun productImageUpdateSuccess() {
        hideProgressDialog()
        makeLongToast(getString(R.string.imagen_anadida))
    }
}