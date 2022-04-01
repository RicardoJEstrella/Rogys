package com.techpig.rogys.ui.adapters

import android.content.Context
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.techpig.rogys.R
import com.techpig.rogys.models.CartItem
import com.techpig.rogys.utils.GlideLoader
import kotlinx.android.synthetic.main.item_cart_layout.*
import kotlinx.android.synthetic.main.item_cart_layout.view.*
import nl.dionsegijn.steppertouch.OnStepCallback

class CartItemAdapter(private val context: Context, private val listOfItems: List<CartItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (context.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                ViewHoler(
                    LayoutInflater.from(context).inflate(R.layout.item_cart_layout_night, parent, false)
                )
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                ViewHoler(
                    LayoutInflater.from(context).inflate(R.layout.item_cart_layout, parent, false)
                )
            }
            else -> {
                ViewHoler(
                    LayoutInflater.from(context).inflate(R.layout.item_cart_layout, parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = listOfItems[position]

        for (item in listOfItems) {
            GlideLoader(context).loadProductPicture(model.image, holder.itemView.cart_item_image)
            holder.itemView.cart_item_title.text = item.title
            holder.itemView.cart_item_price.text = "$${item.price}"
            holder.itemView.cart_item_stepper_touch.minValue = 1
            holder.itemView.cart_item_stepper_touch.count = model.car_quantity.toInt() //POSIBLE ERROR

            holder.itemView.cart_item_stepper_touch.sideTapEnabled = true
            if (model.car_quantity == "0") {
                holder.itemView.cart_item_stepper_touch.allowNegative(false)
                holder.itemView.cart_item_stepper_touch.count = 0
            } else {
                holder.itemView.cart_item_stepper_touch.allowNegative(true)
            }
            holder.itemView.cart_item_stepper_touch.addStepCallback(object : OnStepCallback {
                override fun onStep(value: Int, positive: Boolean) {
                    //Para cuando se agregue o se quite la cantidad
                    item.car_quantity = value.toString()
                    Toast.makeText(context, value.toString(), Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun getItemCount(): Int {
        return listOfItems.size
    }
}

class ViewHoler(view: View) : RecyclerView.ViewHolder(view)
