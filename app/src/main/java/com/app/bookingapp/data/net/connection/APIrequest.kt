package com.app.bookingapp.data.net.connection

import org.json.JSONObject
import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.AuthFailureError
import java.lang.String.format
import com.android.volley.DefaultRetryPolicy
import com.android.volley.VolleyLog
import com.android.volley.VolleyError
import com.app.bookingapp.data.net.VolleySingleton
import com.app.bookingapp.presentation.base.BaseActivity
import com.app.bookingapp.data.net.connection.ErrorCode.EXCEPTION_ERR
import com.app.bookingapp.data.net.connection.ErrorCode.VOLLY_ERR
import com.app.bookingapp.presentation.receiver.ErrorCodeHandler


class APIrequest {


    var mapobject: Map<String, String>? = null
    private var type: String? = null
    private var activity: Activity? = null
    private var onApiResponse: OnApiResponse? = null
    private var networkurl: String? = null
    private var jsonObject: JSONObject? = null
    private var postJSONObject: JSONObject? = null
    var method: Int = 0
    var token: String? = null
    var context:Context? = null

    var postMapObject:Map<String,String>? = null

    var baseActivity: BaseActivity? = null

    constructor(baseActivity: BaseActivity, activity:Activity, context: Context, onApiResponse: OnApiResponse,
                type:String, networkurl:String, method:Int, token:String
    ){
        this.baseActivity = baseActivity
        this.activity = activity
        this.onApiResponse = onApiResponse
        this.type = type
        this.networkurl = networkurl
        this.method = method
        this.token = token
        this.context = context

        executeTaskAPI()

    }


    /*constructor(activity:Activity, context: Context, onApiResponse:OnApiResponse,
                type:String, networkurl:String, method:Int, token:String, postJSONObject:JSONObject
    ){
        this.activity = activity
        this.onApiResponse = onApiResponse
        this.type = type
        this.networkurl = networkurl
        this.method = method
        this.token = token
        this.postJSONObject = postJSONObject
        this.context = context

        executeTaskWithObject()
    }*/

    constructor(baseActivity: BaseActivity, activity:Activity, context: Context, onApiResponse: OnApiResponse,
                type:String, networkurl:String, method:Int, token:String, postMapObject:Map<String,String>
    ){
        this.baseActivity = baseActivity
        this.activity = activity
        this.onApiResponse = onApiResponse
        this.type = type
        this.networkurl = networkurl
        this.method = method
        this.token = token
        this.postMapObject = postMapObject
        this.context = context

        executeTaskWithMapObject()
    }





    fun executeTaskWithMapObject(){
        val request = object : StringRequest(method,
            networkurl,
            object : Response.Listener<String> {
                override fun onResponse(response: String) {
                    Log.d("@R>","Response: ${format(response.toString())}")
                    onApiResponse?.onSuccess(response, type!!)
                    createJSONResponse(response, type!!)
                }
            },
            object : Response.ErrorListener{

                override fun onErrorResponse(error: VolleyError) {
                    Log.d("@Err>","Response: "+error)
                    VolleyLog.d("volley", "Error: " + error.message)

                    error.printStackTrace()
                    //onApiResponse?.onFailure(VOLLY_ERR, ""+error.message)
                    ErrorCodeHandler.showErrorDialog(context!!, baseActivity!!, VOLLY_ERR, ""+error.message)
                }
            }) {

            /*override fun getBodyContentType(): String {
                //return "application/x-www-form-urlencoded; charset=UTF-8"
                return "application/form-data; charset=UTF-8"
            }*/

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {

                val params = postMapObject
                return params!!
            }

        }


       // Log.d("@R>","REQUEST: "+request.body.size)
       // Log.d("@R>","DATAS "+postMapObject)

        // Volley request policy, only one time request to avoid duplicate transaction
        request.retryPolicy = DefaultRetryPolicy(
            60*1000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        // Add the volley post request to the request queue
        VolleySingleton.getInstance(context!!).addToRequestQueue(request)


    }


    fun executeTaskAPI(){
        val request = object : StringRequest(method,
            networkurl,
            object : Response.Listener<String> {
                override fun onResponse(response: String) {
                    Log.d("@R>","Response: ${format(response.toString())}")
//                     onApiResponse?.onSuccess(response, type!!)
                    createJSONResponse(response, type!!)
                }
            },
            object : Response.ErrorListener{

                override fun onErrorResponse(error: VolleyError) {
                    Log.d("@Err>","Response: "+error)
                    VolleyLog.d("volley", "Error: " + error.message)
                    error.printStackTrace()
                    //onApiResponse?.onFailure(VOLLY_ERR, ""+error.message)
                    //Utils.sendToErrorBroadCast(context!!,""+error.message, VOLLY_ERR)
                    ErrorCodeHandler.showErrorDialog(context!!, baseActivity!!, VOLLY_ERR, ""+error.message)
                }
            }) {

            override fun getBodyContentType(): String {
                return "application/x-www-form-urlencoded; charset=UTF-8"
            }

            /*@Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {

                val params = postMapObject
                return params!!
            }*/

        }
        // Volley request policy, only one time request to avoid duplicate transaction
        request.retryPolicy = DefaultRetryPolicy(
            60*1000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        // Add the volley post request to the request queue
        VolleySingleton.getInstance(context!!).addToRequestQueue(request)


    }

    fun createJSONResponse(response:String, type:String){
        try {
        //var jsonObject = JSONObject(response)
        /*var status = jsonObject.optString("status")
            if(status.equals("error",true))
            {
                //onApiResponse?.onFailure(RESPONSE_ERR, jsonObject.optString("message"))
                //Utils.sendToErrorBroadCast(context!!,jsonObject.optString("message"), RESPONSE_ERR)
                ErrorCodeHandler.showErrorDialog(context!!, baseActivity!!, RESPONSE_ERR, jsonObject.optString("message"))

            }else{
                onApiResponse?.onSuccess(response, type!!)
            }*/
            onApiResponse?.onSuccess(response, type!!)

        }catch (e:Exception){
            onApiResponse?.onFailure(EXCEPTION_ERR,""+e.message)
            ErrorCodeHandler.showErrorDialog(context!!, baseActivity!!, EXCEPTION_ERR, ""+e.message)
        }

    }



}

