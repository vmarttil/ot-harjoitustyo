# Testausdokumentti

Sovellusta on testattu sekä automatisoiduin JUnit-testein että manuaalisesti tapahtunein järjestelmä- ja käyttöliittymätason testein.

## Yksikkö- ja integraatiotestaus

### Sovelluslogiikka

Automatisoitujen testien ytimen moudostavat sovelluslogiikkaa, eli pakkauksen domain-luokkia testaavat yksikkö- ja integraatiotestit, 
jotka pyrkivät simuloimaan ohjelman toiminnalle tyypillisiä tapahutmaketjuja. Näistä testeistä pienempiä ja yksinkertaisempie luokkia 
_Fluctuator_, _Oscillator_ ja _Breaker_ testaavat testit testaavat lähinnä näiden luokkien sisäistä toiminnallisuutta, mutta 
monimutkaisempien luokkien testit testaavat myös eri luokkien yhteistoimintaa varsin kattavasti:

* [ManagerTest](https://github.com/vmarttil/ot-harjoitustyo/blob/master/PowerManagement/src/test/java/domainTest/ManagerTest.java)
* [PowerChannelTest](https://github.com/vmarttil/ot-harjoitustyo/blob/master/PowerManagement/src/test/java/domainTest/PowerChannelTest.java)
* [PowerLineTest](https://github.com/vmarttil/ot-harjoitustyo/blob/master/PowerManagement/src/test/java/domainTest/PowerLineTest.java)
* [BreakerTest](https://github.com/vmarttil/ot-harjoitustyo/blob/master/PowerManagement/src/test/java/domainTest/BreakerTest.java)
* [OscillatorTest](https://github.com/vmarttil/ot-harjoitustyo/blob/master/PowerManagement/src/test/java/domainTest/OscillatorTest.java)
* [FluctuatorTest](https://github.com/vmarttil/ot-harjoitustyo/blob/master/PowerManagement/src/test/java/domainTest/FluctuatorTest.java)

Koska lokitietojen keruu tapahtuu automaattisesti ohjelman suorituksen yhteydessä, myös _JsonLogDao_-luokan toiminta tulee hyvin kattavasti 
testattua muiden luokkien yhteydessä, eikä sille tämän vuoksi ole kirjoitettu omia testejä.

### Testauskattavuus

Käyttöliittymäkerrosta lukuunottamatta sovelluksen testauksen rivikattavuus on 78% ja haarautumakattavuus 73%

<img src="https://github.com/vmarttil/ot-harjoitustyo/blob/master/documentation/pictures/Testauskattavuus.png" width="800">

## Järjestelmätestaus

Sovelluksen järjestelmä- ja käyttöliittymätestaus on suoritettu manuaalisesti kokeilemalla mahdollisimman kattavasti läpi erilaisia 
sovelluksen mahdollistamia tilanteita ja olosuhteita.

### Asennus ja konfigurointi

Sovellus ei tarvitse varsinaista asennusta eikä konfiguraatiotiedostoja, ja sitä on testattu ajamalla valmis jar-tiedosto 
OSX-ympäristössä. Koska sovelluksen kontrollit eivät mahdollista käyttäjältä virheellisiä syötteitä, näitä ei ole erikseen 
testattu.

### Toiminnallisuudet

Kaikki [vaatimusmäärittelyssä](https://github.com/vmarttil/ot-harjoitustyo/blob/master/documentation/vaatimusmaarittely.md) 
esitetyt tämänhetkiseen versioon sisältyvät toiminnallisuudet ja käyttöohjeessa kuvatut käyttöliittymäelementit on 
kokeiltu läpi.

## Sovellukseen jääneet laatuongelmat

Toiminnallisuuden rajallisuuden lisäksi sovelluksessa ei ole havaittu varsinaisia laatuongelmia. Tilanteissa joissa 
virranhallintajärjestelmä ajetaan hyvin epävakaaseen tilaan, sovellus saattaa vaatia melko paljon suoritintehoa. Vaikka 
ohjelmaa ei olekaan saatu kaatumaan suoritintehon puutteen vuoksi, tämä on merkittävä jatkokehityksen kohde.
