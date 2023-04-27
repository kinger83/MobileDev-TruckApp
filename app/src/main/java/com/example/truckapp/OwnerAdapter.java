package com.example.truckapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class OwnerAdapter extends RecyclerView.Adapter<OwnerAdapter.OwnerViewHolder> {
    private final Context context;
    private static ArrayList<OwnerModel> ownerList = new ArrayList<>();
    static FragmentActivity fragmentActivity;


    public OwnerAdapter(Context context, ArrayList<OwnerModel> ownerList, FragmentActivity activity) {
        this.context = context;
        this.ownerList = ownerList;
        this.fragmentActivity = activity;
    }
    @NonNull
    @Override
    public OwnerAdapter.OwnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_cardview_layout, parent, false);
        return new OwnerAdapter.OwnerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OwnerAdapter.OwnerViewHolder holder, int position) {
        // to set data to textview and imageview of each card layout
        OwnerModel model = ownerList.get(position);
        holder.user.setText(model.getId());
        holder.date.setText(model.getDate());
        holder.time.setText(model.getTime());
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
                            holder.owenerImage.setImageBitmap(bitmap[0]);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Failed to retrieve image", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }

    @Override
    public int getItemCount() {
        // this method is used for showing number of card items in recycler view
        return ownerList.size();
    }






    public static class OwnerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView owenerImage;
        private final TextView user;
        private final TextView time;
        private final TextView date;

        public OwnerViewHolder(@NonNull View itemView) {
            super(itemView);
            owenerImage = itemView.findViewById(R.id.ownerImage);
            user = itemView.findViewById(R.id.orderIDText);
            time = itemView.findViewById(R.id.pickupTimeText);
            date = itemView.findViewById(R.id.pickupDateText);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view){
            int pos = getLayoutPosition();
            OwnerModel order = ownerList.get(pos);
            Bundle bundle = new Bundle();
            bundle.putSerializable("order", (Serializable) order);

            orderDetailsFragment fragment = new orderDetailsFragment();
            fragment.setArguments(bundle);


            FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                    .replace(R.id.mainDisplay, fragment)
                    .addToBackStack(null);
            fragmentTransaction.commit();
        }
    }


}// end class

