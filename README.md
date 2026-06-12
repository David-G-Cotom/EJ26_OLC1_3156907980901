# GoLite IDE 🟦

Intérprete del lenguaje **GoLite** — un subconjunto simplificado de Go — desarrollado como proyecto del curso **Laboratorio de Organización de Lenguajes y Compiladores 1**.

Incluye un IDE de escritorio construido con Java Swing que permite escribir, ejecutar y depurar programas `.glt` directamente desde la interfaz.

---

## Características — Fase 1

- Análisis léxico con **JFlex 1.9.1**
- Análisis sintáctico con **CUP 11b** y generación de AST
- Intérprete de árbol (tree-walker) con tabla de símbolos y scopes anidados
- Tipos soportados: `int`, `float64`, `string`, `bool`, `rune`
- Estructuras de control: `if / else if / else`, `for` clásico y con condición, `break`, `continue`
- Funciones embebidas: `fmt.Println`, `strconv.Atoi`, `strconv.ParseFloat`, `reflect.TypeOf`
- Reporte de tokens y reporte de errores (léxicos, sintácticos y semánticos)
- IDE con múltiples pestañas, números de línea, consola de salida y soporte para archivos `.glt`

---

## Requisitos

| Herramienta | Versión mínima | Descarga |
|---|---|---|
| Java JDK | 21 o superior | https://www.oracle.com/java/technologies/downloads/ |
| Apache NetBeans | 21 o superior | https://netbeans.apache.org/front/main/download/archive/ |
| Maven | Incluido en NetBeans | — |
| JFlex | 1.9.1 | https://jflex.de |
| CUP | 11b | https://www.cs.princeton.edu/~appel/modern/java/CUP/ |

> **Nota:** JFlex y CUP se configuran como dependencias Maven dentro del proyecto. No es necesario instalarlos manualmente.

---

## Instalación y ejecución

### 1. Clonar el repositorio

```bash
git clone https://github.com/David-G-Cotom/EJ26_OLC1_3156907980901.git
cd EJ26_OLC1_3156907980901
```

### 2. Abrir el proyecto en NetBeans

1. Abre **Apache NetBeans**
2. Ve a **File → Open Project**
3. Navega hasta la carpeta `EJ26_OLC1_3156907980901` y selecciónala
4. NetBeans detectará automáticamente el proyecto Maven

### 3. Generar el Lexer y el Parser

Antes de compilar por primera vez es necesario generar las clases Java desde los archivos `.flex` y `.cup`:

1. En el panel de proyectos, haz clic derecho sobre el proyecto
2. Selecciona **Build** (o presiona `F11`)
3. Maven ejecutará automáticamente los plugins de JFlex y CUP y generará:
   - `Lexer.java` desde `lexer.flex`
   - `parser.java` y `sym.java` desde `parser.cup`

### 4. Ejecutar la aplicación

1. Haz clic derecho sobre el proyecto → **Run** (o presiona `F6`)
2. Se abrirá el IDE GoLite
3. Escribe o abre un archivo `.glt` y presiona **▶ Ejecutar** (o `F5`)

---

## Estructura del proyecto

```
src/main/
├── cup/
    └── parser.cup                            → Especificacion sintactica (CUP)
├── java/com/mycompany/proyecto_compi1_vj26/            
    ├── ast/                                  → Nodos del Arbol de Sintaxis Abstracta (AST)
    ├── exceptions/                           → Excepciones de control
    ├── frontend/                             → Interfaz grafica (Swing)
    ├── models/                               → Clases de Reporte + Enums para Datos Constantes
    ├── symbols/                              → Tabla de Simbolos (Symbol, SymbolTable)
    ├── visitor/                              → Patron Visitor + Interprete tree-walker
    └── Proyecto_compi_vj.java                → Archivo Main del proyecto
└── jflex/
    └── lexer.flex                            → Especificación léxica (JFlex)
```

---

## Uso rápido

Crea un archivo `.glt` con el siguiente contenido y ejecútalo en el IDE:

```go
func main(){
    var nombre string = "GoLite"
    var version int = 1

    fmt.Println("Bienvenido a", nombre)

    for i := 1; i <= 3; i++ {
        if i % 2 == 0 {
            fmt.Println("Par:", i)
        } else {
            fmt.Println("Impar:", i)
        }
    }
}
```

Salida esperada:
```
Bienvenido a GoLite
Impar: 1
Par: 2
Impar: 3
```

---

## Reportes disponibles

Después de ejecutar un programa, accede desde el menú **Reportes**:

- **Tabla de Tokens** — lista todos los tokens reconocidos con lexema, tipo, línea y columna
- **Tabla de Errores** — lista errores léxicos, sintácticos y semánticos con su ubicación exacta

---

## Roadmap

- [x] Fase 1 — Intérprete base, tipos primitivos, control de flujo, funciones embebidas, IDE
- [ ] Fase 2 — `switch`, `return`, funciones declaradas por usuario, slices, structs, reporte AST

---

## Licencia

Proyecto académico — Laboratorio de Organización de Lenguajes y Compiladores 1.
Universidad de San Carlos de Guatemala.
