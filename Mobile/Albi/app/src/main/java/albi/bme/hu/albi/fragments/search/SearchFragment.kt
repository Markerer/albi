package albi.bme.hu.albi.fragments.search

import albi.bme.hu.albi.R
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Spinner

class SearchFragment : Fragment(){

    private var priceSeekBarValue: Int? = 0
    private var priceSeekBar: SeekBar? = null
    private var address: String? = null
    private var numberOfRooms: Int? = 0

    private var addressLayout: TextInputLayout? = null
    private var roomsLayout: TextInputLayout? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.search_fragment, container, false)
        priceSeekBar = view.findViewById(R.id.search_price_seekBar)
        addressLayout = view.findViewById(R.id.search_address)
        roomsLayout = view.findViewById(R.id.search_number_of_rooms)

        return view
    }

}