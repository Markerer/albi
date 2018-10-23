package albi.bme.hu.albi.adapter.recycleviewadapter

import albi.bme.hu.albi.R
import albi.bme.hu.albi.model.Flat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class RecyclerAdapter(private val flatList: ArrayList<Flat>) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.house_detail_fragment_row, viewGroup, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return flatList.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val flat: Flat = flatList[position]
        viewHolder.tvPrice.text = flat.price.toString()
        viewHolder.tvAddress.text = flat.address
        viewHolder.tvNumberOfRooms.text = flat.numberOfRooms.toString()
    }

    class ViewHolder(itemViews: View) : RecyclerView.ViewHolder(itemViews){
        val tvPrice = itemView.findViewById<TextView>(R.id.tvPrice)!!
        val tvAddress = itemView.findViewById<TextView>(R.id.tvAddress)!!
        val tvNumberOfRooms = itemView.findViewById<TextView>(R.id.tvNumberOfRooms)!!
    }

    public fun clearData(){
        flatList.clear()
        notifyDataSetChanged()
    }

    public fun addAll(list: ArrayList<Flat>){
        flatList.addAll(list)
        notifyDataSetChanged()
    }
}

