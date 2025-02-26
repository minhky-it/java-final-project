package com.finalpos.POSsystem.Controller;

import com.finalpos.POSsystem.Config.FirebaseService;
import com.finalpos.POSsystem.Entity.ProductEntity;
import com.finalpos.POSsystem.Exception.FailedException;
import com.finalpos.POSsystem.Exception.ResponseHandler;
import com.finalpos.POSsystem.Repository.ProductRepository;
import com.finalpos.POSsystem.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.finalpos.POSsystem.Entity.Package;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RestController
@ResponseBody
@RequestMapping("/api/products")
public class ProductsController {

    @Autowired
    ProductService service;

    @GetMapping("/")
    public ResponseEntity<?> index(@RequestParam Optional<String> page) {
        try {
            int pageNumber = Integer.parseInt(page.orElse("1"));
            return ResponseHandler.builder("OK", service.products(pageNumber, 10));
        }
        catch (Exception e) {
            throw new FailedException("Failed at product controller: " + e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestParam("barcode") String barcode,
                       @RequestParam("name") String name,
                       @RequestParam("description") String description,
                       @RequestParam("import_price") String import_price,
                       @RequestParam("retail_price") String retail_price,
                       @RequestParam("quantity") String quantity,
                       @RequestParam("category") String category,
                       @RequestParam("image") Optional<MultipartFile> image
                       ){
        try {
            if(barcode.isEmpty() || name.isEmpty() || description.isEmpty() || import_price.isEmpty() ||
                    retail_price.isEmpty() || quantity.isEmpty() || category.isEmpty() || image.isEmpty())
                return ResponseHandler.failed("Missing fields", HttpStatus.BAD_REQUEST);
            return ResponseHandler.builder("OK", service.add(
                    barcode, name, description, import_price, retail_price, quantity, category, image));
        }
        catch (Exception e){
            throw new FailedException("Failed at product controller: " + e.getMessage());
        }
    }

    @GetMapping("/{barcode}")
    public ResponseEntity<?> get(@PathVariable("barcode") String barcode){
        try {
           return ResponseHandler.builder("OK", service.getWBarcode(barcode));
        } catch (Exception e) {
            throw new FailedException("Failed at product controller: " + e.getMessage());
        }
    }

    @PutMapping("/{barcode}")
    public ResponseEntity<?> update(@PathVariable("barcode") String barcode,
                          @RequestParam("name") String name,
                          @RequestParam("quantity") int quantity,
                          @RequestParam("description") String description) {
        try {
            if (name.isEmpty() || description.isEmpty()) {
                return ResponseHandler.failed("Name and description cannot be empty", HttpStatus.BAD_REQUEST);
            }

            return ResponseHandler.builder("OK", service.update(barcode, name, quantity, description));
        } catch (Exception e) {
            throw new FailedException("Failed at product controller: " + e.getMessage());
        }
    }

    @PatchMapping("/{barcode}")
    public ResponseEntity<?> updatePatch(@PathVariable("barcode") String barcode,
                               @RequestParam("amount") int amount){
        try {
            return ResponseHandler.builder("OK", service.updateAmount(barcode,amount));
        } catch (Exception e) {
            throw new FailedException("Failed at product controller: " + e.getMessage());
        }
    }

    @DeleteMapping("/{barcode}")
    public ResponseEntity<?> delete(@PathVariable("barcode") String barcode){
        try {
            return ResponseHandler.builder("OK", service.delete(barcode));
        }
        catch (Exception e){
            throw new FailedException("Failed at product service: " + e.getMessage());
        }
    }
}