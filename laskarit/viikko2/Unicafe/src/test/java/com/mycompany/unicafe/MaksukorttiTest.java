package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(1000);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti!=null);      
    }
    
    @Test
    public void saldoAlussaOikein() {
        assertEquals(1000, kortti.saldo());
    }
    
    @Test
    public void rahanLataaminenToimii() {
        kortti.lataaRahaa(2000);
        assertEquals(3000, kortti.saldo());
    }
    
    @Test
    public void rahanOttaminenVahentaaSaldoaOikein() {
        kortti.otaRahaa(500);
        assertEquals(500, kortti.saldo());
    }
    
    @Test
    public void liikaRahanOttaminenEiOnnistu() {
        kortti.otaRahaa(1500);
        assertEquals(1000, kortti.saldo());
    }
    
    @Test
    public void trueJosRahatRiittavat() {
        boolean vastaus = kortti.otaRahaa(500);
        assertEquals(true, vastaus);
    }
    
    @Test
    public void falseJosRahatEivatRiita() {
        boolean vastaus = kortti.otaRahaa(2000);
        assertEquals(false, vastaus);
    }
    
    @Test
    public void saldonTulostaminenToimii() {
        assertEquals("saldo: 10.0", kortti.toString());
    }
    
}
