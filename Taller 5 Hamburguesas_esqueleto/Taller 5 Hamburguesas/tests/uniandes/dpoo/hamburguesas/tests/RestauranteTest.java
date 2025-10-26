package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import uniandes.dpoo.hamburguesas.excepciones.HamburguesaException;
import uniandes.dpoo.hamburguesas.excepciones.IngredienteRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.NoHayPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoFaltanteException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.YaHayUnPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;
import uniandes.dpoo.hamburguesas.mundo.Restaurante;

public class RestauranteTest {
    
    private Restaurante restaurante;
    
    @TempDir
    File tempDir;
    @TempDir
    File tempDir2;
    @TempDir
    File tempDir3;
    
    @BeforeEach
    void setUp() {
        restaurante = new Restaurante();
    }
    
    @AfterEach
    void tearDown() {
        restaurante = null;
    }
    
    @Test
    void testIniciarPedido() throws YaHayUnPedidoEnCursoException {
        restaurante.iniciarPedido("Juan", "Calle 123");
        assertNotNull(restaurante.getPedidoEnCurso());
        assertEquals("Juan", restaurante.getPedidoEnCurso().getNombreCliente());
    }
    
    @Test
    void testIniciarPedidoCuandoYaHayUno() throws YaHayUnPedidoEnCursoException {
        restaurante.iniciarPedido("Juan", "Calle 123");
        assertThrows(YaHayUnPedidoEnCursoException.class, () -> {
            restaurante.iniciarPedido("Maria", "Calle 456");
        });
    }
    
    @Test
    void testCerrarYGuardarPedido() throws YaHayUnPedidoEnCursoException, NoHayPedidoEnCursoException, IOException {
        File carpeta = new File("./facturas/");
        carpeta.mkdirs();
        
        restaurante.iniciarPedido("Juan Perez", "Calle 123");
        ProductoMenu producto = new ProductoMenu("Hamburguesa", 10000);
        restaurante.getPedidoEnCurso().agregarProducto(producto);
        
        int idPedido = restaurante.getPedidoEnCurso().getIdPedido();
        
        restaurante.cerrarYGuardarPedido();
        
        assertNull(restaurante.getPedidoEnCurso());
        
        File archivo = new File("./facturas/factura_" + idPedido + ".txt");
        assertTrue(archivo.exists());
        
        String contenido = Files.readString(archivo.toPath());
        assertTrue(contenido.contains("Juan Perez"));
        assertTrue(contenido.contains("Calle 123"));
        assertTrue(contenido.contains("Hamburguesa"));
        
        archivo.delete();
    }
    
    @Test
    void testCerrarYGuardarPedidoSinPedido() {
        assertThrows(NoHayPedidoEnCursoException.class, () -> {
            restaurante.cerrarYGuardarPedido();
        });
    }
    
    @Test
    void testGetPedidoEnCurso() {
        assertNull(restaurante.getPedidoEnCurso());
    }

    @Test
    void testGetPedidos() {
        assertNotNull(restaurante.getPedidos());
        assertEquals(0, restaurante.getPedidos().size());
    }

    @Test
    void testGetMenuBase() {
        assertNotNull(restaurante.getMenuBase());
        assertEquals(0, restaurante.getMenuBase().size());
    }

    @Test
    void testGetMenuCombos() {
        assertNotNull(restaurante.getMenuCombos());
        assertEquals(0, restaurante.getMenuCombos().size());
    }

    @Test
    void testGetIngredientes() {
        assertNotNull(restaurante.getIngredientes());
        assertEquals(0, restaurante.getIngredientes().size());
    }
    
    @Test
    void testCargarInformacionRestaurante() throws IOException, HamburguesaException {
        File ingredientes = new File(tempDir, "ingredientes.txt");
        FileWriter writer1 = new FileWriter(ingredientes);
        writer1.write("Lechuga;800\n");
        writer1.write("\n");
        writer1.write("Pepinillos;1200");
        writer1.close();
        
        File menu = new File(tempDir2, "menu.txt");
        FileWriter writer2 = new FileWriter(menu);
        writer2.write("Corral;15000\n");
        writer2.write("\n");
        writer2.write("Agua;2500");
        writer2.close();
        
        File combos = new File(tempDir3, "combos.txt");
        FileWriter writer3 = new FileWriter(combos);
        writer3.write("Especial;7%;Corral;Agua\n");
        writer3.write("\n");
        writer3.close();
        
        restaurante.cargarInformacionRestaurante(ingredientes, menu, combos);
        
        assertEquals(2, restaurante.getIngredientes().size());
        assertEquals(2, restaurante.getMenuBase().size());
        assertEquals(1, restaurante.getMenuCombos().size());
    }
    
    @Test
    void testCargarIngredientesRepetidos() throws Exception {
        File ingredientes = new File(tempDir, "ingredientes.txt");
        FileWriter writer = new FileWriter(ingredientes);
        writer.write("Lechuga;800\n");
        writer.write("Lechuga;900");
        writer.close();
        
        File menu = new File(tempDir2, "menu.txt");
        new FileWriter(menu).close();
        
        File combos = new File(tempDir3, "combos.txt");
        new FileWriter(combos).close();
        
        assertThrows(IngredienteRepetidoException.class, () -> {
            restaurante.cargarInformacionRestaurante(ingredientes, menu, combos);
        });
    }
    
    @Test
    void testCargarProductosRepetidos() throws Exception {
        File ingredientes = new File(tempDir, "ingredientes.txt");
        new FileWriter(ingredientes).close();
        
        File menu = new File(tempDir2, "menu.txt");
        FileWriter writer = new FileWriter(menu);
        writer.write("Perro;8000\n");
        writer.write("Perro;9000");
        writer.close();
        
        File combos = new File(tempDir3, "combos.txt");
        new FileWriter(combos).close();
        
        assertThrows(ProductoRepetidoException.class, () -> {
            restaurante.cargarInformacionRestaurante(ingredientes, menu, combos);
        });
    }
    
    @Test
    void testCargarCombosRepetidos() throws Exception {
        File ingredientes = new File(tempDir, "ingredientes.txt");
        new FileWriter(ingredientes).close();
        
        File menu = new File(tempDir2, "menu.txt");
        FileWriter writer = new FileWriter(menu);
        writer.write("Salchipapa;12000");
        writer.close();
        
        File combos = new File(tempDir3, "combos.txt");
        FileWriter writer3 = new FileWriter(combos);
        writer3.write("SuperCombo;25%;Salchipapa\n");
        writer3.write("SuperCombo;30%;Salchipapa");
        writer3.close();
        
        assertThrows(ProductoRepetidoException.class, () -> {
            restaurante.cargarInformacionRestaurante(ingredientes, menu, combos);
        });
    }
    
    @Test
    void testCargarComboConProductoFaltante() throws Exception {
        File ingredientes = new File(tempDir, "ingredientes.txt");
        new FileWriter(ingredientes).close();
        
        File menu = new File(tempDir2, "menu.txt");
        FileWriter writer = new FileWriter(menu);
        writer.write("Jugo;4000");
        writer.close();
        
        File combos = new File(tempDir3, "combos.txt");
        FileWriter writer3 = new FileWriter(combos);
        writer3.write("MegaCombo;20%;Pizza;Jugo");
        writer3.close();
        
        assertThrows(ProductoFaltanteException.class, () -> {
            restaurante.cargarInformacionRestaurante(ingredientes, menu, combos);
        });
    }
    
    @Test
    void testCargarComboConMultiplesProductos() throws Exception {
        File ingredientes = new File(tempDir, "ingredientes.txt");
        new FileWriter(ingredientes).close();
        
        File menu = new File(tempDir2, "menu.txt");
        FileWriter writer = new FileWriter(menu);
        writer.write("Corral;15000\n");
        writer.write("Papas;5000\n");
        writer.write("Gaseosa;3000\n");
        writer.write("Postre;4000");
        writer.close();
        
        File combos = new File(tempDir3, "combos.txt");
        FileWriter writer3 = new FileWriter(combos);
        writer3.write("ComboFamiliar;18%;Corral;Papas;Gaseosa;Postre");
        writer3.close();
        
        restaurante.cargarInformacionRestaurante(ingredientes, menu, combos);
        
        assertEquals(4, restaurante.getMenuBase().size());
        assertEquals(1, restaurante.getMenuCombos().size());
    }

    @Test
    void testCargarVariosIngredientes() throws Exception {
        File ingredientes = new File(tempDir, "ingredientes.txt");
        FileWriter writer = new FileWriter(ingredientes);
        writer.write("Lechuga;800\n");
        writer.write("Tomate;1000\n");
        writer.write("Cebolla;600\n");
        writer.write("Pepinillos;1200\n");
        writer.write("Queso;2000");
        writer.close();
        
        File menu = new File(tempDir2, "menu.txt");
        new FileWriter(menu).close();
        
        File combos = new File(tempDir3, "combos.txt");
        new FileWriter(combos).close();
        
        restaurante.cargarInformacionRestaurante(ingredientes, menu, combos);
        
        assertEquals(5, restaurante.getIngredientes().size());
    }

    @Test
    void testCargarVariosProductos() throws Exception {
        File ingredientes = new File(tempDir, "ingredientes.txt");
        new FileWriter(ingredientes).close();
        
        File menu = new File(tempDir2, "menu.txt");
        FileWriter writer = new FileWriter(menu);
        writer.write("Corral;15000\n");
        writer.write("Especial;18000\n");
        writer.write("Papas Grandes;6000\n");
        writer.write("Papas Medianas;4500\n");
        writer.write("Gaseosa;3000");
        writer.close();
        
        File combos = new File(tempDir3, "combos.txt");
        new FileWriter(combos).close();
        
        restaurante.cargarInformacionRestaurante(ingredientes, menu, combos);
        
        assertEquals(5, restaurante.getMenuBase().size());
    }

    @Test
    void testCargarVariosCombos() throws Exception {
        File ingredientes = new File(tempDir, "ingredientes.txt");
        new FileWriter(ingredientes).close();
        
        File menu = new File(tempDir2, "menu.txt");
        FileWriter writer = new FileWriter(menu);
        writer.write("Corral;15000\n");
        writer.write("Papas;5000\n");
        writer.write("Gaseosa;3000");
        writer.close();
        
        File combos = new File(tempDir3, "combos.txt");
        FileWriter writer3 = new FileWriter(combos);
        writer3.write("Combo1;10%;Corral;Papas\n");
        writer3.write("Combo2;15%;Corral;Gaseosa\n");
        writer3.write("Combo3;20%;Papas;Gaseosa\n");
        writer3.write("ComboCompleto;25%;Corral;Papas;Gaseosa");
        writer3.close();
        
        restaurante.cargarInformacionRestaurante(ingredientes, menu, combos);
        
        assertEquals(4, restaurante.getMenuCombos().size());
    }
    
    @Test
    void testCargarYVerificarDatos() throws Exception {
        File ingredientes = new File(tempDir, "ingredientes.txt");
        FileWriter writer = new FileWriter(ingredientes);
        writer.write("Mostaza;700\n");
        writer.write("Mayonesa;900");
        writer.close();
        
        File menu = new File(tempDir2, "menu.txt");
        FileWriter writer2 = new FileWriter(menu);
        writer2.write("Whopper;20000\n");
        writer2.write("Nuggets;8000");
        writer2.close();
        
        File combos = new File(tempDir3, "combos.txt");
        FileWriter writer3 = new FileWriter(combos);
        writer3.write("ComboKids;12%;Nuggets");
        writer3.close();
        
        restaurante.cargarInformacionRestaurante(ingredientes, menu, combos);
        
        assertEquals(2, restaurante.getIngredientes().size());
        assertEquals("Mostaza", restaurante.getIngredientes().get(0).getNombre());
        assertEquals(700, restaurante.getIngredientes().get(0).getCostoAdicional());
        
        assertEquals(2, restaurante.getMenuBase().size());
        assertEquals("Whopper", restaurante.getMenuBase().get(0).getNombre());
        assertEquals(20000, restaurante.getMenuBase().get(0).getPrecio());
        
        assertEquals(1, restaurante.getMenuCombos().size());
        assertEquals("ComboKids", restaurante.getMenuCombos().get(0).getNombre());
    }
}