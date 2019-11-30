package sv.edu.bitlab.ride

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import sv.edu.bitlab.ride.R
import sv.edu.bitlab.ride.fragments.dialogFragment.DialogFragmentClass

private var img_pastillas:ImageView? = null

private var btnIngresar: Button? = null
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        img_pastillas = findViewById(R.id.imageView2)
        img_pastillas!!.setImageResource(R.drawable.ic_pastillas2)

        btnIngresar = findViewById(R.id.button)

        btnIngresar!!.setOnClickListener{
            launchDialog()
        }

    }

    fun launchDialog(){
        val collectionFragment = DialogFragmentClass.newInstance()
        val builder = supportFragmentManager
            .beginTransaction()
        val dialog = DialogFragmentClass()
        dialog.show(builder,"")
    }
}
