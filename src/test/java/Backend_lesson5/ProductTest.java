package Backend_lesson5;


import api.ProductService;
import dto.Product;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import utils.RetrofitUtils;
import okhttp3.ResponseBody;
import org.hamcrest.CoreMatchers;
import retrofit2.Response;

import java.io.IOException;


import static org.hamcrest.MatcherAssert.assertThat;


public class ProductTest {

    static ProductService productService;
    Product product = new Product();
    static int id;

    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);
    }

    @BeforeEach
    void setUp() {
        product = new Product()
                .withTitle("Bread")
                .withCategoryTitle("Food")
                .withPrice((int) (Math.random() * 10000));
    }

    @Test
    void createProductInFoodCategoryTest() throws IOException {
        Response<Product> response = productService.createProduct(product)
                .execute();

        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.code(), Matchers.is(201));
        assertThat(response.body().getTitle(), Matchers.is("Bread"));

    }

    @Test
    void getProductsTest() throws IOException {

        Response<ResponseBody> allProducts = productService.getProducts()
                .execute();

        System.out.println("AllProducts: " + allProducts.body().string());

        assertThat(allProducts.code(), Matchers.is(200));
        assertThat(allProducts.isSuccessful(), CoreMatchers.is(true));
    }


    @Test
    void modifyProductTest() throws IOException {

        Response<Product> response1 = productService.createProduct(product)
                .execute();
        id = response1.body().getId();
        assertThat(response1.isSuccessful(), CoreMatchers.is(true));
        assertThat(response1.code(), Matchers.is(201));


        Response<Product> response2 = productService.getProductById(id)
                .execute();
        assertThat(response2.isSuccessful(), CoreMatchers.is(true));
        assertThat(response2.body().getTitle(), Matchers.is("Bread"));


        product = new Product()
                .withId(id)
                .withTitle("Lemon")
                .withCategoryTitle("Food")
                .withPrice(540);
        Response<Product> response3 = productService.modifyProduct(product)
                .execute();
        assertThat(response3.code(), Matchers.is(200));

        Response<Product> response4 = productService.getProductById(id)
                .execute();
        assertThat(response4.isSuccessful(), CoreMatchers.is(true));
        assertThat(response4.body().getTitle(), Matchers.is("Lemon"));
        assertThat(response4.body().getPrice(), Matchers.is(540));


    }

    @Test
    void getProductByIdTest() throws IOException {

        Response<Product> response = productService.createProduct(product)
                .execute();
        id = response.body().getId();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.code(), Matchers.is(201));
        assertThat(response.body().getTitle(), Matchers.is("Bread"));

        Response<Product> response1 = productService.getProductById(id)
                .execute();

        assertThat(response1.isSuccessful(), CoreMatchers.is(true));
        assertThat(response1.code(), Matchers.is(200));
        assertThat(response1.body().getTitle(), Matchers.is("Bread"));

    }

    @SneakyThrows
    @AfterAll
    static void tearDown() {
        Response<ResponseBody> response = productService.deleteProduct(id).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
    }


}