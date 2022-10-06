Библиотека предназначена для преобразования строки (в данной версии библиотеки только **json**) в формате ```std::string::String``` к структуре команды, объявленному внутри данной библиотеки, а также для отправки ответа на запрос (формируется строка (в данной версии библиотеки только ```json```) в формате ```std::string::String```).

Таким образом, библиотека делится на два интерфейса: отправка в ```send::interface``` и получение в ```receive::interface```.

С каждой командой должны отправляться также и данные пользователя, а также опционально идентификатор запроса в формате ```not negative i64```. Общий формат входящего запроса:
```JSON
{
    "user": {
        "login": "string",
        "password": "string",
    },
    "command name": "string",
    // (Optional):
    "request id": /*not neg i64*/,
    "command arguments": {
        // --snip--
    }
}
```
Не каждая команда требует аргументы для выполнения, поэтому ключ **command arguments** в **json** можно не указывать.

Описание дракона, который будет передаваться в аргументах некоторых команд:
```JSON
{
    // --snip--
    "dragon": {
        "name": "string",
        "coordinates": {
            "x": /*f64*/,
            "y": /*i64*/
        },
        "age": /*not neg i64*/,
        "description": "string",
        "wingspan": /*not neg i32*/,
        "type name": "string",
        "number of treasures": /*f32, not neg*/,
        // (Optional, coz its meta data):
        "id": /*not neg i64*/,
        "owner login": "string",
        "creation date": /*string, in RFC 3339 (ISO 8601) format, e.g. "2011-12-03T10:15:30+01:00", "2022-09-02T22:02:11Z"*/,
        "key": "string",
    }
}
```
Ни одна команда не требует ```id```, ```owner login```, ```creation date```, но может требовать `key` для своего выполнения, поэтому эти ключи можно не указывать. Однако в ответ могут быть посланы драконы, которые содержат три эти параметра, поэтому было принято решение сделать структуру с опциональными параметрами

В ответ на команду может быть отправлен ответ: удобно описать возможные варианты для каждой команды. В ответе, так же как и в запросе, опционально указывается идентификатор запроса в формате ```not neg i64``` (библиотека не может знать, указал ли его отправитель, поэтому этот момент не контролируется библиотекой). Могут возникать ошибки, поэтому узнать это можно получив значение ключа **error flag**. Опционально возможно указание ошибки - а именно идентификатора ошибки и ее описание (оба варианта также опциональны, но если есть структура ошибки, один вариант должен присутствовать):

```JSON
{
    // --snip--
    "error flag": /*true or false*/,
    // (Optional):
    "enter state": /*"entered", "wrong login" or "wrong password"*/,
    "request id": /*not neg i64*/,
    "error": {
        // (Optional), at least one key:
        "identificator": "string",
        "message": "string"
    },
    "command specific answer": {
        // commans specific keys in answer
    }
}
```

С каждой командой, если не было ошибок, отправляется **"error flag"**, который, в состоянии **false**, что говорит о том, что операция прошла без ошибок. **"enter state"** не присутствует лишь в команде регистрации (а в команде авторизации вовсе заменяет **"state"**) и в случае возникновения ошибок (так как не обязательно сообщать, на каком этапе возникла ошибка). Однако у операций может быть состояние неуспеха, не сопряженное с состоянием ошибки. Оно может добавляться в некоторые команды:
```JSON
{
    // --snip--
    // (Optional)
    "success flag": /*true or false*/
}
```
Если же описание состояния не укладывается в ```boolean```, то следует описать указанием варианта из описаных в API в формате строки:
```JSON
{
    // --snip--
    // (Optional)
    "state": "string"
}
```

## Ошибки (при возникновении **"error flag" = true**):
- если нет ключей, предусмотренных командой (например, объект дракона не имеет ключа "name") — **"identificator" = "key not found"**, **"message"** же содержит название ключа
- если ключ имеет тип, не предусмотренный командой (например, значение ключа "age" объекта дракона является строкой) — **"identificator" = "key value has illegal type"**, **"message"** же содержит название ключа
- если ключ имеет нарушение типа, предусмотренного командой (например, значение ключа "age" объекта дракона не является положительным) — **"identificator" = "key value is out of bounds"**, **"message"** же содержит название ключа
- если приключилась внутренняя ошибка, не касающаяся бизнес-логики, — **"identificator" = "server internal error"**

## Список команд с требуемыми для них аргументами:

#### Получение
```Register``` с **"command name": "register"** - не требуется
#### Отправка
**"success flag" = true** если регистрация прошла успешно, **false**, если такой пользователь уже существует

#### Получение
```SignIn``` с **"command name": "sign in"** - не требуется
#### Отправка
**"enter state" = "entered"**, если пользователь с парой логин-пароль существует, **"wrong login"**, если логина не существует, **"wrong password"**, если пароль для указанного логина неверный

#### Получение
```InsertDragon``` с **"command name": "insert dragon"** (только `"key"`):
```JSON
{
    // --snip--
    "command arguments": {
        "dragon": /*dragon*/
    }
}
```
#### Отправка
**"state" = "inserted"**, если успешно, **"dublicate key"** если дракон с таким ключом уже есть

#### Получение
```GetDragons``` с **"command name": "get dragons"** - не требуется
#### Отправка
```JSON
{
    "dragons": [
        /*dragon 1*/,
        /*dragon 2*/,
        /*...*/,
        /*dragon N*/
    ]
}
```
Под **dragon** подразумевается структура. Если драконов нет, то под ключом **"dragons"** скрывается пустой массив

#### Получение
```DragonsInfo``` с **"command name": "dragons info"** - не требуется
#### Отправка
```JSON
{
    "collection type": "string",
    "creation date": /*ISO 8601*/,
    "dragons count": /*not neg i64*/
}
```

#### Получение
```UpdateDragon``` с **"command name": "update dragon"** (только `"key"`):
```JSON
{
    // --snip--
    "command arguments": {
        "id": /*not neg i64*/,
        "dragon": /*dragon*/
    }
}
```
#### Отправка
**"state" = "updated"**, если успешно, **"wrong id"**, если дракона с таким **id** нет, **"not yours"**, если не владеет этим драконом

#### Получение
```RemoveDragonByKey``` с **"command name": "remove dragon by key"**:
```JSON
{
    // --snip--
    "command arguments": {
        "key": "string"
    }
}
```
#### Отправка
**"state" = "removed"**, если успешно, **"wrong key"**, если дракона с таким ключом нет, **"not yours"**, если не владеет этим драконом

#### Получение
```ClearDragons``` с **"command name": "clear dragons"** - не требуется
#### Отправка
```JSON
{
    "cleared count": /*not neg i64*/
}
```
Удаляются только те драконы, которыми пользователь владеет

#### Получение
```RemoveGreaterDragons``` с **"command name: "remove greater dragons"** и ```RemoveLowerDragons``` с **"command name: "remove lower dragons"** (опциональные не потребуются):
```JSON
{
    // --snip--
    "command arguments": {
        "dragon": /*dragon*/
    }
}
```
#### Отправка
```JSON
{
    "removed count": /*not neg i64*/
}
```
Удаляются только те драконы, которыми пользователь владеет


#### Получение
```ReplaceIfDragonIsGreater``` с **"command name: "replace if dragon is greater"** (только `"key"`):
```JSON
{
    // --snip--
    "command arguments": {
        "key": "string",
        "dragon": /*dragon*/
    }
}
```
#### Отправка
**"state" = "replaced"**, если успешно, **"state" = "isn't greater"**, если не заменили, потому что дракон не больше, **"wrong id"**, если дракона с таким **id** нет, **"not yours"**, если не владеет этим драконом

#### Получение
```GetWingspansSum``` с **"command name": "get wingspans sum"** - не требуется
#### Отправка
```JSON
{
    // --snip--
    "wingspans sum": /*not neg i64*/
}
```
Сумма от всех драконов

#### Получение
```GetDragonsWithThisDescription``` с **"command name": "get dragons with this description"**:
```JSON
{
    // --snip--
    "command arguments": {
        "description": "string"
    }
}
```
#### Отправка
```JSON
{
    "dragons": [
        /*dragon 1*/,
        /*dragon 2*/,
        /*...*/,
        /*dragon N*/
    ]
}
```

#### Получение
```GetUniqueNumbersOfTreasures``` с **"command name": "get unique numbers of treasures"** - не требуется
#### Отправка
```JSON
{
    "unique numbers of treasures": [
        /*f32 1*/,
        /*f32 2*/,
        /*...*/,
        /*f32 N*/
    ]
}
```
