package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ProductoAjustadoTest {
    
    private ProductoMenu productoBase;
    private Ingrediente queso;
    private Ingrediente tocineta;
    private Ingrediente tomate;
    private Ingrediente cebolla;
    
    @BeforeEach
    public void setUp() {
        productoBase = new ProductoMenu("Hamburguesa sencilla", 15000);
        queso = new Ingrediente("Queso", 2000);
        tocineta = new Ingrediente("Tocineta", 3000);
        tomate = new Ingrediente("Tomate", 1000);
        cebolla = new Ingrediente("Cebolla", 500);
    }
    
    @AfterEach
    void tearDown() throws Exception {
        productoBase = null;
        queso = null;
        tocineta = null;
        tomate = null;
        cebolla = null;
    }

    @Test
    void testGetNombre() {
        ProductoAjustado productoAjustado = new ProductoAjustado(productoBase);
        assertEquals("Hamburguesa sencilla", productoAjustado.getNombre());
    }
    
    @Test
    void testGetPrecioSinModificaciones() {
        ProductoAjustado productoAjustado = new ProductoAjustado(productoBase);
        assertEquals(15000, productoAjustado.getPrecio());
    }
    
    @Test
    void testGetPrecioConUnIngrediente() {
        ProductoAjustado productoAjustado = new ProductoAjustado(productoBase);
        productoAjustado.agregarIngrediente(queso);
        assertEquals(17000, productoAjustado.getPrecio());
    }
    
    @Test
    void testGetPrecioConVariosIngredientes() {
        ProductoAjustado productoAjustado = new ProductoAjustado(productoBase);
        productoAjustado.agregarIngrediente(queso);
        productoAjustado.agregarIngrediente(tocineta);
        productoAjustado.agregarIngrediente(tomate);
        assertEquals(21000, productoAjustado.getPrecio());
    }
    
    @Test
    void testIngredienteRepetido() {
        ProductoAjustado productoAjustado = new ProductoAjustado(productoBase);
        productoAjustado.agregarIngrediente(queso);
        productoAjustado.agregarIngrediente(queso);
        assertEquals(19000, productoAjustado.getPrecio());
    }
    
    @Test
    void testEliminarNoAfectaElPrecio() {
        ProductoAjustado productoAjustado = new ProductoAjustado(productoBase);
        productoAjustado.eliminarIngrediente(tomate);
        productoAjustado.eliminarIngrediente(cebolla);
        assertEquals(15000, productoAjustado.getPrecio());
    }
    
    @Test
    void testPrecioConAgregadosYEliminados() {
        ProductoAjustado productoAjustado = new ProductoAjustado(productoBase);
        productoAjustado.agregarIngrediente(queso);
        productoAjustado.agregarIngrediente(tocineta);
        productoAjustado.eliminarIngrediente(tomate);
        assertEquals(20000, productoAjustado.getPrecio());
    }
    
    @Test
    void testGenerarTextoFacturaSinModificaciones() {
        ProductoAjustado productoAjustado = new ProductoAjustado(productoBase);
        
        String factura = productoAjustado.generarTextoFactura();
        
        assertTrue(factura.contains("15000"), "La factura debe contener el precio base");
        assertTrue(factura.contains("\n"), "La factura debe contener saltos de linea");
    }

    @Test
    void testGenerarTextoFacturaConAgregados() {
        ProductoAjustado productoAjustado = new ProductoAjustado(productoBase);
        productoAjustado.agregarIngrediente(queso);
        productoAjustado.agregarIngrediente(tocineta);
        
        String factura = productoAjustado.generarTextoFactura();
        
        assertTrue(factura.contains("+Queso"), "La factura debe contener los ingredientes agregados");
        assertTrue(factura.contains("+Tocineta"), "La factura debe contener todos los ingredientes agregados");
        assertTrue(factura.contains("20000"), "La factura debe contener el precio total");
        assertTrue(factura.contains("\n"), "La factura debe contener saltos de linea");
    }

    @Test
    void testGenerarTextoFacturaConEliminados() {
        ProductoAjustado productoAjustado = new ProductoAjustado(productoBase);
        productoAjustado.eliminarIngrediente(tomate);
        productoAjustado.eliminarIngrediente(cebolla);
        
        String factura = productoAjustado.generarTextoFactura();
        
        assertTrue(factura.contains("-Tomate"), "La factura debe contener los ingredientes eliminados");
        assertTrue(factura.contains("-Cebolla"), "La factura debe contener todos los ingredientes eliminados");
        assertTrue(factura.contains("15000"), "La factura debe contener el precio base");
        assertTrue(factura.contains("\n"), "La factura debe contener saltos de linea");
    }

    @Test
    void testGenerarTextoFactura() {
        ProductoAjustado productoAjustado = new ProductoAjustado(productoBase);
        productoAjustado.agregarIngrediente(queso);
        productoAjustado.eliminarIngrediente(tomate);
        
        String factura = productoAjustado.generarTextoFactura();
        
        assertTrue(factura.contains("+Queso"), "La factura debe contener ingredientes agregados");
        assertTrue(factura.contains("-Tomate"), "La factura debe contener ingredientes eliminados");
        assertTrue(factura.contains("17000"), "La factura debe contener el precio total");
        assertTrue(factura.contains("\n"), "La factura debe contener saltos de linea");
    }
}