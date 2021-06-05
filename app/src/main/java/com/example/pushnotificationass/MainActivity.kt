package com.example.pushnotificationass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException


class MainActivity : AppCompatActivity() {
    private lateinit var requestQueue: RequestQueue
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSignUp!!.setOnClickListener {
            val data = "{" +
                    "\"firstName\"" + ":" + "\"" + fName!!.text.toString() + "\"," +
                    "\"lastName\"" + ":" + "\"" + lName!!.text.toString() + "\"," +
                    "\"email\"" + ":" + "\"" + emailSignUp!!.text.toString() + "\"," +
                    "\"password\"" + ":" + "\"" + passSignUp!!.text.toString() + "\"" +
                    "}"

            SubmitData(data)
            getRegesterToken()

            var intent = Intent(this, SignInActivity::class.java)
            intent.putExtra("email", emailSignUp!!.text.toString())
            intent.putExtra("password", passSignUp!!.text.toString())
            startActivity(intent)
        }

    }

    private fun SubmitData(data: String) {
        val URL = "https://mcc-users-api.herokuapp.com/add_new_user"

        requestQueue = Volley.newRequestQueue(applicationContext)
        Log.d("TAG", "requestQueue: $requestQueue")

        val stringRequest: StringRequest = object : StringRequest(Request.Method.POST, URL, Response.Listener<String?> { response ->
            try {
                val objres = JSONObject(response)
                Log.d("TAG", "onResponse: $objres")
            } catch (e: JSONException) {
                Log.d("TAG", "Server Error ")
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

    fun getRegesterToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("dna", "Failed to get token ${task.exception}")

                return@addOnCompleteListener
            }
            var token = task.result
            val data = "{" +
                    "\"email\"" + ":" + "\"" + emailSignUp!!.text.toString() + "\"," +
                    "\"password\"" + ":" + "\"" + passSignUp!!.text.toString() + "\"" +
                    "\"reg_token\"" + ":" + "\"" + token!! + "\"," +
                    "}"
            val URL = "https://mcc-users-api.herokuapp.com/add_reg_token"

            requestQueue = Volley.newRequestQueue(applicationContext)
            Log.d("TAG", "requestQueue: $requestQueue")

            val stringRequest: StringRequest = object : StringRequest(Method.PUT, URL, Response.Listener<String?> { response ->
                try {
                    val objresponse = JSONObject(response)
                    Log.d("TAG", "onResponse: $objresponse")
                } catch (e: JSONException) {
                    Log.d("TAG", "Server Error ")
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
            Log.e("dnaTok", token!!)

        }
    }


}