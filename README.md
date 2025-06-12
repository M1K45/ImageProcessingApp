# Aplikacja do przetwarzania obrazów
> Autor: Michał Kaszowski

## Wstęp 
Napisano aplikację do przetwarzania równolegle obrazu wczytanego z pliku. Obraz można również skalować, obracać i zapisywać. Zapisywane również są logi po każdej operacji

## Kontrolery (okna aplikacji) 
Stworzono następujące kontrolery: 
- `HelloController`: Reprezentacja ekranu głównego aplikacji
![image](https://github.com/user-attachments/assets/16e7021f-6f53-491c-be5a-335ef1bf11ef)

- `ModalScaleController`: Modal odpowiedzialny za skalowanie obrazu
![image](https://github.com/user-attachments/assets/7787b39a-9765-409e-8314-7c36274b75c5)

- `ModalThresholdController`: Modal odpowiedzialny za progowanie obrazu
![image](https://github.com/user-attachments/assets/87b39db8-7d92-418a-8673-2dfa86e30c0e)

- `ModalWindowController`: Modal odpowiedzialny za zapis obrazu
![image](https://github.com/user-attachments/assets/921ab6f4-b36b-49e5-9a52-8391500f5241)

## Taski (implementacje równoległego przetwarzania obrazów)
- `EdgeDetectionTask`: implementacja wykrywania krawędzi
- `NegativeTask`: implementacja negatywu
- `ThresholdTask`: implementacja progowania

## Zewnętrzne aplikacje
Do automatycznego generowania FXML-a wykorzystano aplikację [SceneBuilder](https://gluonhq.com/products/scene-builder/)

## Biblioteki potrzebne do uruchomienia aplikacji i w jaki sposób je dodać
1. Sprawdź wersję JDK
W IntelliJ można to sprawdzić następująco:
-  kliknij w **File** -> **ProjectStructure** (skrót klawiszowy `Ctrl+Alt+Shift+S
-  Wybierz Project z lewej strony
W polu `SDK` będzie informacja o wersji np. `Oracle OpenJDK 24.0.1`
2. Pobierz JavaFX SDK ze strony: https://gluonhq.com/products/javafx/ w wersji odpowiadającej JDK
3. Rozpakuj plik na dysku
4. Dodaj JavaFX SDK do IntelliJ
- Otwórz **File** -> **Project Structure** -> **Libraries**
- Kliknij **+** -> **Java** i wskaż folder lib z JavaFX SDK np. (C:\javafx-sdk-20\lib)
- Zatwierdź dodanie klikając przycisk **Apply**
