package albi.bme.hu.albi.adapter.recycleviewadapter

import albi.bme.hu.albi.R
import albi.bme.hu.albi.model.Flat
import android.media.Image
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class RecyclerAdapter(private val flatList: ArrayList<Flat>) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.house_detail_fragment_row, viewGroup, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return flatList.size
    }

    // TODO: set image to imageView
    /**
     * https://developer.android.com/topic/performance/graphics/load-bitmap
     * https://stackoverflow.com/questions/8717333/converting-drawable-resource-image-into-bitmap
     * https://stackoverflow.com/questions/41311179/how-do-i-set-an-image-in-recyclerview-in-a-fragment-from-the-drawable-folder
     */
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val flat: Flat = flatList[position]
        // TODO ötletelni --> https://developer.android.com/guide/topics/resources/drawable-resource
        viewHolder.ivHousePicture.setImageDrawable(flat.image) // nem biztos!
        viewHolder.tvPrice.text = flat.price.toString()
        viewHolder.tvAddress.text = flat.address
        viewHolder.tvNumberOfRooms.text = flat.numberOfRooms.toString()
    }

    class ViewHolder(itemViews: View) : RecyclerView.ViewHolder(itemViews){
        val ivHousePicture = itemView.findViewById<ImageView>(R.id.ivFlatImagePreview)
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

