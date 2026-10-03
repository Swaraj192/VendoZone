package com.vegvendor.vegvendorbackend;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import java.util.List;


public class TestDataWriter implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        Firestore db = FirestoreClient.getFirestore();

        Vendor vendor = new Vendor("v1", "Ramesh Bhaji Stall", "9999999999", "test123", "MG Road Market", true);

        db.collection("vendors").document(vendor.getVendorId()).set(vendor);

        System.out.println("Vendor written to Firestore successfully!");
        ApiFuture<QuerySnapshot> future = db.collection("vendors").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for(QueryDocumentSnapshot document : documents){
            Vendor v = document.toObject(Vendor.class);
            System.out.println("Found Vendor: " + v.getName() + "in Zone" + v.getZoneName());
        }
    }
}