package sv.edu.bitlab.ride

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.core.Tag
import com.google.firebase.firestore.FirebaseFirestore
import sv.edu.bitlab.ride.models.Usuario

class LoginActivity : AppCompatActivity() {

    private val mauth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginbtn = findViewById<View>(R.id.btn_login)
        val register = findViewById<View>(R.id.btn_registrar)

        loginbtn.setOnClickListener(View.OnClickListener{
            view -> login()
        })

        register.setOnClickListener(View.OnClickListener{
                view -> registerUser()
        })

        //verifica si el usuario dejo abierta la sesion para que
        // aparezca de una vez en el home de nuestra app
        val user = mauth!!.currentUser
        if (user != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    private fun login(){
        var emailtext = findViewById<View>(R.id.edt_email_id) as EditText
        var pass = findViewById<View>(R.id.edt_password_id) as EditText

        var email = emailtext.text.toString()
        var password = pass.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            this.mauth.signInWithEmailAndPassword(email, password).addOnCompleteListener ( this, OnCompleteListener<AuthResult> { task ->
                if (task.isSuccessful) {

                    val userLoggedIn = mauth.currentUser
                    val sharedPreferences = getSharedPreferences("User details", Context.MODE_PRIVATE)
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putString("FirebaseUser", userLoggedIn!!.email)
                    editor.apply()

                    startActivity(Intent(this, MainActivity::class.java))
                    Toast.makeText(this, "Incio sesión exitosamente :)", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Error en iniciar sesión :(", Toast.LENGTH_SHORT).show()
                }
            })

        }else {
            Toast.makeText(this, "Porfavor complete las credenciales!", Toast.LENGTH_SHORT).show()
        }
    }


    private fun registerUser () {
        val emailtext = findViewById<View>(R.id.edt_email_id) as EditText
        val passwordtext = findViewById<View>(R.id.edt_password_id) as EditText
        val nameTxt = findViewById<View>(R.id.edt_nombre) as EditText
        val apellidoTxt = findViewById<View>(R.id.edt_apellidos) as EditText

        var email = emailtext.text.toString()
        var password = passwordtext.text.toString()
        var name = nameTxt.text.toString()
        var apellido = apellidoTxt.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty() && apellido.isNotEmpty()) {
            mauth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener { task ->
                if (task.isSuccessful) {

                    //se puede obtener el id y el email del usuario ya creado
                    val user = mauth.currentUser
                    val uid = user!!.uid
                    //aqui se guarda los datos del usuario en la collection firestore
                    saveInCollectionUserData(apellido,name,email)
                    Toast.makeText(this, "Registrado exitosamente :)", Toast.LENGTH_LONG).show()
                }else {
                    Toast.makeText(this, "Error en registrar, intente de nuevo :(", Toast.LENGTH_LONG).show()
                }
            })
        }else {
            Toast.makeText(this,"Porfavor complete las credenciales!", Toast.LENGTH_LONG).show()
        }
    }


    private fun saveInCollectionUserData(lastname: String, name: String, email: String){
        //dataclass
        val data = Usuario(lastname,name,email)

        //se manda a la collection el dataclass
        db.collection("users")
            .add(data)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    companion object{
        private const val TAG = "LoginActivity"
    }
}
