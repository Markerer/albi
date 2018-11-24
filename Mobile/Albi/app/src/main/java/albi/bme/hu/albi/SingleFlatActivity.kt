package albi.bme.hu.albi

import albi.bme.hu.albi.adapter.recycleviewadapter.SlidingImageAdapter
import albi.bme.hu.albi.helpers.Today
import albi.bme.hu.albi.model.Flat
import albi.bme.hu.albi.model.User
import albi.bme.hu.albi.network.FlatDateResponse
import albi.bme.hu.albi.network.RestApiFactory
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_single_flat.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.HashMap

class SingleFlatActivity : AppCompatActivity() {

    private lateinit var flat: Flat
    private var owner: Boolean = false
    private var statisticData = HashMap<String, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_flat)

        flat = intent.getSerializableExtra("flat") as Flat
        owner = intent.getBooleanExtra("owner", false)

        val slidingImageAdapter = SlidingImageAdapter(applicationContext, flat.imageNames!!)
        pager.adapter = slidingImageAdapter

        setFormattedText()

        if (owner) {
            editAdvertisement.visibility = View.VISIBLE
        } else {
            sendView()
        }

        editAdvertisement.setOnClickListener { editOnClick() }
        saveAdvertisement.setOnClickListener { saveOnClick() }
        showStatistics.setOnClickListener { showStatistics() }

    }

    override fun onStart() {
        super.onStart()
        getAllDates()
    }

    private fun sendView() {
        val client = RestApiFactory.createFlatClient()
        val call = client.addView(flat._id, Today(), User.token!!)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@SingleFlatActivity, "Couldn't send view: " + t.printStackTrace(), Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getAllDates(){
        val client = RestApiFactory.createFlatClient()
        val call = client.getAllDatesForFlat("5be60d3c2be1db3bc01c2184") // flat._id

        call.enqueue(object : Callback<List<FlatDateResponse>>{
            /**
             * azt kell, hogy eltároljuk, egy hashmap-be
             * ha az adott dátumhoz még nincs megtekintés,
             * akkor ott pl lehetne 0
             * egyébként ha meg már tartalmazza azt a dátumot
             * akkor ha találunk még egy ugyan olyan dátumot
             * akkor annak a counterjét hozzáadjuk a már benne lévő dátumhoz
             *
             * dateFormat: "date": "2018.11.17",
             */
            override fun onResponse(call: Call<List<FlatDateResponse>>, response: Response<List<FlatDateResponse>>) {
                val datesResponse = response.body()
                if (datesResponse != null) {
                    for(i in datesResponse.indices)
                    /**
                     * ha még nem tartalmazza a dátumot, akkor beletesszük
                     */
                    if(!statisticData.contains(datesResponse[i].date)){
                        statisticData[datesResponse[i].date!!] = datesResponse[i].counter!!
                    }
                    /**
                     * egyébként meg megkeressük azt a dátumot,
                     * és hozzáadjuk a countert pluszba
                     */
                    else {
                        val totalView = statisticData[datesResponse[i].date]
                        statisticData[datesResponse[i].date!!] = datesResponse[i].counter!! + totalView!!
                    }
                }
            }

            override fun onFailure(call: Call<List<FlatDateResponse>>, t: Throwable) {
                Toast.makeText(this@SingleFlatActivity, "Couldn't send view: " + t.printStackTrace(), Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun showStatistics(){
        val intent = Intent(this@SingleFlatActivity, StatisticsActivity::class.java)
        // https://stackoverflow.com/questions/7578236/how-to-send-hashmap-value-to-another-activity-using-an-intent
        intent.putExtra("statisticData", statisticData)
        startActivity(intent)
    }

    private fun editOnClick() {
        setElementsEditability(true)
        editAdvertisement.visibility = View.INVISIBLE
        saveAdvertisement.visibility = View.VISIBLE
    }

    private fun saveOnClick() {
        if (everythingFilled()) {
            setFlatValuesFromEdittexts()
            sendNetworkRequestForUpdateFlat()
            editAdvertisement.visibility = View.VISIBLE
            saveAdvertisement.visibility = View.INVISIBLE
            setElementsEditability(false)
        }
    }

    private fun sendNetworkRequestForUpdateFlat() {
        val client = RestApiFactory.createFlatClient()
        val call = client.updateFlat(flat, User.token!!)

        call.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(this@SingleFlatActivity, "Couldn't update flat", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                Toast.makeText(this@SingleFlatActivity, response.body(), Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setFlatValuesFromEdittexts() {
        flat.numberOfRooms = etNumberOfRoomsSingle.text.toString()
        flat.description = etDescriptionSingle.text.toString()
        flat.price = etPriceSingle.text.toString()
        flat.city = etCitySingle.text.toString()
        flat.zipCode = etZipCodeSingle.text.toString()
        flat.address = etAddressSingle.text.toString()
    }

    private fun everythingFilled(): Boolean {
        var filled = true
        if (etPriceSingle.text.isEmpty()) {
            etPriceSingle.error = "This field must be filled!"
            filled = false
        }

        if (etNumberOfRoomsSingle.text.isEmpty()) {
            etNumberOfRoomsSingle.error = "This field must be filled!"
            filled = false
        }

        if (etDescriptionSingle.text.isEmpty()) {
            etDescriptionSingle.error = "This field must be filled!"
            filled = false
        }

        if (etZipCodeSingle.text.isEmpty()) {
            etZipCodeSingle.error = "This field must be filled!"
            filled = false
        }

        if (etCitySingle.text.isEmpty()) {
            etCitySingle.error = "This field must be filled!"
            filled = false
        }

        if (etAddressSingle.text.isEmpty()) {
            etAddressSingle.error = "This field must be filled!"
            filled = false
        }

        return filled
    }

    private fun setFormattedText() {
        etEmailSingle.setText(getString(R.string.email_format, flat.email))
        etPhoneNumberSingle.setText(getString(R.string.phone_number_format, flat.phone_number))
        etPriceSingle.setText(getString(R.string.rent_price_format, flat.price))
        etDescriptionSingle.setText(getString(R.string.description_format, flat.description))
        etNumberOfRoomsSingle.setText(getString(R.string.number_of_rooms_format, flat.numberOfRooms))
        etZipCodeSingle.setText(flat.zipCode)
        etAddressSingle.setText(flat.address)
        etCitySingle.setText(flat.city)
    }

    private fun setTextForEdit() {
        etPriceSingle.setText(flat.price)
        etDescriptionSingle.setText(flat.description)
        etNumberOfRoomsSingle.setText(flat.numberOfRooms)
    }

    private fun setElementsEditability(edit: Boolean) {
        etPriceSingle.isEnabled = edit
        etNumberOfRoomsSingle.isEnabled = edit
        etDescriptionSingle.isEnabled = edit
        etZipCodeSingle.isEnabled = edit
        etCitySingle.isEnabled = edit
        etAddressSingle.isEnabled = edit

        if (edit)
            setTextForEdit()
        else
            setFormattedText()
    }
}
