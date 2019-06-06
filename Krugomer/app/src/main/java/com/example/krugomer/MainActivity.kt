package com.example.krugomer
import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PointF
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.support.multidex.MultiDex
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList
import kotlin.math.abs


class MainActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private val TAG = "MainActivity"
    private lateinit var mGoogleApiClient: GoogleApiClient
    private var mLocationManager: LocationManager? = null
    lateinit var mLocation: Location
    private var mLocationRequest: LocationRequest? = null
    private val UPDATE_INTERVAL = (2 * 1000).toLong()  /* 10 secs */
    private val FASTEST_INTERVAL: Long = 2000 /* 2 sec */
    lateinit var textView: TextView

    lateinit var locationManager: LocationManager

    private var points: ArrayList<PointF> = arrayListOf()
    private var isRecording: Boolean = false

    fun startRecord(view: View){
        isRecording = true
    }

    override fun onStart() {
        super.onStart()
        mGoogleApiClient.connect()
    }

    override fun onStop() {
        super.onStop()
        if (mGoogleApiClient.isConnected) {
            mGoogleApiClient.disconnect()
        }
    }

    override fun onConnectionSuspended(p0: Int) {

        Log.i(TAG, "Connection Suspended")
        mGoogleApiClient.connect()
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.errorCode)
    }

    override fun onLocationChanged(location: Location) {


        var msg = "Updated Location: Latitude " + location.longitude.toString()+ " " + location.latitude.toString()

        if (isRecording){
            if (points.size == 0){
                points.add(PointF(location.latitude.toFloat(), location.longitude.toFloat()))
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                return
            }
            if (PointF(location.latitude.toFloat(), location.longitude.toFloat()) != points[points.size-1]){
                points.add(PointF(location.latitude.toFloat(), location.longitude.toFloat()))
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            } else return
            if (points[0].equals(location.latitude.toFloat(), location.longitude.toFloat())){
                isRecording = false
                checkCircle()
            }
        }



    }
    fun checkCircle(){
        var avgDist: Float
        var xCenter: Float = 0f
        var yCenter: Float = 0f

        for (i in points){
            xCenter += i.x
            yCenter += i.y
        }
        xCenter /= points.size
        yCenter /= points.size

        val centerPoint: PointF = PointF(xCenter, yCenter)
        var avgDistance: Float = 0f

        for (i in points){
            avgDistance += distance(i, centerPoint)
        }
        avgDistance /= points.size
        var dispersion: Float = 0f

        for (i in points){
            dispersion += abs((distance(i, centerPoint)-avgDistance))
        }
        dispersion /= points.size
        if (dispersion/avgDistance < 0.1) {
            textView.text = "На круг похоже " + (dispersion/avgDistance).toString() + " " + dispersion + " " + avgDistance
        } else textView.text = "На круг не похоже " + (dispersion/avgDistance).toString() + " " + dispersion + " " + avgDistance
        points = arrayListOf()
    }

    fun distance(a: PointF, b:PointF): Float{
        return Math.sqrt(Math.pow((a.x - b.x).toDouble(), 2.0) +
                Math.pow((a.y - b.y).toDouble(), 2.0)).toFloat()
    }
    override fun onConnected(p0: Bundle?) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return
        }


        startLocationUpdates()

        val fusedLocationProviderClient :
                FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient .lastLocation
            .addOnSuccessListener(this, OnSuccessListener<Location> { location ->
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    // Logic to handle location object
                    mLocation = location

                }
            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.textView)
        MultiDex.install(this)

        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()

        mLocationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        checkLocation()
    }

    private fun checkLocation(): Boolean {
        if(!isLocationEnabled())
            showAlert()
        return isLocationEnabled()
    }

    private fun isLocationEnabled(): Boolean {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun showAlert() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Enable Location")
            .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " + "use this app")
            .setPositiveButton("Location Settings", DialogInterface.OnClickListener { paramDialogInterface, paramInt ->
                val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(myIntent)
            })
            .setNegativeButton("Cancel") { _, _ -> }
        dialog.show()
    }

    protected fun startLocationUpdates() {

        // Create the location request
        mLocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(UPDATE_INTERVAL)
            .setFastestInterval(FASTEST_INTERVAL)
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
            mLocationRequest, this)
    }
}