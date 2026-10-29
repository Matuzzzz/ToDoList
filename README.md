# Jednoduchý zoznam úloh (Java)
Malý konzolový program na ukladanie úloh.

## Funkcie
-Pridanie úlohy.

-Zobrazenie všetkých úloh.

-Zobrazenie úloh, ktoré nie sú dokončené.

-Označenie úlohy ako dokončenej.

-Ukladanie úloh do súboru `tasks.csv`, takže zostanú zachované aj pri ďalšom spustení.


## Spustenie
Budete potrebovať Javu 17 a Maven.

Otvorte tento priečinok v IntelliJ IDEA alebo spustite nasledujúce príkazy v priečinku projektu:

```Bash
mvn test
mvn package
java -jar target/simple-todo-1.0.jar
```

## Hlavné triedy
`Task` – jedna úloha; uchováva svoje dáta ako súkromné (privátne).

`TaskManager` – pridáva, vyhľadáva a filtruje úlohy.

`TaskStorage` – rozhranie (interface) pre ukladanie a načítavanie úloh.

`CsvTaskStorage` – číta a zapisuje do súboru tasks.csv.

`Main` – zobrazuje menu a spracováva vstup od užívateľa.
