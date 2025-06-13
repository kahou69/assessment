# Java Technical Assessment Project Documentation

## Project Overview

This is a Java backend system technical assessment project designed to manage customers, products, and orders. It contains basic CRUD functionality for each entity. This project is built using Java Spring Boot, JPA for persistence, and H2 for the database.

## Key Features

- **Customer Management**: Create, Read, Update, Delete.
- **Product Management**: Create, Read, Update, Delete.
- **Order Management**: Place and track orders by Customer Id.

## Technologies Used

- **Spring Boot**: Framework for building the backend API.
- **Spring Data JPA**: For database interaction and ORM (Object-Relational Mapping).
- **H2 Database**: In-memory database for development and testing.
- **Lombok**: For reducing boilerplate code (e.g., getters, setters, etc.).
- **JUnit**: For unit testing.
- **Swagger**: For API documentation and testing.

## Project Setup

### 1. Clone the Repository

```bash
git clone https://github.com/kahou69/assessment
```

## Project Requirements

- **Java 11+**: Ensure that you have JDK 11+ installed on your system.
- **Maven**: Apache Maven for dependency management and build automation.

## Building the Project

Navigate to the project directory and run the following command to build and run the project:

```bash
mvn clean install
```
## Running the Application

To run the application locally:

```bash
mvn spring-boot:run
```
The application will start on http://localhost:8081.

## Accessing H2 Database Console

For local development, the H2 database is used. You can access the database console by visiting:

[http://localhost:8081/h2-console](http://localhost:8081/h2-console)

Use the following credentials:

- **JDBC URL**: jdbc:h2:mem:etiqadb
- **Username**: sa
- **Password**: password

## API Documentation (Swagger)

The backend provides an automatically generated Swagger UI for easy exploration of the API endpoints.

To view the Swagger API documentation:

[http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)

This page contains details of the available endpoints, request parameters, and response formats.

## Endpoints

### Customers

#### Request Body Example

```json
{
  "firstName": "John",
  "lastName": "Doe",
  "emails": {
    "PERSONAL": "personal@gmail.com",
    "OFFICE": "office@gmail.com"
  },
  "familyMembers": [
    "Emily Phua"
  ]
}
```
POST /api/products: Add a new product.

GET /api/products: Get all products.

GET /api/products/{id}: Get a product by ID.

PUT /api/products/{id}: Update a product by ID.

PUT /api/products/{id}/restock/?quantity: Restock the quantity of products 

DELETE /api/products/{id}: Delete a product by ID.

### Products

```json
{
  "bookTitle": "Lord of The Rings",
  "bookPrice": 49.9,
  "bookQuantity": 20
}

```
POST /api/products: Add a new product.

GET /api/products: Get all products.

GET /api/products/{id}: Get a product by ID.

PUT /api/products/{id}: Update a product by ID.

PUT /api/products/{id}/restock/?quantity: Restock the quantity of products 

DELETE /api/products/{id}: Delete a product by ID.

### Orders

```json
[
  {
    "productId": 1,
    "quantity": 10
  }
]
```
POST /api/orders/?customerId: place an order 

GET /api/orders/{customersId}:Find an orders by customer ID 


## Database Model

### Entities

- **Customer**: Represents a customer in the system. Fields include name, emails (personal or office enums), list of family members, list of orders.
- **Product**: Represents a product. Fields include book title, price, quantity.
- **Order**: Represents an order placed by a customer. Each order has many order items (`List<OrderItems>`) and is associated with a customer.
- **OrderItem**: Represents a product in an order that stores the quantity ordered and price at that time. Each order item is associated with a product.

### Relationships

- **Customer to Orders**: A customer can have many orders.
- **Order to Customer**: An order can only have one customer.
- **Order to OrderItems**: An order can contain many order items.
- **OrderItem to Product**: An order item is related to one product.
- **Product to OrderItems**: A product can appear in many order items.

### Tables

- **customers**: Stores customer data (e.g., `id`, `first_name`, `last_name`).
- **customer_emails**: Stores customer emails based on email type (Office, Personal).
- **Customer_family_members**: Stores strings of customer’s family members.
- **products**: Stores product data (e.g., `id`, `book_price`, `book_quantity`, `book_title`).
- **orders**: Stores order data (e.g., `id`, `order_date`, `status`, `total_price`, `customer_id`).
- **order_items**: Stores the relationship between orders and products (e.g., `id`, `price`, `quantity`, `order_id`, `product_id`).

## Testing

To run unit tests, use the following Maven command:

```bash
mvn test
```
Test coverage includes CRUD operations for customers, products.

## Logging

API request and response are logged as info logs, while errors are logged as errors.

## Exceptions

### Error Handling

The backend handles common exceptions like `NotFoundException` for products and customers, `IllegalArgumentException`, and `InvalidEmailException` globally through custom exception handlers.


