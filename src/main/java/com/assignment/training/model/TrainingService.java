package com.assignment.training.model;

import com.assignment.mlapi.model.MLModel;
import com.assignment.training.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

/**
 * Created by shashwat on 4/26/18.
 */

@Service
public class TrainingService
{
    @Autowired
    private TrainingInfoRepository trainingInfoRepository;

    public TrainingModel saveTrainingInfo(TrainingModel trainingModel)
    {
        return trainingInfoRepository.save(trainingModel);
    }

    public TrainingModel getHighestAccuracyModel(Long modelId)
    {
        Double accuracy = 0.0;
        TrainingModel bestModel = null;
        for(TrainingModel model : trainingInfoRepository.findAll())
        {
            if(model.getModelId() == modelId && model.getAccuracy() > accuracy)
            {
                bestModel = model;
                accuracy = model.getAccuracy();
            }
        }
        return bestModel;
    }
}
