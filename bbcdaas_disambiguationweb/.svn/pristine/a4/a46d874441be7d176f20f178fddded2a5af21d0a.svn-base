<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="de.bbcdaas.disambiguationweb.constants.Constants" %>

<div>
	
	<br/>
	<b><spring:message code="wikipedia.help.caption"/></b>
	<br/><br/>
	
	<div style="max-width: 800px;">
		Disambiguierung (Begriffsklärung) ist die Auflösung sprachlicher Mehrdeutigkeiten. Über das gegebene Interface
		wird ein Webservice angesprochen, der mit Hilfe einer Menge von angegebenen Termen sowie Parametern die Bedeutung
		der einzelnen Terme versucht aufzulösen. Als Datenbasis wird dafür ein Dump der deutschsprachigen Wikipedia verwendet.
	</div>
	
	<br/><br/>
	
	<span>
		Nutzung:
		<br/><br/>
		1. Terme eingeben (mindestens 2)<br/>
			1.5 Optional: Parameter anpassen<br/>
		2. "Disambiguiere" drücken<br/>
		3. Auf Ergebniss warten (Dauer variiert je nach Parameter)<br/>
	</span>
	
	<br/><br/>
	
	<div style="max-width: 800px;">
		Funktionsweise:
		<br/><br/>
		Bei der Disambiguierung wird sich die Struktur der Wikipediaseite zu Nutze gemacht: Jeder Artikel hat eine
		eindeutige URI, in dem sich der zu erklärende / zu definierende Begriff befindet. Mehrdeutige Begriffe haben
		mehrere Artikel, wobei die URI der jeweiligen Artikel durch Klammerung einer Kategorie oder eines sonstigen
		weiteren Begriffes von den URIs der anderen Artikeln unterscheidbar ist. Auch gibt es hierbei eine URI, die
		auf eine Begriffsklärungsseite verweist, in der sich die URLs der anderen Artikel des mehrdeutigen Begriffs befinden.
		Der gesuchte Begriff kann auch als Teil einer URI auftreten.
		Dabei ist der Begriff entweder durch Leerzeichen von anderen Wörtern innerhalb der URL getrennt oder ein Teil
		eines anderen Begriffes (Beispiel für Teil eines anderen Wortes ist der Begriff "Kartoffel" bei dem Wort
		"Kartoffelvollernemaschine"). Gibt es mehrere Schreibweisen eines Begriffs, so befindet sich nur unter einer
		der URIs der Schreibweisen ein Artikel, alle anderen in Wikipedia bekannten Schreibweisen fungieren als Weiterleitungen
		auf diesen Artikel.<br/>
		Über die Parameter kann die Qualität und die Geschwindigkeit des Disambiguierungsprozesses beeinflusst werden.
		Die Ausgabe des Ergebnisses besteht aus zwei unterschiedlichen Arten der Darstellung: Zum einen werden zu jedem
		eingegebenen Begriff die nach einer Wertung sortierten gefundenen Artikel angezeigt. Hier kann man erkennen, welcher Artikel
		das jeweilige Suchwort im Kontext der anderen eingegebenen Begriffe am besten beschreibt. Die Zweite Art der
		Darstellung zeigt die gefundenen Artikel nur nach der Wertung sortiert an, wobei der Artikel mit der höchsten
		Wertung am besten den Kontext beschreibt, der durch alle eingegebenen Begriffe gemeinsam beschrieben wird.<br/>
		Die Disambiguierung selbst besteht zum einen aus dem Schritt der Kandidatensuche, und zum anderen aus der Bewertung
		der gefundenen Kandidaten. Die Kandidaten, also die Wikipedia-URIs mit ihren jeweiligen Wikipedia-Artikeln, werden
		anhand ihres Titels aus der Menge aller Artikel gefiltert. Der Titel ist der jenige Teil der URI, der nicht konstant ist
		(z.b. bei der URI 'http://de.wikipedia.org/wiki/Java' wäre dies 'Java', bei der URI 'http://de.wikipedia.org/wiki/Java_(Technik)'
		wäre dies 'Java_(Technik)'). Für die Filterung werden Pattern verwendet, die alle möglichen Varianten von Titeln abdecken.
		Jedem Pattern wird eine Gewichtung zugewiesen, die dann jeweils für die gefundenen Wikipedia-URIs übernommen wird. 
		Im nächsten Schritt werden die gefundenen Wikipedia-Artikel bewertet. Dazu werden nacheinander mehrere Bewertungsverfahren
		angewendet, die jeweils eine Gewichtung erhalten können. Aus der Verrechnung aller Bewertungen ergibt sich eine Gesamtwertung,
		nach der die Ergebnislisten sortiert werden.
	</div>
	
	<br/><br/>
	
	<div>
		Parameter:
		<br/><br/>
		1. Kandidatensuche:<br/>
		'maxTermDocuments': Wie viele Dokumente sollen maximal für einen Begriff gefunden werden<br/>
		'maxTermDocumentsPerPattern': Wie viele Dokumente sollen maximal für ein Pattern für einen Begriff gefunden werden. Diese Zahl ist
		kleiner oder gleich maxTermDocuments (es werden nie mehr als maxTermDocuments gefunden), nach dem Pattern mit der höchsten Wertung
		wird zuerst gesucht.<br/>
		'alternativeURIRating': Welche Bewertung sollen Dokumente erhalten, die über Wikipedia-Begriffsklärungsseiten gefunden wurden.<br/>
		'Pattern-Wertungen': Die Wertung für jeden einzelnen Pattern, nach dem in den Titeln gesucht wird. Die Pattern werden absteigend nach
		ihrer Wertung zur Suche herangezogen.<br/>
		<br/>
		2. Bewertung:<br/>
		'multimatchingDocumentsRatingAddend': Um welchen Wert soll die Wertung für ein Dokument erhöht werden, wenn ein anderer der
		Suchbegriffe in seinem Titel bzw in seinem Beschreibungstext auftritt.<br/>
		'Scorer': Hier wird die jeweilige Gewichtung der Bewertungen angegeben, die über unterschiedliche Verfahren gewonnen wird. 
	</div>
	
</div>