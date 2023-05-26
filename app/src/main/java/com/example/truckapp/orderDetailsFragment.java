package com.example.truckapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.truckapp.databinding.FragmentOrderDetailsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link orderDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class orderDetailsFragment extends Fragment {

    private FragmentOrderDetailsBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public orderDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment orderDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static orderDetailsFragment newInstance(String param1, String param2) {
        orderDetailsFragment fragment = new orderDetailsFragment();
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
        binding = FragmentOrderDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        Bundle bundle = getArguments();
        if(bundle == null){
            Toast.makeText(getContext(), "Error loading order", Toast.LENGTH_SHORT).show();
            return view;
        }

        OwnerModel order = (OwnerModel) bundle.getSerializable("order");

        binding.detailsOrderIdView.setText("Order Number: " + order.getId());
        binding.detailsDateView.setText("Pickup Date: " + order.getDate());
        binding.detailsTimeView.setText("Pickup Time: " + order.getTime());
        binding.detailsAddressView.setText("Pickup Address:\n" + order.getAddress());
        binding.detailsWeightView.setText("Weight:\n" + order.getWeight());
        binding.detailsGoodsTypeView.setText("Goods Type:\n" + order.getType());
        binding.detailsWidthView.setText("Width:\n" + order.getWidth());
        binding.detailsHeightView.setText("Height:\n" + order.getHeight());
        binding.detailsLengthView.setText("Length:\n" + order.getLength());
        binding.detailsRequestTruckView.setText("Requested Truck:\n" + order.getTruck());
        setImage();

        binding.detailsGetEstimateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EstimateActivity.class);
                intent.putExtra("job", bundle);
                startActivity(intent);
            }
        });

        binding.detailsDeleteJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Confirm Delete")
                        .setMessage("Are you sure wish to delete order with id" + order.getId())
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteOrder(order.getId());
                    }
                }).setNegativeButton(android.R.string.no,null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();


                //deleteOrder(order.getId());
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void setImage() {
        final Bitmap[] bitmap = new Bitmap[1];
        FirebaseUser user;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference(user.getUid().toString()+"/" + "signUpImage");
        try {
            File localFile = File.createTempFile("tempfile",".png");
            storageReference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            bitmap[0] = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            binding.detailsImageView.setImageBitmap(bitmap[0]);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Failed to retrieve image", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void deleteOrder(String orderID){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("orders").document(orderID).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getContext(), "Order Deleted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), LoggedInActivity.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error Deleting Job", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}