package com.example.truckapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.bumptech.glide.Glide;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TruckAdapter extends RecyclerView.Adapter<TruckAdapter.TruckViewHolder> {
    private final Context context;
    private final ArrayList<TruckModel> truckList;


    public TruckAdapter(Context context, ArrayList<TruckModel> truckList) {
        this.context = context;
        this.truckList = truckList;
    }
    @NonNull
    @Override
    public TruckAdapter.TruckViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.truck_cardview_layout, parent, false);
        return new TruckAdapter.TruckViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TruckAdapter.TruckViewHolder holder, int position) {
        // to set data to textview and imageview of each card layout
        TruckModel model = truckList.get(position);
        holder.truckName.setText(model.getTruckOwnerName());
        holder.truckCapacity.setText(model.getTruckCapacity());
        holder.truckCost.setText(model.getTruckCost());
        String truckUrl = model.getTruckImage();
        final Bitmap[] bitmap = new Bitmap[1];
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference(model.getTruckOwnerName() + "/truck.png");
        try {
            File localFile = File.createTempFile("tempfile",".png");
            storageReference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            bitmap[0] = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            holder.truckImage.setImageBitmap(bitmap[0]);
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
        return truckList.size();
    }






    public static class TruckViewHolder extends RecyclerView.ViewHolder {
        private final ImageView truckImage;
        private final TextView truckName;
        private final TextView truckCapacity;
        private final TextView truckCost;

        public TruckViewHolder(@NonNull View itemView) {
            super(itemView);
            truckImage = itemView.findViewById(R.id.truckImage);
            truckName = itemView.findViewById(R.id.truckOwnerName);
            truckCapacity = itemView.findViewById(R.id.truckCapacity);
            truckCost = itemView.findViewById(R.id.truckCost);
        }
    }


}// end class
