package com.tugas.happyplacesapp.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tugas.happyplacesapp.R
import com.tugas.happyplacesapp.activity.AddHappyPlaceActivity
import com.tugas.happyplacesapp.activity.MainActivity
import com.tugas.happyplacesapp.database.DatabaseHandler
import com.tugas.happyplacesapp.model.HappyPlaceModel
import kotlinx.android.synthetic.main.item_happy_places.view.*

class HappyPlacesAdapter(private val context: Context, private var list: ArrayList<HappyPlaceModel>) :
        RecyclerView.Adapter<HappyPlaceViewHolder>() {

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HappyPlaceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_happy_places, parent, false)
        return HappyPlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: HappyPlaceViewHolder, position: Int) {
        val model = list[position]

        if (holder is HappyPlaceViewHolder) {
            holder.itemView.iv_place_image.setImageURI(Uri.parse(model.image))
            holder.itemView.tvTitle.text = model.title
            holder.itemView.tvDescription.text = model.description

            holder.itemView.setOnClickListener {
                if (onClickListener != null) {
                    onClickListener!!.onClick(position, model)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun notifyEditItem(activity: Activity, position: Int, requestCode: Int) {
        val intent = Intent(context, AddHappyPlaceActivity::class.java)
        intent.putExtra(MainActivity.EXTRA_PLACE_DETAILS, list[position])
        activity.startActivityForResult(
                intent,
                requestCode
        ) // Activity is started with requestCode
        notifyItemChanged(position) // Notify any registered observers that the item at position has changed.
    }

    fun removeAt(position: Int) {
        val dbHandler = DatabaseHandler(context)
        val isDeleted = dbHandler.deleteHappyPlace(list[position])
        if (isDeleted > 0) {
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: HappyPlaceModel)
    }

}

class HappyPlaceViewHolder(view: View): RecyclerView.ViewHolder(view){

}
