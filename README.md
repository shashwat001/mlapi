#Call to create a model. Model Id is returned in response which is used to upload images to the model or train the model  
curl -X POST http://54.200.237.1:8888/mlapi/model

#Call to upload an image to an existing model. The model id is to be provided in the url. The file should exist on the system from which the command is being run
#In following url 1 is model id.  
curl -F 'file=@testimages/test1.jpg' http://54.200.237.1:8888/mlapi/model/1/upload

#Call to upload multiple files in one call. The model id is to be provided in the url. If any file gives error on upload, the file name will be returned in the response  
curl -F 'files[]=@testimages/test1.jpg' -F 'files[]=@testimages/test2.jpg' http://54.200.237.1:8888/mlapi/model/2/uploadall

#Call to train a specific model by giving the parameters. The parameters and accuracy is returned in response  
curl -X POST http://54.200.237.1:8888/mlapi/model/1/train --data "learning-rate=0.01&layers=2&steps=1000"

#Call to train model on all the 27 combinations of given parameters. The parameters and accuracy details for high accuracy is returned in response.  
curl -X POST http://54.200.237.1:8888/mlapi/model/1/trainall
