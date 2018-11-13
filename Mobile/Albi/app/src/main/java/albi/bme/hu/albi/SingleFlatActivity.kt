package albi.bme.hu.albi

import albi.bme.hu.albi.adapter.recycleviewadapter.SlidingImageAdapter
import albi.bme.hu.albi.model.Flat
import albi.bme.hu.albi.network.RestApiFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_single_flat.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SingleFlatActivity : AppCompatActivity() {

    private lateinit var flat: Flat
    private var owner: Boolean = false

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
        }

        editAdvertisement.setOnClickListener {
            setElementsEditability(true)
            editAdvertisement.visibility = View.INVISIBLE
            saveAdvertisement.visibility = View.VISIBLE
        }

        saveAdvertisement.setOnClickListener {
            if (everythingFilled()) {
                setFlatValuesFromEdittexts()
                sendNetworkRequestForUpdateFlat()
                editAdvertisement.visibility = View.VISIBLE
                saveAdvertisement.visibility = View.INVISIBLE
                setElementsEditability(false)
            }
        }
    }

    private fun setFlatValuesFromEdittexts(){
        flat.numberOfRooms = etNumberOfRoomsSingle.text.toString()
        flat.description = etDescriptionSingle.text.toString()
        flat.price = etPriceSingle.text.toString()
        flat.city = etCitySingle.text.toString()
        flat.zipCode = etZipCodeSingle.text.toString()
        flat.address = etAddressSingle.text.toString()
    }

    private fun sendNetworkRequestForUpdateFlat() {
        val client = RestApiFactory.createFlatClient()
        val call = client.updateFlat(flat)

        call.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(this@SingleFlatActivity, "Már megint szar van a levesben", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                Toast.makeText(this@SingleFlatActivity, response.body(), Toast.LENGTH_LONG).show()
            }
        })
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

    private fun setFormattedText(){
        etEmailSingle.setText(getString(R.string.email_format, flat.email))
        etPhoneNumberSingle.setText(getString(R.string.phone_number_format, flat.phone_number))
        etPriceSingle.setText(getString(R.string.rent_price_format, flat.price))
        etDescriptionSingle.setText(getString(R.string.description_format, flat.description))
        etNumberOfRoomsSingle.setText(getString(R.string.number_of_rooms_format, flat.numberOfRooms))
        etZipCodeSingle.setText(flat.zipCode)
        etAddressSingle.setText(flat.address)
        etCitySingle.setText(flat.city)
    }
    private fun setTextForEdit(){
        etPriceSingle.setText(flat.price)
        etDescriptionSingle.setText(flat.description)
        etNumberOfRoomsSingle.setText(flat.numberOfRooms)
    }
}
