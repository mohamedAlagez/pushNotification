package com.example.pushnotificationass

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException

class SignInActivity : AppCompatActivity() {
    private lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)


        if (intent != null) {

            var ema = intent.getStringExtra("email").toString()
            var password = intent.getStringExtra("password").toString()

            btnSignIn!!.setOnClickListener {
                val data = "{" +
                        "\"email\"" + ":" + "\"" + emailSignIn!!.text.toString() + "\"," +
                        "\"password\"" + ":" + "\"" + passSignIn!!.text.toString() + "\"" +
                        "}"
                if (emailSignIn!!.text.toString() == ema && passSignIn!!.text.toString() == password) {
                    SubmitData(data)
                } else {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun SubmitData(data: String) {

        val URL = "https://mcc-users-api.herokuapp.com/login"

        requestQueue = Volley.newRequestQueue(applicationContext)
        Log.d("TAG", "requestQueue: $requestQueue")

        val stringRequest: StringRequest = object :
            StringRequest(Request.Method.POST, URL, Response.Listener<String?> { response ->
                try {
                    val objresponse = JSONObject(response)

                    Log.d("TAG", "onResponse: $objresponse")
                    Toast.makeText(this, "onResponse: $objresponse", Toast.LENGTH_LONG).show()
                } catch (e: JSONException) {
                    Log.d("TAG", "Error in server")
                }
            }, Response.ErrorListener { error -> Log.d("TAG", "Response Error: $error") }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray {
                return try {

                    Log.d("TAG", "savedata: $data")
                    if (data == null) null else data.toByteArray(charset("utf-8"))
                } catch (uee: UnsupportedEncodingException) {
                    null
                }!!
            }
        }
        requestQueue!!.add(stringRequest)
    }
}