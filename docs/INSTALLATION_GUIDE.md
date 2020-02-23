# INSTALATION GUIDE 

## FOR DEVELOPMENT
On this tutorial, we are going to explain how to execute the application with Open Source tools: Eclipse Spring Tool Suite 3 and Visual Studio Code. You can use others if you want to.
**Pre-requisites:** Java and MySQL server installed on your machine.

[ *Tutorial to install MySQL (in Spanish) step by step*](https://www.profesionalreview.com/2018/12/13/mysql-windows-10/)

### (JAVA BACK-END)
1. Download [Spring Tool Suite 3](https://spring.io/tools3/sts/all).
2. Open backend folder on STS.
3. Run as Spring Boot application. **IMPORTANT:** Run the app passing as first arg your Private Stripe Key. For more info about it check [Stripe Integration](./StripeIntegration.md)

You can configure the database url, user and pass on the application.properties.

### (FRONT-END ANGULAR)
 1. Download and install [Node.js and npm](https://nodejs.org/en/)
 2. Download and install Angular CLI: `npm install -g @angular/cli` on the console
 3. Download and install [Visual Studio Code](https://code.visualstudio.com/)
 4. Open Angular project on your IDE(Frontend folder)
 5. Set your Public Stripe Key on file src/environments/environment.ts
 6. Run `npm install` and after, `ng serve`. The app will be running on http://localhost:4200


 ## RUNNING THE APP (DOCKER) - NOT DEVELOPMENT
**Pre-requisites:**
 1. Install [Docker Desktop](https://hub.docker.com/editions/community/docker-ce-desktop-windows)
 2. Download last GitHub version of the project.

---
1. Go to docker-compose.yml file (in docker folder) 
2. Set in licensoft:build:args:privateStripeKey your Private Stripe Api Key.
3. Set in frontend:build:args:publicStripeKey your Public Stripe Api Key.
4. Open your Shell and go to the docker folder.
5. Run `docker-compose up`. The application backend will be running on port 8080 and the frontend on port 80. You can change it in "ports" section on the docker-compose.

**WARNING:** Dockerfiles can't be changed and must be on the path informed on each docker-compose:service:build:context. By default they are placed correctly, there is no need to change anything.