package sv.edu.bitlab.ride.fragments.reservationComponents.recyclerview

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import kotlinx.android.synthetic.main.list_row_card_rsv.view.*

class ReservationViewHolder(itemView: View, val listener: ReservationItemListener) : RecyclerView.ViewHolder(itemView)  {

    //contenido reserva
    var count_txt:TextView? =null
    var round_txt:TextView?=null
    var schedule_txt:TextView?=null
    var buttonrsv:Button?=null
    var image: LottieAnimationView?=null
    var cardviewtag: CardView?=null
    var textestado: TextView?=null
    var check:CardView?=null
    var id_txt_cupo:TextView?=null

    //detalle de viaje
    var result_number:TextView?=null



    fun bindData() {
        //data de estado de viaje
        id_txt_cupo=itemView.id_txt_cupo
        count_txt=itemView.id_reservation_count//este
        round_txt=itemView.id_actual_round//este
        schedule_txt=itemView.id_schedule//este
        buttonrsv=itemView.button_rsv
        image=itemView.id_image_bus
        cardviewtag=itemView.id_card_estado
        textestado=itemView.id_estado_rsv
        check=itemView.id_check

    }


    interface ReservationItemListener{
       fun onItemClickReservation(position: Int,round_status:String,round:String)

    }
}