package albi.bme.hu.albi.fragments.addhouse

import albi.bme.hu.albi.R
import albi.bme.hu.albi.model.Flat
import albi.bme.hu.albi.model.User
import albi.bme.hu.albi.network.ImageUploadResponse
import albi.bme.hu.albi.network.RestApiFactory
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.design.widget.TextInputLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException

class AddFlatFragment : Fragment() {

    var user: User? = null


    var uploadFlat = Flat()
    private var images: ArrayList<Image> = ArrayList()

    companion object {
        private var MY_PERMISSIONS_REQUEST = 100
    }

    private var PICK_IMAGE_FROM_GALERY_REQUEST = 1
    private var PERMISSION_REQUEST_CODE = 200
    private lateinit var uploadImageButton: Button
    private lateinit var priceLayout: TextInputLayout
    private lateinit var numberOfRoomsLayout: TextInputLayout
    private lateinit var descriptionLayout: TextInputLayout
    private lateinit var addressLayout: TextInputLayout
    private lateinit var uploadButtonAdvert: Button
    private lateinit var takePhotoButton: Button
    private var bitmapUri: Uri? = null
    private var bitmap: Bitmap? = null

    val REQUEST_IMAGE_CAPTURE = 101
    private lateinit var iv: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.add_house_fragment, container, false)
        uploadImageButton = view.findViewById(R.id.uploadimagebutton)
        priceLayout = view.findViewById(R.id.price_upload)
        numberOfRoomsLayout = view.findViewById(R.id.numberofrooms_upload)
        descriptionLayout = view.findViewById(R.id.description_upload)
        addressLayout = view.findViewById(R.id.address_upload)
        uploadButtonAdvert = view.findViewById(R.id.addhouse)
        takePhotoButton = view.findViewById(R.id.takephoto)

        iv = view.findViewById(R.id.imageView2)

        uploadImageButton.setOnClickListener {
            requestNeededPermission()
        }

        setButtonUpload()

        takePhotoButton.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }

        return view
    }

    private fun setButtonUpload(){
        uploadButtonAdvert.setOnClickListener {
            if (priceLayout.editText!!.text.isEmpty()) {
                priceLayout.editText!!.error = "This field must be filled!"
            } else {
                uploadFlat.price = priceLayout.editText!!.text.toString()
            }

            if (numberOfRoomsLayout.editText!!.text.isEmpty()) {
                numberOfRoomsLayout.editText!!.error = "This field must be filled!"
            } else {
                uploadFlat.numberOfRooms = numberOfRoomsLayout.editText!!.text.toString()//.toInt()
            }

            if (descriptionLayout.editText!!.text.isEmpty()) {
                descriptionLayout.editText!!.error = "This field must be filled!"
            } else {
                uploadFlat.description = descriptionLayout.editText!!.text.toString()
            }
            if (addressLayout.editText!!.text.isEmpty()) {
                addressLayout.editText!!.error = "This field must be filled!"
            } else {
                uploadFlat.address = addressLayout.editText!!.text.toString()
            }
            uploadFlat.phone_number = user!!.phone_number
            uploadFlat.email = user!!.email

            if (!priceLayout.editText!!.text.isEmpty() &&
                    !numberOfRoomsLayout.editText!!.text.isEmpty() &&
                    !descriptionLayout.editText!!.text.isEmpty() &&
                    !addressLayout.editText!!.text.isEmpty()) {
                sendNetworkRequestAdvertisement()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            data?.also {
                bitmap = it.extras?.get("data") as Bitmap
            }
        } else if (requestCode == PICK_IMAGE_FROM_GALERY_REQUEST && resultCode == RESULT_OK &&
                data != null) {
            try {
                bitmapUri = data.data
                bitmap = MediaStore.Images.Media.getBitmap(this.activity!!.contentResolver, data.data)
            } catch(e: IOException) {
                Toast.makeText(context, "Photo error idk", Toast.LENGTH_LONG).show()
            }
        }
        iv.setImageBitmap(bitmap)
        sendNetworkUploadPhoto("5be82cefdcba3e22b8bb0411")

    }


    private fun sendNetworkUploadPhoto(flatID: String){
        val client = RestApiFactory.createFlatClient()

        val IMAGE_PATH = Environment.getExternalStorageDirectory().absolutePath
        val FULL_PATH = IMAGE_PATH + bitmapUri?.path
        val fileUri = Uri.fromFile(File(FULL_PATH))

        val file = File(fileUri.path)

        val reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body = MultipartBody.Part.createFormData("image", file.name, reqFile)

        val call = client.uploadPhoto(flatID, body)

        call.enqueue(object: Callback<ResponseBody>{
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(context, "Upload failed " + t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                //Toast.makeText(context, "" + response.body().toString(), Toast.LENGTH_LONG).show()
            }
        })

    }

    private fun sendNetworkRequestAdvertisement() {
        val client = RestApiFactory.createFlatClient()
        val call = client.uploadFlat(user?._id!!, uploadFlat)

        call.enqueue(object : Callback<Flat> {
            override fun onResponse(call: retrofit2.Call<Flat>, response: Response<Flat>) {
                sendNetworkUploadPhoto(response.body()!!._id)
                Toast.makeText(context, "advertisement upload was successfull", Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: retrofit2.Call<Flat>?, t: Throwable?) {
                t?.printStackTrace()
                Toast.makeText(context, "error in uploading advertisement :(" + t?.message, Toast.LENGTH_LONG).show()
            }
        })
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
            val intent: Intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "select picture"), PICK_IMAGE_FROM_GALERY_REQUEST)
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


