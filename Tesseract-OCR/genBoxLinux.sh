#!/bin/bash

# Directorios
CAPTURAS_DIR="./capturas"
TRANSCRIPCION_DIR="./transcripcion"

# Verifica si los directorios existen
if [[ ! -d "$CAPTURAS_DIR" ]]; then
    echo "El directorio de capturas no existe: $CAPTURAS_DIR"
    exit 1
fi

if [[ ! -d "$TRANSCRIPCION_DIR" ]]; then
    echo "El directorio de transcripciones no existe: $TRANSCRIPCION_DIR"
    exit 1
fi

# Recorrer todas las imágenes en el directorio de capturas
for imagen in "$CAPTURAS_DIR"/*.png; do
    # Obtener el nombre base del archivo (sin extensión)
    base_name=$(basename "$imagen" .png)
    transcripcion="$TRANSCRIPCION_DIR/$base_name.txt"
    box_file="$CAPTURAS_DIR/$base_name.box"

    # Verifica si el archivo de transcripción existe
    if [[ ! -f "$transcripcion" ]]; then
        echo "No se encontró la transcripción para: $base_name"
        continue
    fi

    # Generar el archivo .box sin el parámetro batch.no
    echo "Generando $box_file..."
    tesseract "$imagen" "$base_name" --psm 6 makebox

    # Mueve el archivo .box generado al directorio de capturas
    if [[ -f "$base_name.box" ]]; then
        mv "$base_name.box" "$box_file"
    fi
done

echo "Proceso completado."

