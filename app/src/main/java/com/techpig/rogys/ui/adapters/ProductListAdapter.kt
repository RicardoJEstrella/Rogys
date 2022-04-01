package com.techpig.rogys.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.techpig.rogys.R
import com.techpig.rogys.models.Product
import com.techpig.rogys.ui.activities.ProductDetailsActivity
import com.techpig.rogys.ui.fragments.ProductsFragment
import com.techpig.rogys.utils.Constants
import com.techpig.rogys.utils.GlideLoader
import kotlinx.android.synthetic.main.item_list_layout.view.*

open class ProductListAdapter(
    private val context: Context,
    private var listOfProducts: ArrayList<Product>,
    private val fragment: ProductsFragment
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProductViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_list_layout, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = listOfProducts[position]

        if (holder is ProductViewHolder) {
            GlideLoader(context).loadProductPicture(model.image, holder.itemView.product_image)
            holder.itemView.product_title.text = model.title
            holder.itemView.product_price.text = "$${model.price}"

            holder.itemView.ib_delete_product.setOnClickListener {
                fragment.deleteProduct(model.product_id)
            }
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra(Constants.EXTRA_PRODUCT_ID, model.product_id)
            intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID, model.user_id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listOfProducts.size
    }

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view)
}