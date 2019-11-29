package sv.edu.bitlab.ride.fragments.recordComponents.recyclerview

import android.content.Context

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sv.edu.bitlab.ride.R
import sv.edu.bitlab.ride.models.UserHistory

class RecordAdapter ( var history:ArrayList<UserHistory>, val listener: RecordViewHolder.RecordItemListener, var context: Context
) : RecyclerView.Adapter<RecordViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_row_history, parent, false)
        return RecordViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder:RecordViewHolder, position: Int) {
        holder.bindData()

        holder.date_txt?.text=history[position].date
        holder.schedule_txt?.text=history[position].schedule



    }


    override fun getItemCount(): Int {

        return history.size

    }
}