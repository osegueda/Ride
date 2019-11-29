package sv.edu.bitlab.ride

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {






    private val mauth = FirebaseAuth.getInstance()






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        /*val user = mauth.currentUser
        if (user != null){
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("user",user)
            startActivity(intent)
            finish()
        }*/

       // requestPermissions()
        val background = object : Thread(){
            override fun run() {
                try {
                    sleep(3000)
                    val intent = Intent(baseContext,LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
        background.start()
    }





}
