# MTCG Thomas Boigner
## Model:
![Model](/src/docs/model.png)

## Changes to MonsterTradingCards.sh
I removed the name change of user kienboec and altenhof, because in the following requests the name was not changed and that leads to errors.

## Project Structure

### Presentation Layer
The presentation layer is responsible for handling user interactions. It receives the data Users input into the application and returns data as responses. 

- **Components**:
    - **Controllers**: They handle HTTP requests, map them to the appropriate services, and return responses to the users.
    - **Views**: Responsible for rendering the user interface. This could be HTML pages, JSON responses, or any other format suitable for the client.

### Service Layer
The service layer contains the core business logic of the application. It acts as an intermediary between the presentation layer and the persistence layer.

- **Components**:
    - **Services**: Contain business logic and are responsible for processing and validating data received from the controllers before interacting with the persistence layer.
    - **DTOs (Data Transfer Objects)**: Used to transfer data between the presentation and service layers.

### Persistence Layer
The persistence layer is responsible for interacting with the database. It provides an abstraction over the data storage and retrieval mechanisms, ensuring that the rest of the application is not concerned with how data is stored.

- **Components**:
    - **Repositories**: Handle CRUD (Create, Read, Update, Delete) operations and data access logic. They interact with the database directly.

## Model
The project model represents the various components and their relationships within the application.

## Flow of Control
1. **User Interaction**:
    - The user interacts with the application via the user interface, triggering a request, such as clicking a button or submitting a form.

2. **Controller Handling**:
    - The request is received by a controller in the presentation layer. The controller processes the request and invokes the appropriate service.

3. **Service Processing**:
    - The service processes the request, performs validation if necessary, and executes the business logic. It may interact with the persistence layer to retrieve or store data.

4. **Data Persistence**:
    - If data storage or retrieval is required, the service interacts with the repository in the persistence layer. The repository performs the necessary CRUD operations on the database.

5. **Response Generation**:
    - After processing the request, the service returns the response to the controller, which then sends it back to the user interface.

## Todo
- singleton refactor
- bearer token refactor
- refactor getting currently logged in user to be called in controller