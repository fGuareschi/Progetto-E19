# Progetto-E19

Realizzazione di un clone del gioco Flappy Bird con alcune modifiche alle meccaniche di gioco e aggiunta di multiplayer a 2 giocatori. Altri aspetti:

* Resa grafica del gioco (libreria slick2d)
* Architettura peer to peer per il multigiocatore
* Diversi livelli di difficoltà per il singleplayer
* Implementazione di una classifica per i punteggi del singleplayer
* Scelta di temi grafici

Istruzioni per l' uso:
- Clonare il progetto E19 da GitHub a IntelliJ
- click su File -> Project Structure -> libraries
- click su + -> selezionare Java -> selezionare la cartella "slick2d"  -> ok
- sempre su Project structure nella sezione Modules, selezionare la cartella src all' interno della directory"flappy" selezionare mark directory as source.
- all' interno di src, nel package "main" fare click destro sulla classe "Launcher" e selezionare l' opzione "create Launcher.main..."
- nel pannello apertosi nella riga "VM OPTIONS" incollare la stringa seguente:  "-Djava.library.path=natives/"
- sempre nello stesso pannello nella riga "Working directory" al termine del path già inserito aggiungere "\flappy"
- eseguire il programma