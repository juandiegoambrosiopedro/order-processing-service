#TABLA: 1
#DESCRIPCIÓN: Almacena la información principal de las órdenes procesadas.
CREATE TABLE ORDERS (
    ID BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Clave primaria técnica',
    ORDER_ID VARCHAR(50) NOT NULL UNIQUE COMMENT 'Identificador de negocio de la orden',
    CUSTOMER_ID VARCHAR(50) NOT NULL COMMENT 'Identificador del cliente',
    CANAL VARCHAR(20) NOT NULL COMMENT 'Canal de origen: WEB, APP O CALLCENTER',
    SUBTOTAL DECIMAL(12,2) NOT NULL COMMENT 'Subtotal calculado de la orden',
    IVA DECIMAL(12,2) NOT NULL COMMENT 'Impuesto IVA(16%)',
    TOTAL DECIMAL(12,2) NOT NULL COMMENT 'Total final de la orden',
    RIESGO VARCHAR(10) NOT NULL COMMENT 'Nivel de riesgo: Alto, Medio, Bajo',
    CREATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de alta de la orden',
    INDEX IDX_ORDER_ID (ORDER_ID)
) COMMENT = 'Tabla de órdenes';

#TABLA: 2
#DESCRIPCIÓN: Detalle de los productos por orden.
CREATE TABLE ORDER_ITEMS (
    ID BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Clave primaria del Item',
    ORDER_ID BIGINT NOT NULL COMMENT 'Relación con orders.ID',
    SKU VARCHAR(50) NOT NULL COMMENT 'Identificador del producto',
    CANTIDAD INT NOT NULL COMMENT 'Cantidad solicitada del producto',
    PRECIO_UNITARIO DECIMAL(12,2) NOT NULL COMMENT 'Precio unitario del producto',
    CONSTRAINT FK_ORDER_ITEMS_ORDER
        FOREIGN KEY (ORDER_ID)
        REFERENCES ORDERS (ID)
        ON DELETE CASCADE
) COMMENT = 'Detalle de items de la orden';
