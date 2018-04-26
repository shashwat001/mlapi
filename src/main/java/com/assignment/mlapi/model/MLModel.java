package com.assignment.mlapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by shashwat on 4/25/18.
 */

@Entity
public class MLModel
{
    @JsonIgnore
    private static String UPLOADED_FOLDER = "/Users/shashwat/Random/images";

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;

    @JsonIgnore
    String imagesPath = "";

    public Long getId()
    {
        return id;
    }

    public String getImagesPath()
    {
        return UPLOADED_FOLDER + "/" + id;
    }
}
