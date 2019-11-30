package sv.edu.bitlab.ride.fragments.recordComponents

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import sv.edu.bitlab.ride.R
import sv.edu.bitlab.ride.fragments.recordComponents.recyclerview.RecordAdapter
import sv.edu.bitlab.ride.fragments.recordComponents.recyclerview.RecordViewHolder
import sv.edu.bitlab.ride.interfaces.OnFragmentInteractionListener
import sv.edu.bitlab.ride.models.UserHistory


class RecordFragment : Fragment(),RecordViewHolder.RecordItemListener {
    override fun onItemClickRecord(position: Int) {

    }


    private var listener: OnFragmentInteractionListener? = null
    private var db=FirebaseFirestore.getInstance()
    private lateinit var history:ArrayList<UserHistory>
    private val mauth = FirebaseAuth.getInstance()
    private lateinit var user:String
    private var listview:RecyclerView?=null
    private var fragmentView:View?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        history= ArrayList()
        user=mauth.currentUser?.email.toString()
       // Log.d("USER","${mauth.currentUser?.email}")
         Log.d("USER", user)
        Log.d("INIT","the arrayList is -> $history")
    //Toast.makeText(requireContext(),"im in record",Toast.LENGTH_LONG).show()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_record, container, false)
        fragmentView=view
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       listview=view.findViewById(R.id.record_recycler)
        listview?.layoutManager = LinearLayoutManager(this.context!!)
        listview?.adapter= RecordAdapter(history,this,requireContext())
        getReservations()






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

    private fun getReservations(){

       db.collection("users").document(user).collection("reservations")
        //db.collectionGroup("reservations")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("ALL-RES", "${document.id} => ${document.data}")

                    val rsv = document.toObject(UserHistory::class.java)
                    history.add(rsv)

                }
                val adapter =listview?.adapter as RecordAdapter
                adapter.history=history
                adapter.notifyDataSetChanged()


                Log.d("RESULT-ARRAYLIST", "THE ARRAY IS ->$history")


            }
            .addOnFailureListener { exception ->
                Log.d("ALL-RES", "Error getting documents: ", exception)
            }




    }


    companion object {

        @JvmStatic
        fun newInstance() = RecordFragment()
    }
}
