# Healthcare-management-System
Software Requirements Specification (SRS)
Table of Contents
1. Introduction
1. Purpose
2. Scope
3. Definitions, Acronyms, and Abbreviations
4. References
5. Overview
2. Overall Description
1. Product Perspective
2. Product Functions
3. User Classes and Characteristics
4. Operating Environment
5. Design and Implementation Constraints
6. Assumptions and Dependencies
3. System Features
1. Feature 1: User Authentication
2. Feature 2: Appointment Management
3. Feature 3: Patient Registration
4. Feature 4: Billing & Invoice
5. Feature 5: Statistics & Reports
6. Feature 6: Help & Support
7. Feature 7: Clinical Management
4. External Interface Requirements
1. User Interfaces
2. Hardware Interfaces
3. Software Interfaces
4. Communication Interfaces
5. Other Non-Functional Requirements
1. Performance Requirements
2. Safety Requirements
3. Security Requirements
4. Software Quality Attributes
5. Business Rules
6. Appendices
1. Appendix A: Glossary
2. Appendix B: Analysis Models
7.Wireframes
1. Introduction
1.1 Purpose
The purpose of this SRS document is to provide a detailed description of the requirements for
the Healthcare Management System. This document will outline the system’s functionality,
constraints, and interfaces.
1.2 Scope
The Healthcare Management System is designed to manage patient information, appointments,
billing, statistical reports, system controls, and database management in a healthcare setting. It
aims to streamline healthcare processes and improve patient care through efficient data
management and accessibility.
1.3 Definitions, Acronyms, and Abbreviations
• SRS: Software Requirements Specification
• GUI: Graphical User Interface
• JavaFX: A Java library used to build Rich Internet Applications
• SQL: Structured Query Language
1.4 References
• JavaFX Documentation: https://openjfx.io/
• SQL Documentation: https://www.mysql.com/
1.5 Overview
This document is organized into several sections that cover the system’s overall description,
specific requirements, external interfaces, and non-functional requirements.
2. Overall Description
2.1 Product Perspective
The Healthcare Management System is an independent application designed to integrate with
existing healthcare infrastructure. It will serve as a comprehensive tool for managing patient
information and healthcare processes.
2.2 Product Functions
• Appointment scheduling and management
• Patient registration and record maintenance
• Billing and payment processing
• Generation of statistical reports and analytics
• System controls for user and data management
• Database management for secure data storage and retrieval
2.3 User Classes and Characteristics
• Healthcare Providers: Doctors, nurses, and other medical staff who manage patient care.
• Administrative Staff: Receptionists and billing clerks who handle appointments, patient
records, and billing.
• System Administrators: IT personnel responsible for system maintenance and user
management.
2.4 Operating Environment
• The system will operate on Windows and multiple platforms.
• It will require a relational database (e.g., MySQL, PostgreSQL).
• The application will be developed using JavaFX for the user interface.
2.5 Design and Implementation Constraints
• The system must be developed using JavaFX for the front end.
• The database must be implemented using MySQL or SQLite.
• The application must be compatible with multiple operating systems.
• Compliance with healthcare data privacy regulations (e.g., HIPAA).
• System must be scalable to handle many users and records.
2.6 Assumptions and Dependencies
• Users will have basic knowledge of operating desktop applications.
• The system will require regular updates and maintenance.
• Users will have basic computer literacy and access to necessary hardware.
3. System Features
3.1 Feature 1: User Authentication
3.1.1 Description and Priority
• Description: The system will provide secure login functionality for users.
• Priority: High
3.1.2 Stimulus/Response Sequences
• User enters credentials and clicks “Login”.
• System validates credentials and grants access if correct.
3.1.3 Functional Requirements
• The system shall require a username and password for login.
• The system shall encrypt user passwords.
3.2 Feature 2: Appointment Management
• Description: Allows scheduling, updating, and cancelling patient appointments.
• Functional Requirements:
• Users can create, view, and manage appointments.
• The system sends reminders to patients.
• Appointments can be categorized by type (e.g., consultation, follow-up).
3.3 Feature 3: Patient Registration
• Description: Facilitates the registration and management of patient records.
• Functional Requirements:
o Users can enter and update patient information.
o Patient records include personal details, medical history.
o Secure access to patient records based on user roles.
3.4 Feature 4: Billing
 Description: Manages billing processes and payment records.
 Functional Requirements:
o Generate and print invoices.
o Track payments and outstanding balances.
3.5 Feature 5: Statistics & Reports
 Description: Provides statistical data and generates reports.
 Functional Requirements:
o Generate reports on patient demographics, appointment statistics, and financial
summaries.
o Export reports in various formats (e.g., PDF, Excel).
o Dashboard for quick access to key statistics.
3.6 Feature 6: Help & Support
 Description: Controls for user and data management.
 Functional Requirements:
o User account management (create, update, delete).
o Role-based access control.
o System backup and restore functionality.
3.5Feature 7: Clinical Management
Description: Manages the underlying database.
 Functional Requirements:
o Secure data storage and retrieval.
o Data encryption for sensitive information.
o Regular database maintenance and optimization.
4. External Interface Requirements
4.1 User Interfaces
 Login Screen: User authentication with username and password.
 Dashboard: Overview of key metrics and quick access to main features.
 Forms: For entering and updating patient and appointment information.
 Reports: Viewing and exporting statistical and financial reports.
4.2 Hardware Interfaces
• The system shall interact with standard input devices (keyboard, mouse).
4.3 Software Interfaces
• The system shall connect to a MySQL or SQLite database.
4.4 Communication Interfaces
• The system shall support email protocols for sending notifications.
5. Other Non-Functional Requirements
5.1 Performance Requirements
 The system should support up to 100 concurrent users without performance degradation.
 Response time for any user action should not exceed 2 seconds.
5.2 Safety Requirements
• Ensure data integrity and prevent loss of information through regular backups.
• Secure handling of patient data to comply with healthcare regulations.
5.3 Security Requirements
• Role-based access control to restrict access to sensitive data.
• Data encryption in transit and at rest.
5.4 Software Quality Attributes
• Usability: User-friendly interface with intuitive navigation
• Reliability: The system should have a high uptime and minimal downtime.
• Maintainability: The system should be easy to update and maintain.
5.5 Business Rules
• All patient records must be retained.
• Billing must comply with local healthcare regulations and standards.
6. Appendix A:
Glossary
• GUI: Graphical User Interface
• JRE: Java Runtime Environment
• SQL: Structured Query Language
• HIPAA: Health Insurance Portability and Accountability Act
Appendix B: Analysis Models
• Diagrams and flowcharts illustrating the system design.
1. Login Screen
• Purpose: To authenticate users (Admin, Doctors, Staff).
• Components:
• Username field
• Password field
• Login button
• Forgot password link
2. Dashboard
• Purpose: To provide a central navigation point for accessing different features of the
system.
• Components:
• Menu bar with options (Home, Username, Settings, Logout)
• Quick access buttons for common tasks
3. Appointment Scheduling Screen
• Purpose: To make appointment.
• Components:
• Type of appointment
• Date picker
• Doctor selection
• Save button
• Cancel button
4. Clinical Management Screen
• Purpose: To view and update patient database
• Components:
• Search by name and patient Id
• Date picker
• List of patients with details (name, type of disease etc.)
• Edit button to update database (selective access)
• Save button for updating records
• Back button to return to the dashboard
5. Patient Registration Screen
• Purpose: To register patient
• Components:
• Date range picker (start date, end date) for DOB.
• Clear all and Save button.
• Back button to return to the dashboard.
6. Billing & Invoice Screen
• Purpose: The Billing and Invoice tab in a healthcare management app is essential for
managing financial transactions by generating and processing invoices for patient services
and accepting various forms of payment.
• Components:
• Basic information
• Invoice date selection
• Download option
• Send option
• Display of Billing and Invoice history
7. Report & Statistics Screen
• Purpose: The Report and Statistics tab in a healthcare management app is crucial for
providing insights and data analysis on various aspects of the healthcare facility's
operations. By offering visualizations like charts and graphs, it simplifies the interpretation of
complex data, aiding in strategic planning and compliance reporting.
• Components:
• Total patients, appointments, and revenue.
• Graphs (bar graph, line graph and pie chart).
• PDF, Excels and printing.
8. Help and Support Screen
• Purpose: To help and support information.
• Components:
• FAQ section
• Contact support form.
• Edit and update profile information.
• Back button to return to the dashboard.
9. Logout Confirmation Screen
• Purpose: To confirm user logout.
• Components:
• Confirmation message
• Logout button
• Cancel button


