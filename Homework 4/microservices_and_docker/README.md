# Containerizing the Application
## Steps

1. [Download this directory](https://download-directory.github.io/?url=https%3A%2F%2Fgithub.com%2Fddukoski%2Fmse-das-analysis-app%2Ftree%2Fmain%2FHomework%25204%2Fmicroservices_and_docker)
2. and place it wherever suits you

2. Open each of the 3 directories local to this README in the terminal, whilst having the Docker Daemon running:
   1. For `company_stock_microservice`:
   
   ```
    docker build -t stock-ms .
   ```
   
   2. For `service-registry`:
   
   ```
    docker build -t eureka-registry .
   ```
   
   3. For `stock_exchange_analyst_ms`: 
   
   ```
   docker build -t analyst-ms .
   ```
   
3. Run the `docker-compose.yaml` file local to this README by the command:
  
   ```
   docker-compose up
   ```

4. The application should be started in *~1-3 minutes* & you can verify this process works by checking the uploaded 
   video in [/mse-das-analysis-app/Homework 4/dockerization-launch-demo.mp4](https://github.com/ddukoski/mse-das-analysis-app/blob/main/Homework%204/dockerization-launch-demo.mp4)