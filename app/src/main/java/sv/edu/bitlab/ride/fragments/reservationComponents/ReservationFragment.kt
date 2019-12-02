package sv.edu.bitlab.ride.fragments.reservationComponents

import android.content.Context

import android.content.Intent

import android.net.ConnectivityManager

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.fragment_reservation.*
import kotlinx.android.synthetic.main.fragment_reservation.view.*


import sv.edu.bitlab.ride.R
import sv.edu.bitlab.ride.interfaces.OnFragmentInteractionListener
import sv.edu.bitlab.ride.models.User
import sv.edu.bitlab.ride.fragments.reservationComponents.recyclerview.ReservationAdapter

import sv.edu.bitlab.ride.APPLICATION_NAME

import sv.edu.bitlab.ride.LoginActivity
import sv.edu.bitlab.ride.RESERVATION_MAX_CAPACITY



import sv.edu.bitlab.ride.fragments.reservationComponents.recyclerview.ReservationViewHolder
import sv.edu.bitlab.unicomer.models.Reservation
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@Suppress("UNCHECKED_CAST")
class ReservationFragment : Fragment(), ReservationViewHolder.ReservationItemListener{
    private var listener: OnFragmentInteractionListener? = null
    private var firestoredb =FirebaseDatabase.getInstance().getReference("reservations")
    private var reservations:ArrayList<Reservation> ?=null
    private var active_reservations:ArrayList<Reservation> ?=null
    // private var db= FirebaseFirestore.getInstance()
    private var listView: RecyclerView?=null
    private var fragmentView:View?=null
    private lateinit  var today_date:String
    val calendar = Calendar.getInstance()
    private lateinit var user: User
    lateinit var  active_round:String
  //  private lateinit var lottieLoadingTransac:View
   // private lateinit var lottieRedBus:View
   // private lateinit var lottieYellowBus:View
  //  private lateinit var lottieBlueBus:View



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reservations= ArrayList()
        active_reservations= ArrayList()
            Log.d("ARGS","${this.arguments?.get("name")}")
        Log.d("USER-FRAG","$user")




    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_reservation, container, false)
        fragmentView=view
//        lottieBlueBus=view.findViewById(R.id.animation_view_bus_blue)
      //  lottieRedBus=view.findViewById(R.id.animation_view_bus_red)
     //   lottieYellowBus=view.findViewById(R.id.animation_view_bus_red)
        //lottieLoadingTransac=view.findViewById(R.id.animation_xml_transcation)
        return view
    }
  @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView=view.findViewById(R.id.recyclerview_reservation)
        listView?.layoutManager = LinearLayoutManager(this.context!!)

        listView?.adapter = ReservationAdapter(user.email!!,reservations!!,active_reservations!!,this,requireContext())
      //Lottie if connection doesn't exist
      if (checkInternet()){
          Log.d("CONNECTION","hay conexion")

      }else {
          Log.d("CONNECTION", "No hay conexion")
          var animacion = fragmentView?.findViewById<ConstraintLayout>(R.id.animation_xml)
          animacion?.visibility = View.GONE


          //moises animation pending
          //var animacion2 =fragmentView?.findViewById<ConstraintLayout>(R.id.lottie_connection)
          //  animacion2?.visibility=View.VISIBLE
      }
        getAllReservations()
        getActiveReservation()
        checkReservation()



    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }
    override fun onDetach() {
        super.onDetach()
        listener = null
    }
   @RequiresApi(Build.VERSION_CODES.O)
    private fun getAllReservations(){
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
      val formatted = current.format(formatter)

     /*   val year=calendar.get(Calendar.YEAR)
        val month=calendar.get(Calendar.MONTH)+1
        val day=calendar.get(Calendar.DAY_OF_MONTH)*/

        //val date = "$day-$month-$year"
     // Toast.makeText(requireContext(), date,Toast.LENGTH_LONG).show()
        today_date=formatted
       // today_date=date
      //  Log.d("DATE", formatted)
        firestoredb.child(today_date).child("rounds").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()){
                    reservations?.clear()
                    active_reservations?.clear()
                    dataSnapshot.children.forEach {reservation->
                        val reserv= reservation.getValue(Reservation::class.java)
                        reservations?.add(reserv!!)
                        Log.d("RESERVAS","$reserv")
                    }
                    // tengo que hacer aqui el checi de reservacion porque el que cambia es rounds
                    //         pasar como  parametro el arrray de reservations y chequear si esta el usar.
                    val adapter =listView?.adapter as ReservationAdapter
                    if (::active_round.isInitialized ){
                        Log.d("adapterRESERV","$reservations")
                        Log.d("adapterRESERVas", active_round)
                        active_reservations=reservations?.filter {reservation ->
                            reservation.id.equals(active_round)
                        } as ArrayList<Reservation>
                        Log.d("adapterRESERVas","$active_reservations")
                        adapter.reservations=active_reservations!!
                    }
                    adapter.notifyDataSetChanged()
                    Log.d("RESERV","$reservations")
                }else{
                    writeFirstRoundOfDay()
                    Log.d("RESERV","NO EXISTE")
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })
    }

    private fun checkInternet():Boolean{
        val cm = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        val isconnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
        var state= false
        if (isconnected){
            state=true
        }
        return state
    }

    private fun getActiveReservation(){
        lateinit var idActiveRound:HashMap<String,String>
        firestoredb.child(today_date).child("active_rounds").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.exists()){
                    active_reservations?.clear()
                    dataSnapshot.children.forEach {reservation->
                        idActiveRound= reservation.value!!as HashMap<String,String>
                        active_round= idActiveRound["id_round"]!!
                        Log.d("ACTIVE","${idActiveRound["id_round"]}")
                    }
                    val idRound= idActiveRound["id_round"]
                    active_reservations=reservations?.filter {reservation ->
                        reservation.id.equals(idRound)
                    } as ArrayList<Reservation>
                    val adapter =listView?.adapter as ReservationAdapter
                    adapter.reservations= active_reservations as ArrayList<Reservation>
                    adapter.notifyDataSetChanged()
                    val anim =fragmentView?.findViewById<ConstraintLayout>(R.id.animation_xml)
                    anim?.visibility=View.GONE

                    Log.d("ACTIVE_RESERVATIONS","$active_reservations")
                }else{
                    Log.d("ACTIVE","no existe")
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })

    }




    fun writeFirstRoundOfDay(){
        val reservationOfDay = Reservation(true,today_date,"",
            RESERVATION_MAX_CAPACITY,1,"7:00AM-9:00AM","available")
        firestoredb.child("$today_date/rounds").push().setValue(reservationOfDay)
    }
    fun writeNewRound(){

        val roundNumber= active_reservations!![0].round!!+1
        val newround = Reservation(true,today_date,"",
            RESERVATION_MAX_CAPACITY,roundNumber,"7:00AM-9:00AM","available")
        firestoredb.child("$today_date/active_rounds").removeValue()
        firestoredb.child("$today_date/rounds").push().setValue(newround)
        Log.d("NEW-ROUND","THE ACTIVE ROUND IS ->$roundNumber ")
    }
    private fun confirmReservation(round:String) {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Confirmar Reservacion")
            .setMessage("Desea Reservar??")
            .setPositiveButton("Reservar") { _, _ ->

              //  fragmentView?.findViewById<RecyclerView>(R.id.recyclerview_reservation)?.visibility=View.GONE
              // fragmentView?.findViewById<ConstraintLayout>(R.id.animation_xml_transcation)?.visibility=View.VISIBLE
                pushReservation()
                subscribe(round)

            }
            .setNegativeButton("Cancelar") { _, _ ->
                Toast.makeText(requireContext(), "No", Toast.LENGTH_LONG).show()
            }
        alertDialog.show()
    }
    private fun deleteReservation(round: String) {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Eliminar Reservacion")
            .setMessage("Desea Elimnar su reservacion?")
            .setPositiveButton("Eliminar") { _, _ ->
                updateUsers()
                unsubscribe(round)
            }
            .setNegativeButton("Cancelar") { _, _ ->
                Toast.makeText(requireContext(), "No", Toast.LENGTH_LONG).show()
            }
        alertDialog.show()
    }
    private fun checkReservation():Boolean{


        val state= reservations?.any { x->x.users.contains(user.email)}



        return  state!!
    }
    private fun pushReservation() {
        firestoredb.child(today_date).child("rounds").child(active_round).runTransaction(object : Transaction.Handler {

            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                val field = mutableData.getValue(Reservation::class.java)
                Log.d("CHILD","$field")




                if (field!!.users.contains(user.email)) {

                   Log.d("TRANSAC","SI ESTA")
                    Transaction.abort()
                } else {
                    if (field.pplsize!!>1){
                        field.pplsize = field.pplsize!! -1
                        field.users.add(user.email!!)



                    }else{
                        if (field.pplsize== 1){

                           field.pplsize=field.pplsize!!-1
                            field.users.add(user.email!!)
                           field.available=false

                            //writeNewRound()
                        }
                    }
                }
                // Set value and report transaction success
                mutableData.value = field
                return Transaction.success(mutableData)
            }

            override fun onComplete(
                databaseError: DatabaseError?,
                b: Boolean,
                dataSnapshot: DataSnapshot?
            ) {
                // Transaction completed
                 //  Log.d("TRANSACTION COMPLETE", "postTransaction:onComplete: ${databaseError?.message}" )
              //  Log.d("TRANSACTION COMPLETE", "postTransaction:onComplete: ${databaseError?.details}" )
               // Log.d("TRANSACTION boolean", "Boolean-> $b" )
                Log.d("TRANSACTION snap", "snap-> $dataSnapshot" )
                val snap=dataSnapshot?.getValue(Reservation::class.java)
                val state= snap?.users?.any { x->x.contains(user.email!!)}

                if (state!!){

                    fragmentView?.findViewById<ConstraintLayout>(R.id.animation_xml_transcation)?.visibility=View.GONE
                    fragmentView?.findViewById<RecyclerView>(R.id.recyclerview_reservation)?.visibility=View.VISIBLE
                    Snackbar.make(requireView(), "Reservation added successfully", Snackbar.LENGTH_LONG)
                        .setAction("Ok") {  }.show()

                }else{
                     fragmentView?.findViewById<RecyclerView>(R.id.recyclerview_reservation)?.visibility=View.GONE
                    fragmentView?.findViewById<ConstraintLayout>(R.id.animation_xml_transcation)?.visibility=View.VISIBLE
                    pushReservation()
                   /* Snackbar.make(requireView(), "Server Error: Please try again", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Retry") { pushReservation() }.show()*/

                }



            }
        })
    }
    private fun updateUsers() {

        val email=user.email
        val round=getUserRound(email!!)
    Log.d("GETUSER","THE ID THE ROUND IN USER IS -> $round")
        firestoredb.child(today_date).child("rounds").child(round).runTransaction(object : Transaction.Handler {
            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                val p = mutableData.getValue(Reservation::class.java)
                Log.d("CHILD","$p")


                if (p!!.users.contains(user.email)) {

                    p.pplsize=p.pplsize!!+1
                    p.users.remove(user.email)
                    p.available=true
                } else {
                    Log.d("UPDATE","NO CONTIENE USER")
                }
                // Set value and report transaction success
                mutableData.value = p
                return Transaction.success(mutableData)
            }
            override fun onComplete(
                databaseError: DatabaseError?,
                b: Boolean,
                dataSnapshot: DataSnapshot?
            ) {
                // Transaction completed
                //   Log.d("TRANSACTION COMPLETE", "postTransaction:onComplete:" + databaseError!!)
            }
        })
    }
    override fun onItemClickReservation(position: Int,round_status:String,round: String) {
        if (checkReservation()){
            /* Snackbar.make(requireView(), "you have already reserved", Snackbar.LENGTH_LONG)
                 .setAction("OK") {  }.show()*/
            //Toast.makeText(requireContext(),"status->$round_status",Toast.LENGTH_LONG).show()
            when(round_status){


                "ongoing"->{

                     Snackbar.make(requireView(), "you cant cancel now, the round is ongoing", Snackbar.LENGTH_LONG)
                 .setAction("OK") {  }.show()
                }
                "available"->{
                    deleteReservation(round)
                }
                "finished"->{ Snackbar.make(requireView(), "Your round is already finished, Thank you for using unicomer ride", Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK") {  }.show()}
            }

        }else{
          //  Toast.makeText(requireContext(),"Card #$position",Toast.LENGTH_LONG).show()
            confirmReservation(round)
        }
    }

    private fun getUserRound(user:String):String{
        val reserv=reservations?.filter{reservation ->

            reservation.users.contains(user)

        }

        return reserv?.get(0)?.id.toString()
    }

     private fun subscribe(round:String){

        Log.d("NOTIFICATION", "Subscribing to round-> $round")
        // [START subscribe_topics]
        FirebaseMessaging.getInstance().subscribeToTopic("round$round")
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

     private fun unsubscribe(round:String){

        Log.d("NOTIFICATION", "unsubscribe to round->$round")
        // [START subscribe_topics]
        FirebaseMessaging.getInstance().unsubscribeFromTopic("round$round")
            .addOnCompleteListener { task ->
                var msg = "UNSUSCRIPTION SUCCESS"
                if (!task.isSuccessful) {
                    msg = "UNSUSCRIPTION FAILED"
                }
                Log.d("NOTIFICATION", msg)
                //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
        // [END subscribe_topics]



    }
    
    companion object {
        @JvmStatic
        fun newInstance(user:User) : ReservationFragment{

            val fragment = ReservationFragment()
            fragment.user =user
            return fragment

        }




    }
}