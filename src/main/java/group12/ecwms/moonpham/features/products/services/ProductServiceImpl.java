package group12.ecwms.moonpham.features.products.services;

import group12.ecwms.moonpham.domain.entity.Category;
import group12.ecwms.moonpham.domain.entity.Product;
import group12.ecwms.moonpham.domain.entity.SubCategory;
import group12.ecwms.moonpham.common.exception.BadRequestException;
import group12.ecwms.moonpham.features.products.repository.ProductRepository;
import group12.ecwms.moonpham.features.products.repository.CategoryRepository;
import group12.ecwms.moonpham.features.products.repository.SubCategoryRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    @Override
    public Page<Product> browseSearchFilter(
            String keyword,
            Long categoryId,
            String brand,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            int page,
            int size,
            String sort
    ) {
        Sort sortBy = switch (sort == null ? "" : sort.trim()) {
            case "price_asc" -> Sort.by("unitPrice").ascending();
            case "price_desc" -> Sort.by("unitPrice").descending();
            case "name_asc" -> Sort.by("name").ascending();
            default -> Sort.by("createdAt").descending();
        };
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1), sortBy);

        Specification<Product> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (keyword != null && !keyword.isBlank()) {
                String like = "%" + keyword.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("name")), like),
                        cb.like(cb.lower(root.get("brand")), like)
                ));
            }

            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            }

            if (brand != null && !brand.isBlank()) {
                predicates.add(cb.equal(cb.lower(root.get("brand")), brand.trim().toLowerCase()));
            }

            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("unitPrice"), minPrice));
            }

            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("unitPrice"), maxPrice));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return productRepository.findAll(spec, pageable);
    }

    @Override
    public Product getProductDetail(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException("Product not found"));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll(Sort.by("name").ascending());
    }

    @Override
    public List<SubCategory> getAllSubCategories() {
        return subCategoryRepository.findAllByOrderByNameAsc();
    }

    @Override
    public Page<Product> getProductsBySubCategory(Long subCategoryId, int page, int size, String sort) {
        if (subCategoryId == null) {
            throw new BadRequestException("subCategoryId is required");
        }

        Sort sortBy = switch (sort == null ? "" : sort.trim()) {
            case "price_asc" -> Sort.by("unitPrice").ascending();
            case "price_desc" -> Sort.by("unitPrice").descending();
            case "sold_desc" -> Sort.by("totalSold").descending();
            default -> Sort.by("createdAt").descending();
        };

        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1), sortBy);
        return productRepository.findAllBySubCategory_Id(subCategoryId, pageable);
    }

    @Override
    public List<Product> getBestSellersBySubCategory(Long subCategoryId, int limit) {
        if (subCategoryId == null) {
            throw new BadRequestException("subCategoryId is required");
        }
        // Repository returns top 5; keep limit parameter for future expansion.
        List<Product> top = productRepository.findTop5BySubCategory_IdOrderByTotalSoldDesc(subCategoryId);
        if (limit <= 0) return top;
        return top.stream().limit(limit).toList();
    }
}

