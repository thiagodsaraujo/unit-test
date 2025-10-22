# 📚 Guia Completo: Testes Unitários com JUnit 5 e Mockito

## 📋 Índice
- [Introdução](#introdução)
- [O que são Testes Unitários?](#o-que-são-testes-unitários)
- [JUnit 5](#junit-5)
- [Mockito](#mockito)
- [Anotações Principais](#anotações-principais)
- [Ciclo de Vida dos Testes](#ciclo-de-vida-dos-testes)
- [Assertions (Asserções)](#assertions-asserções)
- [Mocking e Stubbing](#mocking-e-stubbing)
- [Verificações](#verificações)
- [Testes de Exceções](#testes-de-exceções)
- [Testando Métodos Privados](#testando-métodos-privados)
- [Boas Práticas](#boas-práticas)
- [Exemplos Práticos](#exemplos-práticos)

---

## 🎯 Introdução

Este guia apresenta os conceitos fundamentais de **testes unitários** em Java utilizando **JUnit 5** e **Mockito**, duas das ferramentas mais populares para garantir a qualidade e confiabilidade do código.

## 🔍 O que são Testes Unitários?

Testes unitários são testes automatizados que verificam o comportamento de **pequenas unidades de código** (geralmente métodos ou classes) de forma isolada. 

### Benefícios:
✅ **Detecção precoce de bugs**  
✅ **Documentação viva do código**  
✅ **Facilita refatoração**  
✅ **Aumenta a confiança nas mudanças**  
✅ **Melhora o design do código**  

### Características:
- **Rápidos**: Executam em milissegundos
- **Isolados**: Não dependem de recursos externos (BD, rede, etc.)
- **Repetíveis**: Produzem o mesmo resultado sempre
- **Automáticos**: Não requerem intervenção manual

---

## 🧪 JUnit 5

JUnit 5 é o framework de testes mais popular para Java. Composto por três sub-projetos:

- **JUnit Platform**: Base para executar frameworks de teste na JVM
- **JUnit Jupiter**: API para escrever testes no JUnit 5
- **JUnit Vintage**: Suporte para executar testes JUnit 3 e 4

### Dependência Maven:
```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.10.0</version>
    <scope>test</scope>
</dependency>
```

---

## 🎭 Mockito

Mockito é um framework de **mocking** que permite criar objetos simulados (mocks) para isolar a unidade de código sendo testada de suas dependências.

### Dependência Maven:
```xml
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>5.5.0</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-junit-jupiter</artifactId>
    <version>5.5.0</version>
    <scope>test</scope>
</dependency>
```

### Por que usar Mockito?
- ✅ Isola o código testado de dependências externas
- ✅ Simula comportamentos complexos facilmente
- ✅ Torna os testes mais rápidos
- ✅ Permite testar cenários difíceis de reproduzir

---

## 📌 Anotações Principais

### JUnit 5

| Anotação | Descrição | Quando Usar |
|----------|-----------|-------------|
| `@Test` | Marca um método como teste | Sempre para métodos de teste |
| `@BeforeAll` | Executa uma vez antes de todos os testes | Inicializar recursos caros (DB, servidor) |
| `@BeforeEach` | Executa antes de cada teste | Preparar estado para cada teste |
| `@AfterAll` | Executa uma vez após todos os testes | Liberar recursos compartilhados |
| `@AfterEach` | Executa após cada teste | Limpar estado após cada teste |
| `@DisplayName` | Define um nome descritivo para o teste | Melhorar legibilidade dos relatórios |
| `@Disabled` | Desabilita um teste | Temporariamente pular um teste |
| `@RepeatedTest(n)` | Repete o teste n vezes | Testar comportamento não-determinístico |
| `@ParameterizedTest` | Permite passar múltiplos parâmetros | Testar múltiplos cenários |
| `@Timeout` | Define tempo máximo de execução | Detectar métodos que travam |

### Mockito

| Anotação | Descrição | Uso |
|----------|-----------|-----|
| `@ExtendWith(MockitoExtension.class)` | Habilita o Mockito no JUnit 5 | Nível de classe |
| `@Mock` | Cria um objeto mock | Para dependências |
| `@InjectMocks` | Injeta os mocks na classe testada | Para a classe sendo testada |
| `@Spy` | Cria um spy (mock parcial) | Quando precisa de comportamento real e mock |
| `@Captor` | Captura argumentos passados aos mocks | Para validações complexas |

---

## 🔄 Ciclo de Vida dos Testes

```java
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @BeforeAll
    static void initAll() {
        // Executado UMA VEZ antes de TODOS os testes
        // Deve ser estático
        // Use para: conexões DB, inicializar servidores de teste
        System.out.println("Inicializando recursos compartilhados...");
    }

    @BeforeEach
    void init() {
        // Executado ANTES de CADA teste
        // Não precisa ser estático
        // Use para: resetar estado, preparar dados de teste
        System.out.println("Preparando teste...");
    }

    @Test
    void meuTeste() {
        // Lógica do teste
    }

    @AfterEach
    void tearDown() {
        // Executado APÓS cada teste
        // Use para: limpar dados, resetar mocks
        System.out.println("Limpando após teste...");
    }

    @AfterAll
    static void tearDownAll() {
        // Executado UMA VEZ após TODOS os testes
        // Deve ser estático
        // Use para: fechar conexões, parar servidores
        System.out.println("Liberando recursos compartilhados...");
    }
}
```

### Fluxo de Execução:
```
@BeforeAll (uma vez)
  ↓
@BeforeEach → @Test (teste 1) → @AfterEach
  ↓
@BeforeEach → @Test (teste 2) → @AfterEach
  ↓
@BeforeEach → @Test (teste 3) → @AfterEach
  ↓
@AfterAll (uma vez)
```

---

## ✅ Assertions (Asserções)

Asserções verificam se o resultado é o esperado. JUnit 5 oferece diversas asserções:

### Asserções Básicas

```java
import static org.junit.jupiter.api.Assertions.*;

@Test
void exemploAssertions() {
    Product product = new Product(1, "Laptop", 2000);
    
    // Verificar igualdade
    assertEquals(1, product.getId());
    assertEquals("Laptop", product.getName());
    
    // Verificar não-nulidade
    assertNotNull(product);
    
    // Verificar nulidade
    assertNull(product.getDescription());
    
    // Verificar booleanos
    assertTrue(product.getPrice() > 0);
    assertFalse(product.getName().isEmpty());
    
    // Verificar mesmo objeto (referência)
    assertSame(product, product);
    
    // Verificar objetos diferentes
    assertNotSame(product, new Product(1, "Laptop", 2000));
    
    // Verificar arrays
    int[] expected = {1, 2, 3};
    int[] actual = {1, 2, 3};
    assertArrayEquals(expected, actual);
}
```

### Asserções Avançadas

```java
@Test
void assertionsAvancadas() {
    // Agrupar múltiplas asserções
    assertAll("product",
        () -> assertEquals(1, product.getId()),
        () -> assertEquals("Laptop", product.getName()),
        () -> assertTrue(product.getPrice() > 0)
    );
    
    // Verificar exceções
    Exception exception = assertThrows(RuntimeException.class, () -> {
        productService.addProduct(null);
    });
    assertEquals("Product cannot be null", exception.getMessage());
    
    // Timeout
    assertTimeout(Duration.ofSeconds(1), () -> {
        // Operação que deve completar em 1 segundo
        productService.findAll();
    });
}
```

---

## 🎭 Mocking e Stubbing

### Criando Mocks

```java
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    
    @Mock
    private ProductRepository productRepository;
    
    @InjectMocks
    private ProductService productService;
    
    // Alternativa sem anotações:
    @BeforeEach
    void setup() {
        productRepository = Mockito.mock(ProductRepository.class);
        productService = new ProductService(productRepository);
    }
}
```

### Stubbing (Definindo Comportamento)

```java
@Test
void exemploStubbing() {
    Product product = new Product(1, "Laptop", 2000);
    
    // WHEN-THEN: Quando chamar o método, então retorne o valor
    when(productRepository.save(any(Product.class))).thenReturn(product);
    when(productRepository.findById(1)).thenReturn(Optional.of(product));
    when(productRepository.findAll()).thenReturn(Arrays.asList(product));
    
    // Para métodos void
    doNothing().when(productRepository).deleteById(1);
    
    // Lançar exceções
    when(productRepository.findById(999))
        .thenThrow(new ProductNotFoundException("Product not found"));
    
    // Comportamentos sequenciais
    when(productRepository.count())
        .thenReturn(0L)      // Primeira chamada
        .thenReturn(1L)      // Segunda chamada
        .thenReturn(2L);     // Terceira chamada
    
    // Resposta customizada
    when(productRepository.save(any(Product.class)))
        .thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            p.setId(100);
            return p;
        });
}
```

### ArgumentMatchers

```java
import static org.mockito.ArgumentMatchers.*;

@Test
void exemploMatchers() {
    // Qualquer objeto do tipo
    when(productRepository.save(any(Product.class))).thenReturn(product);
    
    // Qualquer inteiro
    when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));
    
    // Qualquer string
    when(productRepository.findByName(anyString())).thenReturn(product);
    
    // Valor específico
    when(productRepository.findById(eq(1))).thenReturn(Optional.of(product));
    
    // Null
    when(productRepository.save(isNull())).thenThrow(IllegalArgumentException.class);
    
    // Not null
    when(productRepository.save(notNull())).thenReturn(product);
    
    // Lista vazia
    when(productRepository.findByIds(anyList())).thenReturn(Collections.emptyList());
}
```

---

## 🔍 Verificações

Verificações confirmam que certos métodos foram chamados (ou não) nos mocks.

```java
@Test
void exemploVerificacoes() {
    Product product = new Product(1, "Laptop", 2000);
    
    productService.addProduct(product);
    
    // Verificar que o método foi chamado
    verify(productRepository).save(product);
    
    // Verificar número de chamadas
    verify(productRepository, times(1)).save(product);
    
    // Verificar que nunca foi chamado
    verify(productRepository, never()).deleteById(anyInt());
    
    // Verificar pelo menos N chamadas
    verify(productRepository, atLeast(1)).save(any(Product.class));
    
    // Verificar no máximo N chamadas
    verify(productRepository, atMost(2)).save(any(Product.class));
    
    // Verificar ordem de chamadas
    InOrder inOrder = inOrder(productRepository);
    inOrder.verify(productRepository).save(product);
    inOrder.verify(productRepository).findById(1);
    
    // Verificar que não houve mais interações
    verifyNoMoreInteractions(productRepository);
    
    // Verificar que não houve nenhuma interação
    verifyNoInteractions(productRepository);
}
```

---

## 💥 Testes de Exceções

```java
@Test
void testExceptions() {
    Product product = new Product(1, "", 2000); // Nome inválido
    
    // Forma 1: Verificar que exceção foi lançada
    assertThrows(RuntimeException.class, () -> {
        productService.addProduct(product);
    });
    
    // Forma 2: Verificar exceção e mensagem
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
        productService.addProduct(product);
    });
    assertEquals("Invalid Name Of Product", exception.getMessage());
    
    // Forma 3: Verificar com Mockito (quando mock lança exceção)
    when(productRepository.save(any()))
        .thenThrow(new DataAccessException("Database error"));
    
    assertThrows(DataAccessException.class, () -> {
        productService.addProduct(product);
    });
    
    // Verificar que o método não foi chamado devido à exceção
    verify(productRepository, never()).save(any(Product.class));
}
```

---

## 🔒 Testando Métodos Privados

**Boa Prática**: Idealmente, métodos privados são testados indiretamente através de métodos públicos. Porém, quando necessário:

### Usando Reflexão (Java Reflection)

```java
@Test
void testPrivateMethodUsingReflection() throws Exception {
    // Criar instância
    Product product = new Product();
    product.setName("Book");
    
    // Acessar método privado usando reflexão
    Method method = ProductService.class
        .getDeclaredMethod("validateProductName", String.class);
    
    // Tornar acessível
    method.setAccessible(true);
    
    // Invocar o método
    boolean isValid = (boolean) method.invoke(productService, product.getName());
    
    // Verificar resultado
    assertTrue(isValid);
}
```

### Quando Testar Métodos Privados:
- ❌ **Evite**: Na maioria dos casos
- ✅ **Considere**: Se a lógica é complexa e crítica
- ✅ **Melhor**: Extrair para uma classe separada e tornar público
- ✅ **Alternativa**: Mudar visibilidade para `protected` ou `package-private` para testes

---

## 🏆 Boas Práticas

### 1. **Nomenclatura de Testes**

```java
// ❌ Ruim
@Test
void test1() { }

// ✅ Bom - Padrão: metodo_cenario_resultadoEsperado
@Test
void addProduct_WithValidData_ShouldReturnProduct() { }

@Test
void addProduct_WithEmptyName_ShouldThrowException() { }

@Test
void deleteProduct_WhenProductExists_ShouldDeleteSuccessfully() { }
```

### 2. **Padrão AAA (Arrange-Act-Assert)**

```java
@Test
void addProductShouldAddProductSuccessfully() {
    // ARRANGE (Preparar) - Configurar dados e mocks
    Product product = new Product(1, "Laptop", 2000);
    when(productRepository.save(product)).thenReturn(product);
    
    // ACT (Agir) - Executar o método sendo testado
    Product result = productService.addProduct(product);
    
    // ASSERT (Verificar) - Verificar o resultado
    assertNotNull(result);
    assertEquals(product.getId(), result.getId());
    verify(productRepository).save(product);
}
```

### 3. **Um Teste, Um Conceito**

```java
// ❌ Ruim - Testando múltiplos comportamentos
@Test
void testProduct() {
    productService.addProduct(product);
    productService.updateProduct(product);
    productService.deleteProduct(product.getId());
}

// ✅ Bom - Um teste por comportamento
@Test
void addProduct_ShouldAddSuccessfully() {
    productService.addProduct(product);
    verify(productRepository).save(product);
}

@Test
void updateProduct_ShouldUpdateSuccessfully() {
    productService.updateProduct(product);
    verify(productRepository).update(product);
}
```

### 4. **Testes Independentes**

```java
// ❌ Ruim - Testes dependentes
static Product sharedProduct;

@Test
void test1() {
    sharedProduct = new Product(1, "Laptop", 2000);
}

@Test
void test2() {
    // Depende do test1 ter executado antes
    productService.update(sharedProduct);
}

// ✅ Bom - Testes independentes
@BeforeEach
void setup() {
    product = new Product(1, "Laptop", 2000);
}

@Test
void test1() {
    productService.add(product);
}

@Test
void test2() {
    productService.update(product);
}
```

### 5. **Não Testar Código de Framework**

```java
// ❌ Não teste getters/setters simples
@Test
void testGettersSetters() {
    product.setName("Laptop");
    assertEquals("Laptop", product.getName());
}

// ✅ Teste lógica de negócio
@Test
void calculateDiscount_WithVIPCustomer_ShouldApply20PercentDiscount() {
    // Lógica real de negócio
}
```

### 6. **Cobertura de Código**

- **Objetivo**: 70-80% de cobertura é bom
- **Não se obceda**: 100% de cobertura não garante qualidade
- **Foque**: Caminhos críticos e lógica de negócio
- **Ferramentas**: JaCoCo, SonarQube

### 7. **Testes Rápidos**

```java
// ❌ Evite operações lentas
@Test
void slowTest() {
    Thread.sleep(5000); // 5 segundos
    // ...
}

// ✅ Use mocks para recursos externos
@Test
void fastTest() {
    when(externalApi.call()).thenReturn(mockResponse);
    // Executa em milissegundos
}
```

### 8. **Mensagens de Erro Claras**

```java
// ❌ Sem mensagem
assertEquals(expected, actual);

// ✅ Com mensagem descritiva
assertEquals(expected, actual, 
    "O preço calculado deveria ser " + expected + " mas foi " + actual);

// ✅ Ainda melhor - usar assertAll
assertAll("Validação do produto",
    () -> assertEquals(1, product.getId(), "ID incorreto"),
    () -> assertEquals("Laptop", product.getName(), "Nome incorreto"),
    () -> assertTrue(product.getPrice() > 0, "Preço deve ser positivo")
);
```

---

## 💡 Exemplos Práticos

### Exemplo 1: Teste Básico com Mock

```java
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void addProduct_WithValidData_ShouldReturnProduct() {
        // Arrange
        Product product = new Product(1, "Laptop", 2000);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act
        Product result = productService.addProduct(product);

        // Assert
        assertNotNull(result);
        assertEquals(product.getId(), result.getId());
        assertEquals(product.getName(), result.getName());
        verify(productRepository, times(1)).save(product);
    }
}
```

### Exemplo 2: Teste de Método Void

```java
@Test
void deleteProduct_WhenProductExists_ShouldDeleteSuccessfully() {
    // Arrange
    Integer productId = 1;
    doNothing().when(productRepository).deleteById(productId);

    // Act
    productService.deleteProduct(productId);

    // Assert
    verify(productRepository, times(1)).deleteById(productId);
}
```

### Exemplo 3: Teste de Exceção

```java
@Test
void addProduct_WithInvalidName_ShouldThrowException() {
    // Arrange
    Product product = new Product(2, "", 1500); // Nome vazio

    // Act & Assert
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
        productService.addProduct(product);
    });

    assertEquals("Invalid Name Of Product", exception.getMessage());
    verify(productRepository, never()).save(any(Product.class));
}
```

### Exemplo 4: Teste Parametrizado

```java
@ParameterizedTest
@ValueSource(strings = {"Laptop", "Phone", "Tablet"})
void addProduct_WithDifferentNames_ShouldAddSuccessfully(String productName) {
    // Arrange
    Product product = new Product(1, productName, 1000);
    when(productRepository.save(any(Product.class))).thenReturn(product);

    // Act
    Product result = productService.addProduct(product);

    // Assert
    assertEquals(productName, result.getName());
}

@ParameterizedTest
@CsvSource({
    "1, Laptop, 2000",
    "2, Phone, 1000",
    "3, Tablet, 1500"
})
void addProduct_WithMultipleProducts_ShouldAddSuccessfully(
        int id, String name, int price) {
    // Arrange
    Product product = new Product(id, name, price);
    when(productRepository.save(any(Product.class))).thenReturn(product);

    // Act
    Product result = productService.addProduct(product);

    // Assert
    assertAll(
        () -> assertEquals(id, result.getId()),
        () -> assertEquals(name, result.getName()),
        () -> assertEquals(price, result.getPrice())
    );
}
```

### Exemplo 5: Teste com @Spy

```java
@ExtendWith(MockitoExtension.class)
class ProductServiceSpyTest {

    @Spy
    private ProductService productService;

    @Test
    void calculateDiscount_ShouldUseRealMethodButMockPartially() {
        // Spy permite usar comportamento real e mockar parte
        Product product = new Product(1, "Laptop", 1000);
        
        // Mockar apenas um método específico
        doReturn(0.2).when(productService).getDiscountRate();
        
        // Outros métodos usam implementação real
        double finalPrice = productService.calculateFinalPrice(product);
        
        assertEquals(800.0, finalPrice); // 1000 - 20%
    }
}
```

---

## 📊 Estrutura de Projeto de Testes

```
src/
├── main/
│   └── java/
│       └── com/
│           └── example/
│               ├── service/
│               │   └── ProductService.java
│               ├── repository/
│               │   └── ProductRepository.java
│               └── entity/
│                   └── Product.java
└── test/
    └── java/
        └── com/
            └── example/
                └── service/
                    └── ProductServiceTest.java
```

**Convenção**: 
- Classe de teste tem o mesmo nome + "Test"
- Mesma estrutura de pacotes
- Pasta `test/java` separada de `main/java`

---

## 🚀 Executando Testes

### Via Maven

```bash
# Executar todos os testes
mvn test

# Executar classe específica
mvn test -Dtest=ProductServiceTest

# Executar método específico
mvn test -Dtest=ProductServiceTest#addProductShouldAddProductSuccessfully

# Com cobertura de código
mvn clean test jacoco:report
```

### Via IDE (IntelliJ IDEA)

- Clique com botão direito na classe de teste → Run 'ProductServiceTest'
- Clique no ícone verde ao lado do método → Run teste específico
- Ctrl+Shift+F10 (Windows/Linux) ou Cmd+Shift+R (Mac)

---

## 📚 Recursos Adicionais

### Documentação Oficial
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)

### Livros Recomendados
- "Unit Testing Principles, Practices, and Patterns" - Vladimir Khorikov
- "Test Driven Development: By Example" - Kent Beck
- "Growing Object-Oriented Software, Guided by Tests" - Steve Freeman

### Ferramentas Úteis
- **JaCoCo**: Cobertura de código
- **AssertJ**: Asserções fluentes mais legíveis
- **MockServer**: Mock de APIs REST
- **TestContainers**: Containers Docker para testes de integração

---

## 🎓 Resumo dos Conceitos-Chave

| Conceito | Descrição | Exemplo |
|----------|-----------|---------|
| **Mock** | Objeto falso que simula comportamento | `@Mock ProductRepository` |
| **Stub** | Definir comportamento do mock | `when(...).thenReturn(...)` |
| **Spy** | Mock parcial com comportamento real | `@Spy ProductService` |
| **Verify** | Verificar se método foi chamado | `verify(mock).method()` |
| **Assert** | Verificar resultado esperado | `assertEquals(expected, actual)` |
| **AAA** | Arrange-Act-Assert (padrão de teste) | Organização do código de teste |

---

## ✅ Checklist de Teste de Qualidade

- [ ] Testes têm nomes descritivos
- [ ] Seguem o padrão AAA (Arrange-Act-Assert)
- [ ] São independentes (não compartilham estado)
- [ ] São rápidos (< 100ms cada)
- [ ] Testam um comportamento por teste
- [ ] Usam mocks para isolar dependências
- [ ] Verificam exceções esperadas
- [ ] Têm mensagens de erro claras
- [ ] Cobrem casos felizes e infelizes
- [ ] Não testam código de framework

---

## 🏁 Conclusão

Testes unitários com JUnit 5 e Mockito são ferramentas essenciais para desenvolver software de qualidade. Eles garantem que seu código funciona conforme esperado, facilitam refatoração e servem como documentação viva.

**Lembre-se**: 
- ✅ Escrever testes leva tempo, mas economiza muito mais no longo prazo
- ✅ Teste o comportamento, não a implementação
- ✅ Mantenha os testes simples e legíveis
- ✅ Testes são código de produção - mantenha-os limpos!

---

**Autor**: Guia baseado em boas práticas e exemplos do projeto unit-test  
**Última Atualização**: 2025  
**Versão**: 1.0

