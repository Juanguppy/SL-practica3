#!/bin/bash

# Directorio donde se encuentran las imágenes y archivos .box
INPUT_DIR="."  # Cambia esto a la ruta correcta
cd "$INPUT_DIR" || exit

# Iterar sobre todos los archivos .png en el directorio
for img_file in *.png; do
    # Obtener el nombre del archivo sin la extensión
    base_name="${img_file%.png}"
    box_file="${base_name}.box"
    
    # Verificar si el archivo .box existe
    if [[ -f "$box_file" ]]; then
        echo "Generando archivo .tr para $img_file"
        tesseract "$img_file" "$base_name" --psm 6 lstm.train
    else
        echo "Advertencia: No se encontró el archivo .box para $img_file"
    fi
done

echo "Generación de archivos .tr completada."

