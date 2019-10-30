package sv.edu.bitlab.ride

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import sv.edu.bitlab.ride.fragments.locationComponents.DashboardFragment
import sv.edu.bitlab.ride.fragments.locationComponents.LocationFragment
import sv.edu.bitlab.ride.fragments.locationComponents.SettingsFragment
import sv.edu.bitlab.ride.interfaces.OnFragmentInteractionListener
import sv.edu.bitlab.unicomer.models.Reservation

class MainActivity : AppCompatActivity(),OnFragmentInteractionListener{



    private var listener:OnFragmentInteractionListener?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listener=this

        init()
    }


    override fun onFragmentInteraction(index: FragmentsIndex) {

        val transaction = supportFragmentManager.beginTransaction()
        var fragment: Fragment? = null

        when(index){

            FragmentsIndex.KEY_FRAGMENT_DASHBOARD->{
                fragment=DashboardFragment.newInstance()
                transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                    android.R.anim.slide_in_left, android.R.anim.slide_out_right)

            }

            FragmentsIndex.KEY_FRAGMENT_SETTINGS->{
                fragment = SettingsFragment.newInstance()
                transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                    android.R.anim.fade_in, android.R.anim.fade_out)

            }

            FragmentsIndex.KEY_FRAGMENT_LOCATION->{
                fragment = LocationFragment.newInstance()
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
            .add(R.id.container_fragments, DashboardFragment.newInstance())
            .commit()

        this.findViewById<LinearLayout>(R.id.container_layout_dashboard)
            .setOnClickListener {
                Toast.makeText(this, "Dashboard!", Toast.LENGTH_SHORT).show()
                // listener?.listenTome()
                listener?.onFragmentInteraction(FragmentsIndex.KEY_FRAGMENT_DASHBOARD)

            }

        this.findViewById<LinearLayout>(R.id.container_layout_settings)
            .setOnClickListener {
                Toast.makeText(this, "Settings!", Toast.LENGTH_SHORT).show()
                //listener?.listenTome()
                listener?.onFragmentInteraction(FragmentsIndex.KEY_FRAGMENT_SETTINGS)
            }
        this.findViewById<LinearLayout>(R.id.container_layout_location)
            .setOnClickListener{
                Toast.makeText(this, "Location!", Toast.LENGTH_SHORT).show()
                //listener?.listenTome()
                listener?.onFragmentInteraction(FragmentsIndex.KEY_FRAGMENT_LOCATION)
            }




    }


}
