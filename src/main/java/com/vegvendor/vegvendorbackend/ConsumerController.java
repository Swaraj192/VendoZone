package com.vegvendor.vegvendorbackend;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@CrossOrigin(origins = "*")
public class ConsumerController {

    @PostMapping("/api/consumer")
    public ResponseEntity<String> registerConsumer(@RequestBody Consumer consumer) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        ApiFuture<QuerySnapshot> existing = db.collection("consumers")
                .whereEqualTo("phone", consumer.getPhone())
                .get();

        if (!existing.get().getDocuments().isEmpty()) {
            return ResponseEntity.badRequest().body("This mobile number is already registered.");
        }

        String consumerId = UUID.randomUUID().toString();
        consumer.setConsumerId(consumerId);

        db.collection("consumers").document(consumerId).set(consumer);

        return ResponseEntity.ok(consumerId);
    }

    @PostMapping("/api/consumer/login")
    public ResponseEntity<String> loginConsumer(@RequestBody Map<String, String> credentials) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        String phone = credentials.get("phone");
        String password = credentials.get("password");

        ApiFuture<QuerySnapshot> query = db.collection("consumers")
                .whereEqualTo("phone", phone)
                .get();

        List<QueryDocumentSnapshot> docs = query.get().getDocuments();

        if (docs.isEmpty()) {
            return ResponseEntity.status(401).body("No account found with this mobile number.");
        }

        Consumer consumer = docs.get(0).toObject(Consumer.class);

        if (!consumer.getPassword().equals(password)) {
            return ResponseEntity.status(401).body("Incorrect password.");
        }

        return ResponseEntity.ok(consumer.getConsumerId());
    }
}