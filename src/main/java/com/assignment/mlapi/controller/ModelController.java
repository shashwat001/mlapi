package com.assignment.mlapi.controller;

import com.assignment.mlapi.error.MLApiException;
import com.assignment.mlapi.model.MLModel;
import com.assignment.mlapi.model.MLModelRepository;
import com.assignment.mlapi.model.MLModelService;
import com.assignment.training.Training;
import com.assignment.training.TrainingConstants;
import com.assignment.training.TrainingParams;
import com.assignment.training.model.TrainingModel;
import com.assignment.training.model.TrainingService;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

/**
 * Created by shashwat on 4/25/18.
 */

@RestController
@RequestMapping("/model")

public class ModelController
{

    private MLModelService mlModelService;
    private TrainingService trainingService;

    @Autowired
    public void setModelService(MLModelService modelService) {
        this.mlModelService = modelService;
    }

    @Autowired
    public void setTrainingService(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public MLModel createNewModel()
    {
        MLModel mlModel = new MLModel();
        MLModel model = mlModelService.saveModel(mlModel);
        return model;
    }

    @RequestMapping(path = "{id}/upload")
    public String updateModel(
            @PathVariable(value = "id") Long modelId,
            @RequestParam("file") MultipartFile file) throws MLApiException
    {
        validateFile(file);
        Optional<MLModel> model = getModel(modelId);
        try
        {
            saveFile(model, file);
        }
        catch (IOException e)
        {
            throw new MLApiException("Error in uploading file");
        }
        return "File uploaded successfully";
    }

    @RequestMapping(path = "{id}/uploadall")
    public String updateModel(@PathVariable(value = "id") Long modelId,
                              @RequestParam("files[]") MultipartFile[] files) throws MLApiException
    {
        Optional<MLModel> model = getModel(modelId);
        validateFilesList(files);

        List<String> erroneousFiles = new ArrayList<>();

        for (MultipartFile file : files)
        {
            if (file.isEmpty())
            {
                erroneousFiles.add(file.getOriginalFilename());
            }

            try
            {
                saveFile(model, file);
            }
            catch (IOException e)
            {
                erroneousFiles.add(file.getOriginalFilename());
            }

        }

        if(erroneousFiles.isEmpty())
        {
            return "Files uploaded successfully";
        }
        else
        {
            return "Following files could not be uploaded: " + StringUtils.join(erroneousFiles, ',');
        }

    }

    @RequestMapping(path = "{id}/train",
            method = RequestMethod.POST)
    public TrainingModel trainModel(@PathVariable(value = "id") Long modelId,
                                    @RequestParam("learning-rate") Double learningRate,
                                    @RequestParam("layers") Integer layers,
                                    @RequestParam("steps") Integer steps) throws IOException, MLApiException
    {
        Optional<MLModel> model = mlModelService.find(modelId);
        if(model.isPresent() && model.get().doesTrainingImagesExist())
        {
            Training training = new Training(model.get());
            TrainingModel trainingModel = trainingService.saveTrainingInfo(training.train(new TrainingParams(learningRate, layers, steps)));
            return trainingModel;
        }
        else
        {
            throw new MLApiException("Either model with given id or training images for model do not exist.");
        }

    }

    @RequestMapping(path = "{id}/trainall",
            method = RequestMethod.POST)
    public TrainingModel trainModel(@PathVariable(value = "id") Long modelId) throws MLApiException, IOException
    {
        Optional<MLModel> model = mlModelService.find(modelId);
        if(model.isPresent() && model.get().doesTrainingImagesExist())
        {
            try
            {
                Training training = new Training(model.get());
                for (Double learningRate : TrainingConstants.LEARNING_RATE)
                {
                    for (int layers : TrainingConstants.LAYERS)
                    {
                        for (int steps : TrainingConstants.STEPS)
                        {
                            trainingService.saveTrainingInfo(training.train(new TrainingParams(learningRate, layers, steps)));
                        }
                    }
                }
                return trainingService.getHighestAccuracyModel(model.get().getId());
            }
            catch (IOException ex)
            {
                throw new MLApiException("Error in reading file for training");
            }
        }
        else
        {
            throw new MLApiException("Either model with given id or training images for model do not exist.");
        }
    }

    private void validateFile(@RequestParam("file") MultipartFile file) throws MLApiException
    {
        if (file.isEmpty())
        {
            throw new MLApiException("File invalid or empty.");
        }
    }

    private Optional<MLModel> getModel(Long modelId) throws MLApiException
    {
        Optional<MLModel> model = mlModelService.find(modelId);

        if(!model.isPresent())
        {
            throw new MLApiException("Model does not exist.");
        }
        return model;
    }

    private void saveFile(Optional<MLModel> model, MultipartFile file) throws IOException
    {
        byte[] bytes = file.getBytes();
        Path path = Paths.get(model.get().getImagesPath() + "/" + file.getOriginalFilename());
        Files.createDirectories(path.getParent());
        Files.write(path, bytes);
    }

    private void validateFilesList(@RequestParam("files") MultipartFile[] files) throws MLApiException
    {
        if(files.length == 0)
        {
            throw new MLApiException("No file found.");
        }
    }

}
