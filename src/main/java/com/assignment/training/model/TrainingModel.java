package com.assignment.training.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by shashwat on 4/26/18.
 */

@Entity
public class TrainingModel
{
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private Long modelId;
    private Double i;
    private Integer j;
    private Integer k;
    private Double accuracy;

    public TrainingModel(Long modelId, Double i, Integer j, Integer k, Double accuracy)
    {
        this.modelId = modelId;
        this.i = i;
        this.j = j;
        this.k = k;
        this.accuracy = accuracy;
    }

    public TrainingModel()
    {}

    public Long getModelId()
    {
        return modelId;
    }

    public Double getI()
    {
        return i;
    }

    public Integer getJ()
    {
        return j;
    }

    public Integer getK()
    {
        return k;
    }

    public Double getAccuracy()
    {
        return accuracy;
    }
}
