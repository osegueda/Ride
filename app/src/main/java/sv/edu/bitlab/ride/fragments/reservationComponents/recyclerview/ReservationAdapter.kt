import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import sv.edu.bitlab.ride.R
import sv.edu.bitlab.ride.fragments.reservationComponents.recyclerview.ReservationViewHolder
import sv.edu.bitlab.unicomer.models.Reservation


class ReservationAdapter(var user:String, var userReservations:ArrayList<Reservation>, var reservations:ArrayList<Reservation>, val listener: ReservationViewHolder.ReservationItemListener, var context:Context
) : RecyclerView.Adapter<ReservationViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_row_rsv, parent, false)
        return ReservationViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        holder.bindData()

        holder.date_txt?.visibility= View.GONE
        holder.status_txt?.visibility=View.GONE
        holder.line1?.visibility=View.GONE
        holder.line2?.visibility=View.GONE
        holder.id_txt?.text = context.resources.getString(
            R.string.two_format_string,
            "ID:",
            reservations[position].id
        )
        holder.round_txt?.text = context.resources.getString(
            R.string.two_format_string,
            "Round Number:",
            reservations[position].round.toString()
        )
        holder.schedule_txt?.text = context.resources.getString(
            R.string.two_format_string,
            "Schedule:",
            reservations[position].schedule
        )
        holder.count_txt?.text = context.resources.getString(
            R.string.two_format_string,
            "Available Slots:",
            reservations[position].pplsize.toString()
        )
        Log.d("backgorund","$user, $reservations")
        if (userReservations.any { reservation->reservation.users.contains(user)
                .apply {  holder.result_number?.text=reservation.round.toString()

                    holder.buttonrsv?.setOnClickListener{

                        listener.onItemClickReservation(position,reservation.round_status!!)

                    }

                    when(reservation.round_status){

                        "finished"->{
                            holder.card1?.visibility=View.GONE
                            holder.card2?.visibility=View.VISIBLE
                            //holder.card2?.setBackgroundColor(ContextCompat.getColor(context,android.R.color.holo_red_dark))
                            holder.card2?.background?.setColorFilter(Color.parseColor("#EB1B1B"), PorterDuff.Mode.SRC_ATOP)
                            holder.result_txt?.text=context.getString(R.string.reservation_finished)
                            holder.result_txt?.textSize=20.0F

                        }

                        "available"->{
                            Log.d("USER","SI ESTA")
                            holder.card1?.visibility=View.GONE
                            //holder.buttonrsv?.text=context.resources.getString(R.string.reservado)
                            //holder.card2?.visibility=View.VISIBLE
                            //holder.card2?.setBackgroundColor(ContextCompat.getColor(context,android.R.color.holo_green_dark))
                            holder.card2?.background?.setColorFilter(Color.parseColor("#ff669900"), PorterDuff.Mode.SRC_ATOP)
                            holder.result_txt?.text=context.getString(R.string.reservation_result)
                            holder.result_txt?.textSize=20.0F
                        }

                        "ongoing"->{

                            holder.card1?.visibility=View.GONE
                            holder.card2?.visibility=View.VISIBLE
                            //holder.card2?.setBackgroundColor(ContextCompat.getColor(context,R.color.yellow))
                            holder.card2?.background?.setColorFilter(Color.parseColor("#FFC107"), PorterDuff.Mode.SRC_ATOP)
                            holder.result_txt?.text=context.getString(R.string.reservation_begun)
                            holder.result_txt?.textSize=20.0F
                        }

                    }

                } }) {
            Log.d("USER","SI ESTA")

        } else {
            //holder.container!!.setBackgroundColor(ContextCompat.getColor(context,android.R.color.holo_red_dark))
            holder.card1?.visibility=View.VISIBLE
            holder.card2?.visibility=View.GONE
            Log.d("USER","NO ESTA")
        }

    }


    override fun getItemCount(): Int {
        Log.d("SIZE", "${reservations.size}")
        return reservations.size

    }
}



