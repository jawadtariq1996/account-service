# Account Service

This is a simple account service built using Java 17 and Spring Boot, and managed with Maven.

## Installation

To install this project, simply clone the repository and build the application using Maven:

```
git clone https://github.com/jawadtariq1996/account-service.git
cd account-service
mvn clean install
```

## Usage

To run the application, use the following command:

```
java -jar target/account-service.jar
```
> Make Sure that user-service and transaction-service are up and running as the account-service communicates with both of them.

## Endpoints
The application will start and will be accessible at http://localhost:8080.

For more details on how to use these endpoints, please refer to the following [postman collection](https://drive.google.com/file/d/1nKWM39c_7-seJdG6VLNUtSoZzQIlO_KY/view?usp=share_link)

Please get the value of customerId from the user-service data.sql script as the user is created using the script.

## Contributing
Contributions are welcome! Please feel free to submit a pull request or open an issue if you encounter any problems or have suggestions for improvement.

## License
This project is licensed under the MIT License. See the LICENSE file for details.
