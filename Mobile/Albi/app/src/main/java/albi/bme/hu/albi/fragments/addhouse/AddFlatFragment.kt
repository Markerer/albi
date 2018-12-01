package albi.bme.hu.albi.fragments.addhouse

import albi.bme.hu.albi.R
import albi.bme.hu.albi.model.Flat
import albi.bme.hu.albi.model.User
import albi.bme.hu.albi.network.RestApiFactory
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.support.design.widget.TextInputLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.add_house_fragment.*
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

    private var uploadFlat = Flat()

    companion object {
        private const val REQUEST_CAMERA_IMAGE = 101
        private var PICK_IMAGE_FROM_GALLERY_REQUEST = 1
        private var PERMISSION_REQUEST_CODE = 200

        private const val TMP_IMAGE_JPG = "/tmp_image.jpg"
        private val IMAGE_PATH = Environment.getExternalStorageDirectory().absolutePath + TMP_IMAGE_JPG
    }


    private lateinit var uploadImageButton: Button
    private lateinit var priceLayout: TextInputLayout
    private lateinit var numberOfRoomsLayout: TextInputLayout
    private lateinit var descriptionLayout: TextInputLayout
    private lateinit var etAddress: EditText
    private lateinit var etCity: EditText
    private lateinit var etZipCode: EditText
    private lateinit var uploadButtonAdvert: Button
    private lateinit var takePhotoButton: Button
    private lateinit var forSale: ToggleButton
    private lateinit var finalFile: File

    private lateinit var iv: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.add_house_fragment, container, false)
        uploadImageButton = view.findViewById(R.id.uploadimagebutton)
        priceLayout = view.findViewById(R.id.price_upload)
        numberOfRoomsLayout = view.findViewById(R.id.numberofrooms_upload)
        descriptionLayout = view.findViewById(R.id.description_upload)
        etAddress = view.findViewById(R.id.etAddressAdd)
        etCity = view.findViewById(R.id.etCityAdd)
        etZipCode = view.findViewById(R.id.etZipCodeAdd)
        uploadButtonAdvert = view.findViewById(R.id.addhouse)
        takePhotoButton = view.findViewById(R.id.takephoto)
        forSale = view.findViewById(R.id.toggleForRent)

        iv = view.findViewById(R.id.imageView2)


        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        uploadImageButton.setOnClickListener {
            requestNeededPermissionForGallery()
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

    private fun isEmpty(et: EditText): Boolean{
        return et.apply{
            if (this.text.isEmpty()) error = "This field must be filled"
        }.text.isEmpty()
    }

    private fun setButtonUpload() {
        uploadButtonAdvert.setOnClickListener {

            var valid = true

            if (!isEmpty(priceLayout.editText!!)) uploadFlat.price = priceLayout.editText!!.text.toString()
            else valid = false

            if (!isEmpty(numberOfRoomsLayout.editText!!)) uploadFlat.numberOfRooms = numberOfRoomsLayout.editText!!.text.toString()
            else valid = false

            if (!isEmpty(descriptionLayout.editText!!)) uploadFlat.description = descriptionLayout.editText!!.text.toString()
            else valid = false

            if (!isEmpty(etZipCode)) uploadFlat.zipCode = etZipCode.text.toString()
            else valid = false

            if (!isEmpty(etAddress)) uploadFlat.address = etAddress.text.toString()
            else valid = false

            if (!isEmpty(etCity)) uploadFlat.city = etCity.text.toString()
            else valid = false

            uploadFlat.phone_number = user!!.phone_number
            uploadFlat.email = user!!.email
            uploadFlat.forSale = forSale.isChecked

            if (valid) {
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
        } else if (requestCode == PICK_IMAGE_FROM_GALLERY_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            try {

                Glide.with(context!!)
                        .load(Uri.fromFile(File(IMAGE_PATH)))
                        .into(imageView2)

                finalFile = File(data.data.path)

            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(context, "Photo error: " + e.printStackTrace(), Toast.LENGTH_LONG).show()
            }
        }
        //sendNetworkUploadPhoto("5be60d3c2be1db3bc01c2184")
    }


    private fun sendNetworkUploadPhoto(flatID: String) {
        val client = RestApiFactory.createFlatClientPhoto()


        val tmpFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath + "/" + "Camera" + "/" + "IMG_20181129_151450.jpg")
        val reqFile = RequestBody.create(MediaType.parse("image/png"), finalFile) //finalFile, multipart/form-data"
        val body = MultipartBody.Part.createFormData("image", finalFile.name, reqFile)

        val call = client.uploadPhoto(flatID, body, User.token!!)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(context, "Upload failed " + t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Toast.makeText(context, "uploading photo was successful", Toast.LENGTH_LONG).show()
            }
        })

    }

    private fun sendNetworkUploadAdvertisement() {
        val client = RestApiFactory.createFlatClient()
        val call = client.uploadFlat(user?._id!!, uploadFlat, User.token!!)

        call.enqueue(object : Callback<Flat> {
            override fun onResponse(call: retrofit2.Call<Flat>, response: Response<Flat>) {
                Toast.makeText(context, "Advertisement upload was successful", Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: retrofit2.Call<Flat>?, t: Throwable?) {
                t?.printStackTrace()
                Toast.makeText(context, "error in uploading advertisement: " + t?.message, Toast.LENGTH_LONG).show()
            }
        })
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
    private fun requestNeededPermissionForGallery() {
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
            startActivityForResult(Intent.createChooser(intent, "select picture"), PICK_IMAGE_FROM_GALLERY_REQUEST)
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

}
