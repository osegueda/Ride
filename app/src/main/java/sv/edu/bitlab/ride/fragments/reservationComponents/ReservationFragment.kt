package sv.edu.bitlab.ride.fragments.reservationComponents
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*


import sv.edu.bitlab.ride.R
import sv.edu.bitlab.ride.interfaces.OnFragmentInteractionListener
import sv.edu.bitlab.ride.models.User
import sv.edu.bitlab.tarea6.ordenHistorial.recyclerView.ReservationAdapter
import sv.edu.bitlab.ride.fragments.reservationComponents.recyclerview.ReservationViewHolder
import sv.edu.bitlab.unicomer.models.Reservation
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
@Suppress("UNCHECKED_CAST")
class ReservationFragment : Fragment(), ReservationViewHolder.ReservationItemListener{
    private var listener: OnFragmentInteractionListener? = null
    private var firestoredb =FirebaseDatabase.getInstance().getReference("reservations")
    private var reservations:ArrayList<Reservation> ?=null
    private var active_reservations:ArrayList<Reservation> ?=null
    // private var db= FirebaseFirestore.getInstance()
    private var listView: RecyclerView?=null
    private lateinit  var today_date:String
    private lateinit var user: User
    lateinit var  active_round:String
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reservations= ArrayList()
        active_reservations= ArrayList()

        Log.d("USER-FRAG","$user")




    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_reservation, container, false)
        return view
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView=view.findViewById(R.id.recyclerview_reservation)
        listView?.layoutManager = LinearLayoutManager(this.context!!)

        listView?.adapter = ReservationAdapter(user.email!!,reservations!!,active_reservations!!,this,requireContext())

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
        today_date=formatted
        Log.d("DATE", formatted)
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
        val reservationOfDay = Reservation(true,today_date,"",11,1,"7:00AM-9:00AM","available")
        firestoredb.child("$today_date/rounds").push().setValue(reservationOfDay)
    }
    fun writeNewRound(){

        val roundNumber= active_reservations!![0].round!!+1
        val newround = Reservation(true,today_date,"",11,roundNumber,"7:00AM-9:00AM","available")
        firestoredb.child("$today_date/active_rounds").removeValue()
        firestoredb.child("$today_date/rounds").push().setValue(newround)
        Log.d("NEW-ROUND","THE ACTIVE ROUND IS ->$roundNumber ")
    }
    private fun confirmReservation() {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Confirmar Reservacion")
            .setMessage("Desea Reservar??")
            .setPositiveButton("Reservar") { _, _ ->
                pushReservation()
            }
            .setNegativeButton("Cancelar") { _, _ ->
                Toast.makeText(requireContext(), "No", Toast.LENGTH_LONG).show()
            }
        alertDialog.show()
    }
    private fun deleteReservation() {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Eliminar Reservacion")
            .setMessage("Desea Elimnar su reservacion?")
            .setPositiveButton("Eliminar") { _, _ ->
                updateUsers()
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

                            writeNewRound()
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
                   Log.d("TRANSACTION COMPLETE", "postTransaction:onComplete: ${databaseError.toString()}" )
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
    override fun onItemClickReservation(position: Int) {
        if (checkReservation()){
            /* Snackbar.make(requireView(), "you have already reserved", Snackbar.LENGTH_LONG)
                 .setAction("OK") {  }.show()*/
            deleteReservation()
        }else{
            Toast.makeText(requireContext(),"Card #$position",Toast.LENGTH_LONG).show()
            confirmReservation()
        }
    }

    private fun getUserRound(user:String):String{
        val reserv=reservations?.filter{reservation ->

            reservation.users.contains(user)

        }

        return reserv?.get(0)?.id.toString()
    }
    override fun onItemClickDetalle(btn_detalle: Button, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    override fun onTextInput(input: String, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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