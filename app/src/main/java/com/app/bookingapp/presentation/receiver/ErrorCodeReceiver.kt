package com.app.bookingapp.presentation.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.app.bookingapp.R
import com.app.bookingapp.presentation.base.BaseActivity
import com.app.bookingapp.data.net.connection.ErrorCode

class ErrorCodeReceiver:BroadcastReceiver() {


    lateinit var baseActivity: BaseActivity

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(">>Received-","=")

       // baseActivity.hideCustomProgrssDialog()
        baseActivity = context as BaseActivity

        var responseCode = intent.getIntExtra("code",0)
        var responseMessage = intent.getStringExtra("message")
        Log.d(">>Received-CODE","="+responseCode)

        if(responseCode == ErrorCode.RESPONSE_ERR) {
            baseActivity.alertDialogWithMsg(
                context!!,
                R.drawable.img_question, "Alert", responseMessage
            )
        }else if(responseCode == ErrorCode.VOLLY_ERR){
            baseActivity.alertDialogWithMsg(
                context!!,
                R.drawable.img_warning, "Error !", ErrorCode.VOLLY_MSG
            )
        }else if(responseCode == ErrorCode.EXCEPTION_ERR){
            baseActivity.alertDialogWithMsg(
                context!!,
                android.R.drawable.ic_dialog_alert, "Error !", ErrorCode.EXCEPTION_MSG
            )
        }else{
            baseActivity.alertDialogWithMsg(
                context!!,
                android.R.drawable.ic_dialog_alert, "Error !", ErrorCode.ERROR_ALERT_MSG
            )
        }

    }
}