# Hamro Pharmacy - Pharmacy Management System

A full-stack pharmacy management application built with **Spring Boot** and a vanilla **HTML/CSS/JavaScript** frontend. It provides inventory tracking, prescription management, sales/order processing, and analytics reporting — all backed by a MySQL database.

---

## Tech Stack

| Layer    | Technology                          |
| -------- | ----------------------------------- |
| Backend  | Java 25, Spring Boot 4.0.1, Maven   |
| Database | MySQL 8+                            |
| Frontend | HTML5, CSS3, JavaScript (vanilla)   |
| Icons    | Font Awesome 6.5.1                  |
| Fonts    | Google Fonts (Inter, Playfair Display) |

---

## Project Structure

```
pharmacy/
├── pom.xml                              # Maven build config
├── mvnw / mvnw.cmd                      # Maven wrapper scripts
├── README.md
└── src/
    └── main/
        ├── java/com/f1Soft/meta/
        │   ├── MetaApplication.java              # Spring Boot entry point
        │   ├── config/
        │   │   └── WebConfig.java                 # CORS configuration
        │   └── controller/
        │       ├── MedicationController.java       # CRUD for medications
        │       ├── PatientController.java          # CRUD for patients
        │       ├── PharmacistController.java       # CRUD for pharmacists
        │       ├── PrescriptionController.java     # CRUD for prescriptions
        │       ├── SalesController.java            # CRUD for sales/orders
        │       ├── SupplierController.java         # CRUD for suppliers
        │       ├── StatsController.java            # Dashboard statistics
        │       ├── Select*Controller.java          # Find/search endpoints
        │       └── Update*Controller.java          # Update endpoints
        └── resources/
            ├── application.properties              # DB & app configuration
            └── static/                             # Frontend files
                ├── index.html                      # Landing page
                ├── inventory.html                  # Medication inventory
                ├── prescriptions.html              # Prescription management
                ├── orders.html                     # Sales/orders
                ├── reports.html                    # Analytics & reports
                ├── contact.html                    # Contact form
                ├── css/styles.css                  # Stylesheet
                └── js/main.js                      # Frontend logic & API calls
```

---

## Database Setup

The application expects a MySQL database. Create it before running:

```sql
CREATE DATABASE project;
USE project;

CREATE TABLE supplier (
    supplierid INT AUTO_INCREMENT PRIMARY KEY,
    suppliername VARCHAR(255),
    suppliercontact VARCHAR(255),
    supplieremail VARCHAR(255)
);

CREATE TABLE medication (
    medicationid INT AUTO_INCREMENT PRIMARY KEY,
    medicationame VARCHAR(255),
    medicationcategory VARCHAR(255),
    medicationprice DECIMAL(10,2),
    medicationquantity INT,
    medicationexpdate DATE,
    supplierid INT,
    FOREIGN KEY (supplierid) REFERENCES supplier(supplierid)
);

CREATE TABLE patient (
    patientid INT AUTO_INCREMENT PRIMARY KEY,
    patientname VARCHAR(255),
    patientcontact VARCHAR(255),
    patientemail VARCHAR(255),
    patientaddress VARCHAR(255)
);

CREATE TABLE pharmacist (
    pharmacistid INT AUTO_INCREMENT PRIMARY KEY,
    pharmacistname VARCHAR(255),
    pharmacistcontact VARCHAR(255),
    pharmacistemail VARCHAR(255)
);

CREATE TABLE prescription (
    prescriptionid INT AUTO_INCREMENT PRIMARY KEY,
    patientid INT,
    medicationid INT,
    pharmacistid INT,
    prescriptiondate DATE,
    prescriptiondosage VARCHAR(255),
    prescriptionduration VARCHAR(255),
    FOREIGN KEY (patientid) REFERENCES patient(patientid),
    FOREIGN KEY (medicationid) REFERENCES medication(medicationid),
    FOREIGN KEY (pharmacistid) REFERENCES pharmacist(pharmacistid)
);

CREATE TABLE sales (
    salesid INT AUTO_INCREMENT PRIMARY KEY,
    patientid INT,
    pharmacistid INT,
    salesdate DATE,
    salesquantity INT,
    salestotal DECIMAL(10,2),
    FOREIGN KEY (patientid) REFERENCES patient(patientid),
    FOREIGN KEY (pharmacistid) REFERENCES pharmacist(pharmacistid)
);
```

---

## Configuration

Database connection is configured in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/project
spring.datasource.username=root
spring.datasource.password=
```

Update these values if your MySQL instance uses a different port, username, or password.

---

## Prerequisites

- **Java 25** (or compatible JDK)
- **MySQL 8+** running on `localhost:3306`
- No separate Maven install needed — the included Maven wrapper (`mvnw.cmd`) handles it

---

## How to Run

1. **Start MySQL** and create the database (see [Database Setup](#database-setup) above).

2. **Run the application** from the project root:

   Windows:
   ```cmd
   .\mvnw.cmd spring-boot:run
   ```

   Linux / macOS:
   ```bash
   ./mvnw spring-boot:run
   ```

3. **Open your browser** and go to:
   ```
   http://localhost:8080
   ```

The frontend is served directly by Spring Boot from the `static/` directory — no separate web server is needed.

---

## API Endpoints

All API endpoints are prefixed with `/api/`.

### Medications (`/api/medication`)
| Method | Path               | Description                     |
| ------ | ------------------ | ------------------------------- |
| GET    | `/api/medication`  | List all medications            |
| POST   | `/api/medication`  | Add a new medication            |
| DELETE | `/api/medication/{id}` | Delete a medication         |

### Patients (`/api/patient`)
| Method | Path             | Description            |
| ------ | ---------------- | ---------------------- |
| GET    | `/api/patient`   | List all patients      |
| POST   | `/api/patient`   | Add a new patient      |

### Pharmacists (`/api/pharmacist`)
| Method | Path               | Description              |
| ------ | ------------------ | ------------------------ |
| GET    | `/api/pharmacist`  | List all pharmacists     |
| POST   | `/api/pharmacist`  | Add a new pharmacist     |

### Prescriptions (`/api/prescription`)
| Method | Path                 | Description                               |
| ------ | -------------------- | ----------------------------------------- |
| GET    | `/api/prescription`  | List all prescriptions (with joined names)|
| POST   | `/api/prescription`  | Add a new prescription                    |

### Sales (`/api/sales`)
| Method | Path          | Description                          |
| ------ | ------------- | ------------------------------------ |
| GET    | `/api/sales`  | List all sales (with joined names)   |
| POST   | `/api/sales`  | Add a new sale                       |

### Suppliers (`/api/supplier`)
| Method | Path              | Description            |
| ------ | ----------------- | ---------------------- |
| GET    | `/api/supplier`   | List all suppliers     |
| POST   | `/api/supplier`   | Add a new supplier     |

### Statistics (`/api/stats`)
| Method | Path                       | Description                    |
| ------ | -------------------------- | ------------------------------ |
| GET    | `/api/stats/inventory`     | Inventory summary stats        |
| GET    | `/api/stats/orders`        | Order/sales summary            |
| GET    | `/api/stats/prescriptions` | Prescription count             |
| GET    | `/api/stats/reports`       | Full analytics (revenue, top products) |

---

## Frontend Pages

| Page                | URL Path              | Description                                  |
| ------------------- | --------------------- | -------------------------------------------- |
| Home                | `/`                   | Landing page with feature overview           |
| Inventory           | `/inventory.html`     | View, search, add, and delete medications    |
| Prescriptions       | `/prescriptions.html` | View and create prescriptions                |
| Orders              | `/orders.html`        | View and create sales orders                 |
| Reports             | `/reports.html`       | Revenue, order stats, and top products       |
| Contact             | `/contact.html`       | Contact form                                 |

---

## Building for Production

To create an executable JAR:

```bash
.\mvnw.cmd clean package -DskipTests
```

The JAR will be generated at `target/meta-0.0.1-SNAPSHOT.jar`. Run it with:

```bash
java -jar target/meta-0.0.1-SNAPSHOT.jar
```
