package sv.edu.bitlab.ride.fragments.mapComponents

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*

import sv.edu.bitlab.ride.R

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import sv.edu.bitlab.ride.interfaces.OnFragmentInteractionListener
import sv.edu.bitlab.ride.models.Coordinates
import sv.edu.bitlab.ride.models.LatLang
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MapFragment : Fragment(),OnMapReadyCallback {

    private var firestoredb = FirebaseDatabase.getInstance().getReference("reservations")
    private lateinit var todayDate:String
    private lateinit var driverMaker:Marker
    private var listener: OnFragmentInteractionListener? = null
    private var framentView:View?=null
   private lateinit var coordinates: LatLang
    private var mMapView: MapView? = null
    private var googleMap: GoogleMap? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getDate()

        Log.d("PARAMS","latitude ->${coordinates.latitude}  Longitude ->${coordinates.longitude}")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =inflater.inflate(R.layout.fragment_map, container, false)
        framentView= view
        mMapView = view.findViewById(R.id.mapView)
        mMapView?.onCreate(savedInstanceState)
        mMapView?.getMapAsync(this)


        return view
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mMapView?.onSaveInstanceState(outState)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onResume() {
        super.onResume()
        mMapView?.onResume()
    }
    override fun onPause() {
        super.onPause()
        mMapView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView?.onDestroy()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        // For showing a move to my location button
        //googleMap.isMyLocationEnabled = true

        // For dropping a marker at a point on the Map
        val sydney = LatLng(13.707355, -89.251470)

        driverMaker= googleMap.addMarker(MarkerOptions().position(sydney).title("Driver").snippet("Unicomer Driver").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
            googleMap.addMarker(MarkerOptions().position(sydney).title("ME"))

        getLocation()

        // For zooming automatically to the location of the marker
        val cameraPosition = CameraPosition.Builder().target(sydney).zoom(17f).build() //19
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDate(){

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val formatted = current.format(formatter)
        todayDate=formatted
    }
    private fun getLocation(){


        firestoredb.child(todayDate).child("location").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()){

                    //dataSnapshot.children.forEach {  coord ->

                        val coordinate = dataSnapshot.getValue(LatLang::class.java)
                    val coor=LatLng(coordinate?.latitude!!,coordinate?.longitude!!)

                        Log.d("LATLANG-coordinate","$coordinate")
                       coordinates=coordinate
                    driverMaker.position= coor

                   // }

                    Log.d("LATLANG-DRIVER LOCATION","$coordinates")
                }else{

                    Log.d("LATLANG","NO EXISTE")

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })

    }

    companion object {

        @JvmStatic
        fun newInstance(coordinates: LatLang) :MapFragment{

            val fragment=MapFragment()
            fragment.coordinates =coordinates

            return fragment

        }
    }
}