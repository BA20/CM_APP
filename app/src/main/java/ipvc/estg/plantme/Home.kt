package ipvc.estg.plantme

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import ipvc.estg.plantme.ui.camera.CameraActivity
import ipvc.estg.plantme.ui.dashboard.DashboardFragment
import ipvc.estg.plantme.ui.home.HomeFragment
import ipvc.estg.plantme.ui.plantacoes.PlantacoesFragment
import ipvc.estg.plantme.ui.sugestoes.SugestoesFragment

class Home : AppCompatActivity() {

    private val reqCodeCamera = 1
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        bottomNavigationView = findViewById(R.id.nav_view)

        this.title = "Home";
        navegarParaFragmento(HomeFragment())
        bottomNavigationView.selectedItemId = R.id.navigation_home

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.navigation_home -> {
                    this.title = "Home";
                    navegarParaFragmento(HomeFragment())}
                R.id.navigation_camera -> iniciarPrevisaoDoencas()
                R.id.navigation_dashboard -> {
                    this.title = "Dashboard";
                    navegarParaFragmento(DashboardFragment())}
                R.id.navigation_plantacoes -> {
                    this.title = "Plantações";
                    navegarParaFragmento(PlantacoesFragment())}
                R.id.navigation_sugestoes -> {
                    this.title = "Área de Sugestões";
                    navegarParaFragmento(SugestoesFragment())}
            }
            true
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.appbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_lembretes -> {

                true
            }
            R.id.menu_settings -> {

                true
            }
            R.id.log_out -> {
                realizarLogout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun iniciarPrevisaoDoencas() {
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

    private fun realizarLogout() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.warning_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
        val mAlertDialog = mBuilder.show()

        dialogView.findViewById<Button>(R.id.warning_botao_sim).setOnClickListener {
            val intent = Intent(this, Login::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            val sharedPreferences = this.getSharedPreferences(getString(R.string.plantme), Context.MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                clear()
                commit()
            }
            mAlertDialog.dismiss()
            this.finish()
        }

        dialogView.findViewById<Button>(R.id.warning_botao_nao).setOnClickListener {
            mAlertDialog.dismiss()
        }
    }
}