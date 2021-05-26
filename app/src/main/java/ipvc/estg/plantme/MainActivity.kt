package ipvc.estg.plantme

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        val background = object : Thread() {
            override fun run() {
                try {
                    sharedPreferences = getSharedPreferences(getString(R.string.plantme), Context.MODE_PRIVATE)
                    val loggedIn = sharedPreferences.getBoolean(getString(R.string.log_in_state), false)
                    if(loggedIn) {
                        sleep(3000)
                        //TODO - 1. MUDAR SE FOR NECESSÁRIO O NOME DA ATIVIDADE DE ECRÃ PRINCIPAL
                        /*val intent = Intent(this@MainActivity, HomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        finish()
                        */
                    }
                    else {
                        sleep(3000)
                        //TODO - 2. MUDAR SE FOR NECESSÁRIO O NOME DA ATIVIDADE DE LOGIN
                        /*val intent = Intent(baseContext, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                        */
                    }
                } catch (e : Exception) {
                    e.printStackTrace()
                }
            }
        }
        background.start()
    }
}