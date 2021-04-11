package com.sayyed.onlineclothingapplication.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import com.sayyed.onlineclothingapplication.R
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class FileUpload: AppCompatActivity() {
    companion object {
        fun loadPopUpMenu(activity: AppCompatActivity, context: Context, showMenuOn:ImageView) {
            val popupMenu = PopupMenu(context, showMenuOn)
            popupMenu.menuInflater.inflate(R.menu.gallery_camera, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menuCamera -> {
                        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(activity, cameraIntent, 0, null)
                    }

                    R.id.menuGallery -> {
                        val intent = Intent(Intent.ACTION_PICK)
                        intent.type = "image/*"
                        startActivityForResult(activity, intent, 1, null)
                    }
                }
                true
            }
            popupMenu.show()
        }

        private fun getMimeType(file: File): String? {
            var type: String? = null
            val extension = MimeTypeMap.getFileExtensionFromUrl(file.path)
            if (extension != null) {
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            }
            return type
        }

        fun setMimeType(imageUrl: String?): MultipartBody.Part {
            val file = File(imageUrl!!)
            val mimeType = getMimeType(file);
            val reqFile =
                    file.asRequestBody(mimeType!!.toMediaTypeOrNull())
            return MultipartBody.Part.createFormData("image", file.name, reqFile)
        }


        fun bitmapToFile(
                bitmap: Bitmap,
                pathname: String,
                fileNameToSave: String
        ): File? {
            var file: File? = null
            return try {
                file = File(pathname + File.separator + fileNameToSave)
                file.createNewFile()

                //Convert bitmap to byte array
                val bos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
                val bitMapData = bos.toByteArray()
                //write the bytes in file
                val fos = FileOutputStream(file)
                fos.write(bitMapData)
                fos.flush()
                fos.close()
                file

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                file // it will return null
            }
        }

        /*--------------------------------------------CHECK HTTPS-----------------------------------------------------*/
        fun checkImageString(imgString: String): String {
            if (imgString.contains("uploads/")) {
                return "http://192.168.1.69:90/$imgString"
            }
            return imgString
        }

    }
}