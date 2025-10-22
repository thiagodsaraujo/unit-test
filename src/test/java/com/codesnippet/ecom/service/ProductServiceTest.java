package com.codesnippet.ecom.service;

import com.codesnippet.ecom.Entity.Product;
import com.codesnippet.ecom.Repository.ProductRepository;
import com.codesnippet.ecom.Service.ProductService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductService productService;

    @BeforeAll
    public static void init(){
       // Usamos a anotação BeforeAll para executar este método antes de todos os testes
        // Deve ser estático,
        // Muito usado para inicializar recursos compartilhados
        // Como conexões de banco de dados, servidores, etc.
        // Exemplo: Inicializar uma conexão de banco de dados, configurar um servidor de teste, etc.
        System.out.println("Before All Init");
    }

    @BeforeEach
    public void initEach(){
        // Usamos a anotação BeforeEach para executar este método antes de cada teste
        // Não precisa ser estático, será executado antes de cada método de teste
        // Muito usado para configurar o estado necessário para cada teste
        // Exemplo: Resetar variáveis, limpar dados de teste, etc.
        System.out.println("Before Each Test");
    }

    @Test
    void addProductShouldAddProductSuccessfully() {
        System.out.println("myFirstTest");

        // Testar a adição de um produto
        Product product = new Product();
        product.setId(1);
        product.setName("myFirstTest");
        product.setDescription("myFirstTest");
        product.setPrice(2000);
        Mockito.when(productRepository.save(product)).thenReturn(product);

        Product addedProduct = productService.addProduct(product);

        // test product == matched product
        Assertions.assertNotNull(addedProduct);
        Assertions.assertEquals(product.getId(), addedProduct.getId());
        Assertions.assertEquals(product.getName(), addedProduct.getName());
        Assertions.assertTrue(product.getId() != addedProduct.getId());

    }

    @Test
    public void deleteProductShouldDeleteProductSuccessfully() {
        System.out.println("deleteProductTest");

        // Testar a exclusão de um produto
        Integer productId = 1;
        // Simular o comportamento do repositório para o método deleteById
        // doNothing() é usado para métodos void, como deleteById
        Mockito.doNothing().when(productRepository).deleteById(productId);

        productService.deleteProduct(productId);

        // Verificar se o método deleteById foi chamado corretamente
        // O método deleteById é void, então usamos doNothing() para simular seu comportamento
        // O mockito.times(1) verifica se o método foi chamado exatamente uma vez
        verify(productRepository, Mockito.times(1)).deleteById(productId);
    }

    @Test
    void testPrivateMethodUsingReflection() throws Exception {
        System.out.println("testPrivateMethodUsingReflection");

        // Testar o método privado validateProductName usando reflexão
        // Reflexão permite acessar métodos privados para fins de teste
        // Criar uma instância de Product para testar
        Product product = new Product();
        product.setName("Book");

        // Usar reflexão para acessar o método privado
        java.lang.reflect.Method method = ProductService.class.getDeclaredMethod("validateProductName", String.class);
        method.setAccessible(true); // Tornar o método acessível
        boolean isValid = (boolean) method.invoke(productService, product.getName());
        // Verificar o resultado
        Assertions.assertTrue(isValid);


    }

    @Test
    void testPrivateMethodUsingReflectionIfNameIsInvalid() throws Exception {
        System.out.println("testPrivateMethodUsingReflection");

        // Testar o método privado validateProductName usando reflexão
        // Reflexão permite acessar métodos privados para fins de teste
        // Criar uma instância de Product para testar
        Product product = new Product();
        product.setName("");

        // Usar reflexão para acessar o método privado
        java.lang.reflect.Method method = ProductService.class.getDeclaredMethod("validateProductName", String.class);
        method.setAccessible(true); // Tornar o método acessível
        boolean isValid = (boolean) method.invoke(productService, product.getName());
        // Verificar o resultado
        Assertions.assertFalse(isValid);


    }

    @Test
    void addProductShouldThrowExceptionForInvalidName() {
        System.out.println("addProductShouldThrowExceptionForInvalidName");

        // Testar a adição de um produto com nome inválido
        Product product = new Product();
        product.setId(2);
        product.setName(""); // Nome inválido
        product.setDescription("Invalid Product");
        product.setPrice(1500);

        // Verificar se a exceção é lançada ao tentar adicionar o produto
        Assertions.assertThrows(RuntimeException.class, () -> {
            productService.addProduct(product);
        });
        // Verificar a mensagem da exceção
        RuntimeException runtimeException = Assertions.assertThrows(RuntimeException.class, () -> {
            productService.addProduct(product);
        });

        //
        Assertions.assertEquals("Invalid Name Of Product", runtimeException.getMessage());
        // Verificar que o método save do repositório nunca foi chamado, já que o nome é inválido
        verify(productRepository, Mockito.never()).save(any(Product.class));
    }

    @AfterAll
    public static void tearDown(){
       // Usamos a anotação AfterAll para executar este método após todos os testes
        // Deve ser estático,
        // Muito usado para liberar recursos compartilhados
        // Exemplo: Fechar conexões de banco de dados, parar servidores, etc.
        System.out.println("After All Tear Down");
    }

    @AfterEach
    public void tearDownEach(){
        // Usamos a anotação AfterEach para executar este método após cada teste
        // Não precisa ser estático, será executado após cada método de teste
        // Muito usado para limpar o estado após cada teste
        // Exemplo: Limpar variáveis, remover dados de teste, etc.
        System.out.println("After Each Test Tear Down");
    }

}
