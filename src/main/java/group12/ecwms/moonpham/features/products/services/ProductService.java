package group12.ecwms.moonpham.features.products.services;

import group12.ecwms.moonpham.domain.entity.Category;
import group12.ecwms.moonpham.domain.entity.Product;
import group12.ecwms.moonpham.domain.entity.SubCategory;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    Page<Product> browseSearchFilter(
            String keyword,
            Long categoryId,
            String brand,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            int page,
            int size,
            String sort
    );

    Product getProductDetail(Long productId);

    List<Category> getAllCategories();

    List<SubCategory> getAllSubCategories();

    Page<Product> getProductsBySubCategory(Long subCategoryId, int page, int size, String sort);

    List<Product> getBestSellersBySubCategory(Long subCategoryId, int limit);
}

