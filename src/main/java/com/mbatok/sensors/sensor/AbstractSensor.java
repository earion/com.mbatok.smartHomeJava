package com.mbatok.sensors.sensor;

import javax.persistence.*;
import java.io.IOException;
import java.util.Set;

/**
 * Created by mateusz on 31.10.16.
 */
@Entity
@Table(name = "Sensors",
        uniqueConstraints=@UniqueConstraint(columnNames={"description", "name"}))

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class AbstractSensor  implements Sensor{

    @Id
    @GeneratedValue
    @Column(name="sensorId")
    private Integer id;

    private String name;


    private String description;
    private String unit;

    @OneToMany(mappedBy="sensor")
    private Set<SensorResult> sensorMeasurements;


    public AbstractSensor(String unit) {
        this.unit = unit;
    }

    public AbstractSensor(Integer id, String name, String description, String unit) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.unit = unit;
    }

    @Id
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    public abstract SensorResult read() throws IOException;

    public String getDescription() {return description;}

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
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

}
