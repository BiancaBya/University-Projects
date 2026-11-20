package com.mobile.backend.events;

import com.mobile.backend.model.Car;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CarEvent {
    public enum Type {INSERT, UPDATE, DELETE}
    private Type type;
    private Car car;
}

