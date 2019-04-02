# Ohjelmointiprojekti - Power Management

## Dokumentaatio
* [Vaatimusmäärittely](https://github.com/vmarttil/ot-harjoitustyo/blob/master/documentation/vaatimusmaarittely.md)
* [Tuntikirjanpito](https://github.com/vmarttil/ot-harjoitustyo/blob/master/documentation/tuntikirjanpito.md)

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
