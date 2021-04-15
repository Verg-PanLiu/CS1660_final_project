# CS1660_final_project
- Find Demo video here 
[Pan Liu Demo video for CS 1660 final project](https://www.dropbox.com/s/qvcbnwqfaws7owr/CS1660_project_Demo.mp4?dl=0).

# Before runing
- Make sure dock installed and running 
- Make sure XLaunch installed and running

# Running application 
Please follow following steps to run the application 
- Run `docker build --tag "TAG" .`  **REAPLACE "TAG"** 
- Run `docker run -it --privileged -e DISPLAY="YOUR IP ADDRESS":0.0 -v /tmp/.X11-unix:/tmp/.X11-unix "TAG":latest` ** REPLACE "YOUR IP ADDRESS" and "TAG"**
- Press the radio buttons to select the files you want to process and click ***Choose Files*** button. 
- Press ***Load Engine***
- Once Inverted Index is successfully constructed, select the ***Search For Term*** or ***Top-N***
- Enter the term you want to search for and press ***Search***, press ***Go Back To Search*** to go back once it is done
- Enter the N value to fin Top N frequent terms and press ***Search***, press ***Go Back To Search*** to go back once it is done

# Note
- jar files might need to be uploaded to GCP before running the application.
- credential file from GCP is neccessary. 
