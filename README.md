# Journal
A daily website solution
## Intro
The **Journal** is a personal Java/Web project created to solve a diary writing problem, making the process more digital and softer. It's based and inspired by social media' flow where you can post your 'work in progress' stuffs, future ideas, cool (or not) messages and even outbursts and immediately see, edit and remove it on same dashboard.

At the dev/technical side, one of the first idea of the project was build a Java WebSite using the same (or as close as possible) **tech stack** that I've used on 'EspacoSeuPet' (https://github.com/Lucas-Mol/EspacoSeuPet) but bringing/testing my new knowlegdes. Following this idea, I've tried to achieve a modern and customized view as well as a clean/designed code based on variety of design pattern's conventions, according to the tech stack limits. It's bringing up the project close to a 'Java Enterprise Monolith Level' but it's **worthwhile to note** that the project's final aim is **not** to reach a commercial level, so its architecture was not designed to supply it. 

**Conclusion:** Journal is a personal Java/Web project to test my knowledges in JSF, Hibernate Criteria e etc.
## Overview

![Login](https://cdn.discordapp.com/attachments/778788148921761822/1101943586909589655/journal_login.jpg)

![Dashboard](https://cdn.discordapp.com/attachments/778788148921761822/1101943588125954059/journal_dashboard.jpg)

![Filtering](https://cdn.discordapp.com/attachments/778788148921761822/1101943588562153522/journal_dashboard_search.jpg)

[![Video](https://cdn.discordapp.com/attachments/778788148921761822/1104147354866700369/journal-video-preview.jpg)](https://www.youtube.com/watch?v=RywJrNleFzA&list=PLTmnpY-mAW8gtLHPAoK7Kg9807A_7Vu6N&index=1&t=4s)

## How to run the project?

Journal necessary technologies to be installed:
- Java JDK 17
- Maven
- MySQL Database

Just do the following steps:
1. For Infrastructure (set up local database and server) follow the <a href="journal_infra/INFRA_README.md">Infra Readme</a>.
2. On project persistence.xml file [<a href="Journal/src/main/resources/META-INF/persistence.xml">/src/main/resources/META-INF/persistence.xml</a>] insert your MySQL username and password.
3. Still on project, configure your config.properties [<a href="Journal/src/main/resources/config.properties">/src/main/resources/config.properties</a>].
4. Run command: ``mvn clean install`` on ``/Journal``
5. Copy 'journal.war' generated in ``/target`` and paste it on your Java Server/Container deployment folder. (Recommended Jboss Wildfly: ${JBOSS_HOME}/standalone/deployments/)
6. Start your Java Server/Container (Recommended Jboss Wildfly: on ${JBOSS_HOME}/bin/ run:
    - [Windows] ``./standalone.bat``
    - [Linux/Mac] ``./standalone.sh``

**Obs:** Steps 4, 5 and 6 can be favored by your domain IDE

## Tech Stack (and Design Patterns)
- Java 17
- JSF and Primefaces
- Hibernate with Criteria
- Java Mail
- Maven
- MVC (with Service Layer)
- DAO 
- TDD
- JUnit 5
- MySQL Database
- JBoss Wildfly 17.0
- Security (Authentication, Authorization and Access Control)
