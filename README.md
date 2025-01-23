# REST API Products Demo
HI FROM TRIXI
**NOTES:**
Before committing `git pull`

**Reference Article:** <br>
https://medium.com/@pratik.941/building-rest-api-using-spring-boot-a-comprehensive-guide-3e9b6d7a8951

## Setting up
Create a new Spring Boot project using Spring Initializer. Add the following dependencies:

https://start.spring.io/

![Springboot-Init](Assets/springboot-init.png)

## How to Use

1. Clone the repository
2. Open the project in IntelliJ
3. In `src/main/java` folder you may choose `com.g4.RestApiProductsDemo` package,
4. Open the `RestApiProductsDemoApplication.java` file and run the application
5. Access <http://localhost:8080> in the browser
6. You may try experimenting with the endpoints below in Postman.

## H2 Console

1. Open another tab and navigate to this url <http://localhost:8080/h2-console>
2. Enter the login credentials found in `src/main/resources/application.properties` file.
    - **JDBC URL** - `jdbc:h2:mem:testdb`
    - **User Name** - `root`
    - **Password** - `password`
3. Once logged in you can try accessing the data.
    - **Checking data in Product Table** - `SELECT * FROM PRODUCT`
    - **Updating a product** - `UPDATE PRODUCT SET PRICE = 56000 WHERE ID = 1`
    - **Deleting a product** - `DELETE FROM PRODUCT WHERE ID = 1`
    - **Inserting a product** - `INSERT INTO PRODUCT (ID, NAME, DESCRIPTION, PRICE) VALUES (2, 'Alinenware', 'Gaming Laptop', 75000)`

4. H2 SQL
   INSERT INTO PRODUCT (id, name, description, price)
   VALUES
   (1, 'Macbook Pro', 'For soydevs', 100000.0),
   (2, 'Alienware', 'Gaming Laptop', 75000.0);


## Endpoints

### `V1` Endpoints
- Simple REST API, No Exception handling and Asynchronous processing.

#### 1. Get All Products

`GET` - http://localhost:8080/api/v1/product

Sample Response

```json
[
   {
      "id": 1,
      "name": "Macbook Pro",
      "description": "For soydevs",
      "price": 100000.0
   },
   {
      "id": 2,
      "name": "Alienware",
      "description": "Gaming Laptop",
      "price": 75000.0
   }
]
```

#### 2. Get Product by ID

`GET` - http://localhost:8080/api/v1/product/1

Sample Response
```json
{
   "id": 1,
   "name": "Macbook Pro",
   "description": "For soydevs",
   "price": 100000.0
}
```

#### 3. Create new Product

`POST` - http://localhost:8080/api/v1/product

`Body` -

```json
{
   "name": "Alienware",
   "description": "Gaming Laptop",
   "price": 75000
}
```

#### 4. Delete Product by ID

`DELETE` - http://localhost:8080/api/v1/product/1



### `V2` Endpoints
- Implemented Exception handling

#### 1. Get All Products

`GET` - http://localhost:8080/api/v2/product

Sample Response

```json
[
   {
      "id": 1,
      "name": "Macbook Pro",
      "description": "For soydevs",
      "price": 100000.0
   },
   {
      "id": 2,
      "name": "Alienware",
      "description": "Gaming Laptop",
      "price": 75000.0
   }
]
```

#### 2. Get Product by ID

`GET` - http://localhost:8080/api/v2/product/1

Sample Response
```json
{
   "id": 1,
   "name": "Macbook Pro",
   "description": "For soydevs",
   "price": 100000.0
}
```

#### 3. Create new Product

`POST` - http://localhost:8080/api/v2/product

`Body` -

```json
{
   "name": "Alienware",
   "description": "Gaming Laptop",
   "price": 75000
}
```

#### 4. Delete Product by ID

`DELETE` - http://localhost:8080/api/v1/product/1