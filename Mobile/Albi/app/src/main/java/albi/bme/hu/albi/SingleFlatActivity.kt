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
    }
}
