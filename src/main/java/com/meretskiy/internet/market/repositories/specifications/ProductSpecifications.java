package com.meretskiy.internet.market.repositories.specifications;

import com.meretskiy.internet.market.model.Product;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.MultiValueMap;

public class ProductSpecifications {

    //criteriaBuilder позволяет выполнять стандартные HQL операторы(больше того-то и т.д.)
    //root - это корневой элемент (<Product>)
    //запрашиваем стоимость и говорим, что она должна быть больше или равна значению minPrice
    private static Specification<Product> priceGreaterOrEqualsThan(int minPrice) {
        return (Specification<Product>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder
                .greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    private static Specification<Product> priceLesserOrEqualsThan(int maxPrice) {
        return (Specification<Product>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder
                .lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    private static Specification<Product> titleLike(String titlePart) {
        return (Specification<Product>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder
                .like(root.get("title"), String.format("%%%s%%", titlePart));
    }

    //получаем мапу параметров и собираем готовую спецификацию
    public static Specification<Product> build(MultiValueMap<String, String> params) {
        //создаем спецификацию для продукта, которая не будет накладывать никаких ограничений
        Specification<Product> spec = Specification.where(null);
        //если указали minPrice и он не пустой,
        //то выдергиваем из мапы параметр minPrice и через И добавляем к спецификации новую проверку
        if (params.containsKey("min_price") && !params.getFirst("min_price").isBlank()) {
            Integer minPrice = Integer.parseInt(params.getFirst("min_price"));
            spec = spec.and(ProductSpecifications.priceGreaterOrEqualsThan(minPrice));
        }
        if (params.containsKey("max_price") && !params.getFirst("max_price").isBlank()) {
            Integer maxPrice = Integer.parseInt(params.getFirst("max_price"));
            spec = spec.and(ProductSpecifications.priceLesserOrEqualsThan(maxPrice));
        }
        if (params.containsKey("title") && !params.getFirst("title").isBlank()) {
            spec = spec.and(ProductSpecifications.titleLike(params.getFirst("title")));
        }
        return spec;
     }

}
