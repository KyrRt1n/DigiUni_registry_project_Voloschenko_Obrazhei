### Voloschenko Artem and Oleksandr Obrazhei Digital University project

Запуск тестів `mvn test`

Мапа інтерфейсу:
```
--- UNIVERSITY SYSTEM MENU ---
│
├── 1. Search & Reports [Всі ролі]
│   ├── 1. Print University Structure
│   ├── 2. Find Student/Teacher
│   │   ├──  Find students by course, group, full name, ticket ID
│   │   └──  Find teacher by lastname, full name
│   │
│   ├── 3. Sort Students/Teachers
│   │   ├──  Sort students by lastname, course, faculty, department, group
│   │   └──  Sort teachers by lastname, faculty (A-Z)
│   │
│   ├── 4. Show all students
│   ├── 5. Faculty Statistics Report
│   ├── 6. Sorted Students by Course
│   ├── 7. Full Student Report (DTO)
│   ├── 8. Students of a Department by given Course (plain + A-Z)
│   ├── 9. Students of a Department (by course)
│   ├── 10. Teachers of a Faculty (A-Z)
│   └── 11. Teachers of a Department (A-Z)
│
├── 2. Manage Students [MANAGER, ADMIN]
│   ├──  Add/Remove/Update Student
│   └──  Transfer Student to another department
│
├── 3. Manage Teachers [MANAGER, ADMIN]
│   ├──  Add/Remove/Update Teacher
│   └──  Show all Teachers
│
├── 4. Manage Departments [MANAGER, ADMIN]
│   ├──  Add/Remove/Update Department
│   └──  Show all Departments
│
├── 5. Manage Faculties [MANAGER, ADMIN]
│   ├──  Add/Remove/Update Faculty
│   └──  Show all Faculties
│
├── 6. Manage Users & Roles [тільки ADMIN]
│   ├──  Block/Unblock/Add/Remove/Edit  user
│   └──  Show all users
│
├── 7. Save University Structure to file [MANAGER, ADMIN]
│
├── 8. Reflection Demo (auto-built menu) [Всі ролі]
│   ├── 1. Show total counts [All roles]
│   ├── 2. Show all students [All roles]
│   ├── 3. Show all teachers [MANAGER+]
│   ├── 4. Validate first student [MANAGER+]
│   └── 5. DANGER: print university pointer [ADMIN]
│
├── 9. Logout [Всі ролі]
│
├── 10. Restore from backup [MANAGER, ADMIN]
│   ├── [Динамічний список збережених бекапів...]
│   └── 0. Cancel
│
└── 0. Exit Application [Всі ролі]
```

### Стандартні користувачі
| Логін     | Пароль     | Роль                                       |
|-----------|------------|--------------------------------------------|
| `user`    | `user123`  | USER (тільки перегляд)                     |
| `manager` | `mgr123`   | MANAGER (CRUD без керування користувачами) |
| `admin`   | `admin123` | ADMIN (повний доступ)                      |

Запустити TCP-клієнт можна запустивши файл /network.UniversityClient, що автоматично вас під'єднає за потрібною адресою, або використати будь-який TCP-клієнт (telnet / PuTTY / netcat) за адресою `localhost:9090`.

## TCP протокол

Сервер стартує автоматично на порту *9090*. Доступно такі команди, кожна з яких зевершується маркером END в кінці:

| Команда                   | Опис                                   |
|---------------------------|----------------------------------------|
| `HELP`                    | Список усіх команд                     |
| `PING`                    | Перевірка з'єднання (відповідь `PONG`) |
| `LIST_STUDENTS`           | Вивести усіх студентів                 |
| `LIST_FACULTIES`          | Усі факультети                         |
| `FIND_STUDENT <ticketId>` | Пошук студента за ID заліковки         |
| `EXIT`                    | Вийти з мережного режиму               |



```
DigiUni Registry
├── main
│   └── java
│       └── ua.sopsany
│           ├── Main.java
│           ├── university_data.json
│           ├── user_data.json
│           │
│           ├── auth
│           │   ├── AuthService.java
│           │   ├── Role.java
│           │   └── User.java
│           │
│           ├── dto
│           │   └── FacultyStatsRecord.java
│           │   └── StudentDTO.java
│           │
│           ├── exceptions
│           │   ├── DuplicateIdException.java
│           │   ├── EntityNotFoundException.java
│           │   └── UnauthorizedExcpetion.java
│           │
│           ├── models
│           │   ├── Department.java
│           │   ├── Faculty.java
│           │   ├── Person.java
│           │   ├── Student.java
│           │   ├── Teacher.java
│           │   └── University.java
│           │
│           └── utils
│               ├── FileStorageService.java
│               ├── GenericRepository.java
│               ├── InputHandler.java
│               ├── Repository.java
│               └── SearchService.java
└── test
    └── java
        └── ua.sopsany
              └── UniversityTest.java
```
<a href="https://tree.nathanfriend.com/?s=(%27options!(%27fancy!true~fullPat3trailingSlas3rootDot!true)~5(%275%27DigiUni%20Registry0main478ua.sopsany*Main2utils6InputHandler-Repository2Models6Departm9Faculty-Person-Stud9Teacher-University-6%27)~version!%271%27)*8%20-2%200%5Cn%202.7*3h!false~40%205source!6*%207java84%209ent-%01987654320-*">
  <img src="https://static.wikia.nocookie.net/terraria_gamepedia/images/a/ac/Tree.png" alt="fancy tree" width="30">
</a>


4. Мінімальний функціонал (обов'язковий)
   4.1 Мінімальні дані сутностей

   - [x] Університет: повна назва, скорочена назва, місто, адреса.
   - [x] Факультет: унікальний код, назва, скорочена назва, декан (посилання на викладача), контакти.
   - [x] Кафедра: унікальний код, назва, факультет (посилання), завідувач (посилання на викладача), кабінет/локація.
   - [x] Персона (базовий тип): унікальний ідентифікатор, ПІБ (3 частини), дата народження, email, телефон.
   - [x] Студент: ідентифікатор студента/залікова, курс (1-6), група, рік вступу, форма навчання (бюджет/контракт), статус (навчається/академвідпустка/відрахований).
   - [x] Викладач: посада, науковий ступінь, вчене звання, дата прийняття на роботу, ставка/навантаження.

4.2 Операції керування даними

   - [x] Створити/видалити/редагувати факультет.
   - [x] Створити/видалити/редагувати кафедру.
   - [x] Додати/видалити/редагувати студента/викладача в кафедрі.
   - [x] Переводити студента між групами/кафедрами та змінювати курс.

4.3 Пошук і звіти

   - [x] Знайти студента/викладача за ПІБ, курсом або групою.
   - [x] Вивести всіх студентів, впорядкованих за курсами.
   - [x] Вивести всіх студентів/викладачів факультету, впорядкованих за алфавітом.
   - [x] Вивести всіх студентів кафедри, впорядкованих за курсами.
   - [x] Вивести всіх студентів/викладачів кафедри, впорядкованих за алфавітом.
   - [x] Вивести всіх студентів кафедри вказаного курсу (звичайний список та впорядкований за алфавітом).

4.4 Доступ і ролі

   - [x] Потрібна авторизація (логін/пароль) і розмежування прав доступу.
   - [x] Користувач: лише перегляд (пошук і звіти).
   - [x] Менеджер: повний доступ до CRUD, без керування користувачами.
   - [x] Адміністратор: повний доступ + створення/редагування/блокування користувачів і ролей.

5. Розширені вимоги (покриття тем курсу)

   - [x] ООП та дизайн: Ієрархія Person -> Student/Teacher, використання інтерфейсів, принципів SOLID, композиції.
   - [x] Інкапсуляція та валідація: Приватні поля, валідатори, використання Optional.
   - [x] Колекції та дженеріки: Репозиторії Repository<T, ID>, обґрунтований вибір List/Set/Map, коректні equals()/hashCode().
   - [x] Stream API: Фільтрація, сортування, групування через стріми та лямбди.
   - [x] Дата та час: Використання LocalDate, LocalDateTime, розрахунок стажу/віку через Period/Duration.
   - [x] Сучасна Java: Використання record (для Read-only даних), sealed класів, 
   - Lombok.
   - [x] Власні винятки (Exceptions), 
   - [x] логування через SLF4J + Log4j2 (у файл).
   - [x] Тестування: Не менше 20 unit-тестів (JUnit), включаючи параметризовані.
   - [x] I/O: Збереження даних на диск (NIO.2, Path/Files), серіалізація (JSON або бінарна).
   - [x] Багатопоточність: Фонові операції або автозбереження.
   - [x] Мережевий режим: TCP клієнт-сервер.
   - [x] Reflection: Анотації для меню або валідації.

6. Технічні вимоги

   Мова: Java 17+.
   Збірка: Maven.
   Інтерфейс: консольне меню.
   Захист від некоректного введення.

8. Звіт

Word-документ, що містить: постановку задачі, розподіл ролей, опис можливостей, UML-діаграму, опис рішень та проблем, висновки, код.
9. Результат здачі

   Git-репозиторій з комітами і PR.
   Три теги: checkpoint-1, checkpoint-2, checkpoint-3.
   README.md з інструкцією.
   Word-звіт.