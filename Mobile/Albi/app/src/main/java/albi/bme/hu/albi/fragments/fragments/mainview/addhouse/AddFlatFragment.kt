package albi.bme.hu.albi.fragments.fragments.mainview.addhouse

import albi.bme.hu.albi.R
import albi.bme.hu.albi.model.Flat
import albi.bme.hu.albi.model.User
import albi.bme.hu.albi.network.RestApiFactory
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import retrofit2.Callback
import retrofit2.Response

class AddFlatFragment: Fragment() {

    var user: User? = null


    var uploadFlat = Flat()
    private var images: ArrayList<Image> = ArrayList()

    companion object {
        private var MY_PERMISSIONS_REQUEST = 100
    }

    private var PICK_IMAGE_FROM_GALERY_REQUEST = 1
    private var PERMISSION_REQUEST_CODE = 200
    private lateinit var uploadButton: Button
    private lateinit var priceLayout: TextInputLayout
    private lateinit var numberOfRoomsLayout: TextInputLayout
    private lateinit var descriptionLayout: TextInputLayout
    private lateinit var addressLayout: TextInputLayout
    private lateinit var uploadButtonAdvert: Button


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.add_house_fragment, container, false)
        uploadButton = view.findViewById(R.id.uploadimagebutton)
        priceLayout = view.findViewById(R.id.price_upload)
        numberOfRoomsLayout = view.findViewById(R.id.numberofrooms_upload)
        descriptionLayout = view.findViewById(R.id.description_upload)
        addressLayout = view.findViewById(R.id.address_upload)
        uploadButtonAdvert = view.findViewById(R.id.addhouse)

        uploadButton.setOnClickListener {
            requestNeededPermission()
            val intent: Intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "select picture"), PICK_IMAGE_FROM_GALERY_REQUEST)
        }

        // TODO: szebben if-else nélkül!!
        /**
         * valamiért ha az egyik üres marad, nem dobja ki
         * az error-részére a hibát, hanem kilép
         * TODO--> megoldani
         */
        uploadButtonAdvert.setOnClickListener {
            if(priceLayout.editText!!.text.isEmpty()){
                priceLayout.editText!!.error = "This field must be filled!"
            } else {
                uploadFlat.price = priceLayout.editText!!.text.toString()
            }

            if(numberOfRoomsLayout.editText!!.text.isEmpty()){
                numberOfRoomsLayout.editText!!.error = "This field must be filled!"
            }else{
                uploadFlat.numberOfRooms = numberOfRoomsLayout.editText!!.text.toString()//.toInt()
            }

            if(descriptionLayout.editText!!.text.isEmpty()){
                descriptionLayout.editText!!.error = "This field must be filled!"
            }else{
                uploadFlat.description = descriptionLayout.editText!!.text.toString()
            }
            if(addressLayout.editText!!.text.isEmpty()){
                addressLayout.editText!!.error = "This field must be filled!"
            }else{
                uploadFlat.address = addressLayout.editText!!.text.toString()
            }
            uploadFlat.phone_number = user!!.phone_number
            uploadFlat.email = user!!.email

            if(!priceLayout.editText!!.text.isEmpty() &&
               !numberOfRoomsLayout.editText!!.text.isEmpty() &&
               !descriptionLayout.editText!!.text.isEmpty() &&
               !addressLayout.editText!!.text.isEmpty()){
                sendNetworkRequestAddvertisement()
            }
        }
        return view
    }

    private fun sendNetworkRequestAddvertisement(){
        val client = RestApiFactory.createFlatClient()
        val call = client.uploadFlat(user?._id!!, uploadFlat)

        call.enqueue(object : Callback<Flat> {
            override fun onResponse(call: retrofit2.Call<Flat>, response: Response<Flat>) {
                Toast.makeText(context, "advertisement upload was successfull", Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: retrofit2.Call<Flat>?, t: Throwable?) {
                t?.printStackTrace()
                Toast.makeText(context, "error in uploading advertisement :(" + t?.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_FROM_GALERY_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val uri: Uri = data.data!!
            uploadFile(uri)
        }
    }

    private fun uploadFile(fileUri: Uri) {
        val client = RestApiFactory.createFlatClient()
        // TODO --> https://youtu.be/yKxLgEfY49A?t=532
        //var filepart: RequestBody = RequestBody.create(
        //        MediaType.parse(context!!.contentResolver.getType(fileUri)),
                // https://youtu.be/yKxLgEfY49A?t=532
                // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
                //FileUtil
        //)
        //var call = client.uploadPhoto()


    }

    // https://www.aut.bme.hu/Upload/Course/android/hallgatoi_jegyzetek/Android_05.pdf
    private fun requestNeededPermission() {
        if (ContextCompat.checkSelfPermission(context!!,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(context, "I need it for galery", Toast.LENGTH_SHORT).show()
            }
            ActivityCompat.requestPermissions(activity!!,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE)
        } else {
            // már van engedély
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context!!, "GALERY perm granted",
                            Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context!!, "GALERY perm NOT granted",
                            Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}


