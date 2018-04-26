package com.assignment.mlapi.model;

import com.assignment.PathProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by shashwat on 4/25/18.
 */

@Entity
public class MLModel
{
    @JsonIgnore
    private static String UPLOADED_FOLDER = PathProperties.config.getProperty("model_image_path");

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

    public boolean doesTrainingImagesExist() throws IOException
    {
        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(Paths.get(getImagesPath())))
        {
            return dirStream.iterator().hasNext();
        }
    }
}
