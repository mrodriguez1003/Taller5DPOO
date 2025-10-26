package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ComboTest {
    
    private ProductoMenu hamburguesa;
    private ProductoMenu papas;
    private ProductoMenu gaseosa;
    private ArrayList<ProductoMenu> items;
    
    @BeforeEach
    public void setUp() {
        hamburguesa = new ProductoMenu("Hamburguesa sencilla", 10000);
        papas = new ProductoMenu("Papas medianas", 5000);
        gaseosa = new ProductoMenu("Gaseosa", 3000);
        items = new ArrayList<ProductoMenu>();
    }
    
    @AfterEach
    void tearDown() throws Exception {
        hamburguesa = null;
        papas = null;
        gaseosa = null;
        items = null;
    }
    
    @Test
    void testGetNombre() {
        items.add(hamburguesa);
        Combo combo = new Combo("Combo especial", 0.07, items);
        assertEquals("Combo especial", combo.getNombre());
    }
    
    @Test
    void testPrecioSinDescuento() {
        items.add(hamburguesa);
        items.add(papas);
        Combo combo = new Combo("Combo1", 0.0, items);
        assertEquals(15000, combo.getPrecio());
    }
    
    @Test
    void testPrecioConDescuento() {
        items.add(hamburguesa);
        items.add(papas);
        Combo combo = new Combo("Combo especial", 0.07, items);
        assertEquals(13950, combo.getPrecio());
    }
    
    @Test
    void testPrecioVariosProductos() {
        items.add(hamburguesa);
        items.add(papas);
        items.add(gaseosa);
        Combo combo = new Combo("Combo completo", 0.15, items);
        assertEquals(15300, combo.getPrecio());
    }
    
    @Test
    void testGenerarTextoFactura() {
        items.add(hamburguesa);
        items.add(papas);
        Combo combo = new Combo("Combo1", 0.1, items);
        
        String facturaCombo = combo.generarTextoFactura();
        
        assertTrue(facturaCombo.contains("Combo Combo1"), "La factura debe contener el nombre del combo");
        assertTrue(facturaCombo.contains("Descuento: 0.1"), "La factura debe contener el descuento");
        assertTrue(facturaCombo.contains("13500"), "La factura debe contener el precio");
        assertTrue(facturaCombo.contains("\n"), "La factura debe contener saltos de linea");
    }
}