# Esercitazione 2

## Descrizione del contenuto

- JsonToDB: contiene l'applicazione java standalone che estrae il contenuto dal file `linee.json` e popola il DB
- MapViewer: contiene la webapp per la visualizzazione dei dati delle linee sulla mappa

E' stato necessario modificare il file `schema.sql` includendo il `sequenceNumber` nella chiave primaria della tabella `BusLineStop`

## Installazione

Prerequisito: avere docker installato.

```bash
# creare un volume docker con il nome
docker volume create Laboratorio2
# per fare il build assicurarsi di essere nella cartella con il Dockerfile
# ai/lab2 è il nome dell'immagine
docker build -t ai/lab2 .
# controllare che il container esista listando le images
docker images
# esegue l'immagine dando nome postgis all'istanza in esecuzione
# -d modalità detached
docker run --name postgis -v Laboratorio2:/datadb -p 5432:5432 -d ai/lab2
# controllare che sia in esecuzione e con la porta 5432
docker ps
# eseguire bash in modalità interattiva dentro il container
docker exec -it postgis bash
# aprire il db trasporti con utente postgres
psql -U postgres -d trasporti
# mostra le tabelle del db
\d
# comando per uscire
\q
```

Altri comandi utili:
- far partire container esistente: `docker start postgis`
- eliminare un container: `docker rm NAME`
- fermare un container: `docker stop NAME`
- elencare tutti i container (anche non in esecuzione): `docker ps -a`
- eliminare una immagine: `docker rmi NAME`
- eliminare un volume: `docker volume rm NAME`
- eliminare tuttu i container: `docker rm $(docker ps -aq)`

## Connessione a database dentro docker in windows

Dato che con localhost la connessione viene rifiutata, è necessario creare un file `db_ip.txt` nella cartella `resources` di entrambi i progetti.

In questo file bisogna scrivere nella prima riga il proprio indirizzo IP (non 127.0.0.1 ma quello assegnato dal dhcp della rete a cui si è connessi).

Se il file non è presente, la connessione viene fatta verso localhost.
