//package projet;
//
//
//import java.io.*;
//
//
//class Commandes implements Runnable
//{
//  Serveur _Serveur; 
//  BufferedReader _in; 
//  String _strCommande=""; // contiendra la commande tapée
//  Thread _t; // contiendra le thread
//
//  Commandes(Serveur Serveur)
//  {
//    _Serveur=Serveur; // passage de local en global
//    // le flux d'entrée de la console sera géré plus pratiquement dans un BufferedReader
//    _in = new BufferedReader(new InputStreamReader(System.in));
//    _t = new Thread(this); // instanciation du thread
//    _t.start(); // demarrage du thread, la fonction run() est ici lancée
//  }
//
// 
//  public void run() 
//  {
//    try
//    {
//      // si aucune commande n'est tapée, on ne fait rien (bloquant sur _in.readLine())
//      while ((_strCommande=_in.readLine())!=null)
//      {
//        if (_strCommande.equalsIgnoreCase("quit")) // commande "quit" detectée ...
//          System.exit(0); // ... on ferme alors le serveur
//        else if(_strCommande.equalsIgnoreCase("total")) // commande "total" detectée ...
//        {
//          // ... on affiche le nombre de clients actuellement connectés
//          System.out.println("Nombre de connectes : "+_Serveur.getNbClients());
//          System.out.println("--------");
//        }
//        else
//        {
//          // si la commande n'est ni "total", ni "quit", on informe l'utilisateur et on lui donne une aide
//          System.out.println("Cette commande n'est pas supportee");
//          System.out.println("Quitter : \"quit\"");
//          System.out.println("Nombre de connectes : \"total\"");
//          System.out.println("--------");
//        }
//        System.out.flush(); // on affiche tout ce qui est en attente dans le flux
//      }
//    }
//    catch (IOException e) {}
//  }
//}
