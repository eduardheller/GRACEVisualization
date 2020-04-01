Titel: Erdschwerefeld
Team: Eduard Heller, Jerome Rohde, Dominique Schleef

Sämtliche Datendateien und Bibliotheken sind bereits in der Zip enthalten.

Einleitung:

Die Veränderung der Gravitation an der Erdoberfläche wird mit diesem Program veranschaulicht.
Dazu wird eine 3D-Erde mit verschiedenen Visualisierungstrategien gerendert.
Es werde hierfür Daten (vom Zeitraum 2002 bis 2016) vom GRACE Satelliten verarbeitet und auf dem Globus projeziert. 

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

Zum Ausführen ohne IDE liegt eine Executable VHCI.Jar im Ordner 'VHCI\dist' bereit. Ausführbar unter Windows. Für eine Linux umgebung liegen die LWJGL-Lib Dateien im 'Extern/LWJGL' Ordner bereit und müssten per Hand in den korrekten Ordner 'dist/lib' kopiert werden..
Ansonsten einfach als Netbeansprojekt öffnen und über die IDE starten.

UserInterface:

Die GUI sollte soweit selbsterklärend sein.
Sie besitzt diverse Konfigurationsmöglichkeiten um in den OpenGL-Thread und die Shader einzugreifen.
So ist es zum Beispiel möglich den Farbbereich Min/Max HSV-Farbbereich zur Laufzeit anzupassen.
Die Steuerung im OpenGL-Panel läuft über die Maus. 
Bei gedrückter 'ALT'-taste kann das MeshGitter angezeigt werden, sodass dann per Mausklick ein Vertex ausgewählt werden kann.
Wählt man über diesen Mechanismus einen Vertex aus, was einer Longtitude/Latitude koordinate entspricht, so wird ein weiteres Fenster geöffnet.
In dem neuen Fenster wird dann für die gewählte Koordinate ein Diagramm mit den Veränderungen über den gesamten Datensatz gerendert und angezeigt.

Visualisierungstechniken:

- Scenegraph
- HSV->RGB ColorMapping
- Volume Rendering
- Texturing
- Graph Drawing (Bar-, Pie-, Linediagram/-Chart )
- RayCasting
- Phong-Shading
- Color Linear Interpolation



