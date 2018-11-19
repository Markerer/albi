package albi.bme.hu.albi.fragments.addhouse

import albi.bme.hu.albi.R
import albi.bme.hu.albi.model.Flat
import albi.bme.hu.albi.model.User
import albi.bme.hu.albi.network.RestApiFactory
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Environment.DIRECTORY_DCIM
import android.os.StrictMode
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
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream

class AddFlatFragment : Fragment() {

    var user: User? = null


    var uploadFlat = Flat()
    private var images: ArrayList<Image> = ArrayList()

    companion object {
        private var MY_PERMISSIONS_REQUEST = 100
        private val REQUEST_CAMERA_IMAGE = 101
        private const val TMP_IMAGE_JPG = "/tmp_image.jpg"
        private val IMAGE_PATH = Environment.getExternalStorageDirectory().absolutePath + TMP_IMAGE_JPG
        private var PICK_IMAGE_FROM_GALERY_REQUEST = 1
        private var PERMISSION_REQUEST_CODE = 200
    }


    private lateinit var uploadImageButton: Button
    private lateinit var priceLayout: TextInputLayout
    private lateinit var numberOfRoomsLayout: TextInputLayout
    private lateinit var descriptionLayout: TextInputLayout
    private lateinit var addressLayout: TextInputLayout
    private lateinit var uploadButtonAdvert: Button
    private lateinit var takePhotoButton: Button
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

        iv = view.findViewById(R.id.imageView2)

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

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    fun getRealPathFromURI(uri: Uri): String {
        val cursor = context!!.contentResolver.query(uri, null, null, null, null)
        cursor.moveToFirst()
        val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        return cursor.getString(idx)
    }

    /**
     * https://stackoverflow.com/questions/15432592/get-file-path-of-image-on-android
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CAMERA_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            try {
                /*data?.also {
                 bitmap = it.extras?.get("data") as Bitmap
             }*/
                Log.i("data.data.path", data.data.path)
                bitmapUri = data.data
                bitmap = MediaStore.Images.Media.getBitmap(this.activity!!.contentResolver, data.data)

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                val tempUri: Uri = getImageUri(this.context!!, this.bitmap!!)

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                finalFile = File(getRealPathFromURI(tempUri))
                LogFinalFilePath()

                iv.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(context, "Photo error: " + e.printStackTrace(), Toast.LENGTH_LONG).show()
            }
        } else if (requestCode == PICK_IMAGE_FROM_GALERY_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            try {
                /*data?.also {
                    bitmap = it.extras?.get("data") as Bitmap
                }*/
                bitmapUri = data.data
                bitmap = MediaStore.Images.Media.getBitmap(this.activity!!.contentResolver, data.data)

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                val tempUri: Uri = getImageUri(this.context!!, this.bitmap!!)

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                finalFile = File(getRealPathFromURI(tempUri))

                iv.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(context, "Photo error: " + e.printStackTrace(), Toast.LENGTH_LONG).show()
            }
        }
        sendNetworkUploadPhoto("5be82cefdcba3e22b8bb0411")
    }


    private fun sendNetworkUploadPhoto(flatID: String) {
        val client = RestApiFactory.createFlatClientPhoto()
        //val IMAGE_PATH = Environment.getExternalStorageDirectory().absolutePath
        //val FULL_PATH = IMAGE_PATH + bitmapUri?.path
        //val file = File(IMAGE_PATH)
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
        LogFinalFilePath()
        val tmpFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath + "/" + "Camera" + "/" + "IMG_20181119_123054.jpg")
        Log.i("absolutePath", finalFile.absolutePath)
        val reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), finalFile) //finalFile
        val body = MultipartBody.Part.createFormData("image", "image", reqFile)

        val call = client.uploadPhoto(flatID, body)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(context, "Upload failed " + t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                LogFinalFilePath()
                Toast.makeText(context, "uploading photo was successfull", Toast.LENGTH_LONG).show()
            }
        })

    }

    private fun sendNetworkRequestAdvertisement() {
        if (user != null) {
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
            val builder = StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageToUploadUri).also { takePictureIntent ->
                    takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                        startActivityForResult(takePictureIntent, REQUEST_CAMERA_IMAGE)
                    }
                }
            }
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

    /**
     * https://stackoverflow.com/questions/32329461/how-to-get-path-of-picture-in-onactivityresult-intent-data-is-null
     */
    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun getBitmap(path: String): Bitmap? {
        val uri = Uri.fromFile(File(path))
        var inputStream: InputStream?
        try {
            val IMAGE_MAX_SIZE = 1200000 // 1.2MP
            inputStream = context!!.contentResolver.openInputStream(uri)
            // Decode image size
            var o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            BitmapFactory.decodeStream(inputStream, null, o)
            inputStream.close()
            var scale = 1
            while (((o.outWidth * o.outHeight) * (1 / Math.pow(scale.toDouble(), 2.0)) > IMAGE_MAX_SIZE)) {
                scale++
            }
            Log.d("", "scale = " + scale + ", orig-width: " + o.outWidth + ", orig-height: " + o.outHeight)
            var b: Bitmap?
            inputStream = context!!.contentResolver.openInputStream(uri)
            if (scale > 1) {
                scale--
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = BitmapFactory.Options()
                o.inSampleSize = scale
                b = BitmapFactory.decodeStream(inputStream, null, o)
                // resize to desired dimensions
                val height = b.getHeight()
                val width = b.getWidth()
                Log.d("", "1th scale operation dimenions - width: " + width + ", height: " + height)
                val y = Math.sqrt((IMAGE_MAX_SIZE / ((width.toDouble()) / height)))
                val x = (y / height) * width
                val scaledBitmap = Bitmap.createScaledBitmap(b, x.toInt(),
                        y.toInt(), true)
                b.recycle()
                b = scaledBitmap
                System.gc()
            } else {
                b = BitmapFactory.decodeStream(inputStream)
            }
            inputStream.close()
            Log.d("", ("bitmap size - width: " + b.getWidth() + ", height: " +
                    b.getHeight()))
            return b
        } catch (e: IOException) {
            Log.e("", e.message, e)
            return null
        }
    }

    private fun LogFinalFilePath() {
        Log.i("finalFile.path", finalFile.path)
    }

}

