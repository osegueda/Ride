package sv.edu.bitlab.ride.fragments.recordComponents.recyclerview

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_row_history.view.*

import kotlinx.android.synthetic.main.list_row_record.view.*

import kotlinx.android.synthetic.main.list_row_reservation.view.*

class RecordViewHolder(itemView: View, val listener: RecordItemListener) : RecyclerView.ViewHolder(itemView)  {



    var date_txt: TextView?=null

    var schedule_txt: TextView?=null



    fun bindData() {





      //testing
      //  schedule_txt=itemView.history_schedule_txt

/*
        date_txt=itemView.fecha_txt

        round_txt=itemView.round_txt
        schedule_txt=itemView.schedule_txt*/

        date_txt=itemView.id_date

        schedule_txt=itemView.id_horario

        //container=itemView.item_container_reservation



        // listener.onViewDetalleOrden(id_txt!!,fecha_txt!!,total_txt!!,status_txt!!,this.adapterPosition)
      /*  container?.setOnClickListener{

            listener.onItemClickRecord(this.adapterPosition)

        }*/

    }


    interface RecordItemListener{
        fun onItemClickRecord(position: Int)

    }
}