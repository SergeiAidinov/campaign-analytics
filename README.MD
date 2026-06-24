# 📊 Campaign Analytics Service

Spring Boot приложение для анализа рекламных показов и пользовательских событий.

Сервис строит метрики эффективности кампаний:
- временные ряды (CTR, EvPM)
- агрегации по измерениям (mm_dma, site_id)
- обработку событий по uid

---

# 🧠 Бизнес-логика

Данные состоят из двух источников:

## 📌 Impressions (interview.X.csv)
Лог показов пользователей.

## 📌 Events (interview.y.csv)
Лог событий пользователей (clicks, registrations и др.).

Связь между таблицами осуществляется через `uid`.

---

# 📂 Формат данных

## 📄 Impressions

reg_time,uid,fc_imp_chk,fc_time_chk,utmtr,mm_dma,osName,model,hardware,site_id

Пример:
2021-07-21 10:15:00,00000000-0000-0001-0000-000000000001,0,7,2,530,iOS,iPhone XR,Mobile Phone,siteA

---

## 📄 Events

uid,tag

Пример:</br>
00000000-0000-0001-0000-000000000001,fclick  
00000000-0000-0001-0000-000000000001,registration  

---

# 🚀 Запуск проекта

./mvnw spring-boot:run

---

# 📥 Загрузка данных

## Impressions
POST /files/impressions/upload  
Content-Type: multipart/form-data  
file=@interview.X.csv

## Events
POST /files/events/upload  
Content-Type: multipart/form-data  
file=@interview.y.csv

---

# 📊 Analytics API

Базовый путь:
/api/campaign

---

## 📈 Time Series Metrics

GET /api/campaign/metrics

Query параметры:
- eventTypes — список событий (fclick, registration, etc.)
- from — начальная дата (yyyy-MM-dd)
- to — конечная дата (yyyy-MM-dd)

Пример ответа:

[
  {
    "date": "2021-07-21",
    "impressionsCount": 1200,
    "ctr": 12.5,
    "evpm": 145.3
  }
]

---

## 📊 Aggregation by mm_dma

GET /api/campaign/aggregation/mm-dma

Группировка:
- mm_dma (DMA регион)

Пример ответа:

[
  {
    "value": "530",
    "impressionsCount": 3200,
    "ctr": 10.8,
    "evpm": 132.4
  }
]

---

## 🌐 Aggregation by site_id

GET /api/campaign/aggregation/site-id

Группировка:
- site_id (источник показа)

Пример ответа:

[
  {
    "value": "siteA",
    "impressionsCount": 5400,
    "ctr": 9.7,
    "evpm": 118.2
  }
]

---

# 📐 Метрики

## CTR (Click Through Rate)

CTR = clicks / impressions * 100

## EvPM (Events per Mille)

EvPM = events / impressions * 1000

---

# ⚙️ Архитектура

Controller → Service → DataStore (in-memory)
                     ↓
                CSV Parser

---

# 🧩 Особенности реализации

- In-memory хранение (TreeMap + HashMap)
- Асинхронная загрузка CSV файлов
- Группировка через Stream API
- Фильтрация событий по uid + типам

---

# 📌 Возможные улучшения

- переход на ClickHouse / PostgreSQL
- кеширование агрегатов
- pre-aggregation pipeline
- job status endpoint
- pagination и фильтрация API

---

# 🧪 Пример использования

curl -X POST http://localhost:8080/files/impressions/upload -F file=@interview.X.csv  
curl -X POST http://localhost:8080/files/events/upload -F file=@interview.y.csv  

---

# 🛠 Tech Stack

- Java 21
- Spring Boot
- OpenCSV
- Swagger / OpenAPI
- Stream API