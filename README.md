# VelocitySystem
ProxySystem für Velocity by ByRaudy

## Download
- https://kotori.network/download/velocitysystem-1.0.jar/

## Support
- https://dsc.gg/kotori.network

### Generell
- Das VelocitySystem ist simple gesagt ein ProxySystem für Velocity.
- Viele Server wollen einen sicheren Proxy, nutzen aber BungeeCord oder Waterfall.
  - Das aus häufig einem Grund: Es gibt keine guten ProxySysteme für Velocity.
  - Das ändert sich hier und jetzt!
- In meinem VelocitySystem sind viele nützliche und hilfreiche Features für Minecraft Server eingebaut.

### GitHub & Code
- Du darfst jederzeit das VelocitySystem als jar, sowie als Source runterladen, und verändern.
  - Dir ist jedoch nicht gestattet, das VelocitySystem zu froken & eine pull request zu starten.
    - Warum? Ich möchte das VelocitySystem weiterhin als Hobby bzw. Nebenprojekt weiterentwickeln.
  - Ausnahmen: @NachGecodet
- Du darfst das VelocitySystem niemals, egal ob umgeschrieben oder nicht, als dein eigenes ausgeben.

### Was beeinhält das VelocitySystem?
- Ban-, Mute- sowie KickSystem
  - CheckSystem
- Eigenes Wartungsarbeiten System
- MOTD & Tablist (sogar mit farbverläufen möglich | Dafür 'gradients' in der config auf 'true' setzen)
- Einen eigenen /end Befehl, damit du Spieler vor dem restart des Proxies zu warnen
- /hub und /ping Befehl (Die Lobby ist in der config.json einstellbar)

### Permissions
- Ban & Mute: velocitysystem.ban
- Kick: velocitysystem.kick
- Check: velocitysystem.check
- Unban: velocitysystem.unban
- Unmute: velocitysystem.unmute
- Wärend der Wartungsarbeiten joinen können: velocitysystem.maintenancejoin

### Config
- Zuerst: Wie generiere ich die ganzen Datein?
  - Step 1: Ziehe die 'velocitysystem-1.0.jar' in deinen Plugins Ordner von Velocity.
  - Step 2: Starte deinen Velocity Server.
  - Step 3: Du wirst Fehler in der Konsole sehen, daher stoppe deinen Velocity Server nun.
  - Step 4: Guck in deinen 'plugins' Ordner. Dort wirst du nun einen weiteren Ordner sehen, welcher 'VelocitySystem' heißt.
  - Step 5: Gehe nun in diesen Ordner. Da siehst du jetzt alle Datein die du brauchst.
  - Step 6: Ändere alles so ab wie du willst.
  - Step 7: Starte deinen Velocity Server.
  - Step 8: Fertig! :)
- Alles ist in der 'config.json' sowie in der 'messages.json' einstellbar
- Das VelocitySystem brauch aufgrund der Abspeicherung der Bans und Mutes eine Datenbankverbindung
  - Diese bitte in der 'mysql.json' eintragen

### Meine Todo:
- Teamchat
- ReportSystem
- Mehrere Sprachen
