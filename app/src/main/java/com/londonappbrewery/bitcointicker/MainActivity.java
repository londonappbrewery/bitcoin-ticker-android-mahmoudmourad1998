package com.londonappbrewery.bitcointicker;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.*;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

  // Constants:
  private final String TAG = "MainActivity";
  private final String BASE_URL = "https://apiv2.bitcoinaverage.com/indices/global/ticker/BTC";

  // Member Variables:
  private TextView mPriceTextView;
  private Spinner spinner;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mPriceTextView = (TextView) findViewById(R.id.priceLabel);

    spinner = (Spinner) findViewById(R.id.currency_spinner);
    spinner.setOnItemSelectedListener(
      new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
          Log.d(TAG, "item selected: " + adapterView.getItemAtPosition(i).toString());
          final String finalURL= BASE_URL+adapterView.getItemAtPosition(i).toString();
          try {
            letsDoSomeNetworking(finalURL);
          }
          catch (Exception e) {
            e.printStackTrace();
          }
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
          Log.d(TAG, "Nothing");
        }
      }
    );

    // Create an ArrayAdapter using the String array and a spinner layout
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
      R.array.currency_array, R.layout.spinner_item);

    // Specify the layout to use when the list of choices appears
    adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

    // Apply the adapter to the spinner
    spinner.setAdapter(adapter);

  }

  // TODO: complete the letsDoSomeNetworking() method
  private void letsDoSomeNetworking(String url) throws Exception{
    AsyncHttpClient client = new AsyncHttpClient();
    client.get(
      url,
      new JsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
          Log.d(TAG, "JSON Response: " + response.toString());
          try {
            String price= response.getString("last").toString();
            mPriceTextView.setText(price);
          }
          catch (JSONException e) {
            e.printStackTrace();
          }
        }
        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
          Log.d(TAG, "Request fail! Status code: " + statusCode);
          Log.d(TAG, "Fail response: " + errorResponse.toString());
          Log.e(TAG, "ERROR: "+throwable.toString());
          Toast.makeText(MainActivity.this, "Request Failed", Toast.LENGTH_SHORT).show();
        }
      });
  }

}
