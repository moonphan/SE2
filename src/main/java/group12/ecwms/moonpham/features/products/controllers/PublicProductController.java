package group12.ecwms.moonpham.features.products.controllers;

import group12.ecwms.moonpham.domain.entity.Product;
import group12.ecwms.moonpham.domain.entity.SubCategory;
import group12.ecwms.moonpham.features.products.services.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PublicProductController {

    private static final String RECENTLY_VIEWED_SESSION_KEY = "recentlyViewedProductIds";
    private static final int RECENTLY_VIEWED_LIMIT = 6;

    private final ProductService productService;

    @GetMapping("/sub-categories")
    public String subCategories(Model model) {
        List<SubCategory> subCategories = productService.getAllSubCategories();
        model.addAttribute("subCategories", subCategories);
        return "sub-categories";
    }

    @GetMapping("/sub-categories/{subCategoryId}/products")
    public String productsBySubCategory(
            @PathVariable Long subCategoryId,
            Model model
    ) {
        List<SubCategory> subCategories = productService.getAllSubCategories();
        SubCategory selected = subCategories.stream()
                .filter(sc -> sc.getId().equals(subCategoryId))
                .findFirst()
                .orElse(null);

        model.addAttribute("subCategories", subCategories);
        model.addAttribute("selectedSubCategory", selected);
        var page = productService.getProductsBySubCategory(subCategoryId, 0, 20, "newest");
        model.addAttribute("productPage", page);
        model.addAttribute("products", page.getContent());
        return "products-by-subcategory";
    }

    @GetMapping("/sub-categories/{subCategoryId}/best-sellers")
    public String bestSellersBySubCategory(@PathVariable Long subCategoryId, Model model) {
        List<SubCategory> subCategories = productService.getAllSubCategories();
        SubCategory selected = subCategories.stream()
                .filter(sc -> sc.getId().equals(subCategoryId))
                .findFirst()
                .orElse(null);

        model.addAttribute("subCategories", subCategories);
        model.addAttribute("selectedSubCategory", selected);
        model.addAttribute("bestSellers", productService.getBestSellersBySubCategory(subCategoryId, 5));
        return "best-sellers";
    }

    @GetMapping("/product-detail/{productId}")
    public String productDetail(@PathVariable Long productId, HttpSession session, Model model) {
        Product product = productService.getProductDetail(productId);
        model.addAttribute("product", product);
        model.addAttribute("subCategories", productService.getAllSubCategories());

        List<Long> recentlyViewed = getRecentlyViewedIds(session);
        recentlyViewed.remove(productId);
        recentlyViewed.addFirst(productId);
        if (recentlyViewed.size() > RECENTLY_VIEWED_LIMIT) {
            recentlyViewed.subList(RECENTLY_VIEWED_LIMIT, recentlyViewed.size()).clear();
        }
        session.setAttribute(RECENTLY_VIEWED_SESSION_KEY, recentlyViewed);

        List<Product> recentlyViewedProducts = recentlyViewed.stream()
                .filter(id -> !id.equals(productId))
                .map(productService::getProductDetail)
                .toList();
        model.addAttribute("recentlyViewedProducts", recentlyViewedProducts);
        return "product-detail";
    }

    @GetMapping("/recently-viewed")
    public String recentlyViewed(HttpSession session, Model model) {
        List<Long> ids = getRecentlyViewedIds(session);
        List<Product> recentlyViewedProducts = ids.stream()
                .map(productService::getProductDetail)
                .toList();

        model.addAttribute("subCategories", productService.getAllSubCategories());
        model.addAttribute("recentlyViewedProducts", recentlyViewedProducts);
        return "recently-viewed";
    }

    @SuppressWarnings("unchecked")
    private List<Long> getRecentlyViewedIds(HttpSession session) {
        Object value = session.getAttribute(RECENTLY_VIEWED_SESSION_KEY);
        if (value instanceof List<?> list) {
            return new ArrayList<>((List<Long>) list);
        }
        return new ArrayList<>();
    }
}

