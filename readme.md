## Пререквизиты 
    1) Java (JDK v21)
    2) Maven не ниже 3.9.9
    3) СУБД PostgreSQL

## Настройка БД Postgres
    1) Перед тестированием выполнить DDL скрипт postgresql-DDL-scripts/create_schema.sql
    2) После тестирования выполнить DDL скрипт postgresql-DDL-scripts/drop_schema.sql

## Настройки сервисов (для локального запуска можно не менять)
### Сервис api-gateway
https://github.com/Anastasiia-Baldina/analyzer/blob/master/api-gateway-boot/src/main/resources/application.yaml
### Сервис file-storing
https://github.com/Anastasiia-Baldina/analyzer/blob/master/file-storing-boot/src/main/resources/application.yaml
### Сервис file-analysis
https://github.com/Anastasiia-Baldina/analyzer/blob/master/file-analysis-boot/src/main/resources/application.yaml
### Сервис api-word-cloud
https://github.com/Anastasiia-Baldina/analyzer/blob/master/api-word-cloud-boot/src/main/resources/application.yaml

## Сборка проекта
mvn clean package

## Запуск проекта (из директории проекта)
### Сервис api-word-cloud
java -jar api-word-cloud-boot/target/api-word-cloud-boot-1.0.jar
### Сервис file-storing
java -jar file-storing-boot/target/file-storing-boot-1.0.jar
### Сервис file-analysis
java -jar file-analysis-boot/target/file-analysis-boot-1.0.jar
### Сервис api-gateway
java -jar api-gateway-boot/target/api-gateway-boot-1.0.jar

## Swagger-ui
### Сервис api-gateway
http://localhost:8080/swagger-ui
### Сервис file-storing
http://localhost:8081/swagger-ui
### Сервис file-analysis
http://localhost:8082/swagger-ui
### Сервис api-word-cloud
http://localhost:8083/swagger-ui

## Open API
### Сервис api-gateway
http://localhost:8080/api-docs
### Сервис file-storing
http://localhost:8081/api-docs
### Сервис file-analysis
http://localhost:8082/api-docs
### Сервис api-word-cloud
http://localhost:8083/api-docs

## Cпецифические библиотеки
### Springdoc https://springdoc.org/ для внедрение swagger-ui в web-context приложения
### Kumo https://kennycason.com/posts/2014-07-03-kumo-wordcloud.html для генерации картинок word-cloud
