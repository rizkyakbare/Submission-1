package com.example.storyapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.example.storyapp.data.Results
import com.example.storyapp.databinding.ActivityAddStoryBinding
import com.example.storyapp.util.LoadingDialog
import com.example.storyapp.util.SHARED_PREF_NAME
import com.example.storyapp.util.TOKEN
import com.example.storyapp.util.showMessage
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class AddStoryActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var loading: LoadingDialog
    private var uri: Uri? = null
    private val CAPTURE_PHOTO = 1
    private val CHOOSE_PHOTO = 2
    private var file: File? = null

    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val viewModel: AddStoryViewModel by viewModels {
        factory
    }

    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        initlayout()
    }
    
    private fun initlayout(){
        loading = LoadingDialog(this)
        
        with(binding){
            edtDesc.setOnFocusChangeListener{_, _ -> layoutDesc.error = null}
            btnBack.setOnClickListener { finish() }
            btnAddPhoto.setOnClickListener { showPopup() }
            btnUpload.setOnClickListener { uploadStory() }
        }
    }
    
    private val requestCameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
            capturePhoto()
        }
    
    private val requestGalleryPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
            openGallery()
        }
    
    private fun checkGalleryPermission(){
        val checkSelfPermission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (checkSelfPermission != PackageManager.PERMISSION_GRANTED){
            requestGalleryPermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        else{
            openGallery()
        }
    }
    
    private fun checkCameraPermission(){
        val checkSelfPermission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.CAMERA)
        if (checkSelfPermission != PackageManager.PERMISSION_GRANTED){
            requestCameraPermission.launch(Manifest.permission.CAMERA)
        }
        else{
            capturePhoto()
        }
    }
    
    private fun showPopup(){
        val popupMenu = PopupMenu(this, binding.btnAddPhoto)
        popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
        
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.capture -> {
                    checkCameraPermission()
                    true
                }
                R.id.pick -> {
                    checkGalleryPermission()
                    true
                }
                else -> false
            }
        }
        
        popupMenu.show()
    }
    
    private fun capturePhoto(){
        file = File(externalCacheDir, "My_Captured_Photo.jpg")
        if(file!!.exists()) {
            file!!.delete()
        }
        file!!.createNewFile()
        uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider",
                file!!)
    
        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intent, CAPTURE_PHOTO)
    }
    
    private fun openGallery(){
        val intent = Intent("android.intent.action.GET_CONTENT")
        intent.type = "image/*"
        startActivityForResult(intent, CHOOSE_PHOTO)
    }
    
    private fun renderImage(imagePath: String?){
        if (imagePath != null) {
            val bitmap = BitmapFactory.decodeFile(imagePath)
            Glide.with(this)
                .load(bitmap)
                .into(binding.ivPlaceholder)
        }
        else {
            showMessage(this, "ImagePath is null")
        }
    }
    
    private fun uploadStory(){
        val description = binding.edtDesc.text.toString()
        
        binding.layoutDesc.error = viewModel.validateDescription(description)
        
        if(!binding.layoutDesc.error.isNullOrEmpty()){
            showMessage(this, "Please review your input")
        } else if (file == null) {
            showMessage(this, "Photo is empty")
        } else {
            reduceSize()
            file?.let { it ->
                loading.show()
                
                val requestFile = it.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val image = MultipartBody.Part.createFormData(
                    "photo",
                    it.name,
                    requestFile
                )
        
                val token = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE).getString(TOKEN, "")
        
                val authorization = "Bearer $token"

                viewModel.addStory(authorization, description, image).observe(this) {
                    when(it) {
                        is Results.Loading -> {
                            loading.show()
                        }
                        is Results.Success -> {
                            loading.dismiss()
                            showMessage(applicationContext, it.data.message)

                            if(!it.data.error) {
                                finish()
                            }
                        }
                        is Results.Error -> {
                            loading.dismiss()
                            showMessage(this@AddStoryActivity, it.error)
                        }
                    }
                }
            }
        }
    }
    
    private fun handleImage(data: Intent?) {
        var imagePath: String? = null
        val uri = data!!.data
        
        if (DocumentsContract.isDocumentUri(this, uri)){
            val docId = DocumentsContract.getDocumentId(uri)
            if ("com.android.providers.media.documents" == uri!!.authority){
                val id = docId.split(":")[1]
                val selsetion = MediaStore.Images.Media._ID + "=" + id
                imagePath = getImagePath(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    selsetion)
            }
            else if ("com.android.providers.downloads.documents" == uri.authority){
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse(
                        "content://downloads/public_downloads"), java.lang.Long.valueOf(docId))
                imagePath = getImagePath(contentUri, null)
            }
        }
        else if ("content".equals(uri!!.scheme, ignoreCase = true)){
            imagePath = getImagePath(uri, null)
        }
        else if ("file".equals(uri.scheme, ignoreCase = true)){
            imagePath = uri.path
        }
        
        file = File(imagePath.toString())
        renderImage(imagePath)
    }
    
    private fun reduceSize(){
        try {
            
            // BitmapFactory options to downsize the image
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            o.inSampleSize = 6
            // factor of downsizing the image
            var inputStream = FileInputStream(file)
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o)
            inputStream.close()
            
            // The new size we want to scale to
            val REQUIRED_SIZE = 75
            
            // Find the correct scale value. It should be the power of 2.
            var scale = 1
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                o.outHeight / scale / 2 >= REQUIRED_SIZE
            ) {
                scale *= 2
            }
            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            inputStream = FileInputStream(file)
            val selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2)
            inputStream.close()
            
            // here i override the original image file
            file?.createNewFile()
            val outputStream = FileOutputStream(file)
            selectedBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            file
        } catch (e: Exception) {
            null
        }
    }
    
    
    @SuppressLint("Range")
    private fun getImagePath(uri: Uri?, selection: String?): String {
        var path: String? = null
        val cursor = contentResolver.query(uri!!, null, selection, null, null )
        if (cursor != null){
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
        return path!!
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data != null) {
            when(requestCode){
                CAPTURE_PHOTO ->
                    if (resultCode == Activity.RESULT_OK) {
                        val bitmap = BitmapFactory.decodeStream(
                            contentResolver.openInputStream(uri!!))
                        binding.ivPlaceholder.setImageBitmap(bitmap)
                    }
                CHOOSE_PHOTO ->
                    if (resultCode == Activity.RESULT_OK) {
                        handleImage(data)
                    }
            }
        }
    }
}