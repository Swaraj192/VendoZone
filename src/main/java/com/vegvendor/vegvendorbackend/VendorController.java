package com.vegvendor.vegvendorbackend;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
            Vendor v = document.toObject(Vendor.class);
            v.setPassword(null); // never expose passwords through a public endpoint
            vendors.add(v);
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

        // One account per mobile number
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

    @PostMapping("/api/vendor/login")
    public ResponseEntity<Map<String, String>> loginVendor(@RequestBody Map<String, String> credentials) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        String phone = credentials.get("phone");
        String password = credentials.get("password");

        ApiFuture<QuerySnapshot> query = db.collection("vendors")
                .whereEqualTo("phone", phone)
                .get();

        List<QueryDocumentSnapshot> docs = query.get().getDocuments();

        if (docs.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "No account found with this mobile number."));
        }

        Vendor vendor = docs.get(0).toObject(Vendor.class);

        if (password == null || !password.equals(vendor.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Incorrect password."));
        }

        return ResponseEntity.ok(Map.of(
                "vendorId", vendor.getVendorId(),
                "name", vendor.getName(),
                "zoneName", vendor.getZoneName()
        ));
    }
}