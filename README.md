# 🖥️ **Resource Monitor**

## 📚 **Projektbeschreibung**  
**Resource Monitor** ist eine plattformübergreifende Java-Anwendung zur Überwachung von Systemressourcen. Sie liest Hardware- und Leistungsdaten von Windows- und Mac-Systemen aus, speichert diese in einer MySQL-Datenbank und stellt historische Daten für Analysen bereit.

---

## 🚀 **Funktionen**

### 🛠️ **Systemressourcen überwachen**
- **CPU-Informationen:** Kerne, Frequenz, Auslastung  
- **RAM-Informationen:** Maximaler Speicher, Auslastung  
- **Festplatten-Informationen:** Gesamtkapazität, belegter Speicher, Typ  

### 💾 **Datenbank-Integration**
- Speicherung von Systemdaten in einer MySQL-Datenbank  
- Periodische Aktualisierung der Daten (minütlich und täglich)  
- Historische Auslastungsdaten für Analysen  

### 📊 **Plattformübergreifend**
- Unterstützung für **Windows** und **macOS**

---

## 🛠️ **Technischer Überblick**

### 📁 **Projektstruktur**
```plaintext
src/
├── de.pp.rm/
│   ├── Main.java       // Hauptprogramm
│   ├── Windows.java    // Windows-spezifische Methoden
│   ├── Mac.java        // Mac-spezifische Methoden
│
├── de.pp.rm.data/
│   ├── CPU.java        // CPU-Datenmodell
│   ├── RAM.java        // RAM-Datenmodell
│   ├── Drive.java      // Festplatten-Datenmodell
│   ├── PC.java         // PC-Datenmodell
│   ├── Usage.java      // Nutzungsdatenmodell
│
├── de.pp.rm.database/
│   ├── Database.java        // Datenbank-Verwaltung
│   ├── CPURepository.java   // CPU-DB-Methoden
│   ├── RAMRepository.java   // RAM-DB-Methoden
│   ├── DriveRepository.java // Drive-DB-Methoden
│   ├── PCRepository.java    // PC-DB-Methoden
│
resources/
├── ResourceMonitor.sql   // Datenbankschema
```

### 🧩 **Technologien**
- **Programmiersprache:** Java 11+  
- **Build-Tool:** Maven  
- **Datenbank:** MySQL  
- **Versionskontrolle:** Git  

---

## ⚙️ **Installation & Einrichtung**

### 📥 **Voraussetzungen**
- Java 11+  
- Maven  
- MySQL-Datenbank  

### 💻 **Projekt klonen**
```bash
git clone https://github.com/deinBenutzername/ResourceMonitor.git
cd ResourceMonitor
```

### 📊 **Datenbank einrichten**
1. Datenbankschema ausführen:
```sql
source /pfad/zu/ResourceMonitor.sql
```
2. Benutzer und Zugriffsrechte konfigurieren.

### 🛠️ **Build-Prozess**
```bash
mvn clean install
```

### ▶️ **Starten der Anwendung**
```bash
java -jar target/ResourceMonitor-0.0.1-SNAPSHOT.jar
```

---

## 📖 **Anwendungsablauf**

1. **Systeminformationen abrufen:** Das Programm liest minütlich CPU-, RAM- und Festplattendaten aus.  
2. **Datenbankaktualisierung:** Die erfassten Daten werden regelmäßig in der MySQL-Datenbank gespeichert.  
3. **Langzeitüberwachung:** Tägliche Updates von statischen Informationen wie Anzahl der CPU-Kerne und Festplattenkapazität.  

---

## 📄 **Konfigurationsdateien**

- **`Database.java`**  
   Konfiguriere die Datenbankverbindung in `Database.java`.  

   ```java
   static String url = "jdbc:mysql://<DEINE-DATENBANK-URL>/ResourceMonitor";
   static String username = "<BENUTZERNAME>";
   static String password = "<PASSWORT>";
   ```

---

## 🧠 **Mitwirkende**

- **Autor:** Steffen Kästner
- **E-Mail:** [mail@steffen-kaestner.de](mailto:mail@steffen-kaestner.de)

---

## 🐞 **Fehlerbehebung**

- **Verbindungsprobleme zur Datenbank:** Überprüfe die Zugangsdaten in `Database.java`.  
- **Systemdaten werden nicht aktualisiert:** Stelle sicher, dass die entsprechenden Plattformbefehle in `Windows.java` und `Mac.java` korrekt sind.

---

## 📜 **Lizenz**

Dieses Projekt steht unter der **MIT-Lizenz**. Weitere Informationen findest du in der [LICENSE](LICENSE)-Datei.

---

## 🌟 **Danksagung**

- Java Community  
- Open-Source-Bibliotheken und Tools  

---

## 📣 **Feedback**

Hast du Feedback, Fragen oder Vorschläge? Erstelle ein Issue oder kontaktiere mich direkt unter [mail@steffen-kaestner.de](mailto:mail@steffen-kaestner.de).