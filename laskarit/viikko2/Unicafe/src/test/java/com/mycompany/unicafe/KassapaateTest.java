/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.unicafe;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ville
 */
public class KassapaateTest {
    
    Kassapaate kassapaate;
    
    public KassapaateTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        kassapaate = new Kassapaate();
    }
    
    @After
    public void tearDown() {
    }

    
    @Test
    public void kassaAlussaOikein() {
        assertEquals(100000, kassapaate.kassassaRahaa());
    }
    
    @Test
    public void myyntiAlussaOikein() {
        int myynti = kassapaate.edullisiaLounaitaMyyty() + kassapaate.maukkaitaLounaitaMyyty();
        assertEquals(0, myynti);
    }
    
    @Test
    public void kateisostoOnnistuuEdullisilleTasarahalla() {
        kassapaate.syoEdullisesti(240);
        assertEquals(100240, kassapaate.kassassaRahaa());
    }
    
    @Test
    public void vaihtorahaOikeinEdullisessa() {
        int vaihtoraha = kassapaate.syoEdullisesti(500);
        assertEquals(260, vaihtoraha);
    }
    
    @Test
    public void edullistenMaaraKasvaaMyydessa() {
        kassapaate.syoEdullisesti(500);
        kassapaate.syoEdullisesti(240);
        assertEquals(2, kassapaate.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void kateisostoEiOnnistuEdullisille() {
        kassapaate.syoEdullisesti(140);
        assertEquals(100000, kassapaate.kassassaRahaa());
    }
    
    @Test
    public void vaihtorahaOikeinEdullisessaJosOstoEiOnnistu() {
        int vaihtoraha = kassapaate.syoEdullisesti(200);
        assertEquals(200, vaihtoraha);
    }
    
    @Test
    public void edullistenMaaraEiKasvaJosMyyntiEpaonnistuu() {
        kassapaate.syoEdullisesti(100);
        kassapaate.syoEdullisesti(200);
        assertEquals(0, kassapaate.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void kateisostoOnnistuuMaukkailleTasarahalla() {
        kassapaate.syoMaukkaasti(400);
        assertEquals(100400, kassapaate.kassassaRahaa());
    }
    
    @Test
    public void vaihtorahaOikeinMaukkaassa() {
        int vaihtoraha = kassapaate.syoMaukkaasti(500);
        assertEquals(100, vaihtoraha);
    }
    
    @Test
    public void maukkaidenMaaraKasvaaMyydessa() {
        kassapaate.syoMaukkaasti(700);
        kassapaate.syoMaukkaasti(400);
        assertEquals(2, kassapaate.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void kateisostoEiOnnistuMaukkaille() {
        kassapaate.syoMaukkaasti(300);
        assertEquals(100000, kassapaate.kassassaRahaa());
    }
    
    @Test
    public void vaihtorahaOikeinMaukkaassaJosOstoEiOnnistu() {
        int vaihtoraha = kassapaate.syoMaukkaasti(390);
        assertEquals(390, vaihtoraha);
    }
    
    @Test
    public void maukkaidenMaaraEiKasvaJosMyyntiEpaonnistuu() {
        kassapaate.syoMaukkaasti(100);
        kassapaate.syoMaukkaasti(200);
        kassapaate.syoMaukkaasti(390);
        assertEquals(0, kassapaate.maukkaitaLounaitaMyyty());
    }
    
    
    @Test
    public void korttiostoOnnistuuEdullisilleTasarahalla() {
        Maksukortti kortti = new Maksukortti(240);
        kassapaate.syoEdullisesti(kortti);
        assertEquals(0, kortti.saldo());
    }
    
    @Test
    public void veloitusKortillaOikeinEdullisessa() {
        Maksukortti kortti = new Maksukortti(500);
        kassapaate.syoEdullisesti(kortti);
        assertEquals(260, kortti.saldo());
    }
    
    @Test
    public void korttiPalauttaaTrueEdullisille() {
        Maksukortti kortti = new Maksukortti(500);
        boolean vastaus = kassapaate.syoEdullisesti(kortti);
        assertEquals(true, vastaus);
    }
    
    @Test
    public void edullistenMaaraKasvaaMyydessaKortilla() {
        Maksukortti kortti = new Maksukortti(1000);
        kassapaate.syoEdullisesti(kortti);
        kassapaate.syoEdullisesti(kortti);
        assertEquals(2, kassapaate.edullisiaLounaitaMyyty());
    }
    
   @Test
    public void veloitusKortillaEiOnnistuEdullisessa() {
        Maksukortti kortti = new Maksukortti(200);
        kassapaate.syoEdullisesti(kortti);
        assertEquals(200, kortti.saldo());
    }
    
    @Test
    public void korttiPalauttaaFalseEdullisille() {
        Maksukortti kortti = new Maksukortti(100);
        boolean vastaus = kassapaate.syoEdullisesti(kortti);
        assertEquals(false, vastaus);
    }
    
    @Test
    public void edullistenMaaraEiKasvaMyydessaKortilla() {
        Maksukortti kortti = new Maksukortti(200);
        kassapaate.syoEdullisesti(kortti);
        kassapaate.syoEdullisesti(kortti);
        assertEquals(0, kassapaate.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void korttiostoOnnistuuMaukkailleTasarahalla() {
        Maksukortti kortti = new Maksukortti(400);
        kassapaate.syoMaukkaasti(kortti);
        assertEquals(0, kortti.saldo());
    }
    
    @Test
    public void veloitusKortillaOikeinMaukkaassa() {
        Maksukortti kortti = new Maksukortti(500);
        kassapaate.syoMaukkaasti(kortti);
        assertEquals(100, kortti.saldo());
    }
    
    @Test
    public void korttiPalauttaaTrueMaukkaille() {
        Maksukortti kortti = new Maksukortti(500);
        boolean vastaus = kassapaate.syoMaukkaasti(kortti);
        assertEquals(true, vastaus);
    }
    
    @Test
    public void maukkaidenMaaraKasvaaMyydessaKortilla() {
        Maksukortti kortti = new Maksukortti(1000);
        kassapaate.syoMaukkaasti(kortti);
        kassapaate.syoMaukkaasti(kortti);
        assertEquals(2, kassapaate.maukkaitaLounaitaMyyty());
    }
    
   @Test
    public void veloitusKortillaEiOnnistuMaukkaassa() {
        Maksukortti kortti = new Maksukortti(300);
        kassapaate.syoMaukkaasti(kortti);
        assertEquals(300, kortti.saldo());
    }
    
    @Test
    public void korttiPalauttaaFalseMaukkaille() {
        Maksukortti kortti = new Maksukortti(350);
        boolean vastaus = kassapaate.syoMaukkaasti(kortti);
        assertEquals(false, vastaus);
    }
    
    @Test
    public void maukkaidenMaaraEiKasvaMyydessaKortilla() {
        Maksukortti kortti = new Maksukortti(300);
        kassapaate.syoMaukkaasti(kortti);
        kassapaate.syoMaukkaasti(kortti);
        assertEquals(0, kassapaate.edullisiaLounaitaMyyty());
    }

    @Test
    public void korttiostoEiVaikutaRahakassaan() {
        Maksukortti kortti = new Maksukortti(1000);
        kassapaate.syoMaukkaasti(kortti);
        kassapaate.syoEdullisesti(kortti);
        assertEquals(100000, kassapaate.kassassaRahaa());
    }
    
    @Test
    public void rahanLataaminenLisaaRahaaKassaanjaSaldoaKortille() {
        Maksukortti kortti = new Maksukortti(1000);
        kassapaate.lataaRahaaKortille(kortti,500);
        assertEquals(100500, kassapaate.kassassaRahaa());
        assertEquals(1500, kortti.saldo());
    }
    
    @Test
    public void negatiivisenRahanLataaminenEiToimi() {
        Maksukortti kortti = new Maksukortti(1000);
        kassapaate.lataaRahaaKortille(kortti,-100);
        assertEquals(100000, kassapaate.kassassaRahaa());
        assertEquals(1000, kortti.saldo());
    }
    
}
