package org.utl.dsm.MagnoEvento.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;
import org.utl.dsm.MagnoEvento.entity.Resources;
import org.utl.dsm.MagnoEvento.repository.ImageRepository;
import org.utl.dsm.MagnoEvento.utils.FileCompressionUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/img")
@Tag(name = "Image", description = "Endpoints para la gestión de imágenes")
@Slf4j
public class ImageController {

    @Autowired
    private ImageRepository imageRepository;

    FileCompressionUtil fileCompressionUtil = new FileCompressionUtil(); // Inyecta FileCompressionUtil

    @PostMapping("/upload")
    @Operation(summary = "Subir una imagen", description = "Sube un archivo de imagen y guarda su información en la base de datos")
    public String uploadImage(@Parameter(description = "Archivo de imagen para subir", required = true) @RequestParam("file") MultipartFile file) {
        try {
            // Guardar el archivo en la base de datos
            Resources image = new Resources();
            image.setFileName(file.getOriginalFilename());
            image.setType(file.getContentType());

            // Comprimir el archivo antes de guardar los bytes en la base de datos
            byte[] compressedFileBytes = fileCompressionUtil.compressFile(file.getBytes());
            image.setFileContent(compressedFileBytes); // Guardar los bytes del archivo comprimido

            imageRepository.save(image);

            return "¡Archivo subido exitosamente!";
        } catch (Exception e) {
            log.error("Error al subir el archivo", e);
            return "Error al subir el archivo: " + e.getMessage();
        }
    }

    @GetMapping("/download/{id}")
    @Operation(summary = "Descargar una imagen", description = "Descarga un archivo de imagen desde el servidor")
    @ApiResponse(responseCode = "200", description = "Contenido del archivo", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE))
    public ResponseEntity<Resource> downloadFile(@Parameter(description = "ID de la imagen a descargar", required = true) @PathVariable Long id) {
        try {
            Resources file = imageRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Archivo no encontrado con ID: " + id));

            ByteArrayResource resource = new ByteArrayResource(fileCompressionUtil.decompressFile(file.getFileContent())); // Obtener los bytes del archivo desde la base de datos

            // Determine el tipo de contenido apropiado en función de la extensión del archivo
            MediaType contentType = getContentTypeFromFileName(file.getFileName());

            return ResponseEntity.ok()
                    .contentType(contentType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                    .body(resource);
        } catch (Exception e) {
            log.error("Error al descargar el archivo", e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/preview/{id}")
    @Operation(summary = "Vista previa de una imagen", description = "Muestra una vista previa de la imagen en el navegador")
    @ApiResponse(responseCode = "200", description = "Contenido del archivo", content = @Content(mediaType = MediaType.IMAGE_JPEG_VALUE))
    public ResponseEntity<Resource> previewFile(@Parameter(description = "ID de la imagen a previsualizar", required = true) @PathVariable Long id) {
        try {
            Resources file = imageRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Archivo no encontrado con ID: " + id));

            ByteArrayResource resource = new ByteArrayResource(file.getFileContent()); // Obtener los bytes del archivo desde la base de datos

            // Determine el tipo de contenido apropiado en función de la extensión del archivo
            MediaType contentType = getContentTypeFromFileName(file.getFileName());

            return ResponseEntity.ok()
                    .contentType(contentType)
                    .body(resource);
        } catch (Exception e) {
            log.error("Error al previsualizar el archivo", e);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Actualizar una imagen", description = "Actualiza la información de una imagen existente")
    public String updateImage(@Parameter(description = "ID de la imagen a actualizar", required = true) @PathVariable Long id,
                              @Parameter(description = "Archivo de imagen actualizado", required = true) @RequestParam("file") MultipartFile file) {
        try {
            // Recuperar la imagen de la base de datos
            Resources image = imageRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Imagen no encontrada con ID: " + id));

            // Actualizar la información de la imagen
            image.setFileName(file.getOriginalFilename());
            image.setType(file.getContentType());
            image.setFileContent(file.getBytes()); // Guardar los bytes del archivo
            imageRepository.save(image);

            return "¡Archivo actualizado exitosamente!";
        } catch (Exception e) {
            log.error("Error al actualizar el archivo", e);
            return "Error al actualizar el archivo: " + e.getMessage();
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Eliminar una imagen", description = "Elimina una imagen de la base de datos")
    public String deleteImage(@Parameter(description = "ID de la imagen a eliminar", required = true) @PathVariable Long id) {
        try {
            // Recuperar la imagen de la base de datos
            Resources image = imageRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Imagen no encontrada con ID: " + id));

            // Eliminar la imagen de la base de datos
            imageRepository.delete(image);

            return "¡Archivo eliminado exitosamente!";
        } catch (Exception e) {
            log.error("Error al eliminar el archivo", e);
            return "Error al eliminar el archivo: " + e.getMessage();
        }
    }

    private MediaType getContentTypeFromFileName(String fileName) {
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        switch (fileExtension) {
            case "jpg":
            case "jpeg":
            case "png":
                return MediaType.IMAGE_JPEG;
            case "pdf":
                return MediaType.APPLICATION_PDF;
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}