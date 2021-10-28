[![Build status](https://ci.appveyor.com/api/projects/status/i1n7hqpg0vvr95l6?svg=true)](https://ci.appveyor.com/project/VlCherno/testmode)
## Задача №2 - Тестовый режим (необязательная)

Разработчики Интернет Банка изрядно поворчав предоставили вам тестовый режим запуска целевого сервиса, в котором открыта программная возможность создания Клиентов Банка, чтобы вы могли протестировать хотя бы функцию входа.

Вот представленное ими описание (дословно):
```
Для создания клиента нужно делать запрос вида:

POST /api/system/users
Content-Type: application/json

{
    "login": "vasya",
    "password": "password",
    "status": "active" 
}

Возможные значения поля статус:
* "active" - пользователь активен
* "blocked" - пользователь заблокирован

В случае успешного создания пользователя возвращается код 200

При повторной передаче пользователя с таким же логином будет выполнена перезапись данных пользователя
```

Давайте вместе разбираться. Вы уже проходили на одном из предыдущих курсов:
* клиент-серверное взаимодействие
* HTTP-методы и коды ответов
* формат данных - JSON

Ключевое для нас - мы должны научиться из Java-кода отправлять подобные запросы.

В этом нам поможет библиотека [Rest Assured](http://rest-assured.io). Мы крайне настоятельно рекомендуем ознакомиться с этой библиотекой, потому что она достаточно часто используется в тестировании.

Подключается обычным образом в Gradle:
```groovy
testImplementation 'io.rest-assured:rest-assured:4.1.2'
testImplementation 'com.google.code.gson:gson:2.8.6'
```

Библиотека [Gson](https://github.com/google/gson) нужна для того, чтобы иметь возможность сериализовать (преобразовывать) Java-объекты в JSON.

Дальнейшее использование выглядит следующим образом:
```java
// спецификация нужна для того, чтобы переиспользовать настройки в разных запросах
class AuthTest {
    private static RequestSpecification requestSpec = new RequestSpecBuilder()
        .setBaseUri("http://localhost")
        .setPort(9999)
        .setAccept(ContentType.JSON)
        .setContentType(ContentType.JSON)
        .log(LogDetail.ALL)
        .build();

    @BeforeAll
    static void setUpAll() {
        // сам запрос
        given() // "дано"
            .spec(requestSpec) // указываем, какую спецификацию используем 
            .body(new RegistrationDto("vasya", "password", "active")) // передаём в теле объект, который будет преобразован в JSON
        .when() // "когда" 
            .post("/api/system/users") // на какой путь, относительно BaseUri отправляем запрос
        .then() // "тогда ожидаем"
            .statusCode(200); // код 200 OK
    }
    ...
}
```

Это не лучший формат организации, будет лучше, если как в предыдущей задаче, вы вынесете это в класс-генератор, который по требованию вам будет создавать рандомного пользователя, сохранять его через API и возвращать вам в тест.

В логах теста вы увидите:
```
Request method:	POST
Request URI:	http://localhost:9999/api/system/users
Proxy:			<none>
Request params:	<none>
Query params:	<none>
Form params:	<none>
Path params:	<none>
Headers:		Accept=application/json, application/javascript, text/javascript, text/json
				Content-Type=application/json; charset=UTF-8
Cookies:		<none>
Multiparts:		<none>
Body:
{
    "login": "vasya",
    "password": "password",
    "status": "active" 
}
```
