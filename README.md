# Portfolio JAZC

App Android en Kotlin y Jetpack Compose. La primera sección funcional es el catálogo del Design System.

## Estructura

- `:app`: navegación adaptativa, inicio y catálogo interactivo.
- `:core:designsystem`: biblioteca reutilizable con tema, tipografía, Button, RadioButton, RadioGroup y adaptador UiText.

Abre el proyecto en Android Studio, sincroniza Gradle y ejecuta `app`. Entra a **Design System** desde el inicio o la navegación. El catálogo funciona en inglés y español y permite comparar temas claro/oscuro y personalizar componentes.

La documentación de APIs, tamaños exactos, ejemplos y decisiones está en [core/designsystem/README.md](core/designsystem/README.md). Los tamaños también aparecen en KDoc y en nombres como `Xs12`, `Md16` y `Md56`.

## Toolchain

Android SDK 36 (minSdk 26), AGP 8.13.2, Gradle 8.13, Kotlin/Compose Compiler 2.2.10 y Compose BOM 2025.08.01. Core 1.17.0, Activity 1.11.0 y Lifecycle 2.9.3 se fijaron a una combinación compatible con SDK 36: las versiones Core 1.19.0 y Lifecycle 2.11.0 de la plantilla exigían SDK 37 / AGP 9.1.

Usa el JDK incluido con Android Studio como Gradle JDK. En PowerShell:

```powershell
$env:JAVA_HOME = 'C:\Program Files\Android\Android Studio\jbr'
.\gradlew.bat :app:assembleDebug :app:assembleRelease
.\gradlew.bat :core:designsystem:lintDebug :app:lintDebug
.\gradlew.bat :core:designsystem:connectedDebugAndroidTest :app:connectedDebugAndroidTest
```

Las pruebas de UI requieren un emulador o dispositivo. `CatalogTest` genera capturas en el almacenamiento externo privado de la app (`files/catalog-review`). Gradle puede desinstalar los APK al terminar y eliminar ese directorio; para conservarlo, instala los APK de debug y androidTest y ejecuta `CatalogTest` mediante `adb shell am instrument` directamente.

El APK release aún no tiene firma de publicación; no se añadieron secretos, permisos ni configuración de Play Store. Por ahora el catálogo forma parte de ambas variantes. La configuración visual del playground es de demostración y usa rememberSaveable; no se persiste como ajuste permanente.
