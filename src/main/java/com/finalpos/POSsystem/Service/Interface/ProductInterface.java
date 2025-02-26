package com.finalpos.POSsystem.Service.Interface;

import com.finalpos.POSsystem.Model.DTO.ProductDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

public interface ProductInterface {
    Map<String, Object> products(int page, int size);
    ProductDTO add(String barcode, String name, String description, String import_price,
    String retail_price, String quantity, String category, Optional<MultipartFile> image);
    ProductDTO getWBarcode(String barcode);
    ProductDTO update(String barcode, String name, int quantity, String description);
    ProductDTO updateAmount(String barcode, int amount);
    ProductDTO delete(String barcode);
}
