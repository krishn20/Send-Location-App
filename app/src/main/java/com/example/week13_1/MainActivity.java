package com.example.week13_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView latitude;
    private TextView longitude;
    private TextView provText;
    private TextView choice;
    private Button okButton;
    private CheckBox fineAcc;
    private LocationManager locationManager;
    private String provider;
    private LocationListener myListener;
    private Location location;
    private Criteria criteria;
    private EditText phoneText;
    private EditText emailText;
    private String message;
    private String phone;
    private String email;
    private String Subject = "My Location Co-ordinates";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        provText = findViewById(R.id.provider);
        okButton = findViewById(R.id.button);
        fineAcc = findViewById(R.id.checkBox);
        choice = findViewById(R.id.defaultText);
        phoneText = findViewById(R.id.phoneNumber);
        emailText = findViewById(R.id.email);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fineAcc.isChecked()) {
                    criteria.setAccuracy(Criteria.ACCURACY_FINE);
                    choice.setText("Fine Accuracy Selected");
                } else {
                    criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                    choice.setText("Coarse Accuracy Selected");
                }
            }
        });

        criteria.setCostAllowed(false);
        provider = locationManager.getBestProvider(criteria, false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = locationManager.getLastKnownLocation(provider);
        myListener = new MyLocationListener();

        if(location != null){
            myListener.onLocationChanged(location);
        }
        else{
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        locationManager.requestLocationUpdates(provider, 5000, 1, myListener);

    }

    private class MyLocationListener implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {
            latitude.setText("LATITUDE : " + String.valueOf(location.getLatitude()));
            longitude.setText("LONGITUDE : " + String.valueOf(location.getLongitude()));
            provText.setText(provider + "  provider has been selected");
            Toast.makeText(getApplicationContext(), "Location Changed !", Toast.LENGTH_SHORT).show();
            message = "The location is : \n Latitutde - " + String.valueOf(location.getLatitude()) + "\n Longitude - " + String.valueOf(location.getLongitude());
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Toast.makeText(getApplicationContext(), provider + " 's status changed to " + status + " !", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(), "Provider" + provider + "enabled !", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(), "Provider" + provider + "disabled !", Toast.LENGTH_SHORT).show();
        }
    }

    //SMS Message portion...............................................

    public void smsLocation(View v){
        phone = phoneText.getText().toString();
        if(phone.length()!= 10){
            Toast.makeText(getApplicationContext(),"Enter a valid phone number, please!", Toast.LENGTH_SHORT).show();
        }
        else{
            sendSMS(phone, message);
        }
    }

    private void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber,null,message,null,null);
    }

    @SuppressLint("IntentReset")
    public void emailLocation(View v){
        email = emailText.getText().toString();
        String[] TO = {email};
        if(email.length()==0){
            Toast.makeText(getApplicationContext(),"Enter email address, please!", Toast.LENGTH_SHORT).show();
        }
        else{
            Intent emailIntent = new Intent(Intent.ACTION_SEND);

            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, Subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, message);
            emailIntent.setType("text/plain");
            startActivity(Intent.createChooser(emailIntent,"Email"));
        }
    }

}
