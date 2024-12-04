# Architecture Description

## Conceptual Architecture

![Conceptual_Architecture](https://github.com/user-attachments/assets/77c5991d-31ca-4ed9-a637-a6c44257e784)

The system has 4 main central functions according to the SRS (not in any order):
    1.	Filtering Stock Data Observations
    2.	Refreshing the database (CSVs)
    3.	Visualizing the data
    4.	Scraping the data

First, the scrapers fetch the raw data (4. - 10 years if none exists, otherwise only the data that is missing up
until now) from MSE.mk, which is the external system. using multiple scrapers (Data Scraper component).
The scrapers know when to scrape the Data (2.) because the Data Fetcher prompts them to do so (as explained in the
Execution Architecture). The data is then filtered as needed (from which date and to which date the user requests 3.)
and transformed to objects to be displayed in the Web App GUI. Additionally, the database (Structured CSV Files are)
is also a component that sends information to the Stock Service which filters according to the mentioned user requests.

## Execution Architecture

![Execution_Architecture](https://github.com/user-attachments/assets/39361966-ce81-4c0e-b79c-c6de2bae720b)

The Web App GUI is mapped 1:1 in the execution diagram. The same thing is done with the Stock Service.
The user interacts with the server through HTTP since it is a Web Application. The Stock Service gives data to user
as mentioned in the Execution architecture, so this logic is the same as in the Conceptual Architecture.
The composite data fetching component works by interacting with MSE.mk (external system) and has multiple
scrapers as threads, called by the Data Fetcher. This component is active since it is scheduled in the application to
run at 00:00 every midnight and generate data internally within the system, and when finished, call the refresh service
asynchronously to update the database.

## Implementation Architecture

![Implementation_Architecture](https://github.com/user-attachments/assets/95639cc2-9659-42e8-9eae-13e53c813058)

Again, the Web App UI is a 1:1 mapping from the Conceptual Architecture into this one. The back-end used in the application
is Spring Boot. The Spring application runs inside a Tomcat Server Container. The layered web architecture uses
Controllers that fill templates with information for the user to see. The presentation layer uses the business logic
from the Service layer (where our services are located). Additionally, we have a tasks component that corresponds
to the Data Fetcher component (active in the Execution Architecture) which calls the python script (subsystem that uses
 the Pipe & Filter Architecture and gets the data from MSE by scraping) and calls the refresh service to update the database.
The Data Access layer exists as the repository and interacts with the CSVs using through the Java IO API and uses the Apache
Commons library to read them, without the other layers being aware of this.
