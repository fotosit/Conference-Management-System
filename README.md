# Conference-Management-System
The project is an example of console based object orientation programming in Java language.

### The Aim of the Project
It is a program that enables the submission of papers in scientific conferences, the review of the papers by the referees in the conference program committee, and the process of accepting or rejecting the papers in line with the referee opinions.

### Method
This project is based on object oriented programming architecture. All abstraction, wrapping, inheritance and polymorphism features that form the basis of object oriented programming are used in the project. MySQL database is included in the project to store user information and paper information. In the program, the field of Psychiatry was taken as the conference theme.

- The "Login" class, where the user includes transactions such as registering and logging in to the system,

- The "Author" class, which includes the procedures for uploading the papers to the system by the authors and tracking the uploaded papers,

- "Referee" class, which includes the procedures for viewing and evaluating the papers awaiting evaluation by the referees in the system and uploading a paper in case the referees are also authors, and displaying the current status of the paper,

- The "CommitteePresident" class, in which the chairman of the commission can assign referee to the papers in the system, the approval or rejection of the papers that are evaluated by the referee, and the processes for viewing all the conference papers registered in the system,

- The "Database" class, which includes all database transactions within the scope of user transactions and conference announcements,

- The "Evaluation" class, which includes transactions such as the score of the papers evaluated by the referees and the referee opinion,

- The name of the paper, the authors of the paper, the keywords of the paper, etc. "Paper" class with information,

- The super class "Person", which includes common features such as name, surname, mail, institution / university of its users and from which all user classes are derived,

- The "Topics" class, which is an Enum type class structure where the categories of the papers can be grouped and the areas of interest of the referees can be selected.

It is included in the UML diagram of the classes in the project.

### Database

All information about users, papers and evaluations in the project are stored in the database named "ConferenceManagementSystemDB". There are 3 tables named "Users", "Papers" and "Evaluation" in MySQL database.

- Information about users with the role of author, referee and commission chairman is stored in the "Users" table.

- In the "Papers" table, all information regarding the papers uploaded to the system is stored.

- "Evaluation" table containing information such as the ID number of the referee assigned to the papers, the score given to the paper, the opinion about the paper and the evaluation date.

It is included in the UML diagram of the database in the project.

