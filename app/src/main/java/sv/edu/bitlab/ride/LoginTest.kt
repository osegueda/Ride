package sv.edu.bitlab.ride

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_login_test.*
import sv.edu.bitlab.ride.models.User

class LoginTest : AppCompatActivity() {

    var email:EditText?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_test)
        email=findViewById(R.id.email)

        login_btn.setOnClickListener {
            val emailaux=email?.text.toString()+".@unicomer.com"
            val user=User(emailaux,"123")

            val intent= Intent(this,MainActivity::class.java)
            intent.putExtra("user",user)
            startActivity(intent)



        }



    }


}
