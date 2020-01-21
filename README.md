# Power Management App
Sovelluksen tarkoituksena on mallintaa a pelillisesti avaruusaluksen virranhallintajärjestelmän toimintaa. Sovellus on tarkoitettu käytettäväksi yhdessä Empty Epsilon Space Ship Simulator (http://daid.github.io/EmptyEpsilon/) -pelisovelluksen kanssa ja se on suunniteltu kommunikoimaan jatkossa tämän sovelluksen kanssa sen tarjoaman http-rajapinnan välityksellä. Sovelluksen toiminta perustuu aluksen reaktorin ja muun järjestelmän välisen virranhallintajärjestelmän laskennalliseen simulaatioon, jossa käyttäjän/pelaajan tavoitteena on tasapainottaa sekä satunnaisesti että EE-sovelluksesta saatavan palautteen perusteella vaihtelevaa virransyöttöä siten että tehohäviö reaktorista aluksen järjestelmiin on mahdollisimman pieni eivätkä virransyötön liialliset vaihtelut aiheuta vauriota aluksen järjestelmille. Sovellukseen on tarkoitus toteuttaa rajapinta myös myöhemmin toteutettavaan reaktorin toimintaa mallintavaan sovellukseen sekä MIDI-standardia noudattavaan fyysiseen kontrolleriin, joka mahdollistaa sovelluksen käyttöliittymän käyttämisen fyysisten liukusäätimien, enkoodereiden ja painikkeiden avulla.

## Dokumentaatio
* [Vaatimusmäärittely](https://github.com/vmarttil/ot-harjoitustyo/blob/master/documentation/vaatimusmaarittely.md)
* [Tuntikirjanpito](https://github.com/vmarttil/ot-harjoitustyo/blob/master/documentation/tuntikirjanpito.md)
* [Arkkitehtuuri](https://github.com/vmarttil/ot-harjoitustyo/blob/master/documentation/arkkitehtuuri.md)
* [Käyttöohje](https://github.com/vmarttil/ot-harjoitustyo/blob/master/documentation/kayttoohje.md)
* [Testausdokumentti](https://github.com/vmarttil/ot-harjoitustyo/blob/master/documentation/testausdokumentti.md)

## Komentorivitoiminnot

### Testaus

Testit suoritetaan komennolla

```
mvn test
```

Testikattavuusraportti luodaan komennolla

```
mvn jacoco:report
```

Kattavuusraporttia voi tarkastella avaamalla selaimella tiedosto _target/site/jacoco/index.html_

### Suoritettavan jarin generointi

Komento

```
mvn package
```

generoi hakemistoon _target_ suoritettavan jar-tiedoston _PowerManagement-1.0-SNAPSHOT.jar_
