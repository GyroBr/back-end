package com.Gyro.back_end_gyro.controller;

import com.Gyro.back_end_gyro.domain.image.dto.ImageResponse;
import com.Gyro.back_end_gyro.domain.image.service.ImageService;
import com.Gyro.back_end_gyro.domain.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
@Tag(name = "Images", description = "Endpoints para gerenciamento de imagens")
public class ImageController {

    private final ImageService imageService;
    private final ProductService productService;

//    @GetMapping("/render/{productId}")
//    @Operation(
//            summary = "Renderizar imagem de um produto",
//            description = "Retorna a imagem associada a um produto específico, identificado pelo ID do produto."
//    )
//    @ApiResponses({
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Imagem renderizada com sucesso",
//                    content = @Content(
//                            mediaType = "image/*",
//                            schema = @Schema(type = "string", format = "binary")
//                    )
//            ),
//            @ApiResponse(
//                    responseCode = "400",
//                    description = "ID do produto inválido",
//                    content = @Content(
//                            mediaType = "application/json",
//                            examples = @ExampleObject(
//                                    value = "{\"message\": \"ID do produto inválido\"}"
//                            )
//                    )
//            ),
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "Produto ou imagem não encontrada",
//                    content = @Content(
//                            mediaType = "application/json",
//                            examples = @ExampleObject(
//                                    value = "{\"message\": \"Produto ou imagem não encontrada\"}"
//                            )
//                    )
//            ),
//            @ApiResponse(
//                    responseCode = "500",
//                    description = "Erro ao carregar a imagem",
//                    content = @Content(
//                            mediaType = "application/json",
//                            examples = @ExampleObject(
//                                    value = "{\"message\": \"Erro ao carregar a imagem\"}"
//                            )
//                    )
//            )
//    })
//    public ResponseEntity<Resource> renderImageFromProduct(@PathVariable Long productId) {
//        String imageFileName = productService.existsProductById(productId).getImage();
//        ImageResponse imageResponse = imageService.loadImage(imageFileName);
//
//        return ResponseEntity.ok()
//                .contentType(imageResponse.contentType())
//                .body(imageResponse.resource());
//    }

    @GetMapping("/render/{productId}")
    @Operation(
            summary = "Renderizar imagem de um produto",
            description = "Retorna a imagem associada a um produto específico, identificado pelo ID do produto."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Imagem renderizada com sucesso",
                    content = @Content(
                            mediaType = "image/*",
                            schema = @Schema(type = "string", format = "binary")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "ID do produto inválido",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\": \"ID do produto inválido\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Produto ou imagem não encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\": \"Produto ou imagem não encontrada\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro ao carregar a imagem",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\": \"Erro ao carregar a imagem\"}"
                            )
                    )
            )
    })
    public ResponseEntity<String> renderImageFromProduct(@PathVariable Long productId) {
        String imageFileName = productService.existsProductById(productId).getImage();

        return ResponseEntity.ok(imageFileName);
    }


}