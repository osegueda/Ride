package sv.edu.bitlab.ride

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    private val mauth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        window.statusBarColor = Color.TRANSPARENT
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
                    sleep(2000)
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
