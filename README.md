# TeeTime Pro – Golf Store Web Application

A Java web application for an online golf equipment store. It is built with the MVC pattern using Java Servlets, JSP and MySQL. Built individually as coursework for my BSc (Hons) Computing with AI at Islington College (London Metropolitan University).

## Features
- **User accounts:** registration, login/logout and profile management
- **Role-based access:** separate dashboards for customers and staff
- **Security:** session-based authentication enforced by a servlet filter, with SHA-256 password hashing
- **Product management:** staff can add, edit and delete products and categories
- **User management:** staff can view and manage registered users
- **Error handling:** custom 404 and 500 error pages

## Tech Stack
| Layer | Technology |
|---|---|
| Language | Java |
| Web | Jakarta Servlets, JSP, JSTL, HTML, CSS |
| Database | MySQL (via JDBC, MySQL Connector/J) |
| Server | Apache Tomcat 10.1 |
| IDE | Eclipse |

## Project Structure
```
src/main/java/com/golfstore/
├── config/       # Database connection (DBConnection)
├── controllers/  # Servlets: login, register, products, categories, users, dashboard, profile…
├── filters/      # AuthenticationFilter (protects restricted pages)
├── model/        # ProductModel, CategoryModel, UserModel
└── util/         # PasswordUtil (SHA-256 hashing)
src/main/webapp/
├── WEB-INF/pages/  # JSP views
└── css/            # Stylesheets
```

## How to Run
1. Install **JDK 17+**, **Apache Tomcat 10.1** and **MySQL**.
2. Create a MySQL database named `teetime_pro` and its tables (users, products, categories).
3. Update the database username and password in `config/DBConnection.java` if needed.
4. Import the project into Eclipse as an existing project, add Tomcat 10.1 as the server, and run it.
5. Open `http://localhost:8080/<project-name>/login` in your browser.

## Report
The full project report (design, database schema, testing and screenshots) is in [`Project Report`](<Report.pdf>).

## Author
**Akshid Upadhyaya Panthi** · [GitHub](https://github.com/AkshidUpadhyaya)
