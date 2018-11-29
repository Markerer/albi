package albi.bme.hu.albi.fragments.addhouse

import albi.bme.hu.albi.R
import albi.bme.hu.albi.model.Flat
import albi.bme.hu.albi.model.User
import albi.bme.hu.albi.network.RestApiFactory
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
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
import android.widget.ToggleButton
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.add_house_fragment.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File
import java.io.IOException

class AddFlatFragment : Fragment() {

    var user: User? = null


    var uploadFlat = Flat()
    private var images: ArrayList<Image> = ArrayList()

    companion object {
        private var MY_PERMISSIONS_REQUEST = 100
        private val REQUEST_CAMERA_IMAGE = 101
        private var PICK_IMAGE_FROM_GALERY_REQUEST = 1
        private var PERMISSION_REQUEST_CODE = 200

        private const val TMP_IMAGE_JPG = "/tmp_image.jpg"
        private val IMAGE_PATH = Environment.getExternalStorageDirectory().absolutePath + TMP_IMAGE_JPG
    }


    private lateinit var uploadImageButton: Button
    private lateinit var priceLayout: TextInputLayout
    private lateinit var numberOfRoomsLayout: TextInputLayout
    private lateinit var descriptionLayout: TextInputLayout
    private lateinit var addressLayout: TextInputLayout
    private lateinit var uploadButtonAdvert: Button
    private lateinit var takePhotoButton: Button
    private lateinit var forSale: ToggleButton
    private var bitmapUri: Uri? = null
    private var bitmap: Bitmap? = null
    private var imageFile = File(Environment.getExternalStorageDirectory().absolutePath.toString())
    private var imageToUploadUri: Uri = Uri.fromFile(imageFile)
    private lateinit var finalFile: File


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
        forSale = view.findViewById(R.id.toggleForRent)

        iv = view.findViewById(R.id.imageView2)

        /**
         * https://stackoverflow.com/questions/38200282/android-os-fileuriexposedexception-file-storage-emulated-0-test-txt-exposed
         */
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        uploadImageButton.setOnClickListener {
            requestNeededPermissionForGalery()
        }

        takePhotoButton.setOnClickListener {
            requestNeededPermissionForWriteExternalStorage()
            requestNeededPermissionForCamera()
        }

        setButtonUpload()

        /*takePhotoButton.setOnClickListener {
            val imageFile = File(IMAGE_PATH)
            val imageFileUri = Uri.fromFile(imageFile)
            val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri)
            startActivityForResult(cameraIntent, REQUEST_CAMERA_IMAGE)
        }*/

        return view
    }

    private fun setButtonUpload() {
        uploadButtonAdvert.setOnClickListener {
            if (priceLayout.editText!!.text.isEmpty()) {
                priceLayout.editText!!.error = "This field must be filled!"
            } else {
                uploadFlat.price = priceLayout.editText!!.text.toString()
            }

            if (numberOfRoomsLayout.editText!!.text.isEmpty()) {
                numberOfRoomsLayout.editText!!.error = "This field must be filled!"
            } else {
                uploadFlat.numberOfRooms = numberOfRoomsLayout.editText!!.text.toString()
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
            uploadFlat.forSale = forSale.isChecked

            if (!priceLayout.editText!!.text.isEmpty() &&
                    !numberOfRoomsLayout.editText!!.text.isEmpty() &&
                    !descriptionLayout.editText!!.text.isEmpty() &&
                    !addressLayout.editText!!.text.isEmpty()) {
                sendNetworkUploadAdvertisement()
            }
        }
    }

    /**
     * https://stackoverflow.com/questions/15432592/get-file-path-of-image-on-android
     */
    @SuppressLint("CheckResult")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CAMERA_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            try {
                Glide.with(context!!)
                        .load(Uri.fromFile(File(IMAGE_PATH)))
                        .into(imageView2)

                finalFile = File(IMAGE_PATH)

            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(context, "Photo error: " + e.printStackTrace(), Toast.LENGTH_LONG).show()
            }
        } else if (requestCode == PICK_IMAGE_FROM_GALERY_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            try {

                Glide.with(context!!)
                        .load(Uri.fromFile(File(IMAGE_PATH)))
                        .into(imageView2)

                finalFile = File(IMAGE_PATH)

            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(context, "Photo error: " + e.printStackTrace(), Toast.LENGTH_LONG).show()
            }
        }
        sendNetworkUploadPhoto("5be60d3c2be1db3bc01c2184")
        //tryWithAutHerokuapp()
    }


    private fun sendNetworkUploadPhoto(flatID: String) {
        val client = RestApiFactory.createFlatClientPhoto()

        /**
         * original parameter: finalFile !!!!!!
         * kíváncsiságból megnéztem, de a natúr elérési útvonallal sem akarja feltölteni
         * postmanbe ezzel sem jelenik meg
         * /storage/emulated/0/Pictures/1542150123646.jpg --- >Pictures
         * /storage/emulated/0/DCIM/Camera/IMG_20181113_213409.jpg --->Camera
         *
         * kamera feltöltéshez vissza kell állítani a paramétert
         * finalFile-ra
         */
        val tmpFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath + "/" + "Camera" + "/" + "IMG_20181129_151450.jpg")
        val reqFile = RequestBody.create(MediaType.parse("image/jpeg"), tmpFile) //finalFile, multipart/form-data"
        val body = MultipartBody.Part.createFormData("image", "image", reqFile)
        val nameParam = RequestBody.create(okhttp3.MultipartBody.FORM, finalFile.name)
        var map = HashMap<String, RequestBody>()
        map.put("image", reqFile)


        val call = client.uploadPhoto(flatID, map, User.token!!)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(context, "Upload failed " + t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Toast.makeText(context, "uploading photo was successfull", Toast.LENGTH_LONG).show()
            }
        })

    }

    private fun sendNetworkUploadAdvertisement() {
        if (user != null) {
            val client = RestApiFactory.createFlatClient()
            val call = client.uploadFlat(user?._id!!, uploadFlat, User.token!!)

            call.enqueue(object : Callback<Flat> {
                override fun onResponse(call: retrofit2.Call<Flat>, response: Response<Flat>) {
                    sendNetworkUploadPhoto(response.body()!!._id)
                    Toast.makeText(context, "advertisement upload was successfull", Toast.LENGTH_LONG).show()
                }

                override fun onFailure(call: retrofit2.Call<Flat>?, t: Throwable?) {
                    t?.printStackTrace()
                    Toast.makeText(context, "error in uploading advertisement: " + t?.message, Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    private fun requestNeededPermissionForWriteExternalStorage() {
        if (ContextCompat.checkSelfPermission(context!!,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(context, "I need it for write storage", Toast.LENGTH_SHORT).show()
            }
            ActivityCompat.requestPermissions(activity!!,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE)
        }
        return
    }

    // https://www.aut.bme.hu/Upload/Course/android/hallgatoi_jegyzetek/Android_05.pdf
    private fun requestNeededPermissionForGalery() {
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

    private fun requestNeededPermissionForCamera() {
        if (ContextCompat.checkSelfPermission(context!!,
                        android.Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!,
                            android.Manifest.permission.CAMERA)) {
                Toast.makeText(context, "I need it for camera", Toast.LENGTH_SHORT).show()
            }
            ActivityCompat.requestPermissions(activity!!,
                    arrayOf(android.Manifest.permission.CAMERA),
                    PERMISSION_REQUEST_CODE)
        } else {
            /**
             * https://stackoverflow.com/questions/48117511/exposed-beyond-app-through-clipdata-item-geturi
             * https://stackoverflow.com/questions/32329461/how-to-get-path-of-picture-in-onactivityresult-intent-data-is-null
             */
            /*
            val builder = StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageToUploadUri).also { takePictureIntent ->
                    takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                        startActivityForResult(takePictureIntent, REQUEST_CAMERA_IMAGE)
                    }
                }
            }
            */
            val imageFile = File(IMAGE_PATH)
            val imageFileUri = Uri.fromFile(imageFile)
            val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri)
            startActivityForResult(cameraIntent, REQUEST_CAMERA_IMAGE)
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


    fun tryWithAutHerokuapp() {
        val MULTIPART_FORM_DATA = "multipart/form-data"
        val PHOTO_MULTIPART_KEY_IMG = "image"
        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath + "/" + "Camera" + "/" + "IMG_20181118_013510.jpg")

        val requestFile = RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), file)
        val body = MultipartBody.Part.createFormData(PHOTO_MULTIPART_KEY_IMG, file.name, requestFile)

        val nameParam = RequestBody.create(okhttp3.MultipartBody.FORM, "name")
        val descriptionParam = RequestBody.create(okhttp3.MultipartBody.FORM, "description")

        val client = GalleryInteractor.createSomething()
        val call = client.uploadImage(body, nameParam, descriptionParam)

        call.enqueue(object : Callback<ResponseBody>{
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(context, "Nem jóság van itten", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            }
        })
    }
}


class GalleryInteractor {

    companion object {

        fun createSomething(): GalleryAPI {
            val retrofit = Retrofit.Builder()
                    .baseUrl(GalleryAPI.ENDPOINT_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            return retrofit.create(GalleryAPI::class.java)
        }
    }
}

interface GalleryAPI {

    companion object {
        const val ENDPOINT_URL = "https://aut-android-gallery.herokuapp.com/api/"

    }

    @GET("images")
    fun getImages(): Call<List<Image>>

    @Multipart
    @POST("upload")
    fun uploadImage(@Part file: MultipartBody.Part,
                    @Part("name") name: RequestBody,
                    @Part("description") description: RequestBody): Call<ResponseBody>

}