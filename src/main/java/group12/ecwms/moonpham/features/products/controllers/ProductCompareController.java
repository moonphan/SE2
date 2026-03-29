package group12.ecwms.moonpham.features.products.controllers;

import group12.ecwms.moonpham.common.dto.response.ProductCompareResult;
import group12.ecwms.moonpham.features.products.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ProductCompareController {

    private final ProductService productService;

    @GetMapping("/compare")
    public String compare(
            @RequestParam(name = "a", required = false) Long productIdA,
            @RequestParam(name = "b", required = false) Long productIdB,
            Model model
    ) {
        ProductCompareResult result = productService.compareProducts(productIdA, productIdB);
        model.addAttribute("result", result);
        model.addAttribute("paramA", productIdA);
        model.addAttribute("paramB", productIdB);
        if (result.ok()) {
            model.addAttribute("productA", result.productA());
            model.addAttribute("productB", result.productB());
            model.addAttribute("minPriceA", result.minPriceA());
            model.addAttribute("minPriceB", result.minPriceB());
        }
        return "compare";
    }
}
