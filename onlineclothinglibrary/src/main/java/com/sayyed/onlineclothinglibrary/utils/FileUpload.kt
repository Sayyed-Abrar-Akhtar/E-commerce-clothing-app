package com.sayyed.onlineclothinglibrary.utils


import androidx.appcompat.app.AppCompatActivity

class FileUpload: AppCompatActivity() {
    companion object {
        /*--------------------------------------------CHECK HTTPS-----------------------------------------------------*/
        fun checkImageString(imgString: String): String {
            if(imgString.contains("90/uploads/")) {
                return imgString
            }
            else if (imgString.contains("uploads/")) {
                return "http://192.168.1.69:90/$imgString"
            }
            return imgString
        }

    }
}