package com.techpig.rogys.ui.activities

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.widget.FrameLayout
import androidx.core.content.res.ResourcesCompat
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.techpig.rogys.R
import com.techpig.rogys.ui.fragments.DashboardFragment
import com.techpig.rogys.ui.fragments.OrdersFragment
import com.techpig.rogys.ui.fragments.ProductsFragment

class DashboardActivity : BaseActivity() {

    lateinit var chipNavBar: ChipNavigationBar
    lateinit var frame_layout: FrameLayout
    private var aClass: Class<*>? = null
    private var frag = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        supportActionBar!!.title = "Inicio"

        chipNavBar = findViewById(R.id.nav_view)
        frame_layout = findViewById(R.id.host_fragment)


        when (applicationContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                frame_layout.setBackgroundColor(Color.parseColor("#2C2B2C"))
                chipNavBar.setMenuResource(R.menu.bottom_nav_menu_night)
                chipNavBar.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.chip_night, null)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                frame_layout.setBackgroundColor(Color.parseColor("#F3F3F3"))
                chipNavBar.setMenuResource(R.menu.bottom_nav_menu)
                chipNavBar.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.chip_day, null)
            }
        }

//        frame_layout.setBackgroundColor(Color.parseColor("#F3F3F3"))
//        chipNavBar.background =
//            ResourcesCompat.getDrawable(resources, R.drawable.chip_day, null)


        chipNavBar.setItemSelected(R.id.navigation_dashboard, true)
        aClass = DashboardFragment::class.java
        openFragment()

        chipNavBar.setOnItemSelectedListener(object : ChipNavigationBar.OnItemSelectedListener {
            override fun onItemSelected(id: Int) {
                when (id) {
                    R.id.navigation_dashboard -> {
                        supportActionBar!!.title = "Inicio"
                        frag = "dashboard"
                        aClass = DashboardFragment::class.java
                        openFragment()
                    }
                    R.id.navigation_products -> {
                        supportActionBar!!.title = "Nuestros Productos"
                        frag = "products"
                        aClass = ProductsFragment::class.java
                        openFragment()
                    }
                    R.id.navigation_orders -> {
                        supportActionBar!!.title = "Pedidos"
                        frag = "orders"
                        aClass = OrdersFragment::class.java
                        openFragment()
                    }
                }
            }
        })
    }

    private fun openFragment() {
        try {
            //Initialize fragment
            val fragment: androidx.fragment.app.Fragment =
                aClass!!.newInstance() as androidx.fragment.app.Fragment
            //Open Fragment
            if (aClass == DashboardFragment::class.java) {
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.host_fragment, fragment).commit()
            } else {
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.host_fragment, fragment).addToBackStack(frag).commit()
            }
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        }
    }

    override fun onBackPressed() {
        doubleBackToExit()
    }
}