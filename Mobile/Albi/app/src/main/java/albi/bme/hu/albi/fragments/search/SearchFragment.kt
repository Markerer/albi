package albi.bme.hu.albi.fragments.search

import albi.bme.hu.albi.R
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Spinner

class SearchFragment : Fragment(){

    private val districts: ArrayList<String> = ArrayList()
    private var seekBarValue: Int? = 0
    private var spinner: Spinner? = null
    private var seekBar: SeekBar? = null

    private fun initDistrict(){
        for(i in 1..23){
            this.districts.add(i.toString()+ ". kerület")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initDistrict()
        val view = inflater.inflate(R.layout.search_fragment, container, false)
        seekBar = view.findViewById(R.id.search_seekBar)
        spinner = view.findViewById(R.id.spinner_district)
        /**
         * Spinner click listener
         * spinner.setOnItemSelectedListener(this);
         */
        var dataAdapter: ArrayAdapter<String> = ArrayAdapter(context!!, R.layout.support_simple_spinner_dropdown_item, districts)
        dataAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinner!!.adapter = dataAdapter

        return view
    }

}