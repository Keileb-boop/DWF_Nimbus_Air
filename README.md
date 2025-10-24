<p align="center">
  <img width="540" height="350" alt="Gemini_Generated_Image_gsa1oqgsa1oqgsa1"
       src="https://github.com/user-attachments/assets/29afb9f1-9732-40c6-b86a-bd9a3e76a181" />
</p>

# ✈️ Nimbus Air – Sistema de Reservas Aéreas

**Nimbus Air** es una aplicación web que simula el proceso de gestión de reservas aéreas, pagos y sugerencias de usuarios.  
Desarrollado como un sistema modular que integra backend en **PHP** y **Java**, con una base de datos **MariaDB** administrada mediante **phpMyAdmin**.

---

## 🧩 **Características principales**

- 🧍‍♂️ Registro y autenticación de usuarios.
- 🪪 Gestión completa de **reservas** con información de vuelo, destino y clase.
- 💳 Procesamiento de **pagos** (PayPal, tarjeta, transferencia).
- 💬 Módulo de **sugerencias** o feedback de usuarios.
- 🛫 Asociación entre **aerolíneas, vuelos y pasajeros**.
- ⚙️ Documentación técnica y API generada con **Swagger UI**.
- 💡 Diseño escalable y adaptable a futuras funcionalidades (cancelaciones, reprogramaciones, reclamos, tripulación, rutas, etc).

---

##  **⚙️Tecnologías**
<p align="center">
<img width="700" height="392" alt="swagger-banner" src="https://github.com/user-attachments/assets/d5c01a26-f6a5-4eac-aed6-c105a86f09d7" />
<img width="402" height="125" alt="mariadb" src="https://github.com/user-attachments/assets/829e631e-7ecf-407a-a166-9eed689f64db" />
<img width="300" height="215" alt="spring-boot" src="https://github.com/user-attachments/assets/2561a950-d6c0-4c5b-8f86-b1de2bfb838c" />

</p>

<h1>BASE DE DATOS</h1>
Para mantener todo conectado, utilizando Xampp y levantando el servicio de PHPMyAdmin y usando Maria db
se debe crear la base de datos con el siguiente query:

<h1>CREATE DATABASE nimbus_air</h1>

Esto crea la base de datos y una vez se corre el programa en IntelliJ IDEA crea las tablas para
futuros INSERTS de las páginas web.
