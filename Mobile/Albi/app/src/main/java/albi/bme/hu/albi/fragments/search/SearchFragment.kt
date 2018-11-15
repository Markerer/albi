package albi.bme.hu.albi.fragments.search

import albi.bme.hu.albi.R
import albi.bme.hu.albi.network.RestApiFactory
import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {

    private var priceSeekBarValue: Int? = 0
    private var priceSeekBar: SeekBar? = null
    private var address: String? = null
    private var numberOfRooms: Int? = 0

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
            searchDetailsLoad()
            Toast.makeText(context, "price: " + priceSeekBarValue + "\n" +
                    "numberOfRooms: " + numberOfRooms + "\n" +
                    "address: " + address
                    , Toast.LENGTH_LONG).show()
        }

        priceSeekBar!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                priceValueText!!.text = priceFormatter(progress)
                priceSeekBarValue = progress
            }
        })

        return view
    }

    private fun searchDetailsLoad() {
        if (!roomsLayout!!.editText!!.text.isEmpty()) {
            numberOfRooms = Integer.parseInt(roomsLayout!!.editText!!.text.toString())
        } else {
            numberOfRooms = 0
        }

        if (!addressLayout!!.editText!!.text.isEmpty()) {
            address = addressLayout!!.editText!!.text.toString()
        } else {
            address = ""
        }

    }

    private fun priceFormatter(value: Int): String {
        var formatted: String? = null
        formatted = (String.format("%,d", value)).replace(',', ' ')
        return formatted + " Ft"
    }

    private fun networkRequestSearch(){
        val client = RestApiFactory.createFlatClient()
        val call = client.getFlatsBySearch(1, priceSeekBarValue!!, numberOfRooms!!, address!!)

        call.enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                //TODO implement
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(context, "error: " + t.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}