package com.vaadin.forms.orders.views.endpoints;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.server.connect.Endpoint;
import com.vaadin.flow.server.connect.auth.AnonymousAllowed;
import com.vaadin.flow.server.connect.exception.EndpointException;
import com.vaadin.forms.orders.backend.BackendService;
import com.vaadin.forms.orders.entities.Customer;
import com.vaadin.forms.orders.entities.IdEntity;
import com.vaadin.forms.orders.entities.Order;
import com.vaadin.forms.orders.entities.Product;

/**
 * The endpoint for the client-side List View.
 */
@Endpoint
@AnonymousAllowed
public class OrdersEndpoint {

    @Autowired
    private BackendService service;

    public Orders getOrders(int offset, int limit) {
        List<Order> allEmployees = service.getOrders();
        List<Order> employees = allEmployees.stream().skip(offset).limit(limit).collect(Collectors.toList());
        int totalSize = allEmployees.size();
        return new Orders(employees, totalSize);
    }

    public Optional<Order> getOrder(String id) {
        return getItem(service.getOrders(), id);
    }

    public Order saveOrder(Order item) {
        item.getLines().forEach(line -> {
            if (line.getId() == null) {
                line.setId(BackendService.idLine++);
            }
        });
        return saveItem(service.getOrders(), item);
    }

    public void deleteOrder(Order item) {
        deleteItem(service.getOrders(), item);
    }

    public Optional<Customer> getCustomer(String id) {
        return getItem(service.getCustomers(), id);
    }

    public Customer saveCustomer(Customer item) throws EndpointException {
        return saveItem(service.getCustomers(), item);
    }

    public void deleteCustomer(Customer item) {
        deleteItem(service.getCustomers(), item);
    }

    public List<String> getLocations() {
        return service.getLocations();
    }

    public List<Product> getProducts() {
        return service.getProducts();
    }

    public Optional<Product> getProduct(String id) {
        return getItem(service.getProducts(), id);
    }

    public Product saveProduct(Product item) {
        return saveItem(service.getProducts(), item);
    }

    public void deleteProduct(Product item) {
        deleteItem(service.getProducts(), item);
    }


    public List<String> getTimes() {
        return service.getTimes().stream().map(t -> t.format(DateTimeFormatter.ofPattern("HH:mm:ss")))
                .collect(Collectors.toList());
    }

    private <T extends IdEntity> Optional<T> getItem(List<T> items, String id) {
        return id != null && id.matches("\\d+") ? getItem(items, Long.parseLong(id)) : Optional.empty();
    }

    private <T extends IdEntity> Optional<T> getItem(List<T> items, Long id) {
        return items.stream().filter(item -> item.getId().equals(id)).findFirst();
    }

    private <T extends IdEntity> T saveItem(List<T> items, T item) {
        if (item.getId() != null && item.getId() > 0) {
            T old = getItem(items, item.getId()).orElse(null);
            int idx = items.indexOf(old);
            items.set(idx, item);
            return item;
        }
        item.setId(computeFreeId(items));
        items.add(item);
        return item;
    }

    private <T extends IdEntity> void deleteItem(List<T> items, T item) {
        item = getItem(items, item.getId()).orElse(null);
        if (item != null) {
            int idx = items.indexOf(item);
            items.remove(idx);
        }
    }

    private  <T extends IdEntity> long computeFreeId(List<T> items) {
        return 1l + items.stream().map(IdEntity::getId).sorted().reduce(0l,
                (a, b) -> b - a > 1 ? a : b);
    }

    public static class Orders {
        private List<Order> orders;
        private int totalSize;

        private Orders(List<Order> employees, int totalSize){
            this.orders = new ArrayList<>(employees);
            this.totalSize = totalSize;
        }

        public List<Order> getEmployees(){
            return orders;
        }

        public int getTotalSize(){
            return totalSize;
        }
    }

}
