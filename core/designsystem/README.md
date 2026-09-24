# JAZC Design System

Biblioteca Android independiente de `:app`, basada en Compose y Material 3. Incluye tema claro/oscuro, acentos Menta/Azul, tipografía, botones y selección exclusiva. El catálogo de la app permite inspeccionar y personalizar sus variantes.

## La API: composición, no herencia

Las funciones `@Composable` no heredan los parámetros de otras funciones. Un wrapper que expone absolutamente todo acaba duplicando la API de Material. Esta biblioteca usa cuatro piezas:

- **Slots** para el contenido: un único botón acepta texto, iconos, recursos o contenido propio.
- **Modifier** para layout, foco, semántica y pruebas.
- **Objetos de estilo** para opciones visuales relacionadas, personalizables con `copy()`.
- **Estado y callbacks explícitos** para el comportamiento. El componente no guarda la selección de negocio.

No hay un contenedor universal de parámetros. `onClick` pertenece al botón, `onFocusChanged` es un Modifier, y `KeyboardOptions` / `KeyboardActions` pertenecen a un campo de texto, no a Button ni a Text. Cuando se agreguen inputs, tendrán su propia API.

Si necesitas una opción nativa que el wrapper no expone, puedes usar el componente Material directamente dentro de `JazcTheme`, aprovechando los mismos tokens. Agregar un parámetro al wrapper solo tiene sentido cuando sea útil de forma recurrente. No se promete forwarding automático de futuras APIs de Material.

## Instalación y tema

```kotlin
implementation(project(":core:designsystem"))

JazcTheme(darkTheme = isSystemInDarkTheme(), accent = JazcAccent.Mint) {
    // Pantalla de la app o componentes Material nativos.
}
```

La app depende de la biblioteca; la biblioteca nunca depende de la app. Los valores del tema viven en `theme/`, las APIs visuales en `component/` y el adaptador de texto en `text/`.

## Tamaños exactos: visibles en autocompletado y KDoc

| Token `JazcTextSize` | Tamaño | Interlineado | Peso | Uso sugerido |
| --- | --- | --- | --- | --- |
| `Xs12` | 12 sp | 16 sp | 400 | Metadatos |
| `Sm14` | 14 sp | 20 sp | 400 | Texto secundario |
| `Md16` | 16 sp | 24 sp | 400 | Texto principal |
| `Lg20` | 20 sp | 28 sp | 600 | Sección pequeña |
| `Xl24` | 24 sp | 32 sp | 600 | Sección |
| `Xxl32` | 32 sp | 40 sp | 600 | Título de pantalla |
| `Display40` | 40 sp | 48 sp | 600 | Encabezado destacado |

Cada entrada tiene KDoc con estos valores. `Xs12` ya muestra el tamaño en el nombre; Quick Documentation en Android Studio muestra el resto sin abrir el archivo. El catálogo lee tamaño, interlineado y peso del propio token, evitando una segunda tabla hardcodeada en la UI.

**Texto en sp; dimensiones en dp.** Los sp respetan la escala de fuente del usuario. Si Figma dice “12”, confirma que se refiere al tamaño tipográfico y usa `Xs12`; no conviertas todos los textos a dp para igualar una captura.

```kotlin
JazcText(
    text = uiText(R.string.description),
    size = JazcTextSize.Xs12, // 12 sp / 16 sp
    style = TextStyle(color = MaterialTheme.colorScheme.onSurfaceVariant),
    layout = JazcTextLayout(maxLines = 2, overflow = TextOverflow.Ellipsis),
)
```

`size = null` hereda `LocalTextStyle`: dentro de un botón conserva su label de 14/20 sp; en la raíz de `JazcTheme` usa bodyLarge de 16/24 sp. Un `size` explícito aplica el token; después `style` sobrescribe solo las propiedades especificadas. Evita tamaños personalizados cuando un token ya representa la intención.

También puedes usar toda la API de `Text`:

```kotlin
ProvideJazcTextStyle(JazcTextSize.Sm14) {
    Text(text = uiText(R.string.description).asAnnotatedString(), onTextLayout = ::inspect)
}
// O directamente: Text(text = "Hello", style = JazcTextSize.Md16.style)
```

## String, recursos y texto enriquecido: un mismo componente

```kotlin
JazcText(uiText("Hello"))
JazcText(uiText(R.string.greeting, userName))
JazcText(uiText(buildAnnotatedString {
    append("Hello ")
    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(userName) }
}))
JazcText(uiText(existingSpannable))
```

Los overloads pequeños están en **la fábrica `uiText`**, no en cada componente. `UiText.Resource` se resuelve durante la composición y responde a cambios de idioma. No guarda Context. Un recurso sin argumentos conserva sus spans básicos; un recurso con formato usa `stringResource` y devuelve texto plano, igual que esa API. Para texto formateado con estilos, construye un `AnnotatedString` a partir de recursos localizados.

`AnnotatedString` conserva sus anotaciones nativas, incluyendo links e inline content. Para renderizar este último, pasa el mapa correspondiente en `JazcTextLayout.inlineContent`.

El adaptador legacy copia un `Spannable` a `SpannedString` para no depender de mutaciones externas. Preserva bold/italic, subrayado/tachado, color de texto/fondo, tamaños relativos/absolutos, familias genéricas y URL. **No convierte ImageSpan, BulletSpan ni spans personalizados**; el texto permanece, pero esos efectos no. Para esos casos usa `AnnotatedString`, inline content o un slot propio. Al modificar un Spannable, crea un nuevo `uiText`; mutarlo no es estado observable por Compose.

## Botones

```kotlin
JazcButton(
    onClick = ::save,
    loading = isSaving,
    modifier = Modifier.fillMaxWidth().onFocusChanged { /* foco */ },
    style = JazcButtonDefaults.style(
        variant = JazcButtonVariant.Outlined,
        size = JazcButtonSize.Md56,
    ).copy(shape = RoundedCornerShape(24.dp)),
) {
    Icon(Icons.Default.Check, contentDescription = null)
    JazcText(uiText(R.string.save))
}
```

| Variante | Intención |
| --- | --- |
| `Primary` | Acción principal |
| `Secondary` | Acción secundaria con fondo tonal |
| `Outlined` | Acción alternativa con borde |
| `Ghost` | Acción de menor énfasis, sin fondo |

| Tamaño | Alto mínimo | Padding horizontal | Texto predeterminado |
| --- | --- | --- | --- |
| `Sm48` | 48 dp | 16 dp | 14/20 sp, 600 |
| `Md56` | 56 dp | 24 dp | 14/20 sp, 600 |
| `Lg64` | 64 dp | 32 dp | 14/20 sp, 600 |

Todos usan 12 dp de padding vertical. La altura puede crecer por contenido y escala de fuente. `JazcButtonStyle` permite ajustar colores, forma, borde, elevación, padding, altura mínima y TextStyle. `interactionSource` permite observar pressed/focus/hover.

`loading` desactiva clics, conserva el espacio del contenido y anuncia “Cargando” / “Loading”. `enabled = false` también desactiva la interacción. Evita fijar alturas menores de 48 dp o anidar otro control clicable dentro del slot.

## RadioButton y RadioGroup

```kotlin
var selected by rememberSaveable { mutableStateOf("personal") }
JazcRadioGroup(
    options = listOf("personal", "team", "unavailable"),
    selectedOption = selected,
    onOptionSelected = { selected = it },
    optionEnabled = { it != "unavailable" },
    style = JazcRadioDefaults.style(JazcRadioVariant.Card),
) { option ->
    JazcText(uiText(option))
    JazcText(uiText(R.string.option_description), size = JazcTextSize.Sm14)
}
```

`options` debe contener valores únicos con igualdad estable. El caller conserva el estado; puede ser un ID, enum o modelo estable. La selección puede ser null si aún no existe una elección. `Vertical` muestra filas a todo el ancho; `Flow` distribuye opciones con salto de línea. `Plain` y `Card` comparten la misma semántica.

Cada fila con label es **un único RadioButton accesible**, con área táctil mínima de 48 dp; el indicador interno no crea un segundo onClick. El padre aporta `selectableGroup`. El slot de label puede contener descripción, pero debe evitar otros controles interactivos anidados.

Para un indicador sin texto, usa `JazcRadioButton` sin label y agrega `Modifier.semantics { contentDescription = ... }`. Cuando crees un grupo manualmente, agrega `selectableGroup()` al contenedor y controla la exclusividad de la selección.

## Catálogo y pruebas

Abre la app → **Design System**. Las páginas muestran resumen, tipografía, botones, selección y un playground. Este último permite cambiar tema, acento, variante/tamaño/estado/forma de botón, tamaño/peso/color de texto y estilo/distribución de radios. La configuración sobrevive a recreaciones de Activity; no se guarda como preferencia permanente al cerrar la app. Inglés por defecto; español según el dispositivo.

El catálogo está disponible tanto en debug como en release. No añade permisos ni requiere flavors. Si más adelante debe excluirse de la app pública, se puede extraer a un módulo de showcase separado y conectarlo a variantes específicas.

Pruebas instrumentadas relevantes: clics bloqueados durante carga/deshabilitado, selección exclusiva al tocar labels, opciones deshabilitadas, semántica sin indicadores duplicados, conversión de texto y crecimiento del botón con fuentes grandes. La app prueba navegación y controles del playground, y genera capturas para revisión visual.

```powershell
.\gradlew.bat :app:assembleDebug :app:assembleRelease
.\gradlew.bat :core:designsystem:lintDebug :app:lintDebug
# Requiere emulador o dispositivo conectado:
.\gradlew.bat :core:designsystem:connectedDebugAndroidTest :app:connectedDebugAndroidTest
```

Referencias de implementación: [sistemas de diseño personalizados](https://developer.android.com/develop/ui/compose/designsystems/custom), [RadioButton y accesibilidad](https://developer.android.com/develop/ui/compose/components/radio-button), [texto enriquecido en Compose](https://developer.android.com/develop/ui/compose/text/style-text).
