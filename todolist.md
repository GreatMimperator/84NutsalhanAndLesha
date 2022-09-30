Что имеем: окна входа и регистрации. Оба без валидации входных данных. Есть локализация, однако не для вариантов ошибок.
Для начала:
1. Сделать ф-ции валидации ошибок с установкой предупреждений при неверных данных
2. Сделать интерфейс для бизнес логики. То есть отправка логина-пароля при логине и регистрации
3. Добавить валидацию ошибок в бизнес логике - логин используется, пара логин-пароль неверна и т.п.