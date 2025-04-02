package Utils.Events;

import Domain.Order;

public class EntityChangeEvent implements Event{

    private final ChangeEventType changeType;
    private final Order affectedOrder;

    public EntityChangeEvent(ChangeEventType changeType, Order affectedOrder) {
        this.changeType = changeType;
        this.affectedOrder = affectedOrder;
    }

    public ChangeEventType getChangeType() {
        return changeType;
    }

    public Order getAffectedOrder() {
        return affectedOrder;
    }

}
