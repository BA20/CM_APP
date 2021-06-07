package ipvc.estg.plantme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton

class Registo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registo)
    }

    fun backLogin(view: View) {
        if (view is ImageButton) {
            val backLogin = Intent(this, Login::class.java)
            startActivity(backLogin)
            finish()
        }
    }
}