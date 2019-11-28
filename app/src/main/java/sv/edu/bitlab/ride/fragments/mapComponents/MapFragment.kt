package sv.edu.bitlab.ride.fragments.mapComponents

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.*

import sv.edu.bitlab.ride.R
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.MarkerOptions

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import sv.edu.bitlab.ride.interfaces.OnFragmentInteractionListener
import sv.edu.bitlab.ride.models.Coordinates


class MapFragment : Fragment(),OnMapReadyCallback {


    private lateinit var driverMaker:Marker
    private var listener: OnFragmentInteractionListener? = null
    private var framentView:View?=null
   private lateinit var coordinates: Coordinates
    private var mMapView: MapView? = null
    private var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        driverMaker= googleMap.addMarker(MarkerOptions().position(sydney).title("BITLAB").snippet("ELANIIN TECH COMPANY"))




        // For zooming automatically to the location of the marker
        val cameraPosition = CameraPosition.Builder().target(sydney).zoom(19f).build()
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    companion object {

        @JvmStatic
        fun newInstance(coordinates: Coordinates) :MapFragment{

            val fragment=MapFragment()
            fragment.coordinates =coordinates

            return fragment

        }
    }
}