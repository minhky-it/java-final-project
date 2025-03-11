package com.finalpos.POSsystem.Service;

import com.finalpos.POSsystem.Config.FirebaseService;
import com.finalpos.POSsystem.Entity.Package;
import com.finalpos.POSsystem.Entity.ProductEntity;
import com.finalpos.POSsystem.Exception.FailedException;
import com.finalpos.POSsystem.Model.DTO.ProductDTO;
import com.finalpos.POSsystem.Model.Mapstruct.ProductMapper;
import com.finalpos.POSsystem.Repository.ProductRepository;
import com.finalpos.POSsystem.Service.Interface.ProductInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class ProductService implements ProductInterface {
    @Autowired
    private FirebaseService firebase;

    @Autowired
    ProductRepository db;
    ProductMapper mapper = ProductMapper.INSTANCE;
    
    @Override
    public Map<String, Object> products(int page, int size) {
        try{
            int skipAmount = (page - 1) * size;
            int totalUsers = (int) db.count();
            int totalPages = (int) Math.ceil((double) totalUsers / size);

            List<ProductEntity> productModelList = db.findAll();
            List<ProductDTO> product = new ArrayList<>();

            int endIdx = Math.min(skipAmount + size, productModelList.size());
            for (int i = skipAmount; i < endIdx; i++) {
                product.add(mapper.toDTO(productModelList.get(i)));
            }
            Map<String, Object> data = new HashMap<>();
            data.put("products", product);
            data.put("divider", totalPages);
            return data;
        }catch (Exception e){
            throw new FailedException("Error in product service: " + e.getMessage());
        }
    }

    @Override
    public ProductDTO add(String barcode, String name, String description, String import_price,
                          String retail_price, String quantity, String category,
                          Optional<MultipartFile> image) {
        try{
            if(!(isNum(import_price) && isNum(import_price) && isNum(retail_price)))
                throw new FailedException("Quantity, import price and retail price should be a number!");

            if(Integer.valueOf(import_price.trim()) < 0 || Integer.valueOf(retail_price.trim()) < 0)
                throw new FailedException("Price should not be negative");
            else if(Integer.valueOf(quantity.trim()) < 0)
                throw new FailedException("Quantity should not be negative");
            else if(Integer.valueOf(import_price.trim()) > Integer.valueOf(retail_price.trim()))
                throw new FailedException("Import price should not be greater than retail price");
            else if(db.findByBarcode(barcode.trim()) != null)
                throw new FailedException("Barcode existed");
            else {
                String imageUrl = "";
                if (image.isPresent()) {
                    imageUrl = firebase.uploadImage(image.get());
                }

                String creation_date = String.valueOf(java.time.LocalDateTime.now());
                ProductEntity result = create_product(barcode.trim(), name.trim(), Integer.valueOf(quantity.trim()), description.trim(),
                        Double.valueOf(import_price.trim()), Double.valueOf(retail_price.trim()), imageUrl, category.trim(),
                        creation_date, false);
                return mapper.toDTO(result);
            }
            
        } catch (Exception e) {
            throw new FailedException("Error in product service: " + e.getMessage());
        }
    }

    @Override
    public ProductDTO getWBarcode(String barcode) {
        try{
            ProductEntity product = db.findByBarcode(barcode);
            return mapper.toDTO(product);
        } catch (Exception e) {
            throw new FailedException("Error in product service: " + e.getMessage());
        }
    }

    @Override
    public ProductDTO update(String barcode, String name, int quantity, String description) {
        try{
            ProductEntity existingProduct = db.findByBarcode(barcode);
            if(existingProduct == null)
                throw new FailedException("Product not found");

            existingProduct.setName(name);
            existingProduct.setQuantity(quantity);
            existingProduct.setDescription(description);

            db.save(existingProduct);

            return mapper.toDTO(existingProduct);
        } catch (Exception e) {
            throw new FailedException("Error in product service: " + e.getMessage());
        }
    }

    @Override
    public ProductDTO updateAmount(String barcode, int amount) {
        try{
            ProductEntity product = db.findByBarcode(barcode);

            if (product == null)
                throw new FailedException("Product not found!");

            product.setQuantity(product.getQuantity()+amount);

            db.save(product);

            return mapper.toDTO(product);
        } catch (Exception e) {
            throw new FailedException("Error in product service: " + e.getMessage());
        }
    }

    @Override
    public ProductDTO delete(String barcode) {
        try{
            ProductEntity product = db.findByBarcode(barcode);
            if(product == null)
                throw new FailedException("Product not found");
            if(!product.getPurchase()) {
                ProductEntity result = db.removeProductModelByBarcode(barcode);
                return mapper.toDTO(result);
            } else
                throw new FailedException("Product was purchased");
        } catch (Exception e) {
            throw new FailedException("Error product service: " + e.getMessage());
        }
    }

    private ProductEntity create_product(String barcode, String name, int quantity, String description,
                                         double import_price, double retail_price, String image,
                                         String category, String creation_date, Boolean purchase) {
        try {
            ProductEntity product = new ProductEntity();
            product.setBarcode(barcode);
            product.setName(name);
            product.setQuantity(quantity);
            product.setDescription(description);
            product.setImport_price(import_price);
            product.setRetail_price(retail_price);
            product.setImage(image);
            product.setCategory(category);
            product.setCreation_date(creation_date);
            product.setPurchase(purchase);
            db.save(product);
            return product;
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isNum(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }
}
