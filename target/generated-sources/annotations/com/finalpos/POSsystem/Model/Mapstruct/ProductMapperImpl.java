package com.finalpos.POSsystem.Model.Mapstruct;

import com.finalpos.POSsystem.Entity.ProductEntity;
import com.finalpos.POSsystem.Model.DTO.ProductDTO;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-26T23:34:36+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductDTO toDTO(ProductEntity entity) {
        if ( entity == null ) {
            return null;
        }

        ProductDTO productDTO = new ProductDTO();

        productDTO.setBarcode( entity.getBarcode() );
        productDTO.setName( entity.getName() );
        productDTO.setQuantity( entity.getQuantity() );
        productDTO.setDescription( entity.getDescription() );
        productDTO.setImport_price( entity.getImport_price() );
        productDTO.setRetail_price( entity.getRetail_price() );
        productDTO.setImage( entity.getImage() );
        productDTO.setCategory( entity.getCategory() );
        productDTO.setCreation_date( entity.getCreation_date() );
        productDTO.setPurchase( entity.getPurchase() );
        productDTO.setId( entity.getId() );

        return productDTO;
    }

    @Override
    public ProductEntity toEntity(ProductDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ProductEntity productEntity = new ProductEntity();

        productEntity.setId( dto.getId() );
        productEntity.setBarcode( dto.getBarcode() );
        productEntity.setName( dto.getName() );
        productEntity.setQuantity( dto.getQuantity() );
        productEntity.setDescription( dto.getDescription() );
        productEntity.setImport_price( dto.getImport_price() );
        productEntity.setRetail_price( dto.getRetail_price() );
        productEntity.setImage( dto.getImage() );
        productEntity.setCategory( dto.getCategory() );
        productEntity.setCreation_date( dto.getCreation_date() );
        productEntity.setPurchase( dto.getPurchase() );

        return productEntity;
    }
}
