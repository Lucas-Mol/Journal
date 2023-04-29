# Journal
A daily website solution
## Intro
The **Journal** is a personal Java/Web project created to solve a diary writing problem, making the process more digital and softer. It's based and inspired by social media' flow where you can post your 'work in progress' stuffs, future ideas, cool (or not) messages and even outbursts and immediately see, edit and remove it on same dashboard.

On the dev/technical side, one of the first ideas of the project was build a Java WebSite using the same (or as close as possible) **tech stack** that I've used on 'EspacoSeuPet' (https://github.com/Lucas-Mol/EspacoSeuPet) but bringing/testing my new knowlegdes. Following this idea, I've tried to achieve a modern and customized view as well as a clean/designed code based on variety of design pattern's conventions, according the tech stack limits. It's bringing up the project close to a 'Java Enterprise Monolith Level' but it's **worthwhile to note** that the project's final aim is **not** to reach a commercial level, so its archteture was not designed to. 

**Conclusion:** Journal is a personal Java/Web project to test my knowledges in JSF, Hibernate Criteria e etc.
## Overview

![Login](https://cdn.discordapp.com/attachments/778788148921761822/1101943586909589655/journal_login.jpg)

![Dashboard](https://cdn.discordapp.com/attachments/778788148921761822/1101943588125954059/journal_dashboard.jpg)

![Filtering](https://cdn.discordapp.com/attachments/778788148921761822/1101943588562153522/journal_dashboard_search.jpg)

[![Video]()]()

## How to run the project?

Journal necessary technologies to be installed:
- Java JDK 17
- Maven
- MySQL Datbase

Just do the following steps:
1. For Infrastructure (set up local database and server) follow the <a href="journal_infra/INFRA_README.md">Infra Readme</a>.
2. On project persistence.xml file [<a href="Journal/src/main/resources/META-INF/persistence.xml">/src/main/resources/META-INF/persistence.xml</a>] insert your MySQL username and password.
3. Still on project, configure your config.properties [<a href="Journal/src/main/resources/config.properties">/src/main/resources/config.properties</a>].
4. Run command: ``mvn clean install`` on ``/Journal``
5. Copy 'journal.war' generated in ``/target`` and paste it on your Java Server/Container deployment folder. (Recommended Jboss Wildfly: ${JBOSS_HOME}/standalone/deployments/)
6. Start your Java Server/Container (Recommended Jboss Wildfly: on ${JBOSS_HOME}/bin/ run:
    - [Windows] ``./standalone.bat``
    - [Linux/Mac] ``./standalone.sh``

**Obs:** Steps 3, 4 and 6 can be favored by your domain IDE

## Tech Stack
- Java 17
- JSF and Primefaces
- Hibernate with Criteria
- Java Mail
- Maven
- JUnit 5
- MySQL Database
- JBoss Wildfly 17.0
