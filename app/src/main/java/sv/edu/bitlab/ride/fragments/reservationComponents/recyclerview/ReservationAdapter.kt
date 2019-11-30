
package sv.edu.bitlab.ride.fragments.reservationComponents.recyclerview


import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import sv.edu.bitlab.ride.R
import sv.edu.bitlab.unicomer.models.Reservation


class ReservationAdapter(var user:String, var userReservations:ArrayList<Reservation>, var reservations:ArrayList<Reservation>, val listener: ReservationViewHolder.ReservationItemListener, var context:Context
) : RecyclerView.Adapter<ReservationViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_row_card_rsv, parent, false)
        return ReservationViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        holder.bindData()

        /*holder.id_txt?.text = context.resources.getString(
            R.string.two_format_string,
            "ID:",
            reservations[position].id
        )*/
        /*holder.round_txt?.text = context.resources.getString(
            R.string.two_format_string,
            "Viaje N° ",
            reservations[position].round.toString()
        )*/
        holder.schedule_txt?.text = context.resources.getString(
            R.string.two_format_string,
            "",
            reservations[position].schedule
        )
        holder.count_txt?.text = context.resources.getString(
            R.string.two_format_string,
            "",
            reservations[position].pplsize.toString()
        )
        Log.d("backgorund","$user, $reservations")
        if (userReservations.any { reservation->reservation.users.contains(user)
                .apply {    holder.round_txt?.text = context.resources.getString(
                            R.string.two_format_string,
                            "Viaje N° ",
                            reservation.round.toString()
                        )
                        holder.id_txt_cupo?.visibility=View.INVISIBLE
                    holder.buttonrsv?.setOnClickListener{

                        listener.onItemClickReservation(position,reservation.round_status!!)


                    }

                    when(reservation.round_status){

                        "finished"->{
                            holder.image?.setAnimation("bus_red.json")
                            holder.buttonrsv?.visibility=View.INVISIBLE
                            holder.buttonrsv?.text=context.resources.getString(R.string.reservado)
                            holder.buttonrsv?.background?.setColorFilter(Color.parseColor("#EE0909"), PorterDuff.Mode.SRC_ATOP)
                            holder.cardviewtag?.setCardBackgroundColor(Color.parseColor("#D3F1062C"))
                            holder.textestado?.text=context.resources.getString(R.string.viaje_finalizado)
                            holder.textestado?.textSize=20.0F
                            holder.textestado?.setTextColor(ContextCompat.getColor(context, R.color.unicomer_white))
                            holder.check?.visibility=View.VISIBLE
                        }

                        "available"->{
                            Log.d("USER","SI ESTA")
                            holder.image?.setAnimation("bus_green.json")
                            holder.buttonrsv?.text=context.resources.getString(R.string.reservado)
                            holder.buttonrsv?.background?.setColorFilter(Color.parseColor("#EE0909"), PorterDuff.Mode.SRC_ATOP)
                            holder.image?.setColorFilter(Color.argb(0, 255, 255, 255),   PorterDuff.Mode.SRC_ATOP)
                            holder.cardviewtag?.setCardBackgroundColor(Color.parseColor("#C83EAC42"))
                            holder.textestado?.text=context.resources.getString(R.string.viaje_en_espera)
                            holder.textestado?.textSize=20.0F
                           // holder.textestado?.setTextColor(ContextCompat.getColor(context, android.R.color.black))
                            holder.check?.visibility=View.VISIBLE
                        }

                        "ongoing"->{
                            holder.image?.setAnimation("bus_yellow.json")
                            holder.buttonrsv?.text=context.resources.getString(R.string.reservado)
                            holder.buttonrsv?.background?.setColorFilter(Color.parseColor("#EE0909"), PorterDuff.Mode.SRC_ATOP)
                            holder.cardviewtag?.setCardBackgroundColor(Color.parseColor("#D3FFEB3B"))
                            holder.textestado?.text=context.resources.getString(R.string.viaje_en_camino)
                            holder.textestado?.textSize=20.0F
                            holder.textestado?.setTextColor(ContextCompat.getColor(context, android.R.color.black))
                            holder.check?.visibility=View.VISIBLE
                        }

                    }

                } }) {
            Log.d("USER","SI ESTA")

        } else {
            holder.buttonrsv?.text=context.resources.getString(R.string.reservar_btn)
            holder.image?.setColorFilter(Color.argb(150, 155, 155, 155),   PorterDuff.Mode.SRC_ATOP)
            holder.buttonrsv?.background?.setColorFilter(Color.parseColor("#46B64B"), PorterDuff.Mode.SRC_ATOP)
            holder.cardviewtag?.setCardBackgroundColor(Color.parseColor("#9500BCD4"))
            holder.textestado?.text=context.resources.getString(R.string.listo_para_reservar)
            holder.check?.visibility=View.GONE
            Log.d("USER","NO ESTA")
        }

    }


    override fun getItemCount(): Int {
        Log.d("SIZE", "${reservations.size}")
        return reservations.size

    }
}



