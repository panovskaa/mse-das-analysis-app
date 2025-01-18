# Containerizing the Application
## Steps

1. Download *this* directory

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

4. The application should be started in *~1-3 minutes* & you can verify this process works by checking the uploaded video in /mse-das-analysis-app/Homework 4/demo-container.mp4