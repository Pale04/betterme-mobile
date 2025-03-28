# Estándar de codificación para Kotlin
Se usará la [Guía de estilo de Kotlin](https://developer.android.com/kotlin/style-guide).
Se especifican a continuación puntos a sobreescribir o que faltan en el estandar anteriormente mencionado.

## Reglas de nombrado
- Evite el uso de abreviaturas o acrónimos en los nombres, a excepción de las abreviaturas ampliamente conocidas y aceptadas.
- Prefiere la claridad a la brevedad.
- Evite usar nombres de una sola letra, excepto para contadores de bucle simples.

### Prefijos y sufijos
- 

## Comentarios

- Escribe los comentarios una linea arriba de la línea o bloque de codigo del que se habla, con el mismo nivel de indentación.
- Procura utilizarlos solamente cuando sea necesario, como dar explicaciones sobre la manera de codificación que se usó o con finalidades de documentación.
- **Marcas de codigo pendiente:** Utiliza comentarios "*TODO*" para indicar el trabajo de codificación pendiente o con fines de marcar el último lugar donde pausaste tu codificación. Realiza una revisión a tu código antes de subir tu trabajo al repositorio remoto, para evitar que tenga estos comentarios.

***Ejemplo:***
```Kotlin
fun magicSum(a: Int, b: Int) : Int {
    val magicNumber;
    // TODO: generate a random number and assign it to x
    return a + b + x
}
```

## Manejo de excepciones
- Utiliza excepciones específicas cuando manejes excepciones en bloques _try-catch_, no utilices _Exception_ por ningún motivo. 
- Nombra la excepción atrapada por _catch_ como "error".

Ejemplo:

```kotlin
// Wrong
fun SayHello() {
    try {
        // ...
    } catch (e: Exception) {
        //...
    }
}

// OK
fun SayHello2() {
    try {
        // ...
    } catch (error: FileNotFounException) {
        //...
    }
}
```

## Bitácora
La bitácora (logger) deberá registrar, además de errores, todo evento significativo para el sistema. 

- **Librería:**

### Niveles de registro

A continuación se describe cada nivel para que identifiques el que mejor se adapte al evento que deseas registrar en la bitácora:

- **FATAL**: errores que afectan al sistema completamente. Generalmente conexiones fallidas a la base de datos o servidor, y problemas con los archivos manejados en el sistema.
- **WARNING**: situaciones anormales que pueden indicar futuros problemas. Por ejemplo, información que no es actualizada debido a un error de conexión en la base de datos, o correos no enviados.
- **INFO**: eventos significativos en la ejecución del sistema. Pueden ir desde registrar el inicio de sesión de un usuario, intentos fallidos de inicio de sesión, canales de comunicación cerrados inesperadamente, entre otros.