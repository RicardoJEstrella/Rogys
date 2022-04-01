package com.techpig.rogys.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.techpig.rogys.R
import com.techpig.rogys.firestore.FirestoreClass
import com.techpig.rogys.models.Product
import com.techpig.rogys.ui.activities.AddProductActivity
import com.techpig.rogys.ui.adapters.ProductListAdapter
import kotlinx.android.synthetic.main.fragment_products.*

class ProductsFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    fun deleteProduct(productId: String) {
        showDeletionAlertDialog(productId)
    }

    fun showDeletionAlertDialog(productId: String) {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(resources.getString(R.string.delete_dialog_title))
        builder.setMessage(resources.getString(R.string.delete_dialog_message))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, _ ->
            showProgressDialog(resources.getString(R.string.please_wait))
            FirestoreClass().deleteProduct(this, productId)
            dialogInterface.dismiss()
        }

        builder.setNegativeButton(resources.getString(R.string.no)) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    fun productDeleteSuccess() {
        hideProgressDialog()
        Toast.makeText(
            requireActivity(),
            resources.getString(R.string.product_deletion_success),
            Toast.LENGTH_SHORT
        ).show()
        getProductListFromFireStore()
    }

    fun successProductListFromFireStore(productList: ArrayList<Product>) {
        hideProgressDialog()

        if (productList.size > 0) {
            rv_product_items.visibility = View.VISIBLE
            tv_noProductsFound.visibility = View.GONE

            rv_product_items.layoutManager = LinearLayoutManager(activity)
            rv_product_items.setHasFixedSize(true)
            val adapterProducts = ProductListAdapter(requireActivity(), productList, this)
            rv_product_items.adapter = adapterProducts
        } else {
            rv_product_items.visibility = View.GONE
            tv_noProductsFound.visibility = View.VISIBLE
        }

    }

    private fun getProductListFromFireStore() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getProductsList(this)
    }

    override fun onResume() {
        super.onResume()
        getProductListFromFireStore()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_products, container, false)

        return v
    }

    //Complemento de setHasOptionsMenu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_product_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {
            R.id.action_add_product -> {
                startActivity(Intent(activity, AddProductActivity::class.java))
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}