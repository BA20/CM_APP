package ipvc.estg.plantme

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import ipvc.estg.plantme.api.EndPoints
import ipvc.estg.plantme.api.ServiceBuilder
import ipvc.estg.plantme.api.entidades.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {

    private lateinit var email: EditText
    private lateinit var password: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email = findViewById(R.id.loginEmail)
        password = findViewById(R.id.loginPassword)
    }



    fun registo(view: View) {
        if(view is TextView) {
            val registo = Intent(this, Registo::class.java)
            startActivity(registo)
            finish()
        }
    }

    fun realizarLogin(view: View) {
        if(view is ImageButton) {
            val email =  email.text.toString()
            val password = password.text.toString()
            if(email.isNotEmpty() && password.isNotEmpty()){
                val request = ServiceBuilder.buildService(EndPoints::class.java)
                val call = request.login(email, password)
                call.enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if(response.isSuccessful){
                            if(response.body()!!.email == email){
                                val sharedPreferences = getSharedPreferences(getString(R.string.plantme), Context.MODE_PRIVATE)
                                with(sharedPreferences.edit()) {
                                    putString(getString(R.string.email_sp), response.body()!!.email)
                                    putInt(getString(R.string.user_id), response.body()!!.id)
                                    putBoolean(getString(R.string.log_in_state), true)
                                    commit()
                                }
                                val intent = Intent(baseContext, Home::class.java)
                                startActivity(intent)
                                finish()
                            }else {
                                Toast.makeText(this@Login, getString(R.string.emailError), Toast.LENGTH_SHORT).show()
                            }
                        }else {
                            Toast.makeText(this@Login, getString(R.string.error), Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Toast.makeText(this@Login, "${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }else {
                Toast.makeText(this@Login, getString(R.string.blank), Toast.LENGTH_SHORT).show()
            }
        }

    }
}