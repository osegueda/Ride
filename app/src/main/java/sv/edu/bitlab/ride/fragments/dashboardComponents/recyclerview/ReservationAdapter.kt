package sv.edu.bitlab.tarea6.ordenHistorial.recyclerView

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import sv.edu.bitlab.ride.R
import sv.edu.bitlab.unicomer.models.Reservation


class ReservationAdapter( var user:String,var userReservations:ArrayList<Reservation>,var reservations:ArrayList<Reservation>, val listener: ReservationViewHolder.ReservationItemListener,var context:Context
) : RecyclerView.Adapter<ReservationViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_row_reservation, parent, false)
        return ReservationViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        holder.bindData()
        holder.id_txt?.text = context.resources.getString(
            R.string.two_format_string,
            "ID:",
            reservations.get(position).id
        )
        holder.round_txt?.text = context.resources.getString(
            R.string.two_format_string,
            "Round:",
            reservations.get(position).round.toString()
        )
        holder.schedule_txt?.text = context.resources.getString(
            R.string.two_format_string,
            "Schedule:",
            reservations.get(position).schedule
        )
        holder.count_txt?.text = context.resources.getString(
            R.string.two_format_string,
            "Available:",
            reservations.get(position).pplsize.toString()
        )
        Log.d("backgorund","$user, $reservations")
        if (userReservations?.any { reservation->reservation.users.contains(user)}) {
            holder.container!!.setBackgroundColor(context.resources.getColor(android.R.color.holo_green_dark))
            Log.d("USER","SI ESTA")
        } else {
            holder.container!!.setBackgroundColor(context.resources.getColor(android.R.color.holo_red_light))
            Log.d("USER","NO ESTA")
        }

    }


    override fun getItemCount(): Int {
        Log.d("SIZE", "${reservations.size}")
        return reservations.size

    }
}



