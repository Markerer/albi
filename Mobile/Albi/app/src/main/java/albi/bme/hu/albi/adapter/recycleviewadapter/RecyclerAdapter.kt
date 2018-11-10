package albi.bme.hu.albi.adapter.recycleviewadapter

import albi.bme.hu.albi.R
import albi.bme.hu.albi.R.id.imageView
import albi.bme.hu.albi.model.Flat
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso

class RecyclerAdapter(private val flatList: ArrayList<Flat>, private val context: Context) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private val BASE_URL: String = "http://10.0.2.2:3000/"

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.house_detail_fragment_row, viewGroup, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return flatList.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val flat: Flat = flatList[position]
        if(! (flat.imageNames!!.isEmpty())){
            Glide.with(context).load(BASE_URL + flat.imageNames!![0]).into(viewHolder.ivHousePicture)
        } else {
            viewHolder.ivHousePicture.setImageResource(R.drawable.ic_no_image_512)
        }
        viewHolder.tvPrice.text = flat.price
        viewHolder.tvAddress.text = flat.address
        viewHolder.tvNumberOfRooms.text = flat.numberOfRooms
    }

    class ViewHolder(itemViews: View) : RecyclerView.ViewHolder(itemViews){
        var ivHousePicture = itemView.findViewById<ImageView>(R.id.ivFlatImagePreview)
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

