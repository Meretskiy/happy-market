package com.meretskiy.internet.market.endpoints;

import com.meretskiy.internet.market.services.ProductService;
import com.meretskiy.internet.market.soap.productsWS.GetAllProductsRequest;
import com.meretskiy.internet.market.soap.productsWS.GetAllProductsResponse;
import com.meretskiy.internet.market.soap.productsWS.GetProductByIdRequest;
import com.meretskiy.internet.market.soap.productsWS.GetProductByIdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
@RequiredArgsConstructor
public class ProductEndpoint {
    private static final String NAMESPACE_URI = "http://www.meretskiy.com/happy/market/ws/productsWS";
    private final ProductService productService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getProductByIdRequest")
    @ResponsePayload
    public GetProductByIdResponse getProductById(@RequestPayload GetProductByIdRequest request) {
        GetProductByIdResponse response = new GetProductByIdResponse();
        response.setProduct(productService.findProductWSById(request.getId()));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllProductsRequest")
    @ResponsePayload
    public GetAllProductsResponse getAllProducts(@RequestPayload GetAllProductsRequest request) {
        GetAllProductsResponse response = new GetAllProductsResponse();
        productService.findAllProductsWS().forEach(response.getProducts()::add);
        return response;
    }


}
