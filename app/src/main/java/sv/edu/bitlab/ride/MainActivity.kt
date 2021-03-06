package sv.edu.bitlab.ride

import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import sv.edu.bitlab.ride.fragments.reservationComponents.ReservationFragment
import sv.edu.bitlab.ride.fragments.locationComponents.LocationFragment
import sv.edu.bitlab.ride.fragments.recordComponents.RecordFragment
import sv.edu.bitlab.ride.interfaces.OnFragmentInteractionListener
import sv.edu.bitlab.ride.models.User

class MainActivity : AppCompatActivity(),OnFragmentInteractionListener{


    private lateinit var user: User
    private var listener:OnFragmentInteractionListener?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        user = if (savedInstanceState!=null){

            savedInstanceState.getParcelable("reuser")!!
        }else{
            intent.extras?.getParcelable("user")!!
        }
        setContentView(R.layout.activity_main)
        listener=this

        Log.d("USER-MAIN","$user")
        init()
        notifications()
        getToken()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable("reuser",user)
    }




    override fun onFragmentInteraction(index: FragmentsIndex) {

        val transaction = supportFragmentManager.beginTransaction()
        var fragment: Fragment? = null

        when(index){

            FragmentsIndex.KEY_FRAGMENT_RESERVATION->{
                fragment= ReservationFragment.newInstance(user)
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
                fragment = RecordFragment.newInstance()
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
            .add(R.id.container_fragments, ReservationFragment.newInstance(user))
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

    private fun notifications(){

        Log.d("NOTIFICATION", "Subscribing to service topic")
        // [START subscribe_topics]
        FirebaseMessaging.getInstance().subscribeToTopic("service")
            .addOnCompleteListener { task ->
                var msg = "SUSCRIPTION SUCCESS"
                if (!task.isSuccessful) {
                    msg = "SUSCRIPTION FAILED"
                }
                Log.d("NOTIFICATION", msg)
                //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
        // [END subscribe_topics]

    }

    private fun getToken(){

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("TOKEN", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast
                val msg = getString(R.string.msg_token_fmt, token)
                Log.d("TOKEN", msg)
               // Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            })
        // [END retrieve_current_token]

    }


}
