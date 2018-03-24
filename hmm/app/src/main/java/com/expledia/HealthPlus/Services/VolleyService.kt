package com.expledia.HealthPlus.Services

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

/**
 * Created by nimish on 24/3/18.
 */
object VolleyService
{
    fun getDisease(context: Context, diseases:ArrayList<Int>, complete: (Boolean) -> Unit)
    {

        val jsonBody = JSONObject()
        jsonBody.put("symptoms",diseases)
        val requestBody = jsonBody.toString()
        Log.i("mytag","request body is "+requestBody.toString())

        val registerRequest = object : StringRequest(Method.POST, "http://192.168.43.76:2345/fetch/", Response.Listener { response ->
            Log.i("mytag","response is "+response.toString())
            complete(true)
        }, Response.ErrorListener { error ->
            Log.i("mytag", "error is "+" $error")
            complete(false)
        })
        {
            override fun getBodyContentType(): String
            {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray
            {
                return requestBody.toByteArray()
            }
        }
        Volley.newRequestQueue(context).add(registerRequest)
    }
}