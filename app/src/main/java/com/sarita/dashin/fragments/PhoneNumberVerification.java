package com.sarita.dashin.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.sarita.dashin.MainActivity;
import com.sarita.dashin.R;
import com.sarita.dashin.models.ModelMess;
import com.sarita.dashin.utils.Constants;

import java.util.concurrent.TimeUnit;


public class PhoneNumberVerification extends Fragment {

    EditText number, otp;
    ImageButton next;
    ImageView horizontalLine;
    Boolean flag=false;
    private String mVerificationId;
    final String TAG = "phoneNumberVerification";

    PhoneAuthProvider.ForceResendingToken mResendToken;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    public PhoneNumberVerification() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_phone_number_verification, container, false);

        number = view.findViewById(R.id.number);
        otp =  view.findViewById(R.id.otp);
        next = view.findViewById(R.id.phone_number_next_btn);
        horizontalLine = view.findViewById(R.id.horizontal_line);

        onConfirmClicked();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                String code = credential.getSmsCode();
                Log.d(TAG, "onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);

                if (code != null) {
                    otp.setText(code);
                    //verifying the code
                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                    Log.e(TAG,"invalid");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                    Log.e(TAG,"sms quota exceeded");
                    Toast.makeText(getActivity(), "SMS quota exceeded. Please try again after some time.",
                            Toast.LENGTH_LONG).show();
                }
                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d("vds", "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // ...
            }
        };

        return view;
    }

    private void onConfirmClicked() {

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(flag==false){
                    //accept phone number
                    otp.setVisibility(View.VISIBLE);
                    horizontalLine.setVisibility(View.VISIBLE);
                    flag = true;
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + number.getText().toString(),        // Phone number to verify
                        100,                 // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        getActivity(),               // Activity (for callback binding)
                        mCallbacks);        // OnVerificationStateChangedCallbacks

                    number.setEnabled(false);
                    number.setTextColor(getResources().getColor(R.color.pink_layout));
                }
                else {

                    if (!otp.getText().toString().isEmpty()) {
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp.getText().toString());
                        //signing the user
                        signInWithPhoneAuthCredential(credential);
                    } else {
                        Toast.makeText(getActivity(), "Please enter a valid OTP.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        Constants.mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            Log.d(TAG, "signInWithCredential:success");
                            Constants.mFirestore.collection("VENDORS").document("+91" + number.getText().toString())
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        DocumentSnapshot ds=task.getResult();
                                        Constants.CurrentUser.setOWNER_CONTACT("+91" + number.getText().toString());
                                        if(ds.exists())
                                        {
                                            Constants.CurrentUser = ds.toObject(ModelMess.class);
                                            getActivity().finish();
                                            Intent intent = new Intent(getActivity(), MainActivity.class);
                                            startActivity(intent);
                                            Log.d(TAG, "signInWithCredential:success");
                                        }
                                        else
                                        {
                                            FragmentTransaction ft1 = getActivity().getSupportFragmentManager().beginTransaction();
                                            ft1.replace(R.id.main_frame_1, new OwnerInfoFragment(), "");
                                            ft1.commit();
                                            Log.d(TAG, "signInWithCredential:success");
                                        }
                                }
                            });

                        }
                        else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });

    }

}
