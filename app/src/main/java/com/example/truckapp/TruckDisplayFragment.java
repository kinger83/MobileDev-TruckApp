package com.example.truckapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TruckDisplayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TruckDisplayFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<TruckModel> truckList = new ArrayList<>();


    public TruckDisplayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TruckDisplayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TruckDisplayFragment newInstance(String param1, String param2) {
        TruckDisplayFragment fragment = new TruckDisplayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_truck_display, container, false);
        populateTrucksFromDB(view);


        // Inflate the layout for this fragment
        return view;
    }

    private void populateTrucksFromDB(View view){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("trucks").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        truckList = new ArrayList<>();
                        for(QueryDocumentSnapshot docSnapshot : queryDocumentSnapshots){
                            TruckModel truckModel = docSnapshot.toObject(TruckModel.class);
                            Map doc = docSnapshot.getData();
                            truckModel.setTruckImage(doc.get("truckUrl").toString());
                            truckList.add(truckModel);
                        }
                        RecyclerView truckRV = view.findViewById(R.id.truckRecycler);
                        TruckAdapter truckAdapter = new TruckAdapter(getContext(), truckList);
                        LinearLayoutManager linearlayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

                        truckRV.setLayoutManager(linearlayoutManager);
                        truckRV.setAdapter(truckAdapter);


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error retrieving truck list", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}