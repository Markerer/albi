package albi.bme.hu.albi.adapter.recycleviewadapter

import albi.bme.hu.albi.R
import albi.bme.hu.albi.network.RestApiFactory
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide

class SlidingImageAdapter(private var context: Context, private var photos: ArrayList<String>) : PagerAdapter() {
    private var inflater: LayoutInflater = LayoutInflater.from(context)

    override fun isViewFromObject(view: View, any: Any): Boolean {
        return view == any
    }

    override fun getCount(): Int = photos.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = inflater.inflate(R.layout.pager_item, container, false)

        val imageView = itemView.findViewById<ImageView>(R.id.ivFlat)
        if (photos.isEmpty())
            imageView.setImageResource(R.drawable.ic_no_image_512)
        else
            Glide.with(context).load(RestApiFactory.BASE_URL + photos[position]).into(imageView)

        container.addView(itemView)

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
        container.removeView(any as LinearLayout)
    }
}