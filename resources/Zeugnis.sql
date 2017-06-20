CREATE TABLE "ZEUGNIS".ZEUGNIS
(
		ID_ZEUGNIS INTEGER not null primary key,
		ID_SCHUELER INTEGER,
                NOTE_ARBEIT INTEGER,
                NOTE_SOZIAL INTEGER,
		FEHLTAGE INTEGER,
		FEHLTAGEOHNE INTEGER,
		ENTWICKLUNG VARCHAR(500),
		BEMERKUNG VARCHAR(200),
		HALBJAHR INTEGER,
		SCHULJAHR INTEGER
);

CREATE TABLE "ZEUGNIS".SCHUELER
(
		ID_SCHUELER INTEGER not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
		NAME VARCHAR(30),
		VORNAME VARCHAR(30),
		GEBDATUM DATE,
		GEBORT VARCHAR(30),
		KLASSE VARCHAR(10),
		SCHULJAHR INTEGER,
                CONSTRAINT pk PRIMARY KEY (ID_SCHUELER)
);

CREATE TABLE "ZEUGNIS".KRITERIUMSLISTE
(
		ID_KRITERIUMSLISTE INTEGER,
		ID_KRITERIUM INTEGER,
		BEWERTUNG INTEGER

);

CREATE TABLE "ZEUGNIS".KRITERIUM
(
	ID_KRITERIUM INTEGER not null primary key,
	ID_LERNBEREICH INTEGER,
	KRITERIUMTEXT VARCHAR(200),
	SCHULJAHR INTEGER
);

CREATE TABLE "ZEUGNIS".LERNBEREICH
(
	ID_LERNBEREICH INTEGER not null primary key,
	LERNBEREICH VARCHAR(100),
	KLASSENSTUFE INTEGER,
	NOTENBEREICH INTEGER,
	SCHULJAHR INTEGER
);

--ALTER TABLE "ZEUGNIS".KRITERIUMSLISTE ADD FOREIGN KEY(ID_KRITERIUMSLISTE) REFERENCES "ZEUGNIS".ZEUGNIS(ID_ZEUGNIS);
--ALTER TABLE "ZEUGNIS".KRITERIUMSLISTE ADD FOREIGN KEY(ID_KRITERIUM) REFERENCES "ZEUGNIS".KRITERIUM(ID_KRITERIUM); 
--ALTER TABLE "ZEUGNIS".ZEUGNIS ADD FOREIGN KEY(ID_SCHUELER) REFERENCES "ZEUGNIS".SCHUELER(ID_SCHUELER);
--ALTER TABLE "ZEUGNIS".KRITERIUM ADD FOREIGN KEY(ID_LERNBEREICH) REFERENCES "ZEUGNIS".LERNBEREICH(ID_LERNBEREICH);

-- DB LERNBEREICH
-- Alle Jahrgangsstufe
INSERT INTO LERNBEREICH VALUES (10, 'Arbeitsverhalten',						0,3,2016);
INSERT INTO LERNBEREICH VALUES (20, 'Sozialverhalten',						0,3,2016);

-- Jahrgangsstufe 1
INSERT INTO LERNBEREICH VALUES (30, 'Sprechen und Zuhören',					1,5,2016);
INSERT INTO LERNBEREICH VALUES (40, 'Schreiben',								1,5,2016);
INSERT INTO LERNBEREICH VALUES (50, 'Lesen - mit Texten und Medien umgehen',	1,5,2016);
INSERT INTO LERNBEREICH VALUES (60, 'Sprache und Sprachgebrauch untersuchen',1,5,2016);
INSERT INTO LERNBEREICH VALUES (70, 'Zahlen und Operationen',				1,5,2016);
INSERT INTO LERNBEREICH VALUES (80, 'Größen und Messen',						1,5,2016);
INSERT INTO LERNBEREICH VALUES (90, 'Raum und Form',							1,5,2016);
INSERT INTO LERNBEREICH VALUES (100, 'Sachunterricht',						1,5,2016);
INSERT INTO LERNBEREICH VALUES (110, 'Musik',								1,5,2016);
INSERT INTO LERNBEREICH VALUES (120, 'Religion',								1,5,2016);
INSERT INTO LERNBEREICH VALUES (130, 'Kunst',								1,5,2016);
INSERT INTO LERNBEREICH VALUES (140, 'Sport',								1,5,2016);

-- Jahrgangsstufe 2
INSERT INTO LERNBEREICH VALUES (150, 'Sprechen und Zuhören',					2,5,2016);
INSERT INTO LERNBEREICH VALUES (160, 'Schreiben',							2,5,2016);
INSERT INTO LERNBEREICH VALUES (170, 'Lesen - mit Texten und Medien umgehen',2,5,2016);
INSERT INTO LERNBEREICH VALUES (180, 'Sprache und Sprachgebrauch untersuchen',2,5,2016);
INSERT INTO LERNBEREICH VALUES (190, 'Zahlen und Operationen',				2,5,2016);
INSERT INTO LERNBEREICH VALUES (200, 'Größen und Messen',					2,5,2016);
INSERT INTO LERNBEREICH VALUES (210, 'Raum und Form',						2,5,2016);
INSERT INTO LERNBEREICH VALUES (220, 'Sachunterricht',						2,5,2016);
INSERT INTO LERNBEREICH VALUES (230, 'Musik',								2,5,2016);
INSERT INTO LERNBEREICH VALUES (240, 'Religion',								2,5,2016);
INSERT INTO LERNBEREICH VALUES (250, 'Kunst',								2,5,2016);
INSERT INTO LERNBEREICH VALUES (260, 'Sport',								2,5,2016);

-- Jahrgangsstufe 3
INSERT INTO LERNBEREICH VALUES (270, 'Sprechen und Zuhören',					3,5,2016);
INSERT INTO LERNBEREICH VALUES (280, 'Schreiben',							3,5,2016);
INSERT INTO LERNBEREICH VALUES (290, 'Lesen - mit Texten und Medien umgehen',3,5,2016);
INSERT INTO LERNBEREICH VALUES (300, 'Sprache und Sprachgebrauch untersuchen',3,5,2016);
INSERT INTO LERNBEREICH VALUES (310, 'Zahlen und Operationen',				3,5,2016);
INSERT INTO LERNBEREICH VALUES (320, 'Größen und Messen',					3,5,2016);
INSERT INTO LERNBEREICH VALUES (330, 'Raum und Form',						3,5,2016);
INSERT INTO LERNBEREICH VALUES (340, 'Sachunterricht',						3,5,2016);
INSERT INTO LERNBEREICH VALUES (350, 'Englisch',								3,5,2016);
INSERT INTO LERNBEREICH VALUES (360, 'Sport',								3,5,2016);
INSERT INTO LERNBEREICH VALUES (370, 'Textiles Gestalten',					3,5,2016);
INSERT INTO LERNBEREICH VALUES (380, 'Werken',								3,5,2016);
INSERT INTO LERNBEREICH VALUES (390, 'Kunst',								3,5,2016);
INSERT INTO LERNBEREICH VALUES (400, 'Musik',								3,5,2016);
INSERT INTO LERNBEREICH VALUES (410, 'Religion',								3,5,2016);

-- Jahrgangsstufe 4
INSERT INTO LERNBEREICH VALUES (420, 'Sprechen und Zuhören',					4,5,2016);
INSERT INTO LERNBEREICH VALUES (430, 'Schreiben',							4,5,2016);
INSERT INTO LERNBEREICH VALUES (440, 'Lesen - mit Texten und Medien umgehen',4,5,2016);
INSERT INTO LERNBEREICH VALUES (450, 'Sprache und Sprachgebrauch untersuchen',4,5,2016);
INSERT INTO LERNBEREICH VALUES (460, 'Zahlen und Operationen',				4,5,2016);
INSERT INTO LERNBEREICH VALUES (470, 'Größen und Messen',					4,5,2016);
INSERT INTO LERNBEREICH VALUES (480, 'Raum und Form',						4,5,2016);
INSERT INTO LERNBEREICH VALUES (490, 'Sachunterricht',						4,5,2016);
INSERT INTO LERNBEREICH VALUES (500, 'Englisch',								4,5,2016);
INSERT INTO LERNBEREICH VALUES (510, 'Sport',								4,5,2016);
INSERT INTO LERNBEREICH VALUES (520, 'Textiles Gestalten',					4,5,2016);
INSERT INTO LERNBEREICH VALUES (530, 'Werken',								4,5,2016);
INSERT INTO LERNBEREICH VALUES (540, 'Kunst',								4,5,2016);
INSERT INTO LERNBEREICH VALUES (550, 'Musik',								4,5,2016);
INSERT INTO LERNBEREICH VALUES (560, 'Religion',								4,5,2016);


--DB KRITERIUM
-- Kriterien
-- 0 Arbeitsverhalten
INSERT INTO KRITERIUM VALUES (10,  10,'folgt dem Unterricht interessiert und aufmerksam.',2016);
INSERT INTO KRITERIUM VALUES (20,  10,'beteiligt sich aktiv am Unterrichtsgeschehen.',2016);
INSERT INTO KRITERIUM VALUES (30,  10,'hält Gesprächsregeln ein.',2016);
INSERT INTO KRITERIUM VALUES (40,  10,'arbeitet zielstrebig und ausdauernd.',2016);
INSERT INTO KRITERIUM VALUES (50,  10,'hält ein angemessenes Arbeitstempo ein.',2016);
INSERT INTO KRITERIUM VALUES (60,  10,'erfasst Arbeitsanweisungen selbstständig und setzt sie um.',2016);
INSERT INTO KRITERIUM VALUES (70,  10,'führt Arbeiten sorgfältig und gewissenhaft aus.',2016);
INSERT INTO KRITERIUM VALUES (80,  10,'arbeitet erfolgreich in Partner- und Gruppenarbeit.',2016);
INSERT INTO KRITERIUM VALUES (90,  10,'hält das erforderliche Arbeitsmaterial zuverlässig bereit.',2016);
INSERT INTO KRITERIUM VALUES (100, 10,'führt Hefte und Mappen sorgfältig.',2016);
INSERT INTO KRITERIUM VALUES (110, 10,'fertigt Hausaufgaben zuverlässig an.',2016);

-- 0 Sozialverhalten
INSERT INTO KRITERIUM VALUES (120, 20,'verhält sich aufgeschlossen, freundlich und respektvoll.',2016);
INSERT INTO KRITERIUM VALUES (130, 20,'verhält sich rücksichtsvoll und hilfsbereit.',2016);
INSERT INTO KRITERIUM VALUES (140, 20,'nimmt Aufgaben und Pflichten für die Klasse wahr.',2016);
INSERT INTO KRITERIUM VALUES (150, 20,'hält Regeln und Vereinbarungen zuverlässig ein.',2016);
INSERT INTO KRITERIUM VALUES (160, 20,'zeigt Bereitschaft für das eigene Handeln einzustehen.',2016);
INSERT INTO KRITERIUM VALUES (170, 20,'verhält sich fair und einsichtig in Konfliktsituationen.',2016);
INSERT INTO KRITERIUM VALUES (180, 20,'erkennt unterschiedliche Meinungen an.',2016);

-- 1 Sprechen und Zuhören
INSERT INTO KRITERIUM VALUES (190, 30,'kennt Gesprächsregeln und wendet sie an.',2016);
INSERT INTO KRITERIUM VALUES (200, 30,'drückt sich sprachrichtig und verständlich aus.',2016);
INSERT INTO KRITERIUM VALUES (210, 30,'hört aufmerksam und konzentriert zu.',2016);
INSERT INTO KRITERIUM VALUES (220, 30,'beteiligt sich an Unterrichtsgesprächen.',2016);
INSERT INTO KRITERIUM VALUES (230, 30,'versteht mündliche Arbeitsanweisungen und kann diese umsetzen.',2016);
INSERT INTO KRITERIUM VALUES (240, 30,'verfügt über einen altersgemäßen Wortschatz.',2016);
INSERT INTO KRITERIUM VALUES (250, 30,'',2016);

-- 1 Schreiben
INSERT INTO KRITERIUM VALUES (260, 40,'schreibt in einer formklaren, gegliederten, lesbaren Schrift und hält die Linien ein.',2016);
INSERT INTO KRITERIUM VALUES (270, 40,'hält Wortgrenzen ein.',2016);
INSERT INTO KRITERIUM VALUES (280, 40,'schreibt lautgetreue Wörter.',2016);
INSERT INTO KRITERIUM VALUES (290, 40,'kann Wörter aus dem Übungsbereich richtig schreiben.',2016);
INSERT INTO KRITERIUM VALUES (300, 40,'schreibt Wörter und Sätze fehlerfrei ab.',2016);
INSERT INTO KRITERIUM VALUES (310, 40,'schreibt eigene Wörter.',2016);
INSERT INTO KRITERIUM VALUES (320, 40,'schreibt eigene Sätze.',2016);
INSERT INTO KRITERIUM VALUES (330, 40,'',2016);

-- 1 Lesen - mit Texten und Medien umgehen
INSERT INTO KRITERIUM VALUES (340, 50,'kann einfache Wörter lesen.',2016);
INSERT INTO KRITERIUM VALUES (350, 50,'kann Sätze und kurze Texte selbstständig lesen.',2016);
INSERT INTO KRITERIUM VALUES (360, 50,'liest sinnentnehmend und ist in der Lage, Fragen zu gelesenen Inhalten zu beantworten.',2016);
INSERT INTO KRITERIUM VALUES (370, 50,'liest geübte Texte flüssig und betont vor.',2016);
INSERT INTO KRITERIUM VALUES (380, 50,'versteht Aufgaben und bearbeitet diese selbstständig.',2016);
INSERT INTO KRITERIUM VALUES (390, 50,'',2016);

-- 1 Sprache und Sprachgebrauch untersuchen
INSERT INTO KRITERIUM VALUES (400, 60,'kennt alle eingeführten Buchstaben und die dazugehörigen Laute.',2016);
INSERT INTO KRITERIUM VALUES (410, 60,'',2016);

-- 1 Zahlen und Operationen
INSERT INTO KRITERIUM VALUES (420, 70,'kennt im Zahlenraum bis 20 alle Zahlen und kann diese richtig schreiben.',2016);
INSERT INTO KRITERIUM VALUES (430, 70,'kennt die Zeichen +, -, =, <, sowie > und kann sie richtig verwenden.',2016);
INSERT INTO KRITERIUM VALUES (440, 70,'kann Additionsaufgaben ohne Zehnerübergang im Zahlenraum bis 10 sicher lösen.',2016);
INSERT INTO KRITERIUM VALUES (450, 70,'kann Subtraktionsaufgaben ohne Zehnerübergang im Zahlenraum bis 10 sicher lösen.',2016);
INSERT INTO KRITERIUM VALUES (460, 70,'kann Additionsaufgaben ohne Zehnerübergang im Zahlenraum bis 20 sicher lösen.',2016);
INSERT INTO KRITERIUM VALUES (470, 70,'kann Additionsaufgaben mit Zehnerübergang im Zahlenraum bis 20 sicher lösen.',2016);
INSERT INTO KRITERIUM VALUES (480, 70,'kann Subtraktionsaufgaben ohne Zehnerübergang im Zahlenraum bis 20 sicher lösen.',2016);
INSERT INTO KRITERIUM VALUES (490, 70,'kann Subtraktionsaufgaben mit Zehnerübergang im Zahlenraum bis 20 sicher lösen.',2016);
INSERT INTO KRITERIUM VALUES (500, 70,'',2016);

-- 1 Größen und Messen
INSERT INTO KRITERIUM VALUES (510, 80,'kennt die erarbeiteten Einheiten des Bereichs „Geld“ und kann mit diesen auch in Sachsituationen sicher rechnen.',2016);
INSERT INTO KRITERIUM VALUES (520, 80,'kann einfache Sachaufgaben lösen.',2016);
INSERT INTO KRITERIUM VALUES (530, 80,'',2016);

-- 1 Raum und Form
INSERT INTO KRITERIUM VALUES (540, 90,'kann geometrische Figuren nach Eigenschaften sortieren, benennen und sie in der Umwelt wiedererkennen.',2016);
INSERT INTO KRITERIUM VALUES (550, 90,'erkennt vorgegebene Muster.',2016);
INSERT INTO KRITERIUM VALUES (560, 90,'kann vorgegebene Muster selbstständig fortführen.',2016);
INSERT INTO KRITERIUM VALUES (570, 90,'kann Papier ordentlich und genau falten.',2016);
INSERT INTO KRITERIUM VALUES (580, 90,'',2016);

-- 1 SU
INSERT INTO KRITERIUM VALUES (590, 100,'zeigt Interesse an den Themen des Sachunterrichts.',2016);
INSERT INTO KRITERIUM VALUES (600, 100,'kann Sachzusammenhänge wiedergeben, erklären und übertragen.',2016);

-- 1 Musik
INSERT INTO KRITERIUM VALUES (610, 110,'zeigt Interesse am Singen und Musizieren.',2016);
INSERT INTO KRITERIUM VALUES (620, 110,'erfasst Lieder in Text und Melodie.',2016);
INSERT INTO KRITERIUM VALUES (630, 110,'ist in der Lage, Rhythmen wiederzugeben.',2016);
INSERT INTO KRITERIUM VALUES (640, 110,'kann mit Begleitinstrumenten umgehen.',2016);

-- 1 Religion
INSERT INTO KRITERIUM VALUES (650, 120,'nimmt engagiert am Religionsunterricht teil.',2016);
INSERT INTO KRITERIUM VALUES (660, 120,'zeigt Interesse an den Themen des Religionsunterrichts.',2016);
INSERT INTO KRITERIUM VALUES (670, 120,'kann elementare religiöse Feste und Bräuche benennen.',2016);

-- 1 Kunst
INSERT INTO KRITERIUM VALUES (680, 130,'zeichnet und malt mit Interesse.',2016);
INSERT INTO KRITERIUM VALUES (690, 130,'zeigt Kreativität.',2016);
INSERT INTO KRITERIUM VALUES (700, 130,'kann mit den fachspezifischen Arbeitsmitteln umgehen.',2016);

-- 1 Sport
INSERT INTO KRITERIUM VALUES (710, 140,'kann eine Spielidee erfassen, erkennen und umsetzen.',2016);
INSERT INTO KRITERIUM VALUES (720, 140,'beweist Einsatzfreude.',2016);
INSERT INTO KRITERIUM VALUES (730, 140,'zeigt körperliche Geschicklichkeit.',2016);
INSERT INTO KRITERIUM VALUES (740, 140,'zeigt faires Verhalten.',2016);

-- 2 Sprechen und Zuhören
INSERT INTO KRITERIUM VALUES (750, 150,'kennt Gesprächsregeln und wendet sie an.',2016);
INSERT INTO KRITERIUM VALUES (760, 150,'äußert sich sachbezogen.',2016);
INSERT INTO KRITERIUM VALUES (770, 150,'drückt sich sprachrichtig und verständlich aus.',2016);
INSERT INTO KRITERIUM VALUES (780, 150,'kann Inhalte zuhörend verstehen und gezielt nachfragen.',2016);
INSERT INTO KRITERIUM VALUES (790, 150,'vertritt eigene Meinungen.',2016);
INSERT INTO KRITERIUM VALUES (800, 150,'nimmt in Unterrichtsgesprächen Bezug auf andere.',2016);
INSERT INTO KRITERIUM VALUES (810, 150,'trägt Gedichte auswendig und sinngestaltend vor.',2016);
INSERT INTO KRITERIUM VALUES (820, 150,'',2016);

-- 2 Schreiben
INSERT INTO KRITERIUM VALUES (830, 160,'schreibt in einer formklaren, gegliederten, lesbaren Schrift und hält die Linien ein.',2016);
INSERT INTO KRITERIUM VALUES (840, 160,'gestaltet einen Text übersichtlich.',2016);
INSERT INTO KRITERIUM VALUES (850, 160,'kann Wörter aus dem Übungsbereich richtig schreiben.',2016);
INSERT INTO KRITERIUM VALUES (860, 160,'wendet grundlegende Rechtschreibregeln an.',2016);
INSERT INTO KRITERIUM VALUES (870, 160,'kennt das Alphabet und kann einfache Wörter im Wörterbuch nachschlagen.',2016);
INSERT INTO KRITERIUM VALUES (880, 160,'schreibt Texte fehlerfrei ab.',2016);
INSERT INTO KRITERIUM VALUES (890, 160,'schreibt eigene Sätze.',2016);
INSERT INTO KRITERIUM VALUES (900, 160,'schreibt Texte, in denen die Sätze inhaltlich aufeinander bezogen sind.',2016);
INSERT INTO KRITERIUM VALUES (910, 160,'',2016);

-- 2 Lesen - mit Texten und Medien umgehen
INSERT INTO KRITERIUM VALUES (920, 170,'kann Sätze und  kurze Texte selbstständig lesen.',2016);
INSERT INTO KRITERIUM VALUES (930, 170,'liest sinnentnehmend und ist in der Lage, Fragen zu gelesenen Inhalten zu beantworten.',2016);
INSERT INTO KRITERIUM VALUES (940, 170,'liest geübte Texte flüssig und betont vor.',2016);
INSERT INTO KRITERIUM VALUES (950, 170,'liest ungeübte Texte flüssig und betont vor.',2016);
INSERT INTO KRITERIUM VALUES (960, 170,'versteht schriftliche Aufgaben und bearbeitet diese selbstständig.',2016);
INSERT INTO KRITERIUM VALUES (970, 170,'',2016);

-- 2 Sprache und Sprachgebrauch untersuchen
INSERT INTO KRITERIUM VALUES (980, 180,'kann die behandelten Wortarten unterscheiden und benennen.',2016);
INSERT INTO KRITERIUM VALUES (990, 180,'kann Satzarten unterscheiden und richtige Satzschlusszeichen setzen.',2016);
INSERT INTO KRITERIUM VALUES (1000, 180,'',2016);

-- 2 Zahlen und Operationen
INSERT INTO KRITERIUM VALUES (1010, 190,'kennt im Zahlenraum bis 100 alle Zahlen und kann diese richtig schreiben.',2016);
INSERT INTO KRITERIUM VALUES (1020, 190,'kann Größer- und Kleinerbeziehungen angeben und notieren.',2016);
INSERT INTO KRITERIUM VALUES (1030, 190,'vergleicht, strukturiert und zerlegt Zahlen.',2016);
INSERT INTO KRITERIUM VALUES (1040, 190,'kann Additionsaufgaben ohne Zehnerübergang im Zahlenraum bis 100 sicher lösen.',2016);
INSERT INTO KRITERIUM VALUES (1050, 190,'kann Subtraktionsaufgaben ohne Zehnerübergang im Zahlenraum bis 100 sicher lösen.',2016);
INSERT INTO KRITERIUM VALUES (1060, 190,'kann Additionsaufgaben mit Zehnerübergang im Zahlenraum bis 100 sicher lösen.',2016);
INSERT INTO KRITERIUM VALUES (1070, 190,'kann Subtraktionsaufgaben mit Zehnerübergang im Zahlenraum bis 100 sicher lösen.',2016);
INSERT INTO KRITERIUM VALUES (1080, 190,'kann die Aufgaben des Einmaleins sicher lösen.',2016);
INSERT INTO KRITERIUM VALUES (1090, 190,'kann Divisionsaufgaben im Zahlenraum bis 100 sicher lösen.',2016);
INSERT INTO KRITERIUM VALUES (1100, 190,'erklärt Rechenwege und stellt diese dar.',2016);
INSERT INTO KRITERIUM VALUES (1110, 190,'kann mathematische Zusammenhänge entdecken und beschreiben.',2016);
INSERT INTO KRITERIUM VALUES (1120, 190,'wendet Rechenstrategien an.',2016);
INSERT INTO KRITERIUM VALUES (1130, 190,'',2016);

-- 2 Größen und Messen
INSERT INTO KRITERIUM VALUES (1140, 200,'kennt die erarbeiteten Einheiten des Bereichs „Geld“ und kann mit diesen auch in Sachsituationen sicher rechnen.',2016);
INSERT INTO KRITERIUM VALUES (1150, 200,'kennt die erarbeiteten Einheiten des Bereichs „Zeit“ und kann mit diesen auch in Sachsituationen sicher rechnen.',2016);
INSERT INTO KRITERIUM VALUES (1160, 200,'kennt die erarbeiteten Einheiten des Bereichs „Längen“ und kann mit diesen auch in Sachsituationen sicher rechnen.',2016);
INSERT INTO KRITERIUM VALUES (1170, 200,'kann einfache Sachaufgaben lösen und passende Antworten zu Fragestellungen finden.',2016);
INSERT INTO KRITERIUM VALUES (1180, 200,'',2016);

-- 2 Raum und Form
INSERT INTO KRITERIUM VALUES (1190, 210,'kann geometrische Figuren nach Eigenschaften sortieren, benennen und sie in der Umwelt wiedererkennen.',2016);
INSERT INTO KRITERIUM VALUES (1200, 210,'kann geometrische Körper nach Eigenschaften sortieren, benennen und sie in der Umwelt wiedererkennen.',2016);
INSERT INTO KRITERIUM VALUES (1210, 210,'erkennt vorgegebene Muster.',2016);
INSERT INTO KRITERIUM VALUES (1220, 210,'kann vorgegebene Muster selbstständig fortführen.',2016);
INSERT INTO KRITERIUM VALUES (1230, 210,'zeichnet sauber mit dem Lineal.',2016);
INSERT INTO KRITERIUM VALUES (1240, 210,'kann Papier ordentlich und genau falten.',2016);
INSERT INTO KRITERIUM VALUES (1250, 210,'',2016);

-- 2 SU
INSERT INTO KRITERIUM VALUES (1260, 220,'zeigt Interesse an den Themen des Sachunterrichts.',2016);
INSERT INTO KRITERIUM VALUES (1270, 220,'kann Sachzusammenhänge wiedergeben, erklären und übertragen.',2016);
INSERT INTO KRITERIUM VALUES (1280, 220,'kann selbstständig einfache Versuche durchführen und Beobachtungen verständlich darstellen.',2016);

-- 2 Musik
INSERT INTO KRITERIUM VALUES (1290, 230,'zeigt Interesse am Singen und Musizieren.',2016);
INSERT INTO KRITERIUM VALUES (1300, 230,'erfasst Lieder in Text und Melodie.',2016);
INSERT INTO KRITERIUM VALUES (1310, 230,'ist in der Lage, Rhythmen wiederzugeben.',2016);
INSERT INTO KRITERIUM VALUES (1320, 230,'kann mit Begleitinstrumenten umgehen.',2016);

-- 2 Religion
INSERT INTO KRITERIUM VALUES (1330, 240,'nimmt engagiert am Religionsunterricht teil.',2016);
INSERT INTO KRITERIUM VALUES (1340, 240,'kann religiöse Geschichten verstehen und wiedergeben.',2016);
INSERT INTO KRITERIUM VALUES (1350, 240,'kann elementare religiöse Feste und Bräuche benennen und erläutern.',2016);

-- 2 Kunst
INSERT INTO KRITERIUM VALUES (1360, 250,'zeichnet und malt mit Engagement.',2016);
INSERT INTO KRITERIUM VALUES (1370, 250,'zeigt Ausdrucksfähigkeit und Kreativität.',2016);
INSERT INTO KRITERIUM VALUES (1380, 250,'zeigt ein Form- und Farbgefühl.',2016);
INSERT INTO KRITERIUM VALUES (1390, 250,'kann mit den fachspezifischen Arbeitsmitteln umgehen.',2016);

-- 2 Sport
INSERT INTO KRITERIUM VALUES (1400, 260,'kann eine Spielidee erfassen, erkennen und umsetzen.',2016);
INSERT INTO KRITERIUM VALUES (1410, 260,'beweist Einsatzfreude.',2016);
INSERT INTO KRITERIUM VALUES (1420, 260,'zeigt körperliche Geschicklichkeit.',2016);
INSERT INTO KRITERIUM VALUES (1430, 260,'setzt gestellte Bewegungsaufgaben um.',2016);
INSERT INTO KRITERIUM VALUES (1440, 260,'ist geschickt im Umgang mit dem Ball.',2016);
INSERT INTO KRITERIUM VALUES (1450, 260,'zeigt faires Verhalten.',2016);

-- 3 Sprechen und Zuhören
INSERT INTO KRITERIUM VALUES (1460, 270,'wendet Gesprächsregeln an.',2016);
INSERT INTO KRITERIUM VALUES (1470, 270,'äußert sich sachbezogen.',2016);
INSERT INTO KRITERIUM VALUES (1480, 270,'drückt sich sprachrichtig und verständlich aus.',2016);
INSERT INTO KRITERIUM VALUES (1490, 270,'kann Inhalte zuhörend verstehen und gezielt nachfragen.',2016);
INSERT INTO KRITERIUM VALUES (1500, 270,'vertritt und begründet eigene Meinungen.',2016);
INSERT INTO KRITERIUM VALUES (1510, 270,'nimmt in Unterrichtsgesprächen Bezug auf andere.',2016);
INSERT INTO KRITERIUM VALUES (1520, 270,'präsentiert Lernergebnisse kriterienbezogen.',2016);
INSERT INTO KRITERIUM VALUES (1530, 270,'trägt Texte auswendig und sinngestaltend vor.',2016);
INSERT INTO KRITERIUM VALUES (1540, 270,'',2016);

-- 3 Schreiben
INSERT INTO KRITERIUM VALUES (1550, 280,'schreibt in einer flüssigen und gut lesbaren Handschrift.',2016);
INSERT INTO KRITERIUM VALUES (1560, 280,'gestaltet einen Text ordentlich und übersichtlich.',2016);
INSERT INTO KRITERIUM VALUES (1570, 280,'kann Wörter aus dem Übungsbereich richtig schreiben.',2016);
INSERT INTO KRITERIUM VALUES (1580, 280,'wendet grundlegende Rechtschreibregeln  und -strategien  an.',2016);
INSERT INTO KRITERIUM VALUES (1590, 280,'nutzt das Wörterbuch und beherrscht Nachschlagetechniken.',2016);
INSERT INTO KRITERIUM VALUES (1600, 280,'schreibt Texte fehlerfrei ab.',2016);
INSERT INTO KRITERIUM VALUES (1610, 280,'kann eigene Schreibideen entwickeln und dabei Planungsmethoden nutzen.',2016);
INSERT INTO KRITERIUM VALUES (1620, 280,'verfasst verständliche und strukturierte Texte.',2016);
INSERT INTO KRITERIUM VALUES (1630, 280,'wendet Überarbeitungsmethoden an.',2016);
INSERT INTO KRITERIUM VALUES (1640, 280,'',2016);

-- 3 Lesen - mit Texten und Medien umgehen
INSERT INTO KRITERIUM VALUES (1650, 290,'kann altersgemäße Texte sinnverstehend lesen.',2016);
INSERT INTO KRITERIUM VALUES (1660, 290,'entnimmt Texten Informationen und zieht Schlussfolgerungen.',2016);
INSERT INTO KRITERIUM VALUES (1670, 290,'liest geübte Texte flüssig und betont vor.',2016);
INSERT INTO KRITERIUM VALUES (1680, 290,'liest ungeübte Texte flüssig und betont vor.',2016);
INSERT INTO KRITERIUM VALUES (1690, 290,'versteht schriftliche Handlungsanweisungen und setzt diese selbstständig um.',2016);
INSERT INTO KRITERIUM VALUES (1700, 290,'',2016);

-- 3 Sprache und Sprachgebrauch untersuchen
INSERT INTO KRITERIUM VALUES (1710, 300,'kann die behandelten Wortarten unterscheiden und benennen.',2016);
INSERT INTO KRITERIUM VALUES (1720, 300,'kann Satzarten unterscheiden und richtige Satzschlusszeichen setzen.',2016);
INSERT INTO KRITERIUM VALUES (1730, 300,'kann die behandelten Zeitformen unterscheiden und benennen.',2016);
INSERT INTO KRITERIUM VALUES (1740, 300,'kann die Regeln zum Einsatz der wörtlichen Rede anwenden.',2016);
INSERT INTO KRITERIUM VALUES (1750, 300,'',2016);

-- 3 Zahlen und Operationen
INSERT INTO KRITERIUM VALUES (1760, 310,'hat alle Aufgaben des kleinen Einmaleins automatisiert.',2016);
INSERT INTO KRITERIUM VALUES (1770, 310,'beherrscht die Divisionsaufgaben im Zahlenraum bis 100.',2016);
INSERT INTO KRITERIUM VALUES (1780, 310,'kann Divisionsaufgaben mit Rest berechnen.',2016);
INSERT INTO KRITERIUM VALUES (1790, 310,'kann sich im Zahlenraum bis 1000 sicher orientieren.',2016);
INSERT INTO KRITERIUM VALUES (1800, 310,'kann im Zahlenraum bis 1000 Additionsaufgaben halbschriftlich lösen.',2016);
INSERT INTO KRITERIUM VALUES (1810, 310,'kann im Zahlenraum bis 1000 Subtraktionsaufgaben halbschriftlich lösen.',2016);
INSERT INTO KRITERIUM VALUES (1820, 310,'kann im Zahlenraum bis 1000 Additionsaufgaben schriftlich lösen.',2016);
INSERT INTO KRITERIUM VALUES (1830, 310,'kann im Zahlenraum bis 1000 Subtraktionsaufgaben schriftlich lösen.',2016);
INSERT INTO KRITERIUM VALUES (1840, 310,'kann im Zahlenraum bis 1000 Multiplikationsaufgaben halbschriftlich lösen.',2016);
INSERT INTO KRITERIUM VALUES (1850, 310,'kann im Zahlenraum bis 1000 Divisionsaufgaben halbschriftlich lösen.',2016);
INSERT INTO KRITERIUM VALUES (1860, 310,'kann Kopfrechenaufgaben lösen.',2016);
INSERT INTO KRITERIUM VALUES (1870, 310,'erklärt Rechenwege und stellt diese dar.',2016);
INSERT INTO KRITERIUM VALUES (1880, 310,'kann mathematische Zusammenhänge entdecken und beschreiben.',2016);
INSERT INTO KRITERIUM VALUES (1890, 310,'wendet Rechenstrategien an.',2016);
INSERT INTO KRITERIUM VALUES (1900, 310,'',2016);

-- 3 Größen und Messen
INSERT INTO KRITERIUM VALUES (1910, 320,'kennt die erarbeiteten Einheiten des Bereichs „Geld“ und kann mit diesen auch in Sachsituationen sicher rechnen.',2016);
INSERT INTO KRITERIUM VALUES (1920, 320,'kennt die erarbeiteten Einheiten des Bereichs „Zeit“ und kann mit diesen auch in Sachsituationen sicher rechnen.',2016);
INSERT INTO KRITERIUM VALUES (1930, 320,'kennt die erarbeiteten Einheiten des Bereichs „Längen“ und kann mit diesen auch in Sachsituationen sicher rechnen.',2016);
INSERT INTO KRITERIUM VALUES (1940, 320,'kennt die erarbeiteten Einheiten des Bereichs „Gewichte“ und kann mit diesen auch in Sachsituationen sicher rechnen.',2016);
INSERT INTO KRITERIUM VALUES (1950, 320,'kann Sachaufgaben lösen und passende Fragestellungen zu Sachverhalten finden.',2016);
INSERT INTO KRITERIUM VALUES (1960, 320,'',2016);

-- 3 Raum und Form
INSERT INTO KRITERIUM VALUES (1970, 330,'kann geometrische Figuren mit Fachbegriffen benennen und beschreiben.',2016);
INSERT INTO KRITERIUM VALUES (1980, 330,'kann achsensymmetrische Figuren erkennen, untersuchen und zeichnen.',2016);
INSERT INTO KRITERIUM VALUES (1990, 330,'kann geometrische Körper mit Fachbegriffen benennen und beschreiben.',2016);
INSERT INTO KRITERIUM VALUES (2000, 330,'löst Aufgaben und Probleme mit räumlichen Bezügen konkret und in der Vorstellung.',2016);
INSERT INTO KRITERIUM VALUES (2010, 330,'kann vorgegebene Muster selbstständig fortführen.',2016);
INSERT INTO KRITERIUM VALUES (2020, 330,'',2016);

-- 3 SU
INSERT INTO KRITERIUM VALUES (2030, 340,'zeigt Interesse an den Themen des Sachunterrichts.',2016);
INSERT INTO KRITERIUM VALUES (2040, 340,'bereichert den Unterricht mit sachbezogenen Beiträgen.',2016);
INSERT INTO KRITERIUM VALUES (2050, 340,'kann gelernte Sachverhalte und Fachbegriffe anwenden.',2016);
INSERT INTO KRITERIUM VALUES (2060, 340,'dokumentiert Arbeitsergebnisse übersichtlich und systematisch.',2016);
INSERT INTO KRITERIUM VALUES (2070, 340,'wendet fachspezifische Methoden und Arbeitsweisen an.',2016);
INSERT INTO KRITERIUM VALUES (2080, 340,'kann Informationen aus Sachtexten entnehmen, erklären und bewerten. ',2016);
INSERT INTO KRITERIUM VALUES (2090, 340,'präsentiert Arbeitsergebnisse in geeigneter Form.',2016);

-- 3 Englisch
INSERT INTO KRITERIUM VALUES (2100, 350,'erkennt beim Zuhören Schlüsselwörter wieder.',2016);
INSERT INTO KRITERIUM VALUES (2110, 350,'versteht einfache Fragen und Aussagen und reagiert angemessen.',2016);
INSERT INTO KRITERIUM VALUES (2120, 350,'versteht den Kontext von Dialogen und Geschichten.',2016);
INSERT INTO KRITERIUM VALUES (2130, 350,'spricht mithilfe bekannter Redemittel in Zusammenhängen.',2016);
INSERT INTO KRITERIUM VALUES (2140, 350,'kann Abbildungen in einfachen Wörtern/Sätzen frei beschreiben',2016);
INSERT INTO KRITERIUM VALUES (2150, 350,'kann Schriftbilder dem bekannten Klang/Lautbild zuordnen.',2016);
INSERT INTO KRITERIUM VALUES (2160, 350,'kann Wörter und kurze bekannte Sätze vorlesen und verstehen.',2016);
INSERT INTO KRITERIUM VALUES (2170, 350,'schreibt bekannte Wörter richtig nach Vorlage ab.',2016);

-- 3 Sport
INSERT INTO KRITERIUM VALUES (2180, 360,'beteiligt sich gern an sportlichen Aktivitäten und zeigt Einsatzbereitschaft.',2016);
INSERT INTO KRITERIUM VALUES (2190, 360,'kann auch komplexere Spielideen umsetzen.',2016);
INSERT INTO KRITERIUM VALUES (2200, 360,'befolgt Spielregeln und verhält sich fair.',2016);
INSERT INTO KRITERIUM VALUES (2210, 360,'kann mit Sieg und Niederlage wertschätzend umgehen.',2016);
INSERT INTO KRITERIUM VALUES (2220, 360,'setzt gestellte Bewegungsaufgaben erfolgreich um.',2016);
INSERT INTO KRITERIUM VALUES (2230, 360,'beherrscht die Grundformen leichtathletischer Bewegungsabläufe.',2016);
INSERT INTO KRITERIUM VALUES (2240, 360,'bewältigt grundlegende turnerische Bewegungsanforderungen.',2016);
INSERT INTO KRITERIUM VALUES (2250, 360,'ist geschickt im Umgang mit dem Ball.',2016);

-- 3 Textiles Gestalten
INSERT INTO KRITERIUM VALUES (2260, 370,'zeigt  Motivation und Ausdauer bei der Umsetzung textilpraktischer Aufgaben.',2016);
INSERT INTO KRITERIUM VALUES (2270, 370,'gestaltet ihre/seine Textilarbeiten sorgfältig und ideenreich.',2016);
INSERT INTO KRITERIUM VALUES (2280, 370,'geht sachgerecht mit Werkzeugen und Materialien um und beachtet die Sicherheitshinweise.',2016);
INSERT INTO KRITERIUM VALUES (2290, 370,'berücksichtigt  bei der Gestaltung die vorgegebenen Kriterien.',2016);
INSERT INTO KRITERIUM VALUES (2300, 370,'zeigt Neugier, Kreativität und Geschicklichkeit.',2016);

-- 3 Werken
INSERT INTO KRITERIUM VALUES (2310, 380,'zeigt  Motivation und Ausdauer bei der Umsetzung werkpraktischer Aufgaben.',2016);
INSERT INTO KRITERIUM VALUES (2320, 380,'gestaltet ihre/seine Werkarbeiten sorgfältig und ideenreich.',2016);
INSERT INTO KRITERIUM VALUES (2330, 380,'achtet auf die korrekte Durchführung der Arbeitsschritte und Techniken.',2016);
INSERT INTO KRITERIUM VALUES (2340, 380,'geht sachgerecht mit Werkzeugen und Materialien um und beachtet die Sicherheitshinweise. ',2016);
INSERT INTO KRITERIUM VALUES (2350, 380,'zeigt Neugier, Kreativität und Geschicklichkeit.',2016);

-- 3 Kunst
INSERT INTO KRITERIUM VALUES (2360, 390,'plant die Arbeitsschritte und setzt die Aufgabenstellungen nach den vorgegebenen Kriterien um.',2016);
INSERT INTO KRITERIUM VALUES (2370, 390,'zeigt Motivation, Konzentration und Ausdauer im Gestaltungsprozess.',2016);
INSERT INTO KRITERIUM VALUES (2380, 390,'erzielt ein Gestaltungsergebnis nach vorgegebenen Kriterien.',2016);
INSERT INTO KRITERIUM VALUES (2390, 390,'geht sachgerecht mit den unterschiedlichen Gestaltungsmitteln um. ',2016);
INSERT INTO KRITERIUM VALUES (2400, 390,'zeigt Ausdrucksfähigkeit und Kreativität.',2016);

-- 3 Musik
INSERT INTO KRITERIUM VALUES (2410, 400,'singt ausgewählte Lieder auswendig.',2016);
INSERT INTO KRITERIUM VALUES (2420, 400,'führt auf Metrum bezogene Bewegungen zur Musik aus.',2016);
INSERT INTO KRITERIUM VALUES (2430, 400,'erkennt und unterscheidet hörend einfache musikalische Strukturen und Formen.',2016);
INSERT INTO KRITERIUM VALUES (2440, 400,'kennt ausgewählte Schul- und Orchesterinstrumente und kann sie hörend unterscheiden und benennen.',2016);
INSERT INTO KRITERIUM VALUES (2450, 400,'beherrscht die Spieltechniken der Schulinstrumente.',2016);

-- 3 Religion
INSERT INTO KRITERIUM VALUES (2460, 410,'zeigt Interesse an den Themen des Religionsunterrichts.',2016);
INSERT INTO KRITERIUM VALUES (2470, 410,'bereichert den Unterricht mit themenbezogenen Beiträgen.',2016);
INSERT INTO KRITERIUM VALUES (2480, 410,'kann religiöse Geschichten benennen und erklären.',2016);
INSERT INTO KRITERIUM VALUES (2490, 410,'kann religiöse Räume und Feste, Zeichen, Symbole und Rituale benennen.',2016);
INSERT INTO KRITERIUM VALUES (2500, 410,'kann eigene Vorstellungen zum Ausdruck bringen.',2016);
INSERT INTO KRITERIUM VALUES (2510, 410,'dokumentiert und gestaltet Arbeitsergebnisse.',2016);

-- 4 Sprechen und Zuhören
INSERT INTO KRITERIUM VALUES (2520, 420,'wendet Gesprächsregeln an.',2016);
INSERT INTO KRITERIUM VALUES (2530, 420,'äußert sich sachbezogen.',2016);
INSERT INTO KRITERIUM VALUES (2540, 420,'drückt sich sprachrichtig und verständlich aus.',2016);
INSERT INTO KRITERIUM VALUES (2550, 420,'kann Inhalte zuhörend verstehen und gezielt nachfragen.',2016);
INSERT INTO KRITERIUM VALUES (2560, 420,'vertritt und begründet eigene Meinungen.',2016);
INSERT INTO KRITERIUM VALUES (2570, 420,'nimmt in Unterrichtsgesprächen Bezug auf andere.',2016);
INSERT INTO KRITERIUM VALUES (2580, 420,'präsentiert Lernergebnisse kriterienbezogen.',2016);
INSERT INTO KRITERIUM VALUES (2590, 420,'trägt Texte auswendig und sinngestaltend vor.',2016);
INSERT INTO KRITERIUM VALUES (2600, 420,'',2016);

-- 4 Schreiben
INSERT INTO KRITERIUM VALUES (2610, 430,'schreibt in einer flüssigen und gut lesbaren Handschrift.',2016);
INSERT INTO KRITERIUM VALUES (2620, 430,'gestaltet einen Text ordentlich und übersichtlich.',2016);
INSERT INTO KRITERIUM VALUES (2630, 430,'kann Wörter aus dem Übungsbereich richtig schreiben.',2016);
INSERT INTO KRITERIUM VALUES (2640, 430,'wendet grundlegende Rechtschreibregeln und -strategien  an.',2016);
INSERT INTO KRITERIUM VALUES (2650, 430,'nutzt das Wörterbuch und beherrscht Nachschlagetechniken.',2016);
INSERT INTO KRITERIUM VALUES (2660, 430,'schreibt Texte fehlerfrei ab.',2016);
INSERT INTO KRITERIUM VALUES (2670, 430,'kann eigene Schreibideen entwickeln und dabei Planungsmethoden nutzen.',2016);
INSERT INTO KRITERIUM VALUES (2680, 430,'verfasst verständliche und strukturierte Texte.',2016);
INSERT INTO KRITERIUM VALUES (2690, 430,'wendet Überarbeitungsmethoden an.',2016);
INSERT INTO KRITERIUM VALUES (2700, 430,'',2016);

-- 4 Lesen - mit Texten und Medien umgehen
INSERT INTO KRITERIUM VALUES (2710, 440'kann altersgemäße Texte sinnverstehend lesen.',2016);
INSERT INTO KRITERIUM VALUES (2720, 440'kann unterschiedlichen Medien Informationen entnehmen und Schlussfolgerungen ziehen.',2016);
INSERT INTO KRITERIUM VALUES (2730, 440'liest geübte Texte flüssig und betont vor.',2016);
INSERT INTO KRITERIUM VALUES (2740, 440'liest ungeübte Texte flüssig und betont vor.',2016);
INSERT INTO KRITERIUM VALUES (2750, 440'kann grundlegende Texterschließungsverfahren anwenden.',2016);
INSERT INTO KRITERIUM VALUES (2760, 440'versteht schriftliche Handlungsanweisungen und setzt diese selbstständig um.',2016);
INSERT INTO KRITERIUM VALUES (2770, 440'',2016);

-- 4 Sprache und Sprachgebrauch untersuchen
INSERT INTO KRITERIUM VALUES (2780, 450,'kann die behandelten Wortarten unterscheiden und benennen.',2016);
INSERT INTO KRITERIUM VALUES (2790, 450,'unterscheidet Satzarten und setzt richtige Satzschlusszeichen.',2016);
INSERT INTO KRITERIUM VALUES (2800, 450,'kann die eingeführten Kommaregeln anwenden.',2016);
INSERT INTO KRITERIUM VALUES (2810, 450,'erkennt und benennt die behandelten Satzglieder.',2016);
INSERT INTO KRITERIUM VALUES (2820, 450,'kann die behandelten Zeitformen erkennen und gezielt anwenden.',2016);
INSERT INTO KRITERIUM VALUES (2830, 450,'kann die wörtlichen Rede in eigenen Texten anwenden.',2016);
INSERT INTO KRITERIUM VALUES (2840, 450,'',2016);

-- 4 Zahlen und Operationen
INSERT INTO KRITERIUM VALUES (2850, 460,'kann sich im Zahlenraum bis 1 Million sicher orientieren.',2016);
INSERT INTO KRITERIUM VALUES (2860, 460,'kann im Zahlenraum bis 1 Million Multiplikationsaufgaben halbschriftlich lösen.',2016);
INSERT INTO KRITERIUM VALUES (2870, 460,'kann im Zahlenraum bis 1 Million Divisionsaufgaben halbschriftlich lösen.',2016);
INSERT INTO KRITERIUM VALUES (2880, 460,'beherrscht das Verfahren der schriftlichen Multiplikation.',2016);
INSERT INTO KRITERIUM VALUES (2890, 460,'beherrscht das Verfahren der schriftlichen Division.',2016);
INSERT INTO KRITERIUM VALUES (2900, 460,'kann Kopfrechenaufgaben lösen.',2016);
INSERT INTO KRITERIUM VALUES (2910, 460,'erklärt Rechenwege und stellt diese dar.',2016);
INSERT INTO KRITERIUM VALUES (2920, 460,'kann mathematische Zusammenhänge entdecken und beschreiben.',2016);
INSERT INTO KRITERIUM VALUES (2930, 460,'wendet Rechenstrategien an.',2016);
INSERT INTO KRITERIUM VALUES (2940, 460,'',2016);

-- 4 Größen und Messen
INSERT INTO KRITERIUM VALUES (2950, 470,'kennt die erarbeiteten Einheiten des Bereichs „Zeit“ und kann mit diesen auch in Sachsituationen sicher rechnen.',2016);
INSERT INTO KRITERIUM VALUES (2960, 470,'kennt die erarbeiteten Einheiten des Bereichs „Längen“ und kann mit diesen auch in Sachsituationen sicher rechnen.',2016);
INSERT INTO KRITERIUM VALUES (2970, 470,'kennt die erarbeiteten Einheiten des Bereichs „Gewichte“ und kann mit diesen auch in Sachsituationen sicher rechnen.',2016);
INSERT INTO KRITERIUM VALUES (2980, 470,'kann Sachaufgaben lösen und passende Fragestellungen zu Sachverhalten finden.',2016);
INSERT INTO KRITERIUM VALUES (2990, 470,'',2016);

-- 4 Raum und Form
INSERT INTO KRITERIUM VALUES (3000, 480,'kann kopfgeometrische Aufgaben lösen.',2016);
INSERT INTO KRITERIUM VALUES (3010, 480,'kann geometrische Figuren mit Fachbegriffen benennen, beschreiben und zeichnen.',2016);
INSERT INTO KRITERIUM VALUES (3020, 480,'kann mithilfe des Geodreiecks senkrechte und parallele Linien erkennen und zeichnen.',2016);
INSERT INTO KRITERIUM VALUES (3030, 480,'kann Schrägbilder lesen und zeichnen.',2016);
INSERT INTO KRITERIUM VALUES (3040, 480,'kann den Zirkel fachgerecht einsetzen.',2016);
INSERT INTO KRITERIUM VALUES (3050, 480,'',2016);

-- 4 SU
INSERT INTO KRITERIUM VALUES (3060, 490,'zeigt Interesse an den Themen des Sachunterrichts.',2016);
INSERT INTO KRITERIUM VALUES (3070, 490,'bereichert den Unterricht mit sachbezogenen Themen.',2016);
INSERT INTO KRITERIUM VALUES (3080, 490,'kann gelernte Sachverhalte und Fachbegriffe anwenden.',2016);
INSERT INTO KRITERIUM VALUES (3090, 490,'dokumentiert Arbeitsergebnisse übersichtlich und systematisch.',2016);
INSERT INTO KRITERIUM VALUES (3100, 490,'wendet fachspezifische Methoden und Arbeitsweisen an.',2016);
INSERT INTO KRITERIUM VALUES (3110, 490,'kann Informationen aus Sachtexten entnehmen, interpretieren und bewerten.',2016);
INSERT INTO KRITERIUM VALUES (3120, 490,'präsentiert Arbeitsergebnisse in geeigneter Form.',2016);

-- 4 Englisch
INSERT INTO KRITERIUM VALUES (3130, 500,'versteht einfache Fragen und Aussagen und reagiert angemessen.',2016);
INSERT INTO KRITERIUM VALUES (3140, 500,'versteht Anweisungen im Unterrichtsalltag.',2016);
INSERT INTO KRITERIUM VALUES (3150, 500,'kann Hörtexten wesentliche Informationen entnehmen.',2016);
INSERT INTO KRITERIUM VALUES (3160, 500,'zeigt beim Sprechen ein angemessene Aussprache.',2016);
INSERT INTO KRITERIUM VALUES (3170, 500,'liest bekannte Arbeitsanweisungen selbständig und setzt sie um.',2016);
INSERT INTO KRITERIUM VALUES (3180, 500,'kann kurze bekannte Sätze vorlesen und verstehen.',2016);
INSERT INTO KRITERIUM VALUES (3190, 500,'stellt eigene kurze Texte nach Vorlage her.',2016);
INSERT INTO KRITERIUM VALUES (3200, 500,'benutzt Nachschlagemöglichkeiten.',2016);

-- 4 Sport
INSERT INTO KRITERIUM VALUES (3210, 510,'beteiligt sich gern an sportlichen Aktivitäten und zeigt Einsatzbereitschaft.',2016);
INSERT INTO KRITERIUM VALUES (3220, 510,'kann auch komplexere Spielideen umsetzen.',2016);
INSERT INTO KRITERIUM VALUES (3230, 510,'befolgt Spielregeln und verhält sich fair.',2016);
INSERT INTO KRITERIUM VALUES (3240, 510,'kann mit Sieg und Niederlage wertschätzend umgehen.',2016);
INSERT INTO KRITERIUM VALUES (3250, 510,'bewältigt grundlegende turnerische Bewegungsanforderungen.',2016);
INSERT INTO KRITERIUM VALUES (3260, 510,'kann Rhythmen in entsprechende Bewegungen umsetzen.',2016);
INSERT INTO KRITERIUM VALUES (3270, 510,'ist geschickt im Umgang mit dem Ball.',2016);
INSERT INTO KRITERIUM VALUES (3280, 510,'beherrscht die Grundformen leichtathletischer Bewegungsabläufe.',2016);

-- 4 Textiles Gestalten
INSERT INTO KRITERIUM VALUES (3290, 520,'zeigt  Motivation und Ausdauer bei der Umsetzung textilpraktischer Aufgaben.',2016);
INSERT INTO KRITERIUM VALUES (3300, 520,'gestaltet ihre/seine Textilarbeiten sorgfältig und ideenreich.',2016);
INSERT INTO KRITERIUM VALUES (3310, 520,'geht sachgerecht mit Werkzeugen und Materialien um und beachtet die Sicherheitshinweise.',2016);
INSERT INTO KRITERIUM VALUES (3320, 520,'berücksichtigt  bei der Gestaltung die vorgegebenen Kriterien.',2016);
INSERT INTO KRITERIUM VALUES (3330, 520,'zeigt Neugier, Kreativität und Geschicklichkeit.',2016);

-- 4 Werken
INSERT INTO KRITERIUM VALUES (3340, 530,'zeigt  Motivation und Ausdauer bei der Umsetzung werkpraktischer Aufgaben.',2016);
INSERT INTO KRITERIUM VALUES (3350, 530,'gestaltet ihre/seine Werkarbeiten sorgfältig und ideenreich.',2016);
INSERT INTO KRITERIUM VALUES (3360, 530,'achtet auf die korrekte Durchführung der Arbeitsschritte und Techniken.',2016);
INSERT INTO KRITERIUM VALUES (3370, 530,'geht sachgerecht mit Werkzeugen und Materialien um und beachtet die Sicherheitshinweise.',2016);
INSERT INTO KRITERIUM VALUES (3380, 530,'zeigt Neugier, Kreativität und Geschicklichkeit.',2016);

-- 4 Kunst
INSERT INTO KRITERIUM VALUES (3390, 540,'plant die Arbeitsschritte und setzt die Aufgabenstellungen nach den vorgegebenen Kriterien um.',2016);
INSERT INTO KRITERIUM VALUES (3400, 540,'zeigt Motivation, Konzentration und Ausdauer im Gestaltungsprozess.',2016);
INSERT INTO KRITERIUM VALUES (3410, 540,'erzielt ein Gestaltungsergebnis nach vorgegebenen Kriterien.',2016);
INSERT INTO KRITERIUM VALUES (3420, 540,'kann eigene Sichtweisen benennen und ansatzweise begründen.',2016);
INSERT INTO KRITERIUM VALUES (3430, 540,'geht sachgerecht mit den unterschiedlichen Gestaltungsmitteln um.',2016);
INSERT INTO KRITERIUM VALUES (3440, 540,'zeigt Ausdrucksfähigkeit und Kreativität.',2016);

-- 4 Musik
INSERT INTO KRITERIUM VALUES (3450, 550,'singt ausgewählte Lieder auswendig.',2016);
INSERT INTO KRITERIUM VALUES (3460, 550,'kann die Sing- und Sprechstimme vielfältig und kontrolliert einsetzen.',2016);
INSERT INTO KRITERIUM VALUES (3470, 550,'führt auf Metrum bezogene Bewegungen zur Musik aus.',2016);
INSERT INTO KRITERIUM VALUES (3480, 550,'erkennt und unterscheidet hörend einfache musikalische Strukturen und Formen.',2016);
INSERT INTO KRITERIUM VALUES (3490, 550,'beherrscht die Spieltechniken der Schulinstrumente.',2016);
INSERT INTO KRITERIUM VALUES (3500, 550,'kann die behandelten Grundelemente der Notenlehre notieren und reproduzieren.',2016);
INSERT INTO KRITERIUM VALUES (3510, 550,'kennt ausgewählte Komponisten und deren Werke.',2016);

-- 4 Religion
INSERT INTO KRITERIUM VALUES (3710, 560,'zeigt Interesse an den Themen des Religionsunterrichts.',2016);
INSERT INTO KRITERIUM VALUES (3720, 560,'bereichert den Unterricht mit themenbezogenen Beiträgen.',2016);
INSERT INTO KRITERIUM VALUES (3730, 560,'kann zu biblischen Geschichten eigene Ausdrucksformen entwickeln und darstellen.',2016);
INSERT INTO KRITERIUM VALUES (3740, 560,'kann eigene Vorstellungen zum Ausdruck bringen.',2016);
INSERT INTO KRITERIUM VALUES (3750, 560,'dokumentiert und gestaltet Arbeitsergebnisse.',2016);

-- Ab hier nur Daten zum Testen: Muss später gelöscht werden!!!!!
-- *****************************************************************************

