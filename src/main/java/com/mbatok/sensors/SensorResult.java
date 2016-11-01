package com.mbatok.sensors;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by mateusz on 31.10.16.
 */
@Entity
@Table(name = "MeasureResults")
public class SensorResult {

    @Id
    @GeneratedValue
    @Column(name="MeasurementId")
    private Integer id;

    @ManyToOne
    @JoinColumn(name="sensorId")
    private AbstractSensor sensor;

    private Date updateDate = new Date();

    float value;

    public SensorResult() {}

    public float getValue() {return value;}

    public SensorResult(float value, AbstractSensor sensor) {
        this.sensor = sensor;
        this.value = value;
    }

    public void setValue(float value) {this.value = value;}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getUpdateDate() {return updateDate;}

    public void setUpdateDate(Date updateDate) {this.updateDate = updateDate;}

    public AbstractSensor getSensor() {
        return sensor;
    }

    public void setSensor(AbstractSensor sensor) {
        this.sensor = sensor;
    }
}
