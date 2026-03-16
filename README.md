# 📱 ApKuba SDK

[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://developer.android.com/about/versions/lollipop?hl=es-419)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)

> Una aplicación de Android simple para probar la libreria ApKubaSDK usando Kotlin.

## 🤔 ¿Qué es ApKubaSDK?

**ApKuba SDK te permite verificar si un usuario ha comprado tu aplicación o licencia en las tiendas cubanas Apklis y ApKuba. Es como un "detector de compras" que te ayuda a proteger tu app de la piratería.**

### 🏪 ¿Qué son Apklis y ApKuba?

**Apklis**: La tienda oficial de aplicaciones de Cuba

**ApKuba**: Una versión mejorada que incluye sistema de licencias

### ✨ Características

✅ Fácil de usar: Solo unas pocas líneas de código

🔒 Verificación de compras: Comprueba si el usuario pagó por tu app

📄 Sistema de licencias: Maneja licencias temporales y permanentes

🚀 Kotlin nativo: Diseñado específicamente para Android moderno

🛡️ Seguro: Validación robusta de datos

📱 Compatible: Funciona desde Android API 21+

## 📦 Instalación

### Paso 1: Configurar el repositorio

**Agrega JitPack a tu archivo settings.gradle.kts (o settings.gradle):**

**Kotlin DSL:**

```kotlin
dependencyResolutionManagement {
    repositories {
        maven { url = uri("https://repo.megashopcuba.com/releases") }
    }
}
```

**Groovy:**

```kotlin
dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://repo.megashopcuba.com/releases' }
		}
	}
```

### Paso 2: Agregar la dependencia

**En tu archivo build.gradle.kts (módulo app):**

**Kotlin DSL:**

```kotlin
dependencies {
    implementation("cu.apkuba.sdk:apkuba-sdk:LATEST_VERSION")
}
```

**Groovy:**

```kotlin
dependencies {
    implementation 'cu.apkuba.sdk:apkuba-sdk:LATEST_VERSION'
}
```

> 💡 Tip: Reemplaza LATEST_VERSION con la versión mostrada en el badge verde de abajo.

### Útima versión

[![](https://repo.megashopcuba.com/releases/v/megashopcuba/apkuba-sdk.svg)](https://repo.megashopcuba.com/releases/#megashopcuba/apkuba-sdk)

## 🚀 Uso Básico

### 🛒 Verificar si una app fue comprada

#### Con ApKuba (Recomendado)

```kotlin
// Verificar compra de forma síncrona
val result = ApKuba.isPurchased(context, "com.miapp.ejemplo")
if (result.isValid()) {
    // ¡El usuario pagó por la app! 🎉
    println("Usuario: ${result.userName}")
} else {
    // El usuario no ha pagado 😔
    println("Estado: ${result.status}")
}
```

#### Con Apklis tradicional

```kotlin
// Verificar compra de forma asíncrona
lifecycleScope.launch {
    val result = Apklis.isPurchased(context, "com.miapp.ejemplo")
    if (result.isValid()) {
        // ¡Compra verificada! ✅
        showPremiumFeatures()
    } else {
        // Mostrar pantalla de compra 💳
        showPurchaseScreen()
    }
}
```

### 📄 Verificar licencias (Solo ApKuba)

```kotlin
val licenseResult = ApKuba.isLicensePurchased(context, "com.miapp.ejemplo")
if (licenseResult.isValid()) {
    println("Licencia válida hasta: ${licenseResult.expiredIn}")
    // Activar funciones premium
} else {
    println("Estado de licencia: ${licenseResult.status}")
    // Mostrar opciones de compra
}
```

### 🛍️ Dirigir al usuario a comprar

#### Comprar aplicación

```kotlin

// En ApKuba
Utils.openApKubaLink(context, "com.miapp.ejemplo")

// En Apklis tradicional
Utils.openApklisLink(context, "com.miapp.ejemplo")
```

#### Comprar licencia

```kotlin
Utils.openApKubaLicenseLink(
    context = context,
    applicationId = "com.miapp.ejemplo",
    licenseUuid = "tu-uuid-de-licencia",
    pemKey = "tu-clave-publica-pem"
)
```

## 📋 Ejemplos Completos

### Ejemplo 1: App con compra única

```kotlin
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkPurchaseStatus()
    }

    private fun checkPurchaseStatus() {
        val result = ApKuba.isPurchased(this, packageName)

        when {
            result.isValid() -> {
                // Usuario pagó - mostrar app completa
                showFullApp()
                Toast.makeText(this, "¡Bienvenido ${result.userName}!", Toast.LENGTH_SHORT).show()
            }
            result.status == PaymentStatus.ERROR -> {
                // Error de conexión
                showErrorDialog("Error al verificar compra: ${result.message?.message}")
            }
            else -> {
                // Usuario no ha pagado - mostrar versión limitada
                showTrialVersion()
            }
        }
    }

    private fun showPurchaseDialog() {
        AlertDialog.Builder(this)
            .setTitle("Comprar App")
            .setMessage("¿Deseas comprar la versión completa?")
            .setPositiveButton("Comprar") { _, _ ->
                Utils.openApKubaLink(this, packageName)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
```

### Ejemplo 2: App con sistema de licencias

```kotlin
class LicenseManager(private val context: Context) {

    fun checkLicense(): LicenseResult {
        val response = ApKuba.isLicensePurchased(context, context.packageName)

        return when (response.status) {
            LicenseStatus.PAYED -> LicenseResult.Valid(response.expiredIn)
            LicenseStatus.EXPIRED -> LicenseResult.Expired
            LicenseStatus.PENDING -> LicenseResult.Pending
            else -> LicenseResult.Invalid(response.message?.message)
        }
    }

    sealed class LicenseResult {
        data class Valid(val expiresIn: String?) : LicenseResult()
        object Expired : LicenseResult()
        object Pending : LicenseResult()
        data class Invalid(val error: String?) : LicenseResult()
    }
}
```

## 📊 Estados de Respuesta

### Estados de Compra (PaymentStatus)

- PAYED ✅ - El usuario pagó por la aplicación

- NOT_PAYED ❌ - El usuario no ha pagado

- ERROR ⚠️ - Error al consultar (app no encontrada)

- EXCEPTION 💥 - Error técnico (sin conexión, etc.)

### Estados de Licencia (LicenseStatus)

- PAYED ✅ - Licencia activa y válida

- PENDING ⏳ - Pago pendiente de procesamiento

- EXPIRED ⏰ - Licencia expirada

- CANCELLED ❌ - Licencia cancelada

- INVALID ⚠️ - Licencia inválida

- ERROR 💥 - Error al consultar

## 🔧 Utilidades Incluidas

### Validación de datos

```kotlin
// Validar nombre de paquete
val isValidPackage = Utils.validatePackageName("com.ejemplo.miapp") // true

// Validar UUID
val isValidUUID = Utils.validateUUID("123e4567-e89b-12d3-a456-426614174000") // true

// Validar clave PEM
val isValidPEM = Utils.validatePublicKeyPEM(pemKey) // true/false
```

## 🛠️ Requisitos

- Android API 21+ (Android 5.0 Lollipop)

- Kotlin (la librería está escrita en Kotlin)

- Apklis o ApKuba instalado en el dispositivo

## 🤝 Contribuir

¡Las contribuciones son bienvenidas! Si encuentras un bug o tienes una idea:

1. 🍴 Haz fork del proyecto

2. 🌿 Crea una rama para tu feature (git checkout -b feature/AmazingFeature)

3. 💾 Commit tus cambios (git commit -m 'Add some AmazingFeature')

4. 📤 Push a la rama (git push origin feature/AmazingFeature)

5. 🔄 Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo [LICENSE](https://opensource.org/licenses/MIT) para más detalles.

## 🆘 Soporte

¿Tienes problemas o preguntas?

💬 Telegram: [@vs_helper](https://t.me/vs_helper)

🐛 Issues: [GitHub Issues](https://github.com/megashopcuba/testApkubaSDK/issues)

📱 Aplicación de prueba: [Descargar](https://github.com/megashopcuba/testApkubaSDK/blob/main/app/release/app-release.apk)

Para los desarrolladores interesados en contribuir que se comuniquen para agregarlos como colaboradores:

📖 Wiki: [Documentación completa](https://deepwiki.com/megashopcuba/apkuba-sdk) 

[![Preguntar DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/megashopcuba/apkuba-sdk)



## 🙏 Agradecimientos

Al equipo de Apklis por crear la plataforma

A la comunidad de desarrolladores cubanos

A todos los contribuidores del proyecto

<div align="center">

¿Te gusta el proyecto? ¡Dale una ⭐ en [GitHub](https://github.com/megashopcuba/testApkubaSDK)!
 

Hecho con ❤️ para la comunidad de desarrolladores cubanos

</div>
