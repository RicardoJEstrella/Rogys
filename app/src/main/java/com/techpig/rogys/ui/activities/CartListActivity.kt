package com.techpig.rogys.ui.activities

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.techpig.rogys.R
import com.techpig.rogys.firestore.FirestoreClass
import com.techpig.rogys.models.CartItem
import com.techpig.rogys.models.Product
import com.techpig.rogys.ui.adapters.CartItemAdapter
import kotlinx.android.synthetic.main.activity_cart_list.*
import kotlinx.android.synthetic.main.item_cart_layout.*

class CartListActivity : BaseActivity() {

    private lateinit var mProductsList: ArrayList<Product>
    private lateinit var mCartListItems: ArrayList<CartItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_list)
        supportActionBar!!.title = resources.getString(R.string.my_cart_title)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        when (applicationContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                activity_cart_list.setBackgroundColor(Color.parseColor("#000000"))
                ll_checkout.setBackgroundColor(Color.parseColor("#141414"))
                checkout_button.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.button_background_night, null)
                checkout_button.setTextColor(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.white,
                        null
                    )
                )
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                activity_cart_list.setBackgroundColor(Color.parseColor("#F3F3F3"))
                ll_checkout.setBackgroundColor(Color.parseColor("#FAFAFA"))
                checkout_button.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.button_background, null)
                checkout_button.setTextColor(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.white,
                        null
                    )
                )
            }
        }
    }

    fun successCartItemsList(cartList: ArrayList<CartItem>) {
        hideProgressDialog()

        //Items in stock
        for (product in mProductsList) {
            for (cartItem in cartList) {
                if (product.product_id == cartItem.productId) {
                    cartItem.stock_quantity = "99999" //Convert to boolean
                    if (product.stock_quantity == "0") { //Parse int
                        cartItem.car_quantity = product.stock_quantity
                    }
                }
            }
        }

        mCartListItems = cartList

        if (mCartListItems.size > 0) {
            rv_cart_items_list.visibility = View.VISIBLE
            cart_no_items_tv.visibility = View.GONE

            rv_cart_items_list.layoutManager = LinearLayoutManager(this@CartListActivity)
            rv_cart_items_list.setHasFixedSize(true)

            val cartAdapter = CartItemAdapter(this@CartListActivity, cartList)
            rv_cart_items_list.adapter = cartAdapter

            var subtotal: Double = 0.00
            for (item in mCartListItems) {

                val availableQuantity = item.stock_quantity.toInt()

                if (availableQuantity > 0) {
                    val price = item.price.toDouble()
                    val quantity = item.car_quantity.toInt()
                    subtotal += (price * quantity)
                }

            }

            tv_subtotal.text = "$$subtotal"
            tv_shipping.text = "$5.00"

            if (subtotal > 0) {
                checkout_button.isEnabled = true //Asegurarse de que sirve
                val total = subtotal + 5.00 // TODO Cambiar la logica del shipping
                tv_total.text = "$$total"
            } else {
                checkout_button.isEnabled = false
            }
        } else {
            rv_cart_items_list.visibility = View.GONE
            cart_no_items_tv.visibility = View.VISIBLE
            checkout_button.isEnabled = false
        }
    }

    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {
        hideProgressDialog()
        mProductsList = productsList
        getCartItemList()
    }

    private fun getProductList() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getAllProductsList(this)
    }

    private fun getCartItemList() {
//        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getCartList(this@CartListActivity)
    }

    override fun onResume() {
        super.onResume()
        getProductList()
    }

    //TODO(AHORA AGREGAR EL STEPPER TOUCH PARA AGREGAR O QUITAR ITEMS)

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}