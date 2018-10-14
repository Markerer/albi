package albi.bme.hu.albi.adapter.recycleviewadapter

import albi.bme.hu.albi.R
import albi.bme.hu.albi.model.Flat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class RecyclerAdapter(private val flatList: ArrayList<Flat>) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.house_detail_fragment_row, p0, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return flatList.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val flat: Flat = flatList[p1]
        p0.textViewPrice.text = flat.price.toString()
        p0.textViewAddress.text = flat.address
        p0.textViewNumberOfBeds.text = flat._id
    }

    class ViewHolder(itemViews: View) : RecyclerView.ViewHolder(itemViews){
        val textViewPrice = itemView.findViewById<TextureView>(R.id.tvPrice) as TextView
        val textViewAddress = itemView.findViewById<TextureView>(R.id.tvAddress) as TextView
        val textViewNumberOfBeds = itemView.findViewById<TextureView>(R.id.tvNumberOfBeds) as TextView
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

