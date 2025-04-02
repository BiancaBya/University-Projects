package Service;

import Domain.Driver;
import Domain.Order;
import Domain.Status;
import Repository.Repository;
import Repository.OrderDBRepository;
import Utils.Events.ChangeEventType;
import Utils.Events.EntityChangeEvent;
import Utils.Observer.Observable;
import Utils.Observer.Observer;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Service implements Observable<EntityChangeEvent> {



    private final List<Observer<EntityChangeEvent>> observers = new ArrayList<>();

    private final Repository<Integer, Driver> driverRepo;
    private final OrderDBRepository orderRepo;

    Order partialOrder;

    public Service(Repository<Integer, Driver> driverRepo, OrderDBRepository orderRepo) {
        this.driverRepo = driverRepo;
        this.orderRepo = orderRepo;
    }


    public List<Driver> getAllDrivers(){

        List<Driver> drivers = new ArrayList<>();
        driverRepo.findAll().forEach(drivers::add);
        return drivers;

    }


    public List<Order> getAllDriversOrders(int driverID){

        List<Order> orders = new ArrayList<>();
        for (Order order : orderRepo.findOrdersDriver(driverID)) {
            orders.add(order);
        }
        return orders;

    }

    public Order getOrder(int orderID){
        return orderRepo.findOne(orderID);
    }

    public void modifyOrder(Order order){
        orderRepo.update(order);
    }


    public Driver getBestDriver(){

        long dif = 0;
        int id = 0;

        for (Driver driver : driverRepo.findAll()) {

            boolean ok = true;
            long max = 0;

            for (Order order : orderRepo.findAll()) {

                if (order.getStatus().equals(Status.IN_PROGRESS) && order.getDriverID() == driver.getId())
                    ok = false;
                if (Duration.between(order.getStartDate(), order.getEndDate()).toSeconds() > max && ok){
                    max = Duration.between(order.getStartDate(), order.getEndDate()).toSeconds();
                }
            }

            if (ok && max > dif) {
                dif = max;
                id = driver.getId();
            }
        }

        for (Driver driver : driverRepo.findAll()) {
            if(driver.getId().equals(id))
                return driver;
        }

        return null;
    }

    public void addOrder(String address, String destination, String client){

        Status status = Status.PENDING;
        LocalDateTime startDate = LocalDateTime.now();
        partialOrder = new Order(-1, -1, status, startDate, startDate, address, destination, client);
        EntityChangeEvent event = new EntityChangeEvent(ChangeEventType.ADD, partialOrder);
        notifyObservers(event);

    }

    public void acceptOrder(int driverID){

        partialOrder.setDriverID(driverID);
        partialOrder.setStatus(Status.IN_PROGRESS);
        orderRepo.save(partialOrder);

    }


    @Override
    public void addObserver(Observer<EntityChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<EntityChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(EntityChangeEvent t) {
        observers.stream().forEach(x -> x.update(t));
    }
}


