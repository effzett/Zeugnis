CREATE TABLE "ZEUGNIS".ZEUGNIS
(
--		ID_ZEUGNIS INTEGER not null primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
		ID_ZEUGNIS INTEGER not null primary key,
		ID_SCHUELER INTEGER,
                NOTE_ARBEIT INTEGER,
                NOTE_SOZIAL INTEGER,
		FEHLTAGE INTEGER,
		FEHLTAGEOHNE INTEGER,
		ENTWICKLUNG VARCHAR(500),
		BEMERKUNG VARCHAR(200),
		HALBJAHR INTEGER,
		SCHULJAHR INTEGER,
);

CREATE TABLE "ZEUGNIS".SCHUELER
(
		ID_SCHUELER INTEGER not null primary key,
		NAME VARCHAR(20),
		VORNAME VARCHAR(20),
		GEBDATUM DATE,
		GEBORT VARCHAR(20),
		KLASSE VARCHAR(10),
		SCHULJAHR INTEGER
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
INSERT INTO LERNBEREICH VALUES (1, 'Arbeitsverhalten',						0,3,2017);
INSERT INTO LERNBEREICH VALUES (2, 'Sozialverhalten',						0,3,2017);

-- Jahrgangsstufe 1
INSERT INTO LERNBEREICH VALUES (3, 'Sprechen und Zuhören',					1,5,2017);
INSERT INTO LERNBEREICH VALUES (4, 'Schreiben',								1,5,2017);
INSERT INTO LERNBEREICH VALUES (5, 'Lesen - mit Texten und Medien umgehen',	1,5,2017);
INSERT INTO LERNBEREICH VALUES (6, 'Sprache und Sprachgebrauch untersuchen',1,5,2017);
INSERT INTO LERNBEREICH VALUES (7, 'Zahlen und Operationen',				1,5,2017);
INSERT INTO LERNBEREICH VALUES (8, 'Größen und Messen',						1,5,2017);
INSERT INTO LERNBEREICH VALUES (9, 'Raum und Form',							1,5,2017);
INSERT INTO LERNBEREICH VALUES (10, 'Sachunterricht',						1,5,2017);
INSERT INTO LERNBEREICH VALUES (11, 'Musik',								1,5,2017);
INSERT INTO LERNBEREICH VALUES (12, 'Religion',								1,5,2017);
INSERT INTO LERNBEREICH VALUES (13, 'Kunst',								1,5,2017);
INSERT INTO LERNBEREICH VALUES (14, 'Sport',								1,5,2017);

-- Jahrgangsstufe 2
INSERT INTO LERNBEREICH VALUES (15, 'Sprechen und Zuhören',					2,5,2017);
INSERT INTO LERNBEREICH VALUES (16, 'Schreiben',							2,5,2017);
INSERT INTO LERNBEREICH VALUES (17, 'Lesen - mit Texten und Medien umgehen',2,5,2017);
INSERT INTO LERNBEREICH VALUES (18, 'Sprache und Sprachgebrauch untersuchen',2,5,2017);
INSERT INTO LERNBEREICH VALUES (19, 'Zahlen und Operationen',				2,5,2017);
INSERT INTO LERNBEREICH VALUES (20, 'Größen und Messen',					2,5,2017);
INSERT INTO LERNBEREICH VALUES (21, 'Raum und Form',						2,5,2017);
INSERT INTO LERNBEREICH VALUES (22, 'Sachunterricht',						2,5,2017);
INSERT INTO LERNBEREICH VALUES (23, 'Musik',								2,5,2017);
INSERT INTO LERNBEREICH VALUES (24, 'Religion',								2,5,2017);
INSERT INTO LERNBEREICH VALUES (25, 'Kunst',								2,5,2017);
INSERT INTO LERNBEREICH VALUES (26, 'Sport',								2,5,2017);

-- Jahrgangsstufe 3
INSERT INTO LERNBEREICH VALUES (27, 'Sprechen und Zuhören',					3,5,2017);
INSERT INTO LERNBEREICH VALUES (28, 'Schreiben',							3,5,2017);
INSERT INTO LERNBEREICH VALUES (29, 'Lesen - mit Texten und Medien umgehen',3,5,2017);
INSERT INTO LERNBEREICH VALUES (30, 'Sprache und Sprachgebrauch untersuchen',3,5,2017);
INSERT INTO LERNBEREICH VALUES (31, 'Zahlen und Operationen',				3,5,2017);
INSERT INTO LERNBEREICH VALUES (32, 'Größen und Messen',					3,5,2017);
INSERT INTO LERNBEREICH VALUES (33, 'Raum und Form',						3,5,2017);
INSERT INTO LERNBEREICH VALUES (34, 'Sachunterricht',						3,5,2017);
INSERT INTO LERNBEREICH VALUES (35, 'Englisch',								3,5,2017);
INSERT INTO LERNBEREICH VALUES (36, 'Sport',								3,5,2017);
INSERT INTO LERNBEREICH VALUES (37, 'Textiles Gestalten',					3,5,2017);
INSERT INTO LERNBEREICH VALUES (38, 'Werken',								3,5,2017);
INSERT INTO LERNBEREICH VALUES (39, 'Kunst',								3,5,2017);
INSERT INTO LERNBEREICH VALUES (40, 'Musik',								3,5,2017);
INSERT INTO LERNBEREICH VALUES (41, 'Religion',								3,5,2017);

-- Jahrgangsstufe 4
INSERT INTO LERNBEREICH VALUES (42, 'Sprechen und Zuhören',					4,5,2017);
INSERT INTO LERNBEREICH VALUES (43, 'Schreiben',							4,5,2017);
INSERT INTO LERNBEREICH VALUES (44, 'Lesen - mit Texten und Medien umgehen',4,5,2017);
INSERT INTO LERNBEREICH VALUES (45, 'Sprache und Sprachgebrauch untersuchen',4,5,2017);
INSERT INTO LERNBEREICH VALUES (46, 'Zahlen und Operationen',				4,5,2017);
INSERT INTO LERNBEREICH VALUES (47, 'Größen und Messen',					4,5,2017);
INSERT INTO LERNBEREICH VALUES (48, 'Raum und Form',						4,5,2017);
INSERT INTO LERNBEREICH VALUES (49, 'Sachunterricht',						4,5,2017);
INSERT INTO LERNBEREICH VALUES (50, 'Englisch',								4,5,2017);
INSERT INTO LERNBEREICH VALUES (51, 'Sport',								4,5,2017);
INSERT INTO LERNBEREICH VALUES (52, 'Textiles Gestalten',					4,5,2017);
INSERT INTO LERNBEREICH VALUES (53, 'Werken',								4,5,2017);
INSERT INTO LERNBEREICH VALUES (54, 'Kunst',								4,5,2017);
INSERT INTO LERNBEREICH VALUES (55, 'Musik',								4,5,2017);
INSERT INTO LERNBEREICH VALUES (56, 'Religion',								4,5,2017);


--DB KRITERIUM
-- Kriterien
-- 0 Arbeitsverhalten
INSERT INTO KRITERIUM VALUES (1,  1,'folgt dem Unterricht interessiert und aufmerksam.',2017);
INSERT INTO KRITERIUM VALUES (2,  1,'beteiligt sich aktiv am Unterrichtsgeschehen.',2017);
INSERT INTO KRITERIUM VALUES (3,  1,'hält Gesprächsregeln ein.',2017);
INSERT INTO KRITERIUM VALUES (4,  1,'arbeitet zielstrebig und ausdauernd.',2017);
INSERT INTO KRITERIUM VALUES (5,  1,'hält ein angemessenes Arbeitstempo ein.',2017);
INSERT INTO KRITERIUM VALUES (6,  1,'erfasst Arbeitsanweisungen selbstständig und setzt sie um.',2017);
INSERT INTO KRITERIUM VALUES (7,  1,'führt Arbeiten sorgfältig und gewissenhaft aus.',2017);
INSERT INTO KRITERIUM VALUES (8,  1,'arbeitet erfolgreich in Partner- und Gruppenarbeit.',2017);
INSERT INTO KRITERIUM VALUES (9,  1,'hält das erforderliche Arbeitsmaterial zuverlässig bereit.',2017);
INSERT INTO KRITERIUM VALUES (10, 1,'führt Hefte und Mappen sorgfältig.',2017);
INSERT INTO KRITERIUM VALUES (11, 1,'fertigt Hausaufgaben zuverlässig an.',2017);

-- 0 Sozialverhalten
INSERT INTO KRITERIUM VALUES (12, 2,'verhält sich aufgeschlossen, freundlich und respektvoll.',2017);
INSERT INTO KRITERIUM VALUES (13, 2,'verhält sich rücksichtsvoll und hilfsbereit.',2017);
INSERT INTO KRITERIUM VALUES (14, 2,'nimmt Aufgaben und Pflichten für die Klasse wahr.',2017);
INSERT INTO KRITERIUM VALUES (15, 2,'hält Regeln und Vereinbarungen zuverlässig ein.',2017);
INSERT INTO KRITERIUM VALUES (16, 2,'zeigt Bereitschaft für das eigene Handeln einzustehen.',2017);
INSERT INTO KRITERIUM VALUES (17, 2,'verhält sich fair und einsichtig in Konfliktsituationen.',2017);
INSERT INTO KRITERIUM VALUES (18, 2,'erkennt unterschiedliche Meinungen an.',2017);

-- 1 Sprechen und Zuhören
INSERT INTO KRITERIUM VALUES (19, 3,'kennt Gesprächsregeln und wendet sie an.',2017);
INSERT INTO KRITERIUM VALUES (20, 3,'drückt sich sprachrichtig und verständlich aus.',2017);
INSERT INTO KRITERIUM VALUES (21, 3,'hört aufmerksam und konzentriert zu.',2017);
INSERT INTO KRITERIUM VALUES (22, 3,'beteiligt sich an Unterrichtsgesprächen.',2017);
INSERT INTO KRITERIUM VALUES (23, 3,'versteht mündliche Arbeitsanweisungen und kann diese umsetzen.',2017);
INSERT INTO KRITERIUM VALUES (24, 3,'verfügt über einen altersgemäßen Wortschatz.',2017);
INSERT INTO KRITERIUM VALUES (25, 3,'',2017);

-- 1 Schreiben
INSERT INTO KRITERIUM VALUES (26, 4,'schreibt in einer formklaren, gegliederten, lesbaren Schrift und hält die Linien ein.',2017);
INSERT INTO KRITERIUM VALUES (27, 4,'hält Wortgrenzen ein.',2017);
INSERT INTO KRITERIUM VALUES (28, 4,'schreibt lautgetreue Wörter.',2017);
INSERT INTO KRITERIUM VALUES (29, 4,'kann Wörter aus dem Übungsbereich richtig schreiben.',2017);
INSERT INTO KRITERIUM VALUES (30, 4,'schreibt Wörter und Sätze fehlerfrei ab.',2017);
INSERT INTO KRITERIUM VALUES (31, 4,'schreibt eigene Wörter.',2017);
INSERT INTO KRITERIUM VALUES (32, 4,'schreibt eigene Sätze.',2017);
INSERT INTO KRITERIUM VALUES (33, 4,'',2017);

-- 1 Lesen - mit Texten und Medien umgehen
INSERT INTO KRITERIUM VALUES (34, 5,'kann einfache Wörter lesen.',2017);
INSERT INTO KRITERIUM VALUES (35, 5,'kann Sätze und kurze Texte selbstständig lesen.',2017);
INSERT INTO KRITERIUM VALUES (36, 5,'liest sinnentnehmend und ist in der Lage, Fragen zu gelesenen Inhalten zu beantworten.',2017);
INSERT INTO KRITERIUM VALUES (37, 5,'liest geübte Texte flüssig und betont vor.',2017);
INSERT INTO KRITERIUM VALUES (38, 5,'versteht Aufgaben und bearbeitet diese selbstständig.',2017);
INSERT INTO KRITERIUM VALUES (39, 5,'',2017);

-- 1 Sprache und Sprachgebrauch untersuchen
INSERT INTO KRITERIUM VALUES (40, 6,'kennt alle eingeführten Buchstaben und die dazugehörigen Laute.',2017);
INSERT INTO KRITERIUM VALUES (41, 6,'',2017);

-- 1 Zahlen und Operationen
INSERT INTO KRITERIUM VALUES (42, 7,'kennt im Zahlenraum bis 20 alle Zahlen und kann diese richtig schreiben.',2017);
INSERT INTO KRITERIUM VALUES (43, 7,'kennt die Zeichen +, -, =, <, sowie > und kann sie richtig verwenden.',2017);
INSERT INTO KRITERIUM VALUES (44, 7,'kann Additionsaufgaben ohne Zehnerübergang im Zahlenraum bis 10 sicher lösen.',2017);
INSERT INTO KRITERIUM VALUES (45, 7,'kann Subtraktionsaufgaben ohne Zehnerübergang im Zahlenraum bis 10 sicher lösen.',2017);
INSERT INTO KRITERIUM VALUES (46, 7,'kann Additionsaufgaben ohne Zehnerübergang im Zahlenraum bis 20 sicher lösen.',2017);
INSERT INTO KRITERIUM VALUES (47, 7,'kann Additionsaufgaben mit Zehnerübergang im Zahlenraum bis 20 sicher lösen.',2017);
INSERT INTO KRITERIUM VALUES (48, 7,'kann Subtraktionsaufgaben ohne Zehnerübergang im Zahlenraum bis 20 sicher lösen.',2017);
INSERT INTO KRITERIUM VALUES (49, 7,'kann Subtraktionsaufgaben mit Zehnerübergang im Zahlenraum bis 20 sicher lösen.',2017);
INSERT INTO KRITERIUM VALUES (50, 7,'',2017);

-- 1 Größen und Messen
INSERT INTO KRITERIUM VALUES (51, 8,'kennt die erarbeiteten Einheiten des Bereichs „Geld“ und kann mit diesen auch in Sachsituationen rechnen.',2017);
INSERT INTO KRITERIUM VALUES (52, 8,'kann einfache Sachaufgaben lösen.',2017);
INSERT INTO KRITERIUM VALUES (53, 8,'',2017);

-- 1 Raum und Form
INSERT INTO KRITERIUM VALUES (54, 9,'kann geometrische Figuren nach Eigenschaften sortieren, benennen und sie in der Umwelt wiedererkennen.',2017);
INSERT INTO KRITERIUM VALUES (55, 9,'erkennt vorgegebene Muster.',2017);
INSERT INTO KRITERIUM VALUES (56, 9,'kann vorgegebene Muster selbstständig fortführen.',2017);
INSERT INTO KRITERIUM VALUES (57, 9,'kann Papier ordentlich und genau falten.',2017);
INSERT INTO KRITERIUM VALUES (58, 9,'',2017);

-- 1 SU
INSERT INTO KRITERIUM VALUES (59, 10,'zeigt Interesse an den Themen des Sachunterrichts.',2017);
INSERT INTO KRITERIUM VALUES (60, 10,'kann Sachzusammenhänge wiedergeben, erklären und übertragen.',2017);

-- 1 Musik
INSERT INTO KRITERIUM VALUES (61, 11,'zeigt Interesse am Singen und Musizieren.',2017);
INSERT INTO KRITERIUM VALUES (62, 11,'erfasst Lieder in Text und Melodie.',2017);
INSERT INTO KRITERIUM VALUES (63, 11,'ist in der Lage, Rhythmen wiederzugeben.',2017);
INSERT INTO KRITERIUM VALUES (64, 11,'kann mit Begleitinstrumenten umgehen.',2017);

-- 1 Religion
INSERT INTO KRITERIUM VALUES (65, 12,'nimmt engagiert am Religionsunterricht teil.',2017);
-- INSERT INTO KRITERIUM VALUES (66, 12,'zeigt Interesse an den Themen des Religionsunterrichts.',2017);
INSERT INTO KRITERIUM VALUES (67, 12,'kann elementare religiöse Feste und Bräuche benennen.',2017);

-- 1 Kunst
INSERT INTO KRITERIUM VALUES (68, 13,'zeichnet und malt mit Interesse.',2017);
INSERT INTO KRITERIUM VALUES (69, 13,'zeigt Kreativität.',2017);
INSERT INTO KRITERIUM VALUES (70, 13,'kann mit den fachspezifischen Arbeitsmitteln umgehen.',2017);

-- 1 Sport
INSERT INTO KRITERIUM VALUES (71, 14,'kann eine Spielidee erfassen, erkennen und umsetzen.',2017);
INSERT INTO KRITERIUM VALUES (72, 14,'beweist Einsatzfreude.',2017);
INSERT INTO KRITERIUM VALUES (73, 14,'zeigt körperliche Geschicklichkeit.',2017);
INSERT INTO KRITERIUM VALUES (74, 14,'zeigt faires Verhalten.',2017);

-- 2 Sprechen und Zuhören
INSERT INTO KRITERIUM VALUES (75, 15,'kennt Gesprächsregeln und wendet sie an.',2017);
INSERT INTO KRITERIUM VALUES (76, 15,'äußert sich sachbezogen.',2017);
INSERT INTO KRITERIUM VALUES (77, 15,'drückt sich sprachrichtig und verständlich aus.',2017);
INSERT INTO KRITERIUM VALUES (78, 15,'kann Inhalte zuhörend verstehen und gezielt nachfragen.',2017);
INSERT INTO KRITERIUM VALUES (79, 15,'vertritt eigene Meinungen.',2017);
INSERT INTO KRITERIUM VALUES (80, 15,'nimmt in Unterrichtsgesprächen Bezug auf andere.',2017);
INSERT INTO KRITERIUM VALUES (81, 15,'trägt Gedichte auswendig und sinngestaltend vor.',2017);
INSERT INTO KRITERIUM VALUES (82, 15,'',2017);

-- 2 Schreiben
INSERT INTO KRITERIUM VALUES (83, 16,'schreibt in einer formklaren, gegliederten, lesbaren Schrift und hält die Linien ein.',2017);
INSERT INTO KRITERIUM VALUES (84, 16,'gestaltet einen Text übersichtlich.',2017);
INSERT INTO KRITERIUM VALUES (85, 16,'kann Wörter aus dem Übungsbereich richtig schreiben.',2017);
INSERT INTO KRITERIUM VALUES (86, 16,'wendet grundlegende Rechtschreibregeln an.',2017);
INSERT INTO KRITERIUM VALUES (87, 16,'kennt das Alphabet und kann einfache Wörter im Wörterbuch nachschlagen.',2017);
INSERT INTO KRITERIUM VALUES (88, 16,'schreibt Texte fehlerfrei ab.',2017);
INSERT INTO KRITERIUM VALUES (89, 16,'schreibt eigene Sätze.',2017);
INSERT INTO KRITERIUM VALUES (90, 16,'schreibt Texte, in denen die Sätze inhaltlich aufeinander bezogen sind.',2017);
INSERT INTO KRITERIUM VALUES (91, 16,'',2017);

-- 2 Lesen - mit Texten und Medien umgehen
INSERT INTO KRITERIUM VALUES (92, 17,'kann Sätze und  kurze Texte selbstständig lesen.',2017);
INSERT INTO KRITERIUM VALUES (93, 17,'liest sinnentnehmend und ist in der Lage, Fragen zu gelesenen Inhalten zu beantworten.',2017);
INSERT INTO KRITERIUM VALUES (94, 17,'liest geübte Texte flüssig und betont vor.',2017);
INSERT INTO KRITERIUM VALUES (95, 17,'liest ungeübte Texte flüssig und betont vor.',2017);
INSERT INTO KRITERIUM VALUES (96, 17,'versteht schriftliche Aufgaben und bearbeitet diese selbstständig.',2017);
INSERT INTO KRITERIUM VALUES (97, 17,'',2017);

-- 2 Sprache und Sprachgebrauch untersuchen
INSERT INTO KRITERIUM VALUES (98, 18,'kann die behandelten Wortarten unterscheiden und benennen.',2017);
INSERT INTO KRITERIUM VALUES (99, 18,'kann Satzarten unterscheiden und richtige Satzschlusszeichen setzen.',2017);
INSERT INTO KRITERIUM VALUES (100, 18,'',2017);

-- 2 Zahlen und Operationen
INSERT INTO KRITERIUM VALUES (101, 19,'kennt im Zahlenraum bis 100 alle Zahlen und kann diese richtig schreiben.',2017);
INSERT INTO KRITERIUM VALUES (102, 19,'kann Größer- und Kleinerbeziehungen angeben und notieren.',2017);
INSERT INTO KRITERIUM VALUES (103, 19,'vergleicht, strukturiert und zerlegt Zahlen.',2017);
INSERT INTO KRITERIUM VALUES (104, 19,'kann Additionsaufgaben ohne Zehnerübergang im Zahlenraum bis 100 sicher lösen.',2017);
INSERT INTO KRITERIUM VALUES (105, 19,'kann Subtraktionsaufgaben ohne Zehnerübergang im Zahlenraum bis 100 sicher lösen.',2017);
INSERT INTO KRITERIUM VALUES (106, 19,'kann Additionsaufgaben mit Zehnerübergang im Zahlenraum bis 100 sicher lösen.',2017);
INSERT INTO KRITERIUM VALUES (107, 19,'kann Subtraktionsaufgaben mit Zehnerübergang im Zahlenraum bis 100 sicher lösen.',2017);
INSERT INTO KRITERIUM VALUES (108, 19,'kann die Aufgaben des Einmaleins sicher lösen.',2017);
INSERT INTO KRITERIUM VALUES (109, 19,'kann Divisionsaufgaben im Zahlenraum bis 100 sicher lösen.',2017);
INSERT INTO KRITERIUM VALUES (110, 19,'erklärt Rechenwege und stellt diese dar.',2017);
INSERT INTO KRITERIUM VALUES (111, 19,'kann mathematische Zusammenhänge entdecken und beschreiben.',2017);
INSERT INTO KRITERIUM VALUES (112, 19,'wendet Rechenstrategien an.',2017);
INSERT INTO KRITERIUM VALUES (113, 19,'',2017);

-- 2 Größen und Messen
INSERT INTO KRITERIUM VALUES (114, 20,'kennt die erarbeiteten Einheiten des Bereichs „Geld“ und kann mit diesen auch in Sachsituationen sicher rechnen.',2017);
INSERT INTO KRITERIUM VALUES (115, 20,'kennt die erarbeiteten Einheiten des Bereichs „Zeit“ und kann mit diesen auch in Sachsituationen sicher rechnen.',2017);
INSERT INTO KRITERIUM VALUES (116, 20,'kennt die erarbeiteten Einheiten des Bereichs „Längen“ und kann mit diesen auch in Sachsituationen sicher rechnen.',2017);
INSERT INTO KRITERIUM VALUES (117, 20,'kann einfache Sachaufgaben lösen und passende Antworten zu Fragestellungen finden.',2017);
INSERT INTO KRITERIUM VALUES (118, 20,'',2017);

-- 2 Raum und Form
INSERT INTO KRITERIUM VALUES (119, 21,'kann geometrische Figuren nach Eigenschaften sortieren, benennen und sie in der Umwelt wiedererkennen.',2017);
INSERT INTO KRITERIUM VALUES (120, 21,'kann geometrische Körper nach Eigenschaften sortieren, benennen und sie in der Umwelt wiedererkennen.',2017);
INSERT INTO KRITERIUM VALUES (121, 21,'erkennt vorgegebene Muster.',2017);
INSERT INTO KRITERIUM VALUES (122, 21,'kann vorgegebene Muster selbstständig fortführen.',2017);
INSERT INTO KRITERIUM VALUES (123, 21,'zeichnet sauber mit dem Lineal.',2017);
INSERT INTO KRITERIUM VALUES (124, 21,'kann Papier ordentlich und genau falten.',2017);
INSERT INTO KRITERIUM VALUES (125, 21,'',2017);

-- 2 SU
INSERT INTO KRITERIUM VALUES (126, 22,'zeigt Interesse an den Themen des Sachunterrichts.',2017);
INSERT INTO KRITERIUM VALUES (127, 22,'kann Sachzusammenhänge wiedergeben, erklären und übertragen.',2017);
INSERT INTO KRITERIUM VALUES (128, 22,'kann selbstständig einfache Versuche durchführen und Beobachtungen verständlich darstellen.',2017);

-- 2 Musik
INSERT INTO KRITERIUM VALUES (129, 23,'zeigt Interesse am Singen und Musizieren.',2017);
INSERT INTO KRITERIUM VALUES (130, 23,'erfasst Lieder in Text und Melodie.',2017);
INSERT INTO KRITERIUM VALUES (131, 23,'ist in der Lage, Rhythmen wiederzugeben.',2017);
INSERT INTO KRITERIUM VALUES (132, 23,'kann mit Begleitinstrumenten umgehen.',2017);

-- 2 Religion
INSERT INTO KRITERIUM VALUES (133, 24,'nimmt engagiert am Religionsunterricht teil.',2017);
INSERT INTO KRITERIUM VALUES (134, 24,'kann religiöse Geschichten verstehen und wiedergeben.',2017);
INSERT INTO KRITERIUM VALUES (135, 24,'kann elementare religiöse Feste und Bräuche benennen und erläutern.',2017);

-- 2 Kunst
INSERT INTO KRITERIUM VALUES (136, 25,'zeichnet und malt mit Engagement.',2017);
INSERT INTO KRITERIUM VALUES (137, 25,'zeigt Ausdrucksfähigkeit und Kreativität.',2017);
INSERT INTO KRITERIUM VALUES (138, 25,'zeigt ein Form- und Farbgefühl.',2017);
INSERT INTO KRITERIUM VALUES (139, 25,'kann mit den fachspezifischen Arbeitsmitteln umgehen.',2017);

-- 2 Sport
INSERT INTO KRITERIUM VALUES (140, 26,'kann eine Spielidee erfassen, erkennen und umsetzen.',2017);
INSERT INTO KRITERIUM VALUES (141, 26,'beweist Einsatzfreude.',2017);
INSERT INTO KRITERIUM VALUES (142, 26,'zeigt körperliche Geschicklichkeit.',2017);
INSERT INTO KRITERIUM VALUES (143, 26,'setzt gestellte Bewegungsaufgaben um.',2017);
INSERT INTO KRITERIUM VALUES (144, 26,'ist geschickt im Umgang mit dem Ball.',2017);
INSERT INTO KRITERIUM VALUES (145, 26,'zeigt faires Verhalten.',2017);

-- 3 Sprechen und Zuhören
INSERT INTO KRITERIUM VALUES (146, 27,'wendet Gesprächsregeln an.',2017);
INSERT INTO KRITERIUM VALUES (147, 27,'äußert sich sachbezogen.',2017);
INSERT INTO KRITERIUM VALUES (148, 27,'drückt sich sprachrichtig und verständlich aus.',2017);
INSERT INTO KRITERIUM VALUES (149, 27,'kann Inhalte zuhörend verstehen und gezielt nachfragen.',2017);
INSERT INTO KRITERIUM VALUES (150, 27,'vertritt und begründet eigene Meinungen.',2017);
INSERT INTO KRITERIUM VALUES (151, 27,'nimmt in Unterrichtsgesprächen Bezug auf andere.',2017);
INSERT INTO KRITERIUM VALUES (152, 27,'präsentiert Lernergebnisse kriterienbezogen.',2017);
INSERT INTO KRITERIUM VALUES (153, 27,'trägt Texte auswendig und sinngestaltend vor.',2017);
INSERT INTO KRITERIUM VALUES (154, 27,'',2017);

-- 3 Schreiben
INSERT INTO KRITERIUM VALUES (155, 28,'schreibt in einer flüssigen und gut lesbaren Handschrift.',2017);
INSERT INTO KRITERIUM VALUES (156, 28,'gestaltet einen Text ordentlich und übersichtlich.',2017);
INSERT INTO KRITERIUM VALUES (157, 28,'kann Wörter aus dem Übungsbereich richtig schreiben.',2017);
INSERT INTO KRITERIUM VALUES (158, 28,'wendet grundlegende Rechtschreibregeln  und -strategien  an.',2017);
INSERT INTO KRITERIUM VALUES (159, 28,'nutzt das Wörterbuch und beherrscht Nachschlagetechniken.',2017);
INSERT INTO KRITERIUM VALUES (160, 28,'schreibt Texte fehlerfrei ab.',2017);
INSERT INTO KRITERIUM VALUES (161, 28,'kann eigene Schreibideen entwickeln und dabei Planungsmethoden nutzen.',2017);
INSERT INTO KRITERIUM VALUES (162, 28,'verfasst verständliche und strukturierte Texte.',2017);
INSERT INTO KRITERIUM VALUES (163, 28,'wendet Überarbeitungsmethoden an.',2017);
INSERT INTO KRITERIUM VALUES (164, 28,'',2017);

-- 3 Lesen - mit Texten und Medien umgehen
INSERT INTO KRITERIUM VALUES (165, 29,'kann altersgemäße Texte sinnverstehend lesen.',2017);
INSERT INTO KRITERIUM VALUES (166, 29,'entnimmt Texten Informationen und zieht Schlussfolgerungen.',2017);
INSERT INTO KRITERIUM VALUES (167, 29,'liest geübte Texte flüssig und betont vor.',2017);
INSERT INTO KRITERIUM VALUES (168, 29,'liest ungeübte Texte flüssig und betont vor.',2017);
INSERT INTO KRITERIUM VALUES (169, 29,'versteht schriftliche Handlungsanweisungen und setzt diese selbstständig um.',2017);
INSERT INTO KRITERIUM VALUES (170, 29,'',2017);

-- 3 Sprache und Sprachgebrauch untersuchen
INSERT INTO KRITERIUM VALUES (171, 30,'kann die behandelten Wortarten unterscheiden und benennen.',2017);
INSERT INTO KRITERIUM VALUES (172, 30,'kann Satzarten unterscheiden und richtige Satzschlusszeichen setzen.',2017);
INSERT INTO KRITERIUM VALUES (173, 30,'kann die behandelten Zeitformen unterscheiden und benennen.',2017);
INSERT INTO KRITERIUM VALUES (174, 30,'kann die Regeln zum Einsatz der wörtlichen Rede anwenden.',2017);
INSERT INTO KRITERIUM VALUES (175, 30,'',2017);

-- 3 Zahlen und Operationen
INSERT INTO KRITERIUM VALUES (176, 31,'hat alle Aufgaben des kleinen Einmaleins automatisiert.',2017);
INSERT INTO KRITERIUM VALUES (177, 31,'beherrscht die Divisionsaufgaben im Zahlenraum bis 100.',2017);
INSERT INTO KRITERIUM VALUES (178, 31,'kann Divisionsaufgaben mit Rest berechnen.',2017);
INSERT INTO KRITERIUM VALUES (179, 31,'kann sich im Zahlenraum bis 1000 sicher orientieren.',2017);
INSERT INTO KRITERIUM VALUES (180, 31,'kann im Zahlenraum bis 1000 Additionsaufgaben halbschriftlich lösen.',2017);
INSERT INTO KRITERIUM VALUES (181, 31,'kann im Zahlenraum bis 1000 Subtraktionsaufgaben halbschriftlich lösen.',2017);
INSERT INTO KRITERIUM VALUES (182, 31,'kann im Zahlenraum bis 1000 Additionsaufgaben schriftlich lösen.',2017);
INSERT INTO KRITERIUM VALUES (183, 31,'kann im Zahlenraum bis 1000 Subtraktionsaufgaben schriftlich lösen.',2017);
INSERT INTO KRITERIUM VALUES (184, 31,'kann im Zahlenraum bis 1000 Multiplikationsaufgaben halbschriftlich lösen.',2017);
INSERT INTO KRITERIUM VALUES (185, 31,'kann im Zahlenraum bis 1000 Divisionsaufgaben halbschriftlich lösen.',2017);
INSERT INTO KRITERIUM VALUES (186, 31,'kann Kopfrechenaufgaben lösen.',2017);
INSERT INTO KRITERIUM VALUES (187, 31,'erklärt Rechenwege und stellt diese dar.',2017);
INSERT INTO KRITERIUM VALUES (188, 31,'kann mathematische Zusammenhänge entdecken und beschreiben.',2017);
INSERT INTO KRITERIUM VALUES (189, 31,'wendet Rechenstrategien an.',2017);
INSERT INTO KRITERIUM VALUES (190, 31,'',2017);

-- 3 Größen und Messen
INSERT INTO KRITERIUM VALUES (191, 32,'kennt die erarbeiteten Einheiten des Bereichs „Geld“ und kann mit diesen auch in Sachsituationen sicher rechnen.',2017);
INSERT INTO KRITERIUM VALUES (192, 32,'kennt die erarbeiteten Einheiten des Bereichs „Zeit“ und kann mit diesen auch in Sachsituationen sicher rechnen.',2017);
INSERT INTO KRITERIUM VALUES (193, 32,'kennt die erarbeiteten Einheiten des Bereichs „Längen“ und kann mit diesen auch in Sachsituationen sicher rechnen.',2017);
INSERT INTO KRITERIUM VALUES (194, 32,'kennt die erarbeiteten Einheiten des Bereichs „Gewichte“ und kann mit diesen auch in Sachsituationen sicher rechnen.',2017);
INSERT INTO KRITERIUM VALUES (195, 32,'kann Sachaufgaben lösen und passende Fragestellungen zu Sachverhalten finden.',2017);
INSERT INTO KRITERIUM VALUES (196, 32,'',2017);

-- 3 Raum und Form
INSERT INTO KRITERIUM VALUES (197, 33,'kann geometrische Figuren mit Fachbegriffen benennen und beschreiben.',2017);
INSERT INTO KRITERIUM VALUES (198, 33,'kann achsensymmetrische Figuren erkennen, untersuchen und zeichnen.',2017);
INSERT INTO KRITERIUM VALUES (199, 33,'kann geometrische Körper mit Fachbegriffen benennen und beschreiben.',2017);
INSERT INTO KRITERIUM VALUES (200, 33,'löst Aufgaben und Probleme mit räumlichen Bezügen konkret und in der Vorstellung.',2017);
INSERT INTO KRITERIUM VALUES (201, 33,'kann vorgegebene Muster selbstständig fortführen.',2017);
INSERT INTO KRITERIUM VALUES (202, 33,'',2017);

-- 3 SU
INSERT INTO KRITERIUM VALUES (203, 34,'zeigt Interesse an den Themen des Sachunterrichts.',2017);
INSERT INTO KRITERIUM VALUES (204, 34,'bereichert den Unterricht mit sachbezogenen Beiträgen.',2017);
INSERT INTO KRITERIUM VALUES (205, 34,'kann gelernte Sachverhalte und Fachbegriffe anwenden.',2017);
INSERT INTO KRITERIUM VALUES (206, 34,'dokumentiert Arbeitsergebnisse übersichtlich und systematisch.',2017);
INSERT INTO KRITERIUM VALUES (207, 34,'wendet fachspezifische Methoden und Arbeitsweisen an.',2017);
INSERT INTO KRITERIUM VALUES (208, 34,'kann Informationen aus Sachtexten entnehmen, erklären und bewerten. ',2017);
INSERT INTO KRITERIUM VALUES (209, 34,'präsentiert Arbeitsergebnisse in geeigneter Form.',2017);

-- 3 Englisch
INSERT INTO KRITERIUM VALUES (210, 35,'erkennt beim Zuhören Schlüsselwörter wieder.',2017);
INSERT INTO KRITERIUM VALUES (211, 35,'versteht einfache Fragen und Aussagen und reagiert angemessen.',2017);
INSERT INTO KRITERIUM VALUES (212, 35,'versteht den Kontext von Dialogen und Geschichten.',2017);
INSERT INTO KRITERIUM VALUES (213, 35,'spricht mithilfe bekannter Redemittel in Zusammenhängen.',2017);
INSERT INTO KRITERIUM VALUES (214, 35,'kann Abbildungen in einfachen Wörtern/Sätzen frei beschreiben',2017);
INSERT INTO KRITERIUM VALUES (215, 35,'kann Schriftbilder dem bekannten Klang/Lautbild zuordnen.',2017);
INSERT INTO KRITERIUM VALUES (216, 35,'kann Wörter und kurze bekannte Sätze vorlesen und verstehen.',2017);
INSERT INTO KRITERIUM VALUES (217, 35,'schreibt bekannte Wörter richtig nach Vorlage ab.',2017);

-- 3 Sport
INSERT INTO KRITERIUM VALUES (218, 36,'beteiligt sich gern an sportlichen Aktivitäten und zeigt Einsatzbereitschaft.',2017);
INSERT INTO KRITERIUM VALUES (219, 36,'kann auch komplexere Spielideen umsetzen.',2017);
INSERT INTO KRITERIUM VALUES (220, 36,'befolgt Spielregeln und verhält sich fair.',2017);
INSERT INTO KRITERIUM VALUES (221, 36,'kann mit Sieg und Niederlage wertschätzend umgehen.',2017);
INSERT INTO KRITERIUM VALUES (222, 36,'setzt gestellte Bewegungsaufgaben erfolgreich um.',2017);
INSERT INTO KRITERIUM VALUES (223, 36,'beherrscht die Grundformen leichtathletischer Bewegungsabläufe.',2017);
INSERT INTO KRITERIUM VALUES (224, 36,'bewältigt grundlegende turnerische Bewegungsanforderungen.',2017);
INSERT INTO KRITERIUM VALUES (225, 36,'ist geschickt im Umgang mit dem Ball.',2017);

-- 3 Textiles Gestalten
INSERT INTO KRITERIUM VALUES (226, 37,'zeigt  Motivation und Ausdauer bei der Umsetzung textilpraktischer Aufgaben.',2017);
INSERT INTO KRITERIUM VALUES (227, 37,'gestaltet ihre/seine Textilarbeiten sorgfältig und ideenreich.',2017);
INSERT INTO KRITERIUM VALUES (228, 37,'geht sachgerecht mit Werkzeugen und Materialien um und beachtet die Sicherheitshinweise.',2017);
INSERT INTO KRITERIUM VALUES (229, 37,'berücksichtigt  bei der Gestaltung die vorgegebenen Kriterien.',2017);
INSERT INTO KRITERIUM VALUES (230, 37,'zeigt Neugier, Kreativität und Geschicklichkeit.',2017);

-- 3 Werken
INSERT INTO KRITERIUM VALUES (231, 38,'zeigt  Motivation und Ausdauer bei der Umsetzung werkpraktischer Aufgaben.',2017);
INSERT INTO KRITERIUM VALUES (232, 38,'gestaltet ihre/seine Werkarbeiten sorgfältig und ideenreich.',2017);
INSERT INTO KRITERIUM VALUES (233, 38,'achtet auf die korrekte Durchführung der Arbeitsschritte und Techniken.',2017);
INSERT INTO KRITERIUM VALUES (234, 38,'geht sachgerecht mit Werkzeugen und Materialien um und beachtet die Sicherheitshinweise. ',2017);
INSERT INTO KRITERIUM VALUES (235, 38,'zeigt Neugier, Kreativität und Geschicklichkeit.',2017);

-- 3 Kunst
INSERT INTO KRITERIUM VALUES (236, 39,'plant die Arbeitsschritte und setzt die Aufgabenstellungen nach den vorgegebenen Kriterien um.',2017);
INSERT INTO KRITERIUM VALUES (237, 39,'zeigt Motivation, Konzentration und Ausdauer im Gestaltungsprozess.',2017);
INSERT INTO KRITERIUM VALUES (238, 39,'erzielt ein Gestaltungsergebnis nach vorgegebenen Kriterien.',2017);
INSERT INTO KRITERIUM VALUES (239, 39,'geht sachgerecht mit den unterschiedlichen Gestaltungsmitteln um. ',2017);
INSERT INTO KRITERIUM VALUES (240, 39,'zeigt Ausdrucksfähigkeit und Kreativität.',2017);

-- 3 Musik
INSERT INTO KRITERIUM VALUES (241, 40,'singt ausgewählte Lieder auswendig.',2017);
INSERT INTO KRITERIUM VALUES (242, 40,'führt auf Metrum bezogene Bewegungen zur Musik aus.',2017);
INSERT INTO KRITERIUM VALUES (243, 40,'erkennt und unterscheidet hörend einfache musikalische Strukturen und Formen.',2017);
INSERT INTO KRITERIUM VALUES (244, 40,'kennt ausgewählte Schul- und Orchesterinstrumente und kann sie hörend unterscheiden und benennen.',2017);
INSERT INTO KRITERIUM VALUES (245, 40,'beherrscht die Spieltechniken der Schulinstrumente.',2017);

-- 3 Religion
INSERT INTO KRITERIUM VALUES (246, 41,'zeigt Interesse an den Themen des Religionsunterrichts.',2017);
INSERT INTO KRITERIUM VALUES (247, 41,'bereichert den Unterricht mit themenbezogenen Beiträgen.',2017);
INSERT INTO KRITERIUM VALUES (248, 41,'kann religiöse Geschichten benennen und erklären.',2017);
INSERT INTO KRITERIUM VALUES (249, 41,'kann religiöse Räume und Feste, Zeichen, Symbole und Rituale benennen.',2017);
INSERT INTO KRITERIUM VALUES (250, 41,'kann eigene Vorstellungen zum Ausdruck bringen.',2017);
INSERT INTO KRITERIUM VALUES (251, 41,'dokumentiert und gestaltet Arbeitsergebnisse.',2017);

-- 4 Sprechen und Zuhören
INSERT INTO KRITERIUM VALUES (252, 42,'wendet Gesprächsregeln an.',2017);
INSERT INTO KRITERIUM VALUES (253, 42,'äußert sich sachbezogen.',2017);
INSERT INTO KRITERIUM VALUES (254, 42,'drückt sich sprachrichtig und verständlich aus.',2017);
INSERT INTO KRITERIUM VALUES (255, 42,'kann Inhalte zuhörend verstehen und gezielt nachfragen.',2017);
INSERT INTO KRITERIUM VALUES (256, 42,'vertritt und begründet eigene Meinungen.',2017);
INSERT INTO KRITERIUM VALUES (257, 42,'nimmt in Unterrichtsgesprächen Bezug auf andere.',2017);
INSERT INTO KRITERIUM VALUES (258, 42,'präsentiert Lernergebnisse kriterienbezogen.',2017);
INSERT INTO KRITERIUM VALUES (259, 42,'trägt Texte auswendig und sinngestaltend vor.',2017);
INSERT INTO KRITERIUM VALUES (260, 42,'',2017);

-- 4 Schreiben
INSERT INTO KRITERIUM VALUES (261, 43,'schreibt in einer flüssigen und gut lesbaren Handschrift.',2017);
INSERT INTO KRITERIUM VALUES (262, 43,'gestaltet einen Text ordentlich und übersichtlich.',2017);
INSERT INTO KRITERIUM VALUES (263, 43,'kann Wörter aus dem Übungsbereich richtig schreiben.',2017);
INSERT INTO KRITERIUM VALUES (264, 43,'wendet grundlegende Rechtschreibregeln und -strategien  an.',2017);
INSERT INTO KRITERIUM VALUES (265, 43,'nutzt das Wörterbuch und beherrscht Nachschlagetechniken.',2017);
INSERT INTO KRITERIUM VALUES (266, 43,'schreibt Texte fehlerfrei ab.',2017);
INSERT INTO KRITERIUM VALUES (267, 43,'kann eigene Schreibideen entwickeln und dabei Planungsmethoden nutzen.',2017);
INSERT INTO KRITERIUM VALUES (268, 43,'verfasst verständliche und strukturierte Texte.',2017);
INSERT INTO KRITERIUM VALUES (269, 43,'wendet Überarbeitungsmethoden an.',2017);
INSERT INTO KRITERIUM VALUES (270, 43,'',2017);

-- 4 Lesen - mit Texten und Medien umgehen
INSERT INTO KRITERIUM VALUES (271, 44,'kann altersgemäße Texte sinnverstehend lesen.',2017);
INSERT INTO KRITERIUM VALUES (272, 44,'kann unterschiedlichen Medien Informationen entnehmen und Schlussfolgerungen ziehen.',2017);
INSERT INTO KRITERIUM VALUES (273, 44,'liest geübte Texte flüssig und betont vor.',2017);
INSERT INTO KRITERIUM VALUES (274, 44,'liest ungeübte Texte flüssig und betont vor.',2017);
INSERT INTO KRITERIUM VALUES (275, 44,'kann grundlegende Texterschließungsverfahren anwenden.',2017);
INSERT INTO KRITERIUM VALUES (276, 44,'versteht schriftliche Handlungsanweisungen und setzt diese selbstständig um.',2017);
INSERT INTO KRITERIUM VALUES (277, 44,'',2017);

-- 4 Sprache und Sprachgebrauch untersuchen
INSERT INTO KRITERIUM VALUES (278, 45,'kann die behandelten Wortarten unterscheiden und benennen.',2017);
INSERT INTO KRITERIUM VALUES (279, 45,'unterscheidet Satzarten und setzt richtige Satzschlusszeichen.',2017);
INSERT INTO KRITERIUM VALUES (280, 45,'kann die eingeführten Kommaregeln anwenden.',2017);
INSERT INTO KRITERIUM VALUES (281, 45,'erkennt und benennt die behandelten Satzglieder.',2017);
INSERT INTO KRITERIUM VALUES (282, 45,'kann die behandelten Zeitformen erkennen und gezielt anwenden.',2017);
INSERT INTO KRITERIUM VALUES (283, 45,'kann die wörtlichen Rede in eigenen Texten anwenden.',2017);
INSERT INTO KRITERIUM VALUES (284, 45,'',2017);

-- 4 Zahlen und Operationen
INSERT INTO KRITERIUM VALUES (285, 46,'kann sich im Zahlenraum bis 1 Million sicher orientieren.',2017);
INSERT INTO KRITERIUM VALUES (286, 46,'kann im Zahlenraum bis 1 Million Multiplikationsaufgaben halbschriftlich lösen.',2017);
INSERT INTO KRITERIUM VALUES (287, 46,'kann im Zahlenraum bis 1 Million Divisionsaufgaben halbschriftlich lösen.',2017);
INSERT INTO KRITERIUM VALUES (288, 46,'beherrscht das Verfahren der schriftlichen Multiplikation.',2017);
INSERT INTO KRITERIUM VALUES (289, 46,'beherrscht das Verfahren der schriftlichen Division.',2017);
INSERT INTO KRITERIUM VALUES (290, 46,'kann Kopfrechenaufgaben lösen.',2017);
INSERT INTO KRITERIUM VALUES (291, 46,'erklärt Rechenwege und stellt diese dar.',2017);
INSERT INTO KRITERIUM VALUES (292, 46,'kann mathematische Zusammenhänge entdecken und beschreiben.',2017);
INSERT INTO KRITERIUM VALUES (293, 46,'wendet Rechenstrategien an.',2017);
INSERT INTO KRITERIUM VALUES (294, 46,'',2017);

-- 4 Größen und Messen
INSERT INTO KRITERIUM VALUES (295, 47,'kennt die erarbeiteten Einheiten des Bereichs „Zeit“ und kann mit diesen auch in Sachsituationen sicher rechnen.',2017);
INSERT INTO KRITERIUM VALUES (296, 47,'kennt die erarbeiteten Einheiten des Bereichs „Längen“ und kann mit diesen auch in Sachsituationen sicher rechnen.',2017);
INSERT INTO KRITERIUM VALUES (297, 47,'kennt die erarbeiteten Einheiten des Bereichs „Gewichte“ und kann mit diesen auch in Sachsituationen sicher rechnen.',2017);
INSERT INTO KRITERIUM VALUES (298, 47,'kann Sachaufgaben lösen und passende Fragestellungen zu Sachverhalten finden.',2017);
INSERT INTO KRITERIUM VALUES (299, 47,'',2017);

-- 4 Raum und Form
INSERT INTO KRITERIUM VALUES (300, 48,'kann kopfgeometrische Aufgaben lösen.',2017);
INSERT INTO KRITERIUM VALUES (301, 48,'kann geometrische Figuren mit Fachbegriffen benennen, beschreiben und zeichnen.',2017);
INSERT INTO KRITERIUM VALUES (302, 48,'kann mithilfe des Geodreiecks senkrechte und parallele Linien erkennen und zeichnen.',2017);
INSERT INTO KRITERIUM VALUES (303, 48,'kann Schrägbilder lesen und zeichnen.',2017);
INSERT INTO KRITERIUM VALUES (304, 48,'kann den Zirkel fachgerecht einsetzen.',2017);
INSERT INTO KRITERIUM VALUES (305, 48,'',2017);

-- 4 SU
INSERT INTO KRITERIUM VALUES (306, 49,'zeigt Interesse an den Themen des Sachunterrichts.',2017);
INSERT INTO KRITERIUM VALUES (307, 49,'bereichert den Unterricht mit sachbezogenen Themen.',2017);
INSERT INTO KRITERIUM VALUES (308, 49,'kann gelernte Sachverhalte und Fachbegriffe anwenden.',2017);
INSERT INTO KRITERIUM VALUES (309, 49,'dokumentiert Arbeitsergebnisse übersichtlich und systematisch.',2017);
INSERT INTO KRITERIUM VALUES (310, 49,'wendet fachspezifische Methoden und Arbeitsweisen an.',2017);
INSERT INTO KRITERIUM VALUES (311, 49,'kann Informationen aus Sachtexten entnehmen, interpretieren und bewerten.',2017);
INSERT INTO KRITERIUM VALUES (312, 49,'präsentiert Arbeitsergebnisse in geeigneter Form.',2017);

-- 4 Englisch
INSERT INTO KRITERIUM VALUES (313, 50,'versteht einfache Fragen und Aussagen und reagiert angemessen.',2017);
INSERT INTO KRITERIUM VALUES (314, 50,'versteht Anweisungen im Unterrichtsalltag.',2017);
INSERT INTO KRITERIUM VALUES (315, 50,'kann Hörtexten wesentliche Informationen entnehmen.',2017);
INSERT INTO KRITERIUM VALUES (316, 50,'zeigt beim Sprechen ein angemessene Aussprache.',2017);
INSERT INTO KRITERIUM VALUES (317, 50,'liest bekannte Arbeitsanweisungen selbständig und setzt sie um.',2017);
INSERT INTO KRITERIUM VALUES (318, 50,'kann kurze bekannte Sätze vorlesen und verstehen.',2017);
INSERT INTO KRITERIUM VALUES (319, 50,'stellt eigene kurze Texte nach Vorlage her.',2017);
INSERT INTO KRITERIUM VALUES (320, 50,'benutzt Nachschlagemöglichkeiten.',2017);

-- 4 Sport
INSERT INTO KRITERIUM VALUES (321, 51,'beteiligt sich gern an sportlichen Aktivitäten und zeigt Einsatzbereitschaft.',2017);
INSERT INTO KRITERIUM VALUES (322, 51,'kann auch komplexere Spielideen umsetzen.',2017);
INSERT INTO KRITERIUM VALUES (323, 51,'befolgt Spielregeln und verhält sich fair.',2017);
INSERT INTO KRITERIUM VALUES (324, 51,'kann mit Sieg und Niederlage wertschätzend umgehen.',2017);
INSERT INTO KRITERIUM VALUES (325, 51,'bewältigt grundlegende turnerische Bewegungsanforderungen.',2017);
INSERT INTO KRITERIUM VALUES (326, 51,'kann Rhythmen in entsprechende Bewegungen umsetzen.',2017);
INSERT INTO KRITERIUM VALUES (327, 51,'ist geschickt im Umgang mit dem Ball.',2017);
INSERT INTO KRITERIUM VALUES (328, 51,'beherrscht die Grundformen leichtathletischer Bewegungsabläufe.',2017);

-- 4 Textiles Gestalten
INSERT INTO KRITERIUM VALUES (329, 52,'zeigt  Motivation und Ausdauer bei der Umsetzung textilpraktischer Aufgaben.',2017);
INSERT INTO KRITERIUM VALUES (330, 52,'gestaltet ihre/seine Textilarbeiten sorgfältig und ideenreich.',2017);
INSERT INTO KRITERIUM VALUES (331, 52,'geht sachgerecht mit Werkzeugen und Materialien um und beachtet die Sicherheitshinweise.',2017);
INSERT INTO KRITERIUM VALUES (332, 52,'berücksichtigt  bei der Gestaltung die vorgegebenen Kriterien.',2017);
INSERT INTO KRITERIUM VALUES (333, 52,'zeigt Neugier, Kreativität und Geschicklichkeit.',2017);

-- 4 Werken
INSERT INTO KRITERIUM VALUES (334, 53,'zeigt  Motivation und Ausdauer bei der Umsetzung werkpraktischer Aufgaben.',2017);
INSERT INTO KRITERIUM VALUES (335, 53,'gestaltet ihre/seine Werkarbeiten sorgfältig und ideenreich.',2017);
INSERT INTO KRITERIUM VALUES (336, 53,'achtet auf die korrekte Durchführung der Arbeitsschritte und Techniken.',2017);
INSERT INTO KRITERIUM VALUES (337, 53,'geht sachgerecht mit Werkzeugen und Materialien um und beachtet die Sicherheitshinweise.',2017);
INSERT INTO KRITERIUM VALUES (338, 53,'zeigt Neugier, Kreativität und Geschicklichkeit.',2017);

-- 4 Kunst
INSERT INTO KRITERIUM VALUES (339, 54,'plant die Arbeitsschritte und setzt die Aufgabenstellungen nach den vorgegebenen Kriterien um.',2017);
INSERT INTO KRITERIUM VALUES (340, 54,'zeigt Motivation, Konzentration und Ausdauer im Gestaltungsprozess.',2017);
INSERT INTO KRITERIUM VALUES (341, 54,'erzielt ein Gestaltungsergebnis nach vorgegebenen Kriterien.',2017);
INSERT INTO KRITERIUM VALUES (342, 54,'kann eigene Sichtweisen benennen und ansatzweise begründen.',2017);
INSERT INTO KRITERIUM VALUES (343, 54,'geht sachgerecht mit den unterschiedlichen Gestaltungsmitteln um.',2017);
INSERT INTO KRITERIUM VALUES (344, 54,'zeigt Ausdrucksfähigkeit und Kreativität.',2017);

-- 4 Musik
INSERT INTO KRITERIUM VALUES (345, 55,'singt ausgewählte Lieder auswendig.',2017);
INSERT INTO KRITERIUM VALUES (346, 55,'kann die Sing- und Sprechstimme vielfältig und kontrolliert einsetzen.',2017);
INSERT INTO KRITERIUM VALUES (347, 55,'führt auf Metrum bezogene Bewegungen zur Musik aus.',2017);
INSERT INTO KRITERIUM VALUES (348, 55,'erkennt und unterscheidet hörend einfache musikalische Strukturen und Formen.',2017);
INSERT INTO KRITERIUM VALUES (349, 55,'beherrscht die Spieltechniken der Schulinstrumente.',2017);
INSERT INTO KRITERIUM VALUES (350, 55,'kann die behandelten Grundelemente der Notenlehre notieren und reproduzieren.',2017);
INSERT INTO KRITERIUM VALUES (351, 55,'kennt ausgewählte Komponisten und deren Werke.',2017);

-- 4 Religion
INSERT INTO KRITERIUM VALUES (371, 56,'zeigt Interesse an den Themen des Religionsunterrichts.',2017);
INSERT INTO KRITERIUM VALUES (372, 56,'bereichert den Unterricht mit themenbezogenen Beiträgen.',2017);
INSERT INTO KRITERIUM VALUES (373, 56,'kann zu biblischen Geschichten eigene Ausdrucksformen entwickeln und darstellen.',2017);
INSERT INTO KRITERIUM VALUES (374, 56,'kann eigene Vorstellungen zum Ausdruck bringen.',2017);
INSERT INTO KRITERIUM VALUES (375, 56,'dokumentiert und gestaltet Arbeitsergebnisse.',2017);

-- Ab hier nur Daten zum Testen: Muss später gelöscht werden!!!!!
-- *****************************************************************************

