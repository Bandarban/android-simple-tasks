package com.example.lab7

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle


class MainActivity : AppCompatActivity() {
    val manager: WifiP2pManager? by lazy(LazyThreadSafetyMode.NONE) {
        getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager?
    }
    var mChannel: WifiP2pManager.Channel? = null
    var mReceiver: BroadcastReceiver? = null
    lateinit var intentFilter: IntentFilter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mChannel = manager?.initialize(this, mainLooper, null)
        mChannel?.also { channel ->
            mReceiver = WiFiDirectBroadcastReceiver(manager, channel, this)
        }
        intentFilter = IntentFilter().apply {
            addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
            addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
            addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
            addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
        }

        manager?.discoverPeers(mChannel, object : WifiP2pManager.ActionListener {

            override fun onSuccess() {

            }

            override fun onFailure(reasonCode: Int) {

            }
        })
    }

    /* register the broadcast mReceiver with the intent values to be matched */
    override fun onResume() {
        super.onResume()
        mReceiver?.also { receiver ->
            registerReceiver(receiver, intentFilter)
        }
    }

    /* unregister the broadcast mReceiver */
    override fun onPause() {
        super.onPause()
        mReceiver?.also { receiver ->
            unregisterReceiver(receiver)
        }
    }
}

/**
 * A BroadcastReceiver that notifies of important Wi-Fi p2p events.
 */
class WiFiDirectBroadcastReceiver(
    private val manager: WifiP2pManager?,
    private val channel: WifiP2pManager.Channel,
    private val activity: MainActivity

) : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {
        val action: String = intent.action
        when (action) {
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                // Check to see if Wi-Fi is enabled and notify appropriate activity

            }
            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                // Call WifiP2pManager.requestPeers() to get a list of current peers
                manager?.requestPeers(channel) { peers: WifiP2pDeviceList? ->
                    // Handle peers list
                    peers!![]
                }
            }
            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {
                // Respond to new connection or disconnections
            }
            WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {
                // Respond to this device's wifi state changing
            }

        }


    }

}