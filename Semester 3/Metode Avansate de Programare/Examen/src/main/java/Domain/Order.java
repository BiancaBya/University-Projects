package Domain;

import java.time.LocalDateTime;

public class Order extends Entity<Integer>{

    private int driverID;
    private Status status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String address;
    private String destination;
    private String clientName;

    public Order(Integer integer, int driverID, Status status, LocalDateTime startDate, LocalDateTime endDate, String address, String destination, String clientName) {
        super(integer);
        this.driverID = driverID;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.address = address;
        this.destination = destination;
        this.clientName = clientName;
    }

    public int getDriverID() {
        return driverID;
    }

    public void setDriverID(int driverID) {
        this.driverID = driverID;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    @Override
    public String toString() {
        return "Order{" + "driverID=" + driverID + ", status=" + status + ", startDate=" + startDate + ", endDate=" + endDate + ", address=" + address + ", destination=" + destination + ", clientName=" + clientName + '}';
    }
}
