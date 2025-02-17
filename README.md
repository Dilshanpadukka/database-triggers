# Customer Management System with Audit Logs

A Spring Boot application for managing customer data with automatic audit logging using PostgreSQL triggers.

## Features
- Create, Read, Update, and Delete customers
- Automatic audit history tracking
- Immutable audit logs
- REST API endpoints
- Layered architecture
- PostgreSQL database integration

## Prerequisites
- Java 17+
- PostgreSQL 14+
- Maven 3.8+
- Postman (for API testing)

## Database Setup

1. Create the database and tables:

```sql
CREATE DATABASE customer_audit;
\c customer_audit

CREATE TABLE customer (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    nic VARCHAR(20),
    inActive BOOLEAN DEFAULT false
);

CREATE TABLE customer_h (
    audit_id SERIAL PRIMARY KEY,
    id INT NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    nic VARCHAR(20),
    inActive BOOLEAN,
    operation_type VARCHAR(10),
    operation_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    operation_by VARCHAR(255)
);
```

2. Create the trigger function:

```sql
CREATE OR REPLACE FUNCTION log_customer_changes()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO customer_h (id, first_name, last_name, nic, inActive, operation_type, operation_by)
        VALUES (OLD.id, OLD.first_name, OLD.last_name, OLD.nic, OLD.inActive, 'DELETE', current_user);
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO customer_h (id, first_name, last_name, nic, inActive, operation_type, operation_by)
        VALUES (NEW.id, NEW.first_name, NEW.last_name, NEW.nic, NEW.inActive, 'UPDATE', current_user);
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO customer_h (id, first_name, last_name, nic, inActive, operation_type, operation_by)
        VALUES (NEW.id, NEW.first_name, NEW.last_name, NEW.nic, NEW.inActive, 'INSERT', current_user);
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER customer_audit_trigger
AFTER INSERT OR UPDATE OR DELETE ON customer
FOR EACH ROW EXECUTE FUNCTION log_customer_changes();
```

## Application Setup

1. Clone the repository:

```bash
git clone https://github.com/yourusername/customer-audit-system.git
cd customer-audit-system
```

2. Configure the database in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/customer_audit
spring.datasource.username=postgres
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

3. Build and run the application:

```bash
mvn clean install
mvn spring-boot:run
```

## API Endpoints

| Method | Endpoint                     | Description        |
|--------|------------------------------|--------------------|
| POST   | `/api/customers`             | Create customer   |
| PUT    | `/api/customers/{id}`        | Update customer   |
| DELETE | `/api/customers/{id}`        | Delete customer   |
| GET    | `/api/customers`             | Get all customers |
| GET    | `/api/customers/history`     | Get audit history |

## Example Requests

### Create Customer

```http
POST http://localhost:8080/api/customers
Content-Type: application/json

{
    "firstName": "John",
    "lastName": "Doe",
    "nic": "123456789V",
    "inActive": false
}
```

### Update Customer

```http
PUT http://localhost:8080/api/customers/1
Content-Type: application/json

{
    "firstName": "Jane",
    "lastName": "Smith",
    "nic": "987654321V",
    "inActive": true
}
```

### Get Audit History

```http
GET http://localhost:8080/api/customers/history
```

## Project Structure

```
src/main/java
├── com.example.customer
│   ├── controller
│   ├── entity
│   ├── repository
│   ├── service
│   └── CustomerAuditApplication.java
```

## Key Components
- **Database Trigger**: Automatically logs all changes to `customer_h` table.
- **Immutable History**: `@Immutable` annotation on `CustomerHistory` entity.
- **Layered Architecture**: `Controller -> Service -> Repository -> Entity`.
- **Lombok**: Reduces boilerplate code with `@Data` annotations.

## Troubleshooting
- Ensure PostgreSQL is running on port `5432`.
- Verify database credentials in `application.properties`.
- Check trigger creation in PostgreSQL.
- Enable Lombok in your IDE.

## License
MIT License

