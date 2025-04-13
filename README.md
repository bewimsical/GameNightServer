# Game Night Backend

## Overview
This project is a backend service built with Spring Boot that interacts with the boardgame geek API and a database. It provides a structured and efficient way to process data from the API, store and retrieve information from the database, and expose endpoints for client applications.

## Features
- Connects to [Boardgame Geek API](https://boardgamegeek.com/xmlapi) to fetch data.
- Stores and retrieves data using a relational database.
- Provides RESTful endpoints for client applications.
- Uses dependency injection and layered architecture for maintainability.

## Technologies Used
- **Spring Boot 3.4.4** - Backend framework
- **Spring Web** - For building REST APIs
- **Spring Data JPA** - For database interactions
- **Spring Data JDBC** - For simpler database operations
- **Microsoft SQL Server JDBC Driver** - For connecting to SQL Server
- **Jackson Dataformat XML** - For XML data processing
- **Maven** - Build tool
- **Database** - Azure

## Local Development
- **Install Docker**
    - Download and install [Docker](https://www.docker.com/get-started) if you haven’t already.

- **Run Docker Compose**
    - Open a terminal and navigate to the project directory.
    - Run the following command to start the containers:  `docker compose up -d`

- **Set Up Environment Variables**
    - Copy the example environment file:  `cp .env.example .env.properties`
    - Edit `.env.properties` as needed to configure your settings.

## Azure Development
- **Set Up Environment Variables**
  - Copy the example environment file:  `cp .env.example .env.properties`
  - Edit `.env.properties` as needed to configure your settings.

## Contributors
- Kathleen Wims [GitHub](https://github.com/bewimsical)  
- Darianne Ramos [GitHub](https://github.com/darianne123)  
- Kenneth LeGare [GitHub](https://github.com/DataHiveMind)
- Joshua Samson [GitHub](https://github.com/jsams909)
- Efrat Weiss [GitHub](https://github.com/Wieefi)

