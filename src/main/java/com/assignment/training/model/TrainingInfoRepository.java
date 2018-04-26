package com.assignment.training.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Iterator;
import java.util.List;

/**
 * Created by shashwat on 4/26/18.
 */
public interface TrainingInfoRepository extends CrudRepository<TrainingModel, Long>
{
}
