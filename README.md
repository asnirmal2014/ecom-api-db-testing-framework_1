E-commerce API + Database Testing Framework:
--------------------------------------------

A comprehensive TestNG + RestAssured + JDBC framework for testing a complete E-commerce backend.

Features:
- Layered Architecture: Client → Service → Tests + DB Validation
- Full CRUD coverage for Users, Products, Orders, OrderItems, Payments
- API + Database validation in every test
- Business Logic Validation:
  - Stock deduction on order creation
  - Order total calculation (`price × quantity`)
  - Payment amount must match order total
  - Inventory consistency checks
- Negative & Edge Case testing
- Dynamic test data generation (no hardcoded IDs)
- Detailed logging with emojis for easy debugging
- No automatic cleanup (test data preserved for analysis)

Modules Covered:
| Module        | Tests | Key Validations                            |
|---------------|-------|--------------------------------------------|
| Users         | 12    | CRUD, validation, duplicate email          |
| Products      | 12    | CRUD, price/stock validation, long name    |
| Orders        | 15    | CRUD, stock deduction, total calculation   |
| OrderItems    | 9     | CRUD, quantity validation                  |
| Payments      | 10    | CRUD, amount matching order total          |

Total Tests: 48 (All Passing)

Project Structure:
src/test/java/com/webprj/ecom/tests/
├── base/
│   └── BaseTest.java
├── client/           # HTTP clients
├── model/            # DB models (DBUser, DBProduct, etc.)
├── services/         # Business logic layer
├── testcases/        # TestNG test classes
├── utils/
│   └── DBUtils.java
└── TestSuite.java

How to Run:
```bash
mvn clean test