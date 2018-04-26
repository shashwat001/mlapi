package com.assignment.mlapi.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by shashwat on 4/26/18.
 */

@Service
public class MLModelService
{
    @Autowired
    private MLModelRepository mlModelRepository;

    public MLModel saveModel(MLModel model)
    {
        return mlModelRepository.save(model);
    }

    public Optional<MLModel> find(Long id)
    {
        return mlModelRepository.findById(id);
    }
}
