package group12.ecwms.moonpham.features.products.controllers;

import group12.ecwms.moonpham.domain.entity.Product;
import group12.ecwms.moonpham.features.products.services.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private static final String RECENTLY_VIEWED_SESSION_KEY = "recentlyViewedProductIds";
    private static final int RECENTLY_VIEWED_LIMIT = 6;
    private final ProductService productService;

    @GetMapping
    public String listProducts(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "newest") String sort,
            Model model
    ) {
        Page<Product> productPage = productService.browseSearchFilter(
                q, categoryId, brand, minPrice, maxPrice, page, size, sort
        );

        model.addAttribute("productPage", productPage);
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("subCategories", productService.getAllSubCategories());
        model.addAttribute("q", q);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("brand", brand);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("sort", sort);
        return "products";
    }

    @GetMapping("/{id}")
    public String productDetail(@PathVariable("id") Long productId, HttpSession session, Model model) {
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

    @SuppressWarnings("unchecked")
    private List<Long> getRecentlyViewedIds(HttpSession session) {
        Object value = session.getAttribute(RECENTLY_VIEWED_SESSION_KEY);
        if (value instanceof List<?>) {
            return new ArrayList<>((List<Long>) value);
        }
        return new ArrayList<>();
    }
}

