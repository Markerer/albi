package albi.bme.hu.albi.fragments.search

import albi.bme.hu.albi.R
import albi.bme.hu.albi.SearchDetailActivity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import java.io.Serializable



data class SearchResult(var price: Int, var numberOfRooms: Int?, var address: String?) : Serializable



class SearchFragment: Fragment(){

    private var priceSeekBarValue = 0
    private lateinit var priceSeekBar: SeekBar

    private var addressLayout: TextInputLayout? = null
    private var roomsLayout: TextInputLayout? = null
    private var searchButton: Button? = null
    private var priceValueText: TextView? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.search_fragment, container, false)
        priceSeekBar = view.findViewById(R.id.search_price_seekBar)
        addressLayout = view.findViewById(R.id.search_address)
        roomsLayout = view.findViewById(R.id.search_number_of_rooms)
        searchButton = view.findViewById(R.id.search_button)
        priceValueText = view.findViewById(R.id.search_price_value)

        searchButton!!.setOnClickListener {
            var numberOfRooms: Int? = null
            var address: String? = null
            if (roomsLayout!!.editText!!.text.isNotEmpty()) {
                numberOfRooms = Integer.parseInt(roomsLayout!!.editText!!.text.toString())
            }

            if (addressLayout!!.editText!!.text.isNotEmpty()) {
                address = addressLayout!!.editText!!.text.toString()
            }

            val intent = Intent(activity!!, SearchDetailActivity::class.java)

            val bundle = Bundle()
            bundle.putSerializable("result", SearchResult(priceSeekBarValue, numberOfRooms, address))
            intent.putExtras(bundle)

            startActivity(intent)

        }

        priceSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                priceSeekBarValue = seekBar.progress
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                priceValueText!!.text = priceFormatter(progress)
            }
        })

        return view
    }

    private fun priceFormatter(value: Int): String {
        val formatted = (String.format("%,d", value)).replace(',', ' ')
        return "$formatted Ft"
    }
}