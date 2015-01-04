BeerLover
==========

Projet de développement mobile à l'ESIEA Paris, année universitaire 2014/2015.
Licence WTFPL -- Patrick Pereira Almeida et Yann Privé


# Features

* Écran d'accueil simple affichant 5 bières au hasard.
* Gestion des utilisateurs (pseudo + email) sur le serveur et de ses favoris.
* Liste des bières classables par origine (France, Belgique...) ou par type (Bière blonde, brune...).
* Possibilité d'avoir les détails de chaque bière et de les ajouter (ou supprimer) de ses favoris.
* Page "favoris" accessible facilement depuis l'écran d'accueil.


# Notes de développement

* Un soin particulier a été apporté à la gestion des utilisateurs et l'application gère le cas où l'utilisateur existe déjà sur le serveur.

* Cette application pourrait être optimisée :
	* Niveau réseau : En téléchargeant la liste des bières et en la stockant en mémoire interne.
	* Niveau mise en forme des données : En utilisant une base de données SQLite mise à jour au lancement de l'application pour y mettre les bières. Nous avons découvert cette possibilité le dernier jour et donc elle n'a pas été mise en place.

Le fonctionnement actuel nous a permis de bien comprendre et maîtriser le téléchargement de contenu depuis une API REST et le traitement de données présentées sous forme de fichier JSON.
