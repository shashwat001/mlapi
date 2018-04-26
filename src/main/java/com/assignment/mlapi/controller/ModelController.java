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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
            @RequestParam("file") MultipartFile file
    ) throws MLApiException
    {
        if (file.isEmpty())
        {
            throw new MLApiException("File invalid or empty.");
        }

        Optional<MLModel> model = mlModelService.find(modelId);

        if(!model.isPresent())
        {
            throw new MLApiException("Model does not exist.");
        }

        byte[] bytes = new byte[0];
        try
        {
            bytes = file.getBytes();
            Path path = Paths.get(model.get().getImagesPath() + "/" + file.getOriginalFilename());
            Files.createDirectories(path.getParent());
            Files.write(path, bytes);
        }
        catch (IOException e)
        {
            throw new MLApiException("Error in uploading file");
        }

        return "File uploaded successfully";
    }

    @RequestMapping(path = "{id}/uploadall")
    public String updateModel(@PathVariable(value = "id") Long modelId,
                              @RequestParam("files") MultipartFile[] files)
    {

        StringJoiner sj = new StringJoiner(" , ");

        for (MultipartFile file : files)
        {

            if (file.isEmpty())
            {
                continue; //next pls
            }

            try
            {

                byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
                Files.write(path, bytes);

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }

        String uploadedFileName = sj.toString();
        if (StringUtils.isEmpty(uploadedFileName))
        {
            redirectAttributes.addFlashAttribute("message",
                    "Please select a file to upload");
        }
        else
        {
            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + uploadedFileName + "'");
        }

        return "redirect:/uploadStatus";

    }

    @RequestMapping(path = "{id}/train",
            method = RequestMethod.POST)
    public TrainingModel trainModel(@PathVariable(value = "id") Long modelId,
                                    @RequestParam("learning-rate") Double learningRate,
                                    @RequestParam("layers") Integer layers,
                                    @RequestParam("steps") Integer steps) throws IOException, MLApiException
    {
        Optional<MLModel> model = mlModelService.find(modelId);
        if(model.isPresent())
        {
            Training training = new Training(model.get());
            TrainingModel trainingModel = trainingService.saveTrainingInfo(training.train(new TrainingParams(learningRate, layers, steps)));
            return trainingModel;
        }
        else
        {
            throw new MLApiException("Model does not exist.");
        }

    }

    @RequestMapping(path = "{id}/trainall",
            method = RequestMethod.POST)
    public TrainingModel trainModel(@PathVariable(value = "id") Long modelId) throws MLApiException
    {
        Optional<MLModel> model = mlModelService.find(modelId);
        if(model.isPresent())
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
            throw new MLApiException("Model does not exist.");
        }

    }
}
