Este es un proyecto de spring boot para la gsetion de acceso de una conferencia.
Para poder usarlo es necesario:
1- Crear una base de datos llamada magno_evento en MySQL con
CREATE DATABASE magno_evento;

2.- Ejectar el proyecto para crear las tablas.
3.- Entrar a http://localhost:8080/docs y veran una interfas como
![image](https://github.com/JonathanMM03/MagnoEvento/assets/143897103/8fb82066-ceee-4f73-a1b1-9b4ca77af505)

4.- Creamos un Conferenciasta y Asistentes
![image](https://github.com/JonathanMM03/MagnoEvento/assets/143897103/2473e6a4-95b5-42e6-93b0-0edbaf995789)
![image](https://github.com/JonathanMM03/MagnoEvento/assets/143897103/eca1ce48-85f8-40ff-a4e1-88d8edee5c4b)

![image](https://github.com/JonathanMM03/MagnoEvento/assets/143897103/11a6e95c-4d88-4fd0-a026-0f26710865ec)
El email se usa para enviar correos al asistir a una conferencia
![image](https://github.com/JonathanMM03/MagnoEvento/assets/143897103/a57b49db-3412-4698-b212-b069b64d804f)

5.- Creamos una conferencia con el nombre estableciendo la fecha de inicio y fin.
![image](https://github.com/JonathanMM03/MagnoEvento/assets/143897103/19e15bb8-a70f-44aa-91a5-8e9ad451a6f5)
![image](https://github.com/JonathanMM03/MagnoEvento/assets/143897103/869003bd-d374-4982-aef5-1949b581ca6a)

Para comprobar si se agrego correctamente con /conferencia/all.
![image](https://github.com/JonathanMM03/MagnoEvento/assets/143897103/7a5311c6-3663-4fe5-9d1e-8517aa0989c6)

Con /asistencia/available que mostrará las conferencias disponibles.
![image](https://github.com/JonathanMM03/MagnoEvento/assets/143897103/b73366f7-cddf-4628-8d73-0de808e6a32f)

6.- Para confirmar la asistencia usaremos /asistencia/confirmar-asistencia/{matricula}
Si hay conferencias disponibles se confirmará la asistencia de la misma y enviará un correo
![image](https://github.com/JonathanMM03/MagnoEvento/assets/143897103/6c3db2c1-5009-400c-8814-3735df4fe7c8)

7.- Al entrar a /asistencia/generar/24000001
![image](https://github.com/JonathanMM03/MagnoEvento/assets/143897103/34b7f2c7-d42b-4b46-bf77-463685c361de)
Se mostrará un html con una interfaz basica que usa Theamleaf y Model para agrega valores a los campos

