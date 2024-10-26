import os

# Directorios de imágenes y transcripciones
images_dir = '.'  # Cambia esto a tu directorio de imágenes
transcriptions_dir = '../transcripcion'  # Cambia esto a tu directorio de transcripciones
output_file = 'train_list.txt'

# Abrir el archivo de salida
with open(output_file, 'w') as f:
    # Recorrer los archivos de transcripción
    for filename in os.listdir(transcriptions_dir):
        if filename.endswith('.txt'):
            # Obtener el nombre base sin la extensión
            base_name = os.path.splitext(filename)[0]
            # Formar el nombre del archivo de imagen correspondiente
            image_file = os.path.join(images_dir, f'{base_name}.png')  # Cambia la extensión según sea necesario
            transcription_file = os.path.join(transcriptions_dir, filename)
            
            # Leer el texto de la transcripción
            with open(transcription_file, 'r', encoding='utf-8') as tf:
                ground_truth = tf.read().strip()  # Leer el texto y eliminar espacios en blanco
            
            # Escribir en train_list.txt
            f.write(f'{image_file} "{ground_truth}"\n')

print(f'Archivo {output_file} creado con éxito.')

