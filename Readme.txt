Titel: Erdschwerefeld
Team: Eduard Heller, Jerome Rohde, Dominique Schleef

S�mtliche Datendateien und Bibliotheken sind bereits in der Zip enthalten.

Einleitung:

Die Ver�nderung der Gravitation an der Erdoberfl�che wird mit diesem Program veranschaulicht.
Dazu wird eine 3D-Erde mit verschiedenen Visualisierungstrategien gerendert.
Es werde hierf�r Daten (vom Zeitraum 2002 bis 2016) vom GRACE Satelliten verarbeitet und auf dem Globus projeziert. 

Datasource:
	Landmassendaten sind hieraus bezogen:
	https://grace.jpl.nasa.gov/data/get-data/monthly-mass-grids-land/	

	Ozeanmassendaten sind hieraus bezogen:
	https://grace.jpl.nasa.gov/data/get-data/monthly-mass-grids-ocean/


Libraries:
	JOML		https://github.com/JOML-CI/JOML
	LWJGL3		https://www.lwjgl.org/customize
	JFreeChart	https://sourceforge.net/projects/jfreechart/files/

Sprache: Java
IDE: Netbeans 8.2
Entwickelt und getest unter Windows7 und Windows10

Zum Ausf�hren ohne IDE liegt eine Executable VHCI.Jar im Ordner 'VHCI\dist' bereit. Ausf�hrbar unter Windows. F�r eine Linux umgebung liegen die LWJGL-Lib Dateien im 'Extern/LWJGL' Ordner bereit und m�ssten per Hand in den korrekten Ordner 'dist/lib' kopiert werden..
Ansonsten einfach als Netbeansprojekt �ffnen und �ber die IDE starten.

UserInterface:

Die GUI sollte soweit selbsterkl�rend sein.
Sie besitzt diverse Konfigurationsm�glichkeiten um in den OpenGL-Thread und die Shader einzugreifen.
So ist es zum Beispiel m�glich den Farbbereich Min/Max HSV-Farbbereich zur Laufzeit anzupassen.
Die Steuerung im OpenGL-Panel l�uft �ber die Maus. 
Bei gedr�ckter 'ALT'-taste kann das MeshGitter angezeigt werden, sodass dann per Mausklick ein Vertex ausgew�hlt werden kann.
W�hlt man �ber diesen Mechanismus einen Vertex aus, was einer Longtitude/Latitude koordinate entspricht, so wird ein weiteres Fenster ge�ffnet.
In dem neuen Fenster wird dann f�r die gew�hlte Koordinate ein Diagramm mit den Ver�nderungen �ber den gesamten Datensatz gerendert und angezeigt.

Visualisierungstechniken:

- Scenegraph
- HSV->RGB ColorMapping
- Volume Rendering
- Texturing
- Graph Drawing (Bar-, Pie-, Linediagram/-Chart )
- RayCasting
- Phong-Shading
- Color Linear Interpolation



