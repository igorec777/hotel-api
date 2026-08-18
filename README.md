# hotel-api

REST API для работы с отелями (`/property-view`).

```
mvn spring-boot:run
```

Порт: `8092`

- GET `/property-view/hotels`
- GET `/property-view/hotels/{id}`
- GET `/property-view/search?name=&brand=&city=&country=&amenities=`
- POST `/property-view/hotels`
- POST `/property-view/hotels/{id}/amenities`
- GET `/property-view/histogram/{param}` (`brand` | `city` | `country` | `amenities`)

Swagger: http://localhost:8092/property-view/swagger-ui.html