# Fallstudie 12 / 13 - horizontale Skalierung für Luposdate
Dies ist das Repository für die [Fallstudie](http://www.uni-luebeck.de/index.php?id=1521&tx_webparser_pi1[modulid]=497) des [ITM](https://www.itm.uni-luebeck.de/) und [IFIS](http://www.ifis.uni-luebeck.de/) an der Uni Lübeck im Jahr 2012 / 2013.

Diese Fallstudie beschäftigt sich mit dem Bereich des Semantic Web im Zusammenhang mit einem verteilten System (P2P).

Die Open Source Semantic Web Datenbank [Luposdate](https://github.com/luposdate/luposdate) des IFIS wird so erweitert, dass eine horizontale Skalierung über eine beliebige Anzahl von Knoten möglich ist. Um dieses Ziel zu erreichen wird ein P2P Netzwerk als Datastore für Luposdate verwendet. Zur Implementierung des P2P Netzwerkes wird die Open Source Java Bibliothek [TomP2P](https://github.com/tomp2p/TomP2P) verwendet.

Ziel der Fallstudie ist es, SPARQL Anfragen per Lupodate an das P2P Netzwerk zu senden, wobei diese SPARQL Anfragen ggf. auch aufgeteilt und an die P2P Knoten gesendet werden können, um u.a. lokale Joins zu ermöglichen.

## Getting Started
1. Clonen des Git Repositories
    https://github.com/mariodavid/Fallstudie1213/wiki/startanleitung
2. Software ausführen unter
    cd /path/to/the/local/copy/of/fallstudie
    java -jar bin/fallstudie.jar



## Dokumentation
Die gesamte Dokumentation ist im [Wiki](https://github.com/mariodavid/Fallstudie1213/wiki) zu finden. Die Dokumentation enthält die Bereiche Aufgabenstellung, Luposdate, TomP2P und eine ausführliche Installationsanleitung.

