For start app: 
1. Upadte tmdb.apikey in src\main\resources\application.properties
2. Run mvn clean install
3. Run mvn spring-boot:run

### Register user
curl -X POST --data "email=test@test2.com&password=test" http://localhost:8080/user/register/ -v

### View popular movies
curl -X POST --data "email=test@test2.com&password=test" http://localhost:8080/movie/popular/ -v

### Add actor to favorites (Tom Cruise for current example)
curl -X POST --data "email=test@test2.com&password=test&actorName=Tom Cruise" http://localhost:8080/actor/add-to-favorites/ -v

### Remove actor from favorites (Tom Cruise for current example)
curl -X POST --data "email=test@test2.com&password=test&actorName=Tom Cruise" http://localhost:8080/actor/remove-from-favorites/ -v

### Mark movie watched (Iron Man 3 for current example)
curl -X POST --data "email=test@test2.com&password=test&movieName=Iron Man 3" http://localhost:8080/movie/add-to-watched-list/ -v

### Search by month and year unviewed movies with favorite actors
curl -X POST --data "email=test@test2.com&password=test&year=2013&month=04" http://localhost:8080/movie/search-by-date-unviewed-with-favorite-actors/ -v