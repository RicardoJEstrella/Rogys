package com.techpig.rogys.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.GridLayoutManager
import com.techpig.rogys.R
import com.techpig.rogys.firestore.FirestoreClass
import com.techpig.rogys.models.Product
import com.techpig.rogys.ui.activities.CartListActivity
import com.techpig.rogys.ui.activities.ProductDetailsActivity
import com.techpig.rogys.ui.activities.SettingsActivity
import com.techpig.rogys.ui.adapters.DashboardItemsListAdapter
import com.techpig.rogys.utils.Constants
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //If we want to use the options menu in fragment, we need to add it.
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        getDashboardItemList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    //Complemento de setHasOptionsMenu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(activity, SettingsActivity::class.java))
                return true
            }
            R.id.action_cart -> {
                startActivity(Intent(activity, CartListActivity::class.java))
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun successDashboardItemsList(dashboardItemList: ArrayList<Product>) {
        hideProgressDialog()

        if (dashboardItemList.size > 0) {
            dashboard_no_items_tv.visibility = View.GONE
            rv_dashboard_items.visibility = View.VISIBLE
            setupRecyclerView(dashboardItemList)

            val adapter = DashboardItemsListAdapter(requireActivity(), dashboardItemList)
            rv_dashboard_items.adapter = adapter
        } else {
            dashboard_no_items_tv.visibility = View.VISIBLE
            rv_dashboard_items.visibility = View.GONE
        }
    }

    private fun setupRecyclerView(dashboardItemList: ArrayList<Product>) {
        rv_dashboard_items.layoutManager = GridLayoutManager(activity, 1)
        rv_dashboard_items.setHasFixedSize(true)
        val adapter = DashboardItemsListAdapter(requireActivity(), dashboardItemList)
        rv_dashboard_items.adapter = adapter
    }

    private fun getDashboardItemList() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getDashboardItemsList(this@DashboardFragment)
    }
}