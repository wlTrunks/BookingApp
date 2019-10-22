package com.app.bookingapp.presentation.base

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.app.bookingapp.R
import com.app.bookingapp.presentation.receiver.ConnectivityReceiver
import com.google.android.material.snackbar.Snackbar
import ru.dimorinny.floatingtextbutton.FloatingTextButton


//THIS IS THE BASE ACTIVITY OF ALL ACTIVITIES OF THE APPLICATION.

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {
    private var mSnackBar: Snackbar? = null

    lateinit var broadcastReceiver: BroadcastReceiver
    lateinit var customProgressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        broadcastReceiver = ConnectivityReceiver()
        setPB(this)
    }

    private fun setPB(activity: Context) {
        customProgressDialog = Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar)
        customProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        customProgressDialog.setCancelable(true)
        customProgressDialog.setContentView(R.layout.dialog_custom_progress)
    }

    private fun showMessage(isConnected: Boolean) {


        if (!isConnected) {

            val messageToUser = "You are offline now." //TODO

            mSnackBar = Snackbar.make(
                findViewById(R.id.rootLayout),
                messageToUser,
                Snackbar.LENGTH_INDEFINITE
            ) //Assume "rootLayout" as the root layout of every activity.

            mSnackBar?.show()
        } else {
            mSnackBar?.dismiss()
        }


    }


    protected fun showCustomProgrssDialog() {
        if (!customProgressDialog.isShowing) {
            customProgressDialog.show()
        }
    }

    protected fun hideCustomProgrssDialog() {
        customProgressDialog?.let {
            it.dismiss()
        }
    }


    fun alertDialogWithMsg(activity: Context, image: Int, textHeader: String, textMsg: String) {

        var dialog = Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_alert_msg)
        dialog.show()

        var tvHeader = dialog.findViewById<TextView>(R.id.tv_dlg_header)
        var tvMsg = dialog.findViewById<TextView>(R.id.tv_dlg_msg)
        var imgAlert = dialog.findViewById<ImageView>(R.id.img_dlg_alert)

        tvHeader.setText(textHeader)
        tvMsg.setText(textMsg)
        imgAlert.setImageResource(image)

        var btn_close = dialog.findViewById<FloatingTextButton>(R.id.btn_dlg_close)
        btn_close.setOnClickListener {
            dialog.dismiss()
        }


    }

    fun alertDialogCloseTryWithMsg(
        activity: Context,
        image: Int,
        textHeader: String,
        textMsg: String
    ) {

        var dialog = Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_alert_closetry_msg)
        dialog.show()

        var tvHeader = dialog.findViewById<TextView>(R.id.tv_dlg_header)
        var tvMsg = dialog.findViewById<TextView>(R.id.tv_dlg_msg)
        var imgAlert = dialog.findViewById<ImageView>(R.id.img_dlg_alert)

        tvHeader.setText(textHeader)
        tvMsg.setText(textMsg)
        imgAlert.setImageResource(image)

        var btn_close = dialog.findViewById<FloatingTextButton>(R.id.btn_dlg_close)
        btn_close.setOnClickListener {
            dialog.dismiss()
            finish()
            System.exit(0)
        }


    }

    fun noInternetConnection() {
        Log.d(">>@D-NO INTERNET", "=")
        alertDialogWithMsg(
            this, android.R.drawable.ic_dialog_info,
            "No Connection", "Please check your internet connection"
        )
    }

    override fun onResume() {
        super.onResume()

        registerReceiver(
            broadcastReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )

        ConnectivityReceiver.connectivityReceiverListener = this
    }

    override fun onPause() {
        super.onPause()

        if (broadcastReceiver != null)
            unregisterReceiver(broadcastReceiver)
    }

    /**
     * Callback will be called when there is change
     */
    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showMessage(isConnected)
    }


    override fun onDestroy() {
        super.onDestroy()


    }
}