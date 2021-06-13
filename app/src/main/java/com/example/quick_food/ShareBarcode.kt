package com.example.quick_food

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import java.io.File
import java.io.FileOutputStream
import java.util.*


class ShareBarcode : AppCompatActivity() {

    var imageQR: ImageView? = null
    var buttonSend: Button? = null
    var qrTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_barcode)
        supportActionBar!!.title = "Order QR code"

        val writer = QRCodeWriter()
        imageQR = findViewById(R.id.weww)
        buttonSend = findViewById(R.id.button_send)
        qrTextView = findViewById(R.id.order_id_text)

        val qrText = intent.getStringExtra("DECRYPTDATA")

        qrTextView!!.text = qrText
        val hints: MutableMap<EncodeHintType, Any> =
            EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)
        hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
        hints[EncodeHintType.MARGIN] = 0 /* default = 4 */

        val bitMatrix = writer.encode(qrText, BarcodeFormat.QR_CODE, 300, 300, hints)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(
                    x,
                    y,
                    if (bitMatrix.get(x, y)) ContextCompat.getColor(
                        this,
                        R.color.blackTextColor
                    ) else Color.WHITE
                )
            }
        }
        imageQR?.setImageBitmap(bitmap)

        buttonSend?.setOnClickListener {
            saveImageExternal(bitmap)

        }
    }


    private fun saveImageExternal(image: Bitmap) {

        var uri: Uri? = null
        try {
            val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "to-share.png")
            val stream = FileOutputStream(file)
            image.compress(Bitmap.CompressFormat.PNG, 90, stream)
            stream.close()
            uri = FileProvider.getUriForFile(
                this,
                "com.example.quick_food.myprovider",
                file
            )
        } catch (e: Exception) {
            Log.d(
                "TAG",
                "IOException while trying to write file for sharing: " + e.printStackTrace()
            )
        }

        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.type = "image/png"
        startActivity(intent)
    }
}