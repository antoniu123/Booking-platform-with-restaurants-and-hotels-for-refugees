DROP TABLE ORDERS_DETAIL;
DROP TABLE ORDERS;
DROP TABLE MENU_RESTAURANT;
DROP TABLE RESTAURANT;
DROP TABLE RESERVATION;
DROP TABLE HOTEL;
DROP TABLE HELP_POINT;
DROP TABLE MAILS;


CREATE TABLE HOTEL
(
    ID NUMBER(3) GENERATED ALWAYS AS IDENTITY MINVALUE 1 MAXVALUE 999 INCREMENT BY 1 START WITH 1 CACHE 5 NOORDER NOCYCLE NOKEEP NOSCALE NOT NULL,
    NAME VARCHAR2(100 BYTE) NOT NULL,
    ZONE VARCHAR2(100 BYTE) NOT NULL,
    NR_ROOMS NUMBER(3) NOT NULL,
    IMAGE VARCHAR2(100 BYTE),
    PRIMARY KEY (ID)
);


CREATE TABLE RESERVATION
(
    ID NUMBER(10) GENERATED ALWAYS AS IDENTITY MINVALUE 1 MAXVALUE 9999999999 INCREMENT BY 1 START WITH 1 CACHE 5 NOORDER  NOCYCLE  NOKEEP  NOSCALE  NOT NULL ENABLE,
    HOTEL_ID NUMBER(10) NOT NULL,
    DATE_IN DATE NOT NULL,
    DATE_OUT DATE NOT NULL,
    VALID NUMBER(1),
    USER_ID VARCHAR2(60),
    PRIMARY KEY (ID),
    CONSTRAINT FK_HOTEL FOREIGN KEY (HOTEL_ID) REFERENCES HOTEL (ID)
);


CREATE TABLE RESTAURANT
(
    ID NUMBER(3) GENERATED ALWAYS AS IDENTITY MINVALUE 1 MAXVALUE 999 INCREMENT BY 1 START WITH 1 CACHE 5 NOORDER NOCYCLE NOKEEP NOSCALE NOT NULL,
    NAME VARCHAR2(100 BYTE) NOT NULL,
    IMAGE VARCHAR2(100 BYTE),
    PRIMARY KEY (ID)
);


CREATE TABLE MENU_RESTAURANT
(
    ID NUMBER(10) GENERATED ALWAYS AS IDENTITY MINVALUE 1 MAXVALUE 9999999999 INCREMENT BY 1 START WITH 1 CACHE 5 NOORDER  NOCYCLE  NOKEEP  NOSCALE  NOT NULL ENABLE,
    RESTAURANT_ID NUMBER(10) NOT NULL,
    NAME VARCHAR2(30) NOT NULL,
    PRICE NUMBER(5,2) NOT NULL,
    IMAGE VARCHAR2(50),
    PRIMARY KEY (ID),
    CONSTRAINT FK_RESTAURANT FOREIGN KEY (RESTAURANT_ID) REFERENCES RESTAURANT(ID)
);

CREATE TABLE ORDERS
(
    ID NUMBER(3) GENERATED ALWAYS AS IDENTITY MINVALUE 1 MAXVALUE 999 INCREMENT BY 1 START WITH 1 CACHE 5 NOORDER NOCYCLE NOKEEP NOSCALE NOT NULL,
    RESTAURANT_ID NUMBER(10) NOT NULL,
    STATUS VARCHAR2(10) NOT NULL, --NEW,OPEN,SENT
    PRICE NUMBER(10,2) NOT NULL,
    USER_ID VARCHAR2(60),
    PRIMARY KEY (ID),
    CONSTRAINT FK_RESTAURANT_ORDER FOREIGN KEY (RESTAURANT_ID) REFERENCES RESTAURANT(ID)
);


CREATE TABLE ORDERS_DETAIL
(
    ID NUMBER(3) GENERATED ALWAYS AS IDENTITY MINVALUE 1 MAXVALUE 999 INCREMENT BY 1 START WITH 1 CACHE 5 NOORDER NOCYCLE NOKEEP NOSCALE NOT NULL,
    ORDERS_ID NUMBER(10) NOT NULL,
    MENU_RESTAURANT_ID NUMBER(10) NOT NULL,
    QUANTITY NUMBER(2) NOT NULL,
    PRICE NUMBER(10,2) NOT NULL,
    PRIMARY KEY (ID),
    CONSTRAINT FK_ORDERS FOREIGN KEY (ORDERS_ID) REFERENCES ORDERS(ID),
    CONSTRAINT FK_MENU_RESTAURANT FOREIGN KEY (MENU_RESTAURANT_ID) REFERENCES MENU_RESTAURANT(ID)
);

CREATE TABLE HELP_POINT
(
    ID NUMBER(3) GENERATED ALWAYS AS IDENTITY MINVALUE 1 MAXVALUE 999 INCREMENT BY 1 START WITH 1 CACHE 5 NOORDER NOCYCLE NOKEEP NOSCALE NOT NULL,
    NAME VARCHAR2(100 BYTE) NOT NULL,
    ADDRESS VARCHAR2(40) NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE MAILS
(
    ID NUMBER(3) GENERATED ALWAYS AS IDENTITY MINVALUE 1 MAXVALUE 999 INCREMENT BY 1 START WITH 1 CACHE 5 NOORDER NOCYCLE NOKEEP NOSCALE NOT NULL,
    SUBJECT VARCHAR2(100 BYTE) NOT NULL,
    MESSAGE_TEXT VARCHAR2(4000 BYTE) NOT NULL,
    SENDER_ADDRESS VARCHAR2(100 BYTE) NOT NULL,
    PRIMARY KEY (ID)
);