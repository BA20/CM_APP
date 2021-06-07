package ipvc.estg.plantme

import android.app.Activity
import android.content.Intent
import android.graphics.Camera
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import ipvc.estg.plantme.ui.camera.CameraActivity
import ipvc.estg.plantme.ui.dashboard.DashboardFragment
import ipvc.estg.plantme.ui.home.HomeFragment
import ipvc.estg.plantme.ui.notifications.NotificationsFragment

class Home : AppCompatActivity() {

    private val reqCodeCamera = 1
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        bottomNavigationView = findViewById(R.id.nav_view)

        navegarParaFragmento(HomeFragment())
        bottomNavigationView.selectedItemId = R.id.navigation_home

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.navigation_home -> navegarParaFragmento(HomeFragment())
                R.id.navigation_camera -> previsaoDoencas()
                R.id.navigation_dashboard -> navegarParaFragmento(DashboardFragment())
                R.id.navigation_notifications -> navegarParaFragmento(NotificationsFragment())
            }
            true
        }
    }

    private fun previsaoDoencas() {
        val intent = Intent(this, CameraActivity::class.java)
        startActivityForResult(intent, reqCodeCamera)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == reqCodeCamera && resultCode == Activity.RESULT_CANCELED) {
            navegarParaFragmento(HomeFragment())
            bottomNavigationView.selectedItemId = R.id.navigation_home;
        }
    }

    private fun navegarParaFragmento(fragmento: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.nav_host_fragment, fragmento)
            commit()
        }
    }
}