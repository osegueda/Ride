package sv.edu.bitlab.ride.fragments.mapComponents

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*

import sv.edu.bitlab.ride.R

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import sv.edu.bitlab.ride.PERMISSION_ID_COARSE_FINE_LOCATION
import sv.edu.bitlab.ride.interfaces.OnFragmentInteractionListener
import sv.edu.bitlab.ride.models.LatLang
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MapFragment : Fragment(),OnMapReadyCallback {

    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    private var firestoredb = FirebaseDatabase.getInstance().getReference("reservations")
    private lateinit var todayDate:String
    private lateinit var driverMaker:Marker
    private lateinit var userMaker:Marker
    private var listener: OnFragmentInteractionListener? = null
    private var framentView:View?=null
   private lateinit var coordinates: LatLang
    private var mMapView: MapView? = null
    private var googleMap: GoogleMap? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getDate()
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())


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
        getLastLocation()


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
        val elaniin = LatLng(13.707355, -89.251470)

        //driverMaker= googleMap.addMarker(MarkerOptions().position(sydney).title("Driver").snippet("Unicomer Driver").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
        driverMaker= googleMap.addMarker(MarkerOptions().position(elaniin).title("Driver").snippet("Unicomer Driver").icon(BitmapDescriptorFactory.fromResource(R.drawable.bus4)))


        getLocation()

        // For zooming automatically to the location of the marker
        val cameraPosition = CameraPosition.Builder().target(elaniin).zoom(15f).build() //19
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDate(){

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val formatted = current.format(formatter)
        todayDate=formatted
    }


    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        val coordinate= LatLng(location.latitude,location.longitude)
                        userMaker= googleMap!!.addMarker(MarkerOptions().position(coordinate).title("ME"))
                            //userMaker.position = LatLng(location.latitude, location.longitude)

                        Log.d("LOCATION-LONG-LAST","${location.longitude}")
                        Log.d("LOCATION-LAT-LAST","${location.latitude}")


                    }
                }
            } else {
                Toast.makeText(requireContext(), "Turn on location", Toast.LENGTH_LONG).show()
                // val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                //startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID_COARSE_FINE_LOCATION
        )
    }

    private fun getLocation(){


        firestoredb.child(todayDate).child("location").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()){

                    //dataSnapshot.children.forEach {  coord ->

                        val coordinate = dataSnapshot.getValue(LatLang::class.java)
                    val coor=LatLng(coordinate?.latitude!!, coordinate.longitude!!)

                        Log.d("LATLANG-coordinate","$coordinate")
                       coordinates=coordinate
                    driverMaker.position= coor
                    val cameraPosition = CameraPosition.Builder().target(coor).zoom(16f).build() //19
                    googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

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
    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 5000
        mLocationRequest.fastestInterval = 1000
        mLocationRequest.numUpdates = 5

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation

            val coordinate= LatLang(mLastLocation.latitude,mLastLocation.longitude)
            userMaker.position= LatLng(mLastLocation.latitude,mLastLocation.longitude)
          //  Toast.makeText(requireContext(),"lat->${mLastLocation.latitude} lang->${mLastLocation.longitude} ",Toast.LENGTH_LONG).show()
            Log.d("LOCATION-LONG","${mLastLocation.longitude}")
            Log.d("LOCATION-LAT","${mLastLocation.latitude}")
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =  requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_ID_COARSE_FINE_LOCATION-> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                    Log.d("PERMITIONS","GRANTED")
                } else {

                    Log.d("PERMITIONS","NOT GRANTED")
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
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