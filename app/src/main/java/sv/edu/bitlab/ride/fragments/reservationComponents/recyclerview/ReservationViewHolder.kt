package sv.edu.bitlab.ride.fragments.reservationComponents.recyclerview

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_row_rsv.view.*

class ReservationViewHolder(itemView: View, val listener: ReservationItemListener) : RecyclerView.ViewHolder(itemView)  {

    var card1:View?=null
    var card2:View?=null
    var id_container:View?=null

    //card1
    var id_txt: TextView? = null
    var status_txt: TextView? =null
    var date_txt: TextView?=null
    var count_txt:TextView? =null
    var round_txt:TextView?=null
    var schedule_txt:TextView?=null
    var buttonrsv:Button?=null
    var line1: View?=null
    var line2: View?=null
    //card2

    var result_number:TextView?=null
    var result_txt:TextView?=null
    fun bindData() {

        card1=itemView.id_card1
        card2=itemView.id_card2
        id_container=itemView.id_container
        //para mostrar el numero de ronda
        result_number=itemView.result_round_number_txt
        result_txt=itemView.result_txt



        id_txt=itemView.id_round
        status_txt=itemView.id_round_status
        date_txt=itemView.id_txt_date
        count_txt=itemView.id_reservation_count
        round_txt=itemView.id_round_number
        schedule_txt=itemView.id_schedule
        buttonrsv=itemView.button_rsv

        line1=itemView.id_line_date
        line2=itemView.id_line_status

       // listener.onViewDetalleOrden(id_txt!!,fecha_txt!!,total_txt!!,status_txt!!,this.adapterPosition)

        
    }


    interface ReservationItemListener{
       fun onItemClickReservation(position: Int,round_status:String)

    }
}