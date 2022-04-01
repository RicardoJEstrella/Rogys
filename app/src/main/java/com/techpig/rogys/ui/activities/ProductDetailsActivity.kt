package com.techpig.rogys.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.techpig.rogys.R
import com.techpig.rogys.firestore.FirestoreClass
import com.techpig.rogys.models.CartItem
import com.techpig.rogys.models.Product
import com.techpig.rogys.utils.Constants
import com.techpig.rogys.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_product_details.*

class ProductDetailsActivity : BaseActivity(), View.OnClickListener {

    private var mProductId: String = ""
    private lateinit var mProductDetails: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        setupActionBar()
        if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID)) {
            mProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
        }

        var productOwnerId: String = ""

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_OWNER_ID)) {
            productOwnerId = intent.getStringExtra(Constants.EXTRA_PRODUCT_OWNER_ID)!!
        }

        if (FirestoreClass().getCurrentUserId() == productOwnerId) {
            button_add_to_cart.visibility = View.GONE
            button_go_to_cart.visibility = View.GONE
        } else {
            button_add_to_cart.visibility = View.VISIBLE
        }
        getProductDetails()

        button_add_to_cart.setOnClickListener(this)
        button_go_to_cart.setOnClickListener(this)
    }

    fun productDetailsSuccess(product: Product) {
        mProductDetails = product
        GlideLoader(this@ProductDetailsActivity).loadProductPicture(
            product.image,
            product_details_image
        )
        product_description.text = product.description
        product_price.text = "$${product.price}"
        product_title.text = product.title

        if (FirestoreClass().getCurrentUserId() == product.user_id) {
            hideProgressDialog()
        } else {
            FirestoreClass().checkIfItemExistsInCart(this@ProductDetailsActivity, mProductId) //AAAAAAAAAAAAAAAAA
        }
    }

    private fun getProductDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getProductDetails(this@ProductDetailsActivity, mProductId)
    }

    fun productExistsInCart() {
        hideProgressDialog()
//        button_add_to_cart.visibility = View.GONE
        button_go_to_cart.visibility = View.VISIBLE
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_product_details_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = " "
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white)
        }

        toolbar_product_details_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun addToCart() {
        val addToCart = CartItem(
            FirestoreClass().getCurrentUserId(),
            mProductId,
            mProductDetails.title,
            mProductDetails.price,
            mProductDetails.image,
            Constants.DEFAULT_CART_QUANTITY
        )
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().addCartItems(this@ProductDetailsActivity, addToCart)
    }

    fun addToCartSuccess() {
        hideProgressDialog()
        Toast.makeText(this@ProductDetailsActivity, "Se ha añadido al carrito", Toast.LENGTH_SHORT)
            .show()

//        button_add_to_cart.visibility = View.GONE
        button_go_to_cart.visibility = View.VISIBLE
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.button_add_to_cart -> {
                    addToCart()
                }
                R.id.button_go_to_cart -> {
                    startActivity(Intent(this@ProductDetailsActivity, CartListActivity::class.java))
                }
            }
        }
    }
}