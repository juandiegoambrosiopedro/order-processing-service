# Proyecto: Microservicio de Procesamiento de Órdenes

Microservicio desarrollado con arquitectura hexagonal usando Spring Boot para gestionar órdenes,
incluyendo consulta de inventario y disponibilidad de stock por producto.

## Tecnologías
- Java 17
- Spring Boot v3.5.8
- Gradle 8.x
- MySQL 8.0+
- Spring Security (Basic Auth)
- OpenAPI/Swagger
- Lombok

## Estructura del Proyecto
```
order-processing-service/
├── src/
│   ├── main/
│   │   ├── java/mx/com/leyva/order
│   │   │   ├── config/              # Configuraciones (Security, OpenAPI)
│   │   │   ├── controller/          # REST Controllers
│   │   │   ├── service/             # Lógica de negocio
│   │   │   ├── client/              # Clientes REST externos
│   │   │   ├── model/
│   │   │   │   ├── dto/             # Data Transfer Objects
│   │   │   │   └── entity/          # Entidades JPA (Order, OrderItem)
│   │   │   ├── exception/           # Manejo de excepciones
│   │   │   └── util/                # Utilidades
│   │   │
│   │   └── resources/
│   │       ├── application.yml      # Configuración principal
│   │       └── db/migration/        # Scripts SQL(MySQL)
│   │           └── V1__create_orders_tables.sql
│   │
│   └── test/java/mx/com/leyva/order/
│       ├── controller/              # Tests de controladores
│       └── service/                 # Tests de servicios
│
├── build.gradle
└── README.md
```

## Configuración e Instalación

### Prerrequisitos

Antes de ejecutar el proyecto, asegúrate de tener instalado:

- **Java 17** ([Descargar](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html))
- **IntelliJ IDEA** Community o Ultimate
    - Plugin [Lombok](https://plugins.jetbrains.com/plugin/6317-lombok)
    - Plugin Gradle (incluido por defecto)
- **MySQL 8.0+** ([Descargar](https://dev.mysql.com/downloads/mysql/))
- **Gradle 8.x** (o usar el wrapper incluido)
- **(Opcional)** Postman para pruebas de API

---

## Pasos para Ejecutar el Proyecto

### 1.- Clonar el Repositorio
```bash

git clone https://github.com/tu-usuario/order-processing-service.git
cd order-processing-service
```

### 2.- Importar el Proyecto en IntelliJ IDEA

1. Abre IntelliJ IDEA
2. Click en `File` → `Open`
3. Selecciona la carpeta del proyecto `order-processing-service`
4. IntelliJ detectará automáticamente que es un proyecto Gradle
5. Espera a que se descarguen las dependencias

### 3.- Configurar la Base de Datos

Tienes dos opciones: usar **MySQL** (recomendado para producción) o **H2** (más rápido para desarrollo/pruebas).

---

#### Opción A: MySQL (Recomendado)

##### 1. Ejecutar Scripts SQL

Desde MySQL Workbench, abre y ejecuta el script ubicado en:
```
src/main/resources/db/migration/V1__create_orders_tables.sql
```

##### 2. Configurar application.yml

Edita `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/order_processing_db?useSSL=false&serverTimezone=UTC
    username: order_user
    password: order_pass123
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true
    properties:
      hibernate:
        format_sql: true
```

---

#### Opción B: H2 Database (Para desarrollo rápido)

Si **no tienes MySQL instalado** o prefieres una base de datos en memoria para pruebas rápidas:

##### 1. Configurar application.yml

Edita `src/main/resources/application.yml`:
```yaml
spring:
   datasource:
      url: jdbc:h2:mem:testdb;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH
      username: sa
      password:
      driver-class-name: org.h2.Driver
   jpa:
      hibernate:
         ddl-auto: update
      show-sql: false
   h2:
      console:
         enabled: true
         path: /h2-console
```

##### 2. Acceder a la Consola H2 (Opcional)

Una vez iniciada la aplicación, accede a:
```
http://localhost:8080/h2-console
```

**Credenciales de conexión:**
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: *(dejar vacío)*
---

### 4.- Ejecutar la Aplicación

#### Opción A: Desde IntelliJ IDEA (Recomendado)

1. Busca la clase principal: `OrderProcessingServiceApplication.java`
2. Click derecho → `Run 'OrderProcessingServiceApplication'`
3. O usa el botón ▶ verde en la parte superior


### Verificar que el Servicio Esté Corriendo

Deberías ver en la consola:
```
Started OrderProcessingServiceApplication in X.XXX seconds
```

**Verificar health check:**
```bash
curl http://localhost:8080/actuator/health
```

Respuesta esperada:
```json
{"status":"UP"}
```

---

## 5.- Probar el API

### Acceder a Swagger UI

1. Abre tu navegador en: **http://localhost:8080/swagger-ui.html**

2. **Autenticarse:**
    - Click en el botón **"Authorize"** (esquina superior derecha)
    - Ingresa las credenciales:
        - Username: `admin`
        - Password: `admin123`
    - Click en "Authorize" → "Close"

3. **Probar endpoints:**
    - Selecciona `POST /orders/process`
    - Click en "Try it out"
    - Pega el JSON de ejemplo
    - Click en "Execute"

### Probar con Postman

#### Importar Collection 
Importar cada uno de archivo de la ruta: `order-processing-service\postman_collection`
impórtalo en Postman:
```bash

O1__order_riesgo_bajo.json
O2__order_riesgo_medio.json
O3__order_riesgo_alto.json
O4__order_stock_insuficiente.json
O5__inventory_sku_stock_disponible.json
```


### Probar con cURL
```bash

curl --location 'http://localhost:8080/orders/process' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic YWRtaW46YWRtaW4xMjM=' \
--header 'Cookie: JSESSIONID=8F935484871197891ED51BF35DD9A8BE' \
--data '
{
"orderId": "ORD-FUEL-0001",
"customerId": "CUST-PARTICULAR-01",
"canal": "WEB",
"items": [
{
"sku": "SKU-ADIT-LIMP",
"cantidad": 1,
"precioUnitario": 350.00
}
]
}'
```

#### Crear Request Manual

1. **Crear nuevo request:**
    - Method: `POST`
    - URL: `http://localhost:8080/orders/process`

2. **Configurar autenticación:**
    - Tab `Authorization`
    - Type: `Basic Auth`
    - Username: `admin`
    - Password: `admin123`

3. **Configurar Body:**
    - Tab `Body`
    - Selecciona `raw` y `JSON`
    - Pega:
```json
{
  "orderId": "ORD-FUEL-0002",
  "customerId": "CUST-FLOTILLA-02",
  "canal": "APP",
  "items": [
    {
      "sku": "SKU-DIESEL",
      "cantidad": 100,
      "precioUnitario": 24.80
    },
    {
      "sku": "SKU-UREA-DEF",
      "cantidad": 20,
      "precioUnitario": 18.00
    }
  ]
}
```

4. **Enviar:** Click en `Send`


## 6.- Ejecutar Tests (Opcional)

### Desde IntelliJ IDEA:
1. Click derecho en la carpeta `test/`
2. Selecciona `Run 'Tests in...'`

## Credenciales de Prueba

| Usuario | Contraseña | Rol   |
|---------|-----------|-------|
| admin   | admin123  | ADMIN |
| user    | user123   | USER  |

---

## URLs Importantes

| Servicio     | URL                                      |
|--------------|------------------------------------------|
| Órdenes      | http://localhost:8080/orders/process     |
| inventario   | http://localhost:8080/actuator/health    |
| Aplicación   | http://localhost:8080                    |
| Swagger UI   | http://localhost:8080/swagger-ui.html    |
| API Docs     | http://localhost:8080/api-docs           |
| Health Check | http://localhost:8080/actuator/health    |


---

## 👥 Autor

Juan Diego Ambrosio Pedro - [GitHub](https://github.com/tu-usuario)
