Каталог

API:

1. Пользовательский уровень. (OpenAPI)

    1.1. Получение конкретной категории (раздела) с сущностями и подразделами

    1.2. Получение карты разделов

    1.3. Получение списка разделов по типу/названию

2. Уровень администратора. (OpenAPI)

    2.1. CRUD разделов.
    
3. Уровень микросервисов. (Kafka|Rabbit)

    3.1. Добавить сущность соответственного типа.

    3.2. Удалить сущность.

    3.3. Изменить сущность.


MODULES:

**ok-catalogue-backend-common** - Основной модуль бэкенда.

**ok-catalogue-transport-common** - Общие модели и валидаторы транспортного уровня

**ok-catalogue-transport-rest** - Маппер между  транспортом и внутренними моделями 

**ok-catalogue-app-ktor** - Модуль KTOR-фреймворка, REST-сервис

**ok-catalogue-be-logic** - Модуль логики

**ok-catalogue-common-cor** - Библиотека Chain of responsibility

**ok-catalogue-common-validators** - Библиотека валидаторов

**ok-catalogue-be-repo-inmemory** - Модуль репозитория cache2k