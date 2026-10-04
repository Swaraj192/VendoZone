package com.vegvendor.vegvendorbackend;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
public class OrderController {

    @PostMapping("/api/order")
    public String placeOrder(@RequestBody Order order) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        // Fetch the vendor's current stock once, up front
        ApiFuture<QuerySnapshot> stockFuture = db.collection("vendors")
                .document(order.getVendorId())
                .collection("stock")
                .get();

        List<QueryDocumentSnapshot> stockDocs = stockFuture.get().getDocuments();

        // Validate every ordered item against that real stock
        List<QueryDocumentSnapshot> matchedDocs = new ArrayList<>();

        for (OrderItem orderItem : order.getItems()) {

            QueryDocumentSnapshot matchedDoc = null;

            for (QueryDocumentSnapshot doc : stockDocs) {
                StockItem stockItem = doc.toObject(StockItem.class);
                if (stockItem.getItemName().equalsIgnoreCase(orderItem.getItemName())) {
                    matchedDoc = doc;
                    break;
                }
            }

            if (matchedDoc == null) {
                return "Order failed: " + orderItem.getItemName() + " is not available from this vendor.";
            }

            StockItem matchedStock = matchedDoc.toObject(StockItem.class);

            if (orderItem.getQty() > matchedStock.getQuantityKg()) {
                return "Order failed: only " + matchedStock.getQuantityKg() + " kg of " + orderItem.getItemName() + " available.";
            }

            matchedDocs.add(matchedDoc);
        }

        // Every item checks out — now actually place the order
        String orderId = UUID.randomUUID().toString();
        order.setOrderId(orderId);
        order.setStatus("placed");

        db.collection("orders").document(orderId).set(order);

        // Reduce stock by the ordered quantity for each item
        for (int i = 0; i < order.getItems().size(); i++) {
            OrderItem orderItem = order.getItems().get(i);
            StockItem matchedStock = matchedDocs.get(i).toObject(StockItem.class);
            double newQty = matchedStock.getQuantityKg() - orderItem.getQty();
            matchedDocs.get(i).getReference().update("quantityKg", newQty);
        }

        return "Order placed successfully! Order ID: " + orderId;
    }

    @GetMapping("/api/vendor/{vendorId}/orders")
    public List<Order> getOrdersForVendor(@PathVariable String vendorId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        ApiFuture<QuerySnapshot> future = db.collection("orders")
                .whereEqualTo("vendorId", vendorId)
                .get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        List<Order> orders = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            orders.add(document.toObject(Order.class));
        }

        return orders;
    }

    @PatchMapping("/api/order/{orderId}/status")
    public String updateOrderStatus(@PathVariable String orderId, @RequestBody Map<String, String> body) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        String newStatus = body.get("status");

        db.collection("orders").document(orderId).update("status", newStatus);

        return "Order " + orderId + " status updated to: " + newStatus;
    }
}