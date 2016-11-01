package com.mbatok.sensors;

import javax.persistence.*;
import java.io.IOException;

/**
 * Created by mateusz on 31.10.16.
 */
@Entity
@Table(name = "Sensors")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class AbstractSensor  implements Sensor{

    @Id
    @GeneratedValue
    private Integer id;

    private String name;



    private String type;
    private String description;
    private String unit;

    public AbstractSensor(String type, String unit) {
        this.type = type;
        this.unit = unit;
    }

    public AbstractSensor(Integer id, String name, String type, String description, String unit) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.unit = unit;
    }




    public Integer getId() {
        return this.id;
    }

    @Id
    public void setId(Integer id) {
        this.id = id;
    }


    public abstract SensorResult read() throws IOException;

    public String getDescription() {return description;}

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public String getType() {
        return type;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    protected void setName(String name) throws IOException {
        this.name = name;
    }

    protected void setDescription(String description) {
        this.description = description;
    }

    public void setType(String type) {
        this.type = type;
    }
}
