#!/bin/bash

# Directorios
CAPTURAS_DIR="./capturas"
LSTMF_DIR="./lstmf"  # Directorio donde se guardarán los archivos .lstmf

# Verifica si los directorios existen
if [[ ! -d "$CAPTURAS_DIR" ]]; then
    echo "El directorio de capturas no existe: $CAPTURAS_DIR"
    exit 1
fi

# Crea el directorio de salida si no existe
mkdir -p "$LSTMF_DIR"

# Recorrer todas las imágenes y sus archivos .box
for imagen in "$CAPTURAS_DIR"/*.png; do
    # Obtener el nombre base del archivo (sin extensión)
    base_name=$(basename "$imagen" .png)
    box_file="$CAPTURAS_DIR/$base_name.box"

    # Verifica si el archivo de box existe
    if [[ ! -f "$box_file" ]]; then
        echo "No se encontró el archivo .box para: $base_name"
        continue
    fi

    # Generar el archivo .lstmf y guardarlo en el directorio de salida
    echo "Generando $LSTMF_DIR/$base_name.lstmf..."
    
    # Ejecutar Tesseract y capturar la salida y los errores
    tesseract "$imagen" "$LSTMF_DIR/$base_name" --psm 6 lstmbox 2> error.log
    
    # Verificar si se creó el archivo .lstmf
    if [[ -f "$LSTMF_DIR/$base_name.lstmf" ]]; then
        echo "Archivo generado: $LSTMF_DIR/$base_name.lstmf"
    else
        echo "Error al generar $LSTMF_DIR/$base_name.lstmf. Verifique error.log para más detalles."
    fi
done

echo "Proceso de generación de archivos .lstmf completado."

