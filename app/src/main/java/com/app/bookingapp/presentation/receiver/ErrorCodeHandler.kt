package com.app.bookingapp.presentation.receiver

import android.content.Context
import com.app.bookingapp.R
import com.app.bookingapp.presentation.base.BaseActivity
import com.app.bookingapp.data.net.connection.ErrorCode

object ErrorCodeHandler {

    fun showErrorDialog(context:Context, baseActivity: BaseActivity, responseCode:Int, responseMessage:String){


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