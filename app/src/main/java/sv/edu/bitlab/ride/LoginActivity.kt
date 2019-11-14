package sv.edu.bitlab.ride

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import sv.edu.bitlab.ride.R

private var img_pastillas:ImageView? = null
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        img_pastillas = findViewById(R.id.imageView2)
        img_pastillas!!.setImageResource(R.drawable.ic_pastillas)

    }
}
