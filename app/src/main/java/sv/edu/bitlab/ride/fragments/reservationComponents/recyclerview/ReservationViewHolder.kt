package sv.edu.bitlab.ride.fragments.reservationComponents.recyclerview

import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_row_card_rsv.view.*

class ReservationViewHolder(itemView: View, val listener: ReservationItemListener) : RecyclerView.ViewHolder(itemView)  {

    //contenido reserva
    var id_txt: TextView? = null
    var count_txt:TextView? =null
    var round_txt:TextView?=null
    var schedule_txt:TextView?=null
    var buttonrsv:Button?=null

    //detalle de viaje
    var cardview: CardView?=null
    var arrowBtn: Button?=null
    var card1:LinearLayout?=null
    var card2:LinearLayout?=null
    var result_number:TextView?=null
    var result_txt:TextView?=null

    fun bindData() {
        //contenedores de detalle de viaje
        card1=itemView.id_card1
        card2=itemView.id_card2
        cardview=itemView.id_cardview
        arrowBtn=itemView.id_arrowBtn
        //para mostrar el numero de ronda
        result_number=itemView.result_round_number_txt//este
        result_txt=itemView.result_txt//este


        //data de estado de viaje
        id_txt=itemView.id_round//este
        count_txt=itemView.id_reservation_count//este
        round_txt=itemView.id_round_number//este
        schedule_txt=itemView.id_schedule//este
        buttonrsv=itemView.button_rsv

       // listener.onViewDetalleOrden(id_txt!!,fecha_txt!!,total_txt!!,status_txt!!,this.adapterPosition)

        
    }


    interface ReservationItemListener{
       fun onItemClickReservation(position: Int,round_status:String)

    }
}