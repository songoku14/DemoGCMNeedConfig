/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gcm.play.android.samples.com.gcmquickstart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.ArrayList;

import gcm.play.android.samples.com.gcmquickstart.gcm.RegistrationIntentService;
import gcm.play.android.samples.com.gcmquickstart.support.AdapterListProduct;
import gcm.play.android.samples.com.gcmquickstart.support.ItemProduct;
import gcm.play.android.samples.com.gcmquickstart.support.TaskGetListProduct;
import gcm.play.android.samples.com.gcmquickstart.support.TaskNetworkBase;

public class MainActivity extends AppCompatActivity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";

    private Button btnSubcribe;

    ListView lvItemProduct;
    AdapterListProduct adapterListProduct;
    private View.OnClickListener onSubcribeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (checkPlayServices()) {
                // Start IntentService to register this application with GCM.
                Intent intent = new Intent(MainActivity.this, RegistrationIntentService.class);
//                startService(intent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItemProduct = (ListView) findViewById(R.id.list_item);
        btnSubcribe = (Button) findViewById(R.id.btn_register_push);
        btnSubcribe.setOnClickListener(onSubcribeClickListener);

        getDataFormServer();

    }

    private void getDataFormServer() {
        TaskGetListProduct taskGetListProduct = new TaskGetListProduct(this);
        taskGetListProduct.request(new Response.Listener<ArrayList<ItemProduct>>() {
            @Override
            public void onResponse(ArrayList<ItemProduct> response) {
                adapterListProduct = new AdapterListProduct(MainActivity.this, response);
                lvItemProduct.setAdapter(adapterListProduct);
            }
        }, new TaskNetworkBase.ErrorListener() {
            @Override
            public void onErrorListener(int errorCode, String errorMessage) {
                Toast.makeText(MainActivity.this, "errorCode = " + errorCode + " : " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

}
