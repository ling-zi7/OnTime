package com.example.ontime;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import static android.content.Context.MODE_PRIVATE;

/**
 * This is a class for adding a fragment in driver class to show the request detail
 */
public class AddFragment extends DialogFragment {
    private TextView username;
    private TextView phone;
    private TextView start;
    private TextView end;
    private TextView amount;
    private TextView email;
    private OnFragmentInteractionListener listener;
    private CurrentRequests current_request;
    private FirebaseFirestore db;
    private String userName;
    private String srcLocationText;
    private String destinationText;
    private String pay_amount;
    private String phoneText;
    private String emailText;
    private String srcCoordinate;
    private String dstCoordinate;
    private String TAG = "ActiveRequestDetail";
    public String driverName;
    public SharedPreferences sharedPreferences1;
    public SharedPreferences sharedPreferences2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DriverMapActivity activity = (DriverMapActivity) getActivity();
        assert activity != null;
        driverName = activity.getDriver();
        return inflater.inflate(R.layout.request_details, container, false);

    }

    /**
     * This interface is used for check accept button and refresh the fragment
     */
    public interface OnFragmentInteractionListener {
        void onOkPressed(RequestList new_request);
        //void refresh();
    }

    /**
     * This class makes sure that the container context has implemented
     * the callback interface. If not, it throws an exception
     * @param context container
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     *
     * @param requests current request
     * @return A new instance of fragment AddFragment
     */
    public static AddFragment newInstance(CurrentRequests requests){
        Bundle args = new Bundle();
        args.putSerializable("Request",requests);

        AddFragment fragment = new AddFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * This bundle will be passed to onCreate if the process is killed and restarted.
     * @param saveInstanceState Save UI state changes to the savedInstanceState.
     * @return builder
     */
    @Nullable
    @Override
    public Dialog onCreateDialog(@Nullable Bundle saveInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.request_details, null);
        username = view.findViewById(R.id.name_text);
        email = view.findViewById(R.id.email_text);
        phone = view.findViewById(R.id.phone_text);
        start = view.findViewById(R.id.srclocation_text);
        end = view.findViewById(R.id.destination_text);
        amount = view.findViewById(R.id.amount_text);
        Bundle arguments = getArguments();
        if (arguments != null) {
            current_request  = (CurrentRequests)arguments.getSerializable("Request");
            username.setText(current_request.getName());
            username.getPaint().setFakeBoldText(true);
            username.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            phone.setText(current_request.getPhone());
            phone.getPaint().setFakeBoldText(true);
            phone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            email.setText(current_request.getEmail());
            email.getPaint().setFakeBoldText(true);
            email.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            start.setText(current_request.getSrcLocation());
            start.getPaint().setFakeBoldText(true);
            start.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            end.setText(current_request.getDestination());
            end.getPaint().setFakeBoldText(true);
            end.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            amount.setText(current_request.getAmount());
            amount.getPaint().setFakeBoldText(true);
            amount.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        }

        userName = username.getText().toString();
        srcLocationText = start.getText().toString();
        destinationText = end.getText().toString();
        phoneText = phone.getText().toString();
        emailText = email.getText().toString();
        pay_amount = amount.getText().toString();

        srcCoordinate = current_request.getSrcCoordinate();
        dstCoordinate = current_request.getDstCoordinate();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        current_request.setStatus("Accepted");
                        SharedPreferences sharedPreferences;
                        db = FirebaseFirestore.getInstance();
                        final CollectionReference collectionReference = db.collection("Requests");
                        Map<String, Object> data = new HashMap<>();
                        data.put("status", "Accepted");
                        data.put("driver", driverName);

                        sharedPreferences1 =getActivity().getSharedPreferences("current_request",MODE_PRIVATE);
                        SharedPreferences.Editor editor1=sharedPreferences1.edit();
                        editor1.putString("driver_name",driverName);
                        editor1.commit();

                        sharedPreferences2 =getActivity().getSharedPreferences("driver_current_request",MODE_PRIVATE);
                        SharedPreferences.Editor editor2=sharedPreferences1.edit();
                        editor2.putString("driver_name",driverName);
                        editor2.putString("rider",userName);
                        editor2.putString("srcLocationText",srcLocationText);
                        editor2.putString("destinationText",destinationText);
                        editor2.putString("rider_phone_number",phoneText);
                        editor2.commit();


                        collectionReference
                                .document(userName)
                                .update(data)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "Data modification successful");
                                        // after accepting the request ...

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "Data modification failed" + e.toString());
                                    }
                                });
                        Intent intent = new Intent(getActivity(),DriverMapActivity.class);
                        intent.putExtra("srcCoordinate",srcCoordinate);
                        intent.putExtra("dstCoordinate",dstCoordinate);

                        Intent intent2=new Intent(getActivity(), OLDriver_CR.class);
                        intent2.putExtra("username", userName);
                        startActivity(intent2);

                    }


                }).create();


    }
}