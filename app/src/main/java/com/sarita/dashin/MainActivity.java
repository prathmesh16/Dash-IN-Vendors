package com.sarita.dashin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.sarita.dashin.models.ModelMess;
import com.sarita.dashin.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton menu, members, orders, offers, profile, settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        getWindow().setStatusBarColor(getResources().getColor(R.color.pink));

        menu = findViewById(R.id.menu);
        members = findViewById(R.id.members);
        orders = findViewById(R.id.orders);
        offers = findViewById(R.id.offers);
        profile = findViewById(R.id.profile);
        settings = findViewById(R.id.settings);

        TextView tv = findViewById(R.id.rest_name);
        tv.setText(Constants.CurrentUser.getBUSI_NAME());

        menu.setOnClickListener(this);
        members.setOnClickListener(this);
        orders.setOnClickListener(this);
        offers.setOnClickListener(this);
        profile.setOnClickListener(this);
        settings.setOnClickListener(this);

        //start messaging service
        service();
        //send message : first retrieve fcm-token from firestore then send notification to that token
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference fcmKey = db.collection("VENDORS").document(Constants.mAuth.getCurrentUser().getPhoneNumber());
        fcmKey.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null ) {
                      if (!document.get("FCM-TOKEN").equals(""))
                      {
                          JSONObject jsonBody = new JSONObject();
                          JSONObject notification = new JSONObject();
                          try {
                              jsonBody.put("to", document.get("FCM-TOKEN"));
                              notification.put("title", "Title message");
                              notification.put("body", "Body message");
                              jsonBody.putOpt("notification", notification);
                          } catch (JSONException e) {
                              e.printStackTrace();
                          }
                          final String requestBody = jsonBody.toString();
                          RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                          StringRequest sr = new StringRequest(Request.Method.POST,"https://fcm.googleapis.com/fcm/send", new Response.Listener<String>() {
                              @Override
                              public void onResponse(String response) {
                                  Log.d("response ", "Response: " + response);
                              }
                          }, new Response.ErrorListener() {
                              @Override
                              public void onErrorResponse(VolleyError error) {
                                  Log.d("error", "That didn't work..." + error);
                              }
                          }){
                              @Override
                              protected Map<String,String> getParams(){
                                  Map<String,String> params = new HashMap<String, String>();
                                  return params;
                              }

                              @Override
                              public Map<String, String> getHeaders() throws AuthFailureError {
                                  Map<String,String> params = new HashMap<String, String>();
                                  //firebase api key
                                  params.put("Authorization","key=AAAAL9-mXzI:APA91bH-HmV958RjnKRGEo1dgse0yb1qjmoToP0_nPRwSRJcJNvUYs8BhDD4naEInpConrkE73UK5ZZD5nqlxlQpxVdlZLbm-0r9gFWBatbCUn55C1dnmXNbtQ8k-WWAHhGRJeLWHSH1");
                                  return params;
                              }
                              @Override
                              public String getBodyContentType() {
                                  return "application/json; charset=utf-8";
                              }

                              @Override
                              public byte[] getBody() throws AuthFailureError {
                                  try {
                                      return requestBody == null ? null : requestBody.getBytes("utf-8");
                                  } catch (UnsupportedEncodingException uee) {
                                      VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                                      return null;
                                  }
                              }

                          };
                          queue.add(sr);
                      }
                    } else {

                    }
                }
            }
        });



    }

    @Override
    public void onClick(View v) {
        //finish();
        if(v==menu){
            startActivity(new Intent(MainActivity.this, TimeTable.class));
        }else if(v==members){
            startActivity(new Intent(MainActivity.this, SubscriptionsList.class));
        }else if(v==orders){
            startActivity(new Intent(MainActivity.this, vendorOrdersActivity.class));
        }else if(v==offers){
            startActivity(new Intent(MainActivity.this, MyDiscounts.class));
        }else if(v==settings){
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        }else if(v==profile){
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        Constants.mFirestore.collection("VENDORS")
                .document(Constants.mAuth.getCurrentUser().getPhoneNumber()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Constants.CurrentUser = task.getResult().toObject(ModelMess.class);
                    }
                });
    }

    private void service()
    {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.d( "", "getInstanceId failed", task.getException());
                            Toast.makeText(MainActivity.this, "getInstanceId failed "+task.getException(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = "Token : "+token;
                        Log.d("token", msg);
                        FirebaseFirestore db= FirebaseFirestore.getInstance();

                        DocumentReference Ref=db.collection("VENDORS").document( Constants.mAuth.getCurrentUser().getPhoneNumber());
                        Ref.update("FCM-TOKEN",token);
                        //Toast.makeText(Activity_Navigation.this, msg, Toast.LENGTH_SHORT).show();
                    }

                });
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel channel=new NotificationChannel("MyNotification","MyNotification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        //topic wise messages
//        FirebaseMessaging.getInstance().subscribeToTopic("inland")
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        String msg="Successfull!";
//                        if (!task.isSuccessful())
//                        {
//                            msg="Failed!";
//                        }
//                    }
//                });
    }

}
