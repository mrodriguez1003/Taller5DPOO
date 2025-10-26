package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Files;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class PedidoTest {
    
    private Pedido pedido;
    private ProductoMenu hamburguesa;
    private ProductoMenu papas;
    private ProductoMenu gaseosa;
    
    @BeforeEach
    void setUp() {
        pedido = new Pedido("Juan Perez", "Calle 123");
        hamburguesa = new ProductoMenu("Hamburguesa", 10000);
        papas = new ProductoMenu("Papas", 5000);
        gaseosa = new ProductoMenu("Gaseosa", 3000);
    }
    
    @AfterEach
    void tearDown() throws Exception {
        pedido = null;
        hamburguesa = null;
        papas = null;
        gaseosa = null;
    }
    
    
    @Test
    void testCrearPedido(){
        assertEquals("Juan Perez", pedido.getNombreCliente(), "El nombre del cliente no es correcto");
        assertEquals("Calle 123", pedido.getDireccionCliente(), "La dirección del cliente no es correcta");
    }
    
    @Test
    void testGetIdPedido() {
        pedido.getIdPedido();
    }
    
    @Test
    void testGetNombreCliente() {
        assertEquals("Juan Perez", pedido.getNombreCliente());
    }
    
    @Test
    void testGetDireccionCliente() {
        assertEquals("Calle 123", pedido.getDireccionCliente(), "La dirección del cliente no es correcta");
    }
    
    @Test
    void testAgregarProducto() {
        pedido.agregarProducto(hamburguesa);
        assertEquals(11900, pedido.getPrecioTotalPedido());
    }
    
    @Test
    void testPrecioTotalPedidoSinProductos() {
        assertEquals(0, pedido.getPrecioTotalPedido());
    }
    
    
    @Test
    void testPrecioTotal() {
        pedido.agregarProducto(hamburguesa);
        pedido.agregarProducto(papas);
        pedido.agregarProducto(gaseosa);
        assertEquals(21420, pedido.getPrecioTotalPedido());
    }
    
    @Test
    void testPrecioNetoPedidoVacio() {
        String factura = pedido.generarTextoFactura();
        assertTrue(factura.contains("Precio Neto:  0"), "El precio neto de un pedido vacio debe ser 0");
    }

    @Test
    void testPrecioNeto() {
        pedido.agregarProducto(hamburguesa);
        pedido.agregarProducto(papas);
        pedido.agregarProducto(gaseosa);
        
        String factura = pedido.generarTextoFactura();
        assertTrue(factura.contains("Precio Neto:  18000"), "El precio neto debe ser la suma de los productos");
    }
    
    @Test
    void testPrecioIVAPedidoVacio() {
        assertEquals(0, pedido.getPrecioTotalPedido(), "El precio total de un pedido vacio debe ser 0");
    }

    @Test
    void testPrecioIVAPedidoMultiplesProductos() {
        pedido.agregarProducto(hamburguesa);
        pedido.agregarProducto(papas);
        pedido.agregarProducto(gaseosa);
        
        int precioNeto = 18000;
        int ivaEsperado = (int)(precioNeto * 0.19);
        int totalEsperado = precioNeto + ivaEsperado;
        
        assertEquals(21420, pedido.getPrecioTotalPedido(), "El precio total debe incluir el IVA correctamente");
    }
  
    @Test
    void testGenerarTextoFacturaSinProductos() {
        String factura = pedido.generarTextoFactura();
        
        assertTrue(factura.contains("Cliente: Juan Perez"), "La factura debe contener el nombre del cliente");
        assertTrue(factura.contains("Dirección: Calle 123"), "La factura debe contener la direccion");
        assertTrue(factura.contains("Precio Neto:"), "La factura debe contener el precio neto");
        assertTrue(factura.contains("IVA:"), "La factura debe contener el IVA");
        assertTrue(factura.contains("Precio Total:"), "La factura debe contener el precio total");
        assertTrue(factura.contains("\n"), "La factura debe contener saltos de linea");
    }
    
    @Test
    void testGenerarTextoFactura() {
        pedido.agregarProducto(hamburguesa);
        pedido.agregarProducto(papas);
        
        String factura = pedido.generarTextoFactura();
        
        assertTrue(factura.contains("Juan Perez"), "La factura debe contener el nombre del cliente");
        assertTrue(factura.contains("Calle 123"), "La factura debe contener la direccion");
        assertTrue(factura.contains("15000"), "La factura debe contener el precio neto");
        assertTrue(factura.contains("2850"), "La factura debe contener el IVA");
        assertTrue(factura.contains("17850"), "La factura debe contener el precio total");
    }
    
    @Test
    void testGuardarFactura() throws Exception {
        pedido.agregarProducto(hamburguesa);
        pedido.agregarProducto(papas);
        
        File archivo = new File("test_factura.txt");
        pedido.guardarFactura(archivo);
        
        assertTrue(archivo.exists(), "El archivo debe existir");
        
        String contenido = new String(Files.readAllBytes(archivo.toPath()));
        assertTrue(contenido.contains("Juan Perez"), "El archivo debe contener el contenido de la factura");
        assertTrue(contenido.contains("Hamburguesa"), "El archivo debe contener los productos");
        assertTrue(contenido.contains("Papas"), "El archivo debe contener todos los productos");
        
        archivo.delete();
    }
    
}