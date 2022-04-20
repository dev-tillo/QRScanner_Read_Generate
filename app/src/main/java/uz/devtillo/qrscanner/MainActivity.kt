package uz.devtillo.qrscanner

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.appcompat.app.AlertDialog
import com.google.zxing.integration.android.IntentIntegrator
import uz.devtillo.qrscanner.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        scanCode()

        binding.btn1.setOnClickListener {
            getbarCode()
        }
    }

    private fun getbarCode() {
        val data = binding.tvedit.text.toString()
        var encode = QRGEncoder(data, null, QRGContents.Type.TEXT, 800)
        binding.qrcode.setImageBitmap(encode.bitmap)
    }

    private fun scanCode() {
        var intenegrator: IntentIntegrator = IntentIntegrator(this)
        intenegrator.captureActivity = CaptureAct::class.java
        intenegrator.setOrientationLocked(false)
        intenegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
        intenegrator.setPrompt("Scanning code")
        intenegrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                var alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
                alertDialog.setMessage(result.contents)
                alertDialog.setTitle("Scanning result")
                val negativeButton = alertDialog.setPositiveButton(
                    "Scan Again"
                ) { _, _ -> scanCode() }
                    .setNegativeButton("finish", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            finish()
                        }
                    })
                var alert = alertDialog.create()
                alert.show()
            } else {
                Toast.makeText(this, "No result", Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}