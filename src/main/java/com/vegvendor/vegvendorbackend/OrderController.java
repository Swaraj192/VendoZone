package com.vegvendor.vegvendorbackend;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@CrossOrigin(origins = "*")
public class OrderController {

    @PostMapping("/api/order")
    public String placeOrder(@RequestBody Order order) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        String orderId = UUID.randomUUID().toString();
        order.setOrderId(orderId);
        order.setStatus("placed");

        db.collection("orders").document(orderId).set(order);

        return "Order placed successfully! Order ID: " + orderId;
    }
}