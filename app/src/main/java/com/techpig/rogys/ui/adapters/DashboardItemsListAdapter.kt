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
import com.techpig.rogys.utils.Constants
import com.techpig.rogys.utils.GlideLoader
import kotlinx.android.synthetic.main.item_dashboard_layout.view.*

class DashboardItemsListAdapter(
    private val context: Context,
    private val list: ArrayList<Product>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class DashboardViewHolder(view: View) : RecyclerView.ViewHolder(view)

//    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DashboardViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_dashboard_layout, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        if (holder is DashboardViewHolder) {
            GlideLoader(context).loadUserPicture(
                item.image,
                holder.itemView.dashboard_item_iv
            )
            holder.itemView.dashboard_item_title.text = item.title
            holder.itemView.dashboard_item_description.text = item.description
            holder.itemView.dashboard_product_price.text = "$${item.price}"
            holder.itemView.setOnClickListener {
                val intent = Intent(context, ProductDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_PRODUCT_ID, item.product_id)
                intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID, item.user_id)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}