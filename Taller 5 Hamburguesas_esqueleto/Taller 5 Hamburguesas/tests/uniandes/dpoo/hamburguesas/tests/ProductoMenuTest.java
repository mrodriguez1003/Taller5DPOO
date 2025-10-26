package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ProductoMenuTest
{
    private ProductoMenu hamburguesa;
    private ProductoMenu papas;
    private ProductoMenu bebida;

    @BeforeEach
    void setUp( ) throws Exception
    {
        hamburguesa = new ProductoMenu( "corral queso", 16000 );
        papas = new ProductoMenu( "papas medianas", 5500 );
        bebida = new ProductoMenu( "gaseosa", 5000 );
    }

    @AfterEach
    void tearDown( ) throws Exception
    {
        hamburguesa = null;
        papas = null;
        bebida = null;
    }

    @Test
    void testGetNombre( )
    {
        assertEquals( "corral queso", hamburguesa.getNombre( ), "El nombre de la hamburguesa no es el esperado" );
        assertEquals( "papas medianas", papas.getNombre( ),"El nombre de las papas no es el esperado" );
        assertEquals( "gaseosa", bebida.getNombre( ),"El nombre de la bebida no es el esperado" );
    }

    @Test
    void testGetPrecio( )
    {
        assertEquals( 16000, hamburguesa.getPrecio( ), "El precio de la hamburguesa no es el esperado" );
        assertEquals( 5500, papas.getPrecio( ),"El precio de las papas no es el esperado" );
        assertEquals( 5000, bebida.getPrecio( ), "El precio de la bebida no es el esperado" );
    }

    @Test
    void testGenerarTextoFactura( )
    {
        String facturaHamburguesa = hamburguesa.generarTextoFactura( );
        
        assertTrue( facturaHamburguesa.contains( "corral queso" ), "La factura debe contener el nombre del producto");
        assertTrue( facturaHamburguesa.contains( "16000" ),"La factura debe contener el precio del producto" );
        assertTrue( facturaHamburguesa.contains( "\n" ), "La factura debe contener saltos de línea" );
    }

}
