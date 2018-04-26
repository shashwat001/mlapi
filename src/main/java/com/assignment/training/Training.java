package com.assignment.training;

import com.assignment.mlapi.model.MLModel;
import com.assignment.training.model.TrainingModel;
import com.assignment.training.model.TrainingInfoRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shashwat on 4/25/18.
 */

public class Training
{
    MLModel mlModel;
    String filePath = "/Users/shashwat/Documents/nanonets/train.py";


    public Training(MLModel mlModel)
    {
        this.mlModel = mlModel;
    }

    public TrainingModel train(TrainingParams trainingParams) throws IOException
    {
        Response trainingResponse = new Gson().fromJson(PythonExecutor.execute(filePath, getAsArgs(trainingParams)), Response.class);
        TrainingModel trainingModel = new TrainingModel(mlModel.getId(), trainingResponse.getI(), trainingResponse.getJ(), trainingResponse.getK(), trainingResponse.getAccuracy());
        return trainingModel;
    }

    public Map<String, String> getAsArgs(TrainingParams trainingParams)
    {
        Map<String, String> args = new HashMap<>();
        args.put("i", String.valueOf(trainingParams.learningRate));
        args.put("j", String.valueOf(trainingParams.layers));
        args.put("k", String.valueOf(trainingParams.steps));
        args.put("images", mlModel.getImagesPath());

        return args;
    }
}
