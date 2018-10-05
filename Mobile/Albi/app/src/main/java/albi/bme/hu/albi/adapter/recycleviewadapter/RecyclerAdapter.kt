package albi.bme.hu.albi.adapter.recycleviewadapter

import albi.bme.hu.albi.R
import albi.bme.hu.albi.model.Flat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class RecyclerAdapter(val flatList: ArrayList<Flat>) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0?.context).inflate(R.layout.house_detail_fragment_row, p0, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return flatList.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val flat: Flat = flatList[p1]
        p0?.textViewTitle?.text = flat.title
        p0?.textViewNumberOfBeds?.text = flat.numberOfBeds
        p0?.textViewDescription?.text = flat.description
        p0?.textViewPrice?.text = flat.price.toString()
    }

    class ViewHolder(itemViews: View) : RecyclerView.ViewHolder(itemViews){
        val textViewTitle = itemView.findViewById<TextureView>(R.id.tvTitle) as TextView
        val textViewNumberOfBeds = itemView.findViewById<TextureView>(R.id.tvNumberOfBeds) as TextView
        val textViewDescription = itemView.findViewById<TextureView>(R.id.tvDescription) as TextView
        val textViewPrice = itemView.findViewById<TextureView>(R.id.tvPrice) as TextView
    }
}