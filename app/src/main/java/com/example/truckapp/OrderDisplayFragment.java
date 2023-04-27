package com.example.truckapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderDisplayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderDisplayFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<OwnerModel> orderList = new ArrayList<>();
    private ArrayList<OwnerModel> allOrderList = new ArrayList<>();

    public OrderDisplayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderDisplayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderDisplayFragment newInstance(String param1, String param2) {
        OrderDisplayFragment fragment = new OrderDisplayFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_display, container, false);
        populateUserListFromDb(view);
        return view;
    }

    private void populateUserListFromDb(View view){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db.collection("orders").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        orderList = new ArrayList<>();
                        allOrderList = new ArrayList<>();
                        for(QueryDocumentSnapshot docSnapshot : queryDocumentSnapshots){
                            OwnerModel ownerModel = docSnapshot.toObject(OwnerModel.class);
                            ownerModel.setId(docSnapshot.getId());
                            allOrderList.add(ownerModel);
                        }
                        for(int i = 0; i < allOrderList.size(); i++){
                            if(allOrderList.get(i).getOwner().equals(user.getUid())){
                                orderList.add(allOrderList.get(i));
                            }
                        }

                        RecyclerView orderRC = view.findViewById(R.id.oderRecyclerView);
                        OwnerAdapter ownerAdapter = new OwnerAdapter(getContext(), orderList);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

                        orderRC.setLayoutManager(linearLayoutManager);
                        orderRC.setAdapter(ownerAdapter);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Failed to retrieve orders", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}