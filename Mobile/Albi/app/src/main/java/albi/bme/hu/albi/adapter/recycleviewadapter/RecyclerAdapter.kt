package albi.bme.hu.albi.adapter.recycleviewadapter

import albi.bme.hu.albi.R
import albi.bme.hu.albi.SingleFlatActivity
import albi.bme.hu.albi.model.Flat
import albi.bme.hu.albi.network.RestApiFactory
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class RecyclerAdapter(private val flatList: ArrayList<Flat>, private val context: Context) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

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
            Glide.with(context).load(RestApiFactory.BASE_URL + flat.imageNames!![0]).into(viewHolder.ivHousePicture)
        } else {
            viewHolder.ivHousePicture.setImageResource(R.drawable.ic_no_image_512)
        }

        viewHolder.tvPrice.text = context.resources.getString(R.string.price_format, flat.price)
        viewHolder.tvAddress.text = flat.address
        viewHolder.tvNumberOfRooms.text = context.resources.getString(R.string.number_of_rooms_format, flat.numberOfRooms)

        viewHolder.itemView.setOnClickListener{
            val intent = Intent(context, SingleFlatActivity::class.java)
            intent.putExtra("flat", flatList[position])
            context.startActivity(intent)
        }
    }

    class ViewHolder(itemViews: View) : RecyclerView.ViewHolder(itemViews){
        var ivHousePicture = itemView.findViewById<ImageView>(R.id.ivFlatImagePreview)
        val tvPrice = itemView.findViewById<TextView>(R.id.tvPrice)!!
        val tvAddress = itemView.findViewById<TextView>(R.id.tvAddress)!!
        val tvNumberOfRooms = itemView.findViewById<TextView>(R.id.tvNumberOfRooms)!!
    }

}

