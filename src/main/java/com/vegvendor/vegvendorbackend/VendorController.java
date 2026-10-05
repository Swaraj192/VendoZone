package com.vegvendor.vegvendorbackend;
import org.springframework.http.ResponseEntity;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@CrossOrigin(origins = "*")
public class VendorController {

    @GetMapping("/api/vendors")
    public List<Vendor> getAllVendors() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        ApiFuture<QuerySnapshot> future = db.collection("vendors").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        List<Vendor> vendors = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            vendors.add(document.toObject(Vendor.class));
        }

        return vendors;
    }

    @PostMapping("/api/vendor/{vendorId}/stock")
    public String addStock(@PathVariable String vendorId, @RequestBody StockItem stockItem) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        db.collection("vendors")
                .document(vendorId)
                .collection("stock")
                .document(stockItem.getItemName())
                .set(stockItem);

        return "Stock item added successfully for vendor: " + vendorId;
    }

    @GetMapping("/api/vendor/{vendorId}/stock")
    public List<StockItem> getVendorStock(@PathVariable String vendorId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        ApiFuture<QuerySnapshot> future = db.collection("vendors")
                .document(vendorId)
                .collection("stock")
                .get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        List<StockItem> stockItems = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            stockItems.add(document.toObject(StockItem.class));
        }

        return stockItems;
    }

    @PostMapping("/api/vendor")
    public ResponseEntity<String> registerVendor(@RequestBody Vendor vendor) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        // Check if this phone number is already registered
        ApiFuture<QuerySnapshot> existing = db.collection("vendors")
                .whereEqualTo("phone", vendor.getPhone())
                .get();

        if (!existing.get().getDocuments().isEmpty()) {
            return ResponseEntity.badRequest().body("This mobile number is already registered.");
        }

        String vendorId = UUID.randomUUID().toString();
        vendor.setVendorId(vendorId);
        vendor.setActive(true);

        db.collection("vendors").document(vendorId).set(vendor);

        return ResponseEntity.ok(vendorId);
    }
    }
