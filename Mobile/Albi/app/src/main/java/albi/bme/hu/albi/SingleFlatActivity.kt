package albi.bme.hu.albi

import albi.bme.hu.albi.adapter.recycleviewadapter.SlidingImageAdapter
import albi.bme.hu.albi.model.Flat
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_single_flat.*

class SingleFlatActivity : AppCompatActivity() {

    private lateinit var flat: Flat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_flat)

        flat = intent.getSerializableExtra("flat") as Flat

        val slidingImageAdapter = SlidingImageAdapter(applicationContext, flat.imageNames!!)

        pager.adapter = slidingImageAdapter

        tvDescriptionSingle.text = getString(R.string.description_format, flat.description)
        tvEmailSingle.text = getString(R.string.email_format, flat.email)
        tvNumberOfRooms.text = getString(R.string.number_of_rooms_format, flat.numberOfRooms)
        tvPhoneNumberSingle.text = getString(R.string.phone_number_format, flat.phone_number)
        tvPriceSingle.text = getString(R.string.rent_price_format, flat.price)

        tvCitySingle.text = flat.city
        tvZipCodeSingle.text = flat.zipCode
        tvAddressSingle.text = flat.address
    }
}
