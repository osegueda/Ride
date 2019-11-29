package sv.edu.bitlab.ride

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.core.Tag
import com.google.firebase.firestore.FirebaseFirestore
import sv.edu.bitlab.ride.models.User
import sv.edu.bitlab.ride.models.Usuario

class LoginActivity : AppCompatActivity() {


    private val mauth = FirebaseAuth.getInstance()
    private lateinit var username:User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginbtn = findViewById<View>(R.id.btn_login)

 requestPermissions()
        loginbtn.setOnClickListener {
            Toast.makeText(applicationContext,"CLICKER ON LGIN",Toast.LENGTH_LONG).show()
            login()
        }


        //verifica si el usuario dejo abierta la sesion para que
        // aparezca de una vez en el home de nuestra app
        val user = mauth.currentUser
        if (user != null){
            val intent = Intent(this, MainActivity::class.java)

            intent.putExtra("user",user)
            startActivity(intent)
            finish()
        }


    }

    private fun login(){
        val emailtext = findViewById<View>(R.id.edt_email_id) as EditText
        val pass = findViewById<View>(R.id.edt_password_id) as EditText

        val email = emailtext.text.toString()
        val password = pass.text.toString()



        Log.d("credentials","$email,$password")

        if (email.isNotEmpty() && password.isNotEmpty()) {
            Log.d("testing","inside if")
            mauth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener ( this) { task ->
                if (task.isSuccessful) {

                    Log.d("testing","inside task")
                    username=User(email,mauth.currentUser?.uid)
                    val userLoggedIn = mauth.currentUser
                    val sharedPreferences = getSharedPreferences("User details", Context.MODE_PRIVATE)
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putString("FirebaseUser", userLoggedIn!!.email)
                    editor.apply()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    Toast.makeText(applicationContext, "Incio sesión exitosamente :)", Toast.LENGTH_LONG).show()
                    Log.d("testing","success to log in")
                } else {
                    Log.d("testing","failed to log in -> ${task.exception}")

                    Toast.makeText(applicationContext, "Error en iniciar sesión :(", Toast.LENGTH_SHORT).show()
                    Log.d("testing","failed to log in")
                }
            }.addOnFailureListener { exception ->

                Log.d("LOGIN","ERROR -> $exception.message")
            }

        }else {
            Toast.makeText(applicationContext, "Porfavor complete las credenciales!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {

            PERMISSION_ID_COARSE_FINE_LOCATION-> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                    Log.d("PERMITIONS","GRANTED")
                } else {

                    Log.d("PERMITIONS","NOT GRANTED")
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID_COARSE_FINE_LOCATION
        )
    }
    override fun onBackPressed() {
        super.onBackPressed()
        //finishAffinity()
        finish()
    }


    companion object{
        private const val TAG = "LoginActivity"
    }
}
