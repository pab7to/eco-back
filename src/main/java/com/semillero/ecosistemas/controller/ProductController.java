package com.semillero.ecosistemas.controller;

import com.semillero.ecosistemas.dto.ProductDTO;
import com.semillero.ecosistemas.model.Product;
import com.semillero.ecosistemas.service.IProductService;
import com.semillero.ecosistemas.service.ICategoryService;
import com.semillero.ecosistemas.service.ICountryService;
import com.semillero.ecosistemas.service.IProvinceService;
import com.semillero.ecosistemas.service.ISupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    //Dependency Injection
    @Autowired
    private IProductService productService;
    @Autowired
    private ISupplierService supplierService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private ICountryService countryService;
    @Autowired
    private IProvinceService provinceService;

    //Create Endpoint (Just for Suppliers)
    @PreAuthorize("hasRole('SUPPLIER')")
    @PostMapping
    public ResponseEntity<Product> createProduct(
            @RequestParam("name") String name,
            @RequestParam("shortDescription") String shortDescription,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("email") String email,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("instagram") String instagram,
            @RequestParam("facebook") String facebook,
            @RequestParam("countryId") Long countryId,
            @RequestParam("provinceId") Long provinceId,
            @RequestParam("city") String city,
            @RequestParam("longDescription") String longDescription,
            @RequestParam("files") List<MultipartFile> files,
            @RequestHeader("Authorization") String authorizationHeader) {

        try {
            // Extraer el token del header
            String token = authorizationHeader.replace("Bearer ", "");

            // Construir el ProductDTO
            ProductDTO productDTO = productService.buildProductDTO(
                    name,
                    shortDescription,
                    categoryId,
                    email,
                    phoneNumber,
                    instagram,
                    facebook,
                    countryId,
                    provinceId,
                    city,
                    longDescription
            );

            // Crear el producto usando el ProductDTO, archivos y token
            Product product = productService.createProduct(productDTO, files, token);

            return ResponseEntity.ok(product);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    //Update Endpoint (Just for Suppliers)
    @PreAuthorize("hasRole('SUPPLIER')")
    @PutMapping("/update/{productID}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long productID,
            @RequestParam("name") String name,
            @RequestParam("shortDescription") String shortDescription,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("email") String email,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("instagram") String instagram,
            @RequestParam("facebook") String facebook,
            @RequestParam("countryId") Long countryId,
            @RequestParam("provinceId") Long provinceId,
            @RequestParam("city") String city,
            @RequestParam("longDescription") String longDescription,
            @RequestParam("files") List<MultipartFile> files) {

        try {
            // Construir el ProductDTO
            ProductDTO productDTO = productService.buildProductDTO(
                    name,
                    shortDescription,
                    categoryId,
                    email,
                    phoneNumber,
                    instagram,
                    facebook,
                    countryId,
                    provinceId,
                    city,
                    longDescription
            );

            return ResponseEntity.ok(productService.updateProduct(productID, productDTO, files));

        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(null);
        }
    }


    //Find one Product by ID Endpoint (Just for Admins)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/find/{id}")
    public ResponseEntity<Product> findProductById(@PathVariable Long id){
        Optional<Product> optionalProduct = productService.findProductById(id);
        return optionalProduct.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //Searchbar Endpoint
    @GetMapping("/search/{name}")
    public ResponseEntity<List<Product>> findProductByName(@PathVariable String name){
        List<Product> products = productService.findProductByName(name);
        return ResponseEntity.ok(products);
    }

    //Get All Products Endpoint
    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts(){
        List<Product>allProducts = productService.getAllProducts();
        return ResponseEntity.ok(allProducts);
    }

    //Get Products grouped by Supplier (Suppliers and Admins)
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPLIER')")
    @GetMapping("/{supplierID}")
    public ResponseEntity<List<Product>> getProductsBySupplier(@PathVariable Long supplierID){
        List<Product>productsBySupplier = productService.getProductsBySupplier(supplierID);
        return ResponseEntity.ok(productsBySupplier);
    }

    //Get Products grouped by Category (All Users)
    @GetMapping("/category/{categoryID}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Long categoryID){
        List<Product>productsByCategory = productService.getProductsByCategory(categoryID);
        return ResponseEntity.ok(productsByCategory);
    }

    //Send Feedback and Set Status (Just For Admins)
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/feedback/{productID}")
    public ResponseEntity<String> sendFeedbackStatus(@PathVariable Long productID,
                                                     @RequestParam String status,
                                                     @RequestParam String feedback){
        productService.setFeedStatus(productID, status, feedback);
        return ResponseEntity.ok("Se ha actualizado el STATUS del producto y se ha enviado el feedback al Proveedor.");
    }

    //Delete product Endpoint (Just for Suppliers)
    @PreAuthorize("hasRole('SUPPLIER')")
    @DeleteMapping("/delete/{productID}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productID) throws IOException {
        String response = productService.deleteProduct(productID);
        return ResponseEntity.ok(response);
    }

}