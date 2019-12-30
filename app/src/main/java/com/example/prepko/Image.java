package com.example.prepko;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import static java.lang.Integer.parseInt;

public class Image {
    private StorageReference storageRef= FirebaseStorage.getInstance().getReference();
    private  FirebaseFirestore db = FirebaseFirestore.getInstance();
    String imgName;
    String price;
    String desc;
    int picCode;
    boolean showImg;
    String details;

    public Image(String imgName) {
        this.imgName = imgName;
        this.details="";
        this.desc="";
        this.price="0";
        this.showImg=false;
        this.picCode=0;
        getMetaData();
        if(picCode!=0)
            getPicData();
    }

    private void getMetaData() {
// Get reference to the file
        StorageReference forestRef = storageRef.child(imgName);
        Task<StorageMetadata> a = forestRef.getMetadata();

        while (!a.isSuccessful()) { }

        if (a.getResult().getCustomMetadata("PicCode") != null)
            picCode = parseInt(a.getResult().getCustomMetadata("PicCode"));

    }



    private void getPicData() {
        int _picCode=0;
        Task<QuerySnapshot> a = db.collection("mealKit").get();
        while (!a.isSuccessful()) { }
        for (QueryDocumentSnapshot document : a.getResult()) {
            Log.d("image Class", document.getId() + " => " + document.getData());
            Object temp = document.getData().get("picCode");
            if (temp != null) {
                _picCode = parseInt(temp.toString());
                if (picCode == _picCode) {
                    temp = document.getData().get("desc");
                    if (temp != null)
                        desc = temp.toString();

                    temp = document.getData().get("price");
                    if (temp != null)
                        price = temp.toString();

                    temp = document.getData().get("details");
                    if (temp != null)
                        details = temp.toString();

                    temp = document.getData().get("showImg");
                    if (temp != null)
                        showImg = (boolean) temp;

                }
            }
        }


    }
}
