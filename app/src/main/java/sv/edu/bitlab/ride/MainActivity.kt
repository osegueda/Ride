package sv.edu.bitlab.ride

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import sv.edu.bitlab.ride.fragments.reservationComponents.ReservationFragment
import sv.edu.bitlab.ride.fragments.locationComponents.LocationFragment
import sv.edu.bitlab.ride.fragments.recordComponents.RecordFragment
import sv.edu.bitlab.ride.fragments.notificationsFragment.NotificationFragment
import sv.edu.bitlab.ride.interfaces.OnFragmentInteractionListener

class MainActivity : AppCompatActivity(),OnFragmentInteractionListener{


    var fbAuth = FirebaseAuth.getInstance()
    private var listener:OnFragmentInteractionListener?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listener=this

        init()


        fbAuth.addAuthStateListener {
            if(fbAuth.currentUser == null){
                this.finish()
            }
        }
    }


    override fun onFragmentInteraction(index: FragmentsIndex) {

        val transaction = supportFragmentManager.beginTransaction()
        var fragment: Fragment? = null

        when(index){

            FragmentsIndex.KEY_FRAGMENT_RESERVATION->{
                fragment= ReservationFragment.newInstance()
                transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                    android.R.anim.slide_in_left, android.R.anim.slide_out_right)

            }

            FragmentsIndex.KEY_FRAGMENT_RECORD->{
                fragment = RecordFragment.newInstance()
                transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                    android.R.anim.fade_in, android.R.anim.fade_out)

            }

            FragmentsIndex.KEY_FRAGMENT_LOCATION->{
            fragment = LocationFragment.newInstance()
            transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                android.R.anim.fade_in, android.R.anim.fade_out)


        }
            FragmentsIndex.KEY_FRAGMENT_NOTIFICATIONS->{
                fragment = NotificationFragment.newInstance()
                transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                    android.R.anim.fade_in, android.R.anim.fade_out)


            }


        }
        transaction
            .replace(R.id.container_fragments, fragment)
            .commit()
    }

    override fun listenTome() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private  fun init(){
        supportFragmentManager
            .beginTransaction()
            .add(R.id.container_fragments, ReservationFragment.newInstance())
            .commit()

        this.findViewById<LinearLayout>(R.id.container_layout_reservation)
            .setOnClickListener {
                Toast.makeText(this, "Reservations!", Toast.LENGTH_SHORT).show()
                // listener?.listenTome()
                listener?.onFragmentInteraction(FragmentsIndex.KEY_FRAGMENT_RESERVATION)

            }

        this.findViewById<LinearLayout>(R.id.container_layout_settings)
            .setOnClickListener {
                Toast.makeText(this, "Settings!", Toast.LENGTH_SHORT).show()
                //listener?.listenTome()
                listener?.onFragmentInteraction(FragmentsIndex.KEY_FRAGMENT_RECORD)
            }
        this.findViewById<LinearLayout>(R.id.container_layout_location)
            .setOnClickListener{
                Toast.makeText(this, "Location!", Toast.LENGTH_SHORT).show()
                //listener?.listenTome()
                listener?.onFragmentInteraction(FragmentsIndex.KEY_FRAGMENT_LOCATION)
            }
        this.findViewById<LinearLayout>(R.id.container_layout_notifications)
            .setOnClickListener{
                Toast.makeText(this, "Notification!", Toast.LENGTH_SHORT).show()
                //listener?.listenTome()
                listener?.onFragmentInteraction(FragmentsIndex.KEY_FRAGMENT_NOTIFICATIONS)
            }

    }

    ///MENU Y OPCION LOGOUT

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.my_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.logout_action -> {

            /*val builder = AlertDialog.Builder(this@MainActivity)

            // Set the alert dialog title
            builder.setTitle("Log out")
            // Display a message on alert dialog
            builder.setMessage("Desea cerrar sesion?")

            builder.setPositiveButton("YES"){dialog, which ->
                signOut()
                startActivity(Intent(this, LoginActivity::class.java))
            }
            builder.setNegativeButton("No"){dialog,which ->
                Toast.makeText(applicationContext,"Ok",Toast.LENGTH_SHORT).show()
            }*/
            signOut()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    fun signOut(){
        fbAuth.signOut()

    }


}
