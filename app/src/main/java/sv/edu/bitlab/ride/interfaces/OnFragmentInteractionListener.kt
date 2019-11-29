package sv.edu.bitlab.ride.interfaces

import sv.edu.bitlab.ride.FragmentsIndex

interface OnFragmentInteractionListener {
    fun onFragmentInteraction(index: FragmentsIndex)
    fun onFragmentInteraction(index: FragmentsIndex,obj:Any)
    fun listenTome()
}