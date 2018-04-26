package com.assignment.training;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shashwat on 4/25/18.
 */
public class TrainingParams
{
    double learningRate;
    int layers;
    int steps;

    public TrainingParams(double learningRate, int nLayers, int nSteps)
    {
        this.learningRate = learningRate;
        this.layers = nLayers;
        this.steps = nSteps;
    }
}
