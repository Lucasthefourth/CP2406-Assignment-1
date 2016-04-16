import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class Javassignment {

    public static void main(String[] args) {

        CardsAttribute[] cardDeck = new CardsAttribute[54];         //create a deck, 54 is the number of cards

        List<String> cardList = new ArrayList<>();                  //contains the lines of the card.txt
        ArrayList<Integer> deckCode = new ArrayList<>();     //the deck, contains card code from 0-53 so far

        //Start of file reading
        String file_name = "card.txt";
        try {
            ReadFile file = new ReadFile(file_name);
            String[] cardLines = file.OpenFile();

            for (int i = 0; i < cardLines.length; i++) //loop the lines, so far, i should be 0-53, which is 54 cards
            {
                String currentCardStat[] = cardLines[i].split(",");     //
                // Name = 0,    Hardness = 1,   Gravity = 2,    Cleavage = 3,   Crustal Abundance = 4,  EcoValue = 5
                System.out.println("Name: " + currentCardStat[0] + ", Hardness: " + currentCardStat[1] + ", Gravity: " + currentCardStat[2]+ ", Cleavage: " + currentCardStat[3]); //testing

                cardList.add(cardLines[i]);
                deckCode.add(i);

                cardDeck[i] = new CardsAttribute();                     //add cards as object
                cardDeck[i].setCardDetails(cardLines[i]);               //set the card's line attributes inside the card

            }


        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        //end of file reading

        long seed = System.nanoTime();
        Collections.shuffle(deckCode, new Random(seed));        //shuffles deck


        boolean playerLoop = true;
        int playerNumber = 0;

        String playerNumberString;                  //enter number of player
        while (playerLoop){
            playerNumberString = JOptionPane.showInputDialog("Insert number of players from 3 - 5:");
            playerNumber = Integer.parseInt(playerNumberString);
            if (playerNumber <= 5 && playerNumber >= 3)
                playerLoop = false;
            else JOptionPane.showMessageDialog(null, "Wrong input");
        }

        Players[] playerArray = new Players[playerNumber];          //creating an array of players

        for (int i = 0; i<playerNumber; i++) {
            playerArray[i] = new Players();                         //use the Player object inside the array as player
            int properInt = i + 1;                                  //Player [0] --> player 1, playerArray[1] --> Player 2 etc
            playerArray[i].setName("Player " + properInt);


            for (int o = 0; o < 8; o++) {
            playerArray[i].addCard(deckCode.get(0));
            deckCode.remove(0);
            }
        }


        //testing purpose, printing every cards in player's hand

        for (int i = 0; i < playerArray.length; i++) {                                                          //loop for the number of players
            System.out.println("\n\n" + playerArray[i].getName());                                              //print player name
            System.out.println(playerArray[i].getAllCard());                                                    //print all handcard's code
            for (int o = 0; o < playerArray[i].getAllCard().size(); o++){                                       //loop for the number of cards in hand
                System.out.print("\n" + playerArray[i].getCard(o));                                             //print the card's code one by one
                System.out.print("  " + cardDeck[playerArray[i].getCard(o)].printCardDetail() + "  ");          //print the card detail line
                for (int p = 1; p <= 5;p++) {                                                                   //loop from 1 to 5, from hardness to economic value
                    System.out.print("  " + cardDeck[playerArray[i].getCard(o)].getCardAttributeValue(p));      //print the value of the card, one by one
                }
            }

        }   //end of test

        //      finally start game here?

        boolean gameContinue = true;    //is the game still continuing?
        int currentWinner = 0;          //who was the last hand's winner? by default is Player 1 at first, which is playerArray[0]
        String currentHandCardString = "";
        JOptionPane.showMessageDialog(null, "Game start! Player 1 goes first");

        //while (gameContinue){           //loop while game is still continuing

/* just a reference for class methods:
    Methods for players in game:                        (i is the player's number minus 1. Player 1 is 0, Player 2 is 1 etc)
        playerArray[i].getName()                        return player's name in string
        playerArray[i].addCard(int cardCode)            will add a card into the player's hand. The input is the card code. Get the card code from deck class
        playerArray[i].getAllCard()                     will return an arraylist of the CODE of the cards in player's hand
        playerArray[i].getCard(int cardPlace)           will return a card's CODE based on the position of the card. Eg: [1 ,2 ,3], input 0 and you get 1
        playerArray[i].chooseCard(int cardPlace)        is the same as getCard, but it will also REMOVE the card from the player's hand
    Methods for card deck in game:                      (i is the card's code. The first card is 0, and minus the supertrump, last is 53)
        cardDeck[i].printCardDetail()                   will return a line of the card's details. eg: 0 --> "Quartz,7,2.65,poor/none,high,moderate"
        cardDeck[i].getCardAttributes(int Type)         will return the STRING value of a certain attribute based on the line, NOT the numeric value or the comparable value
        cardDeck[i].getCardAttributeValue(int Type)     will return the DOUBLE VALUE of a certain att based on the line.

        dictionary for type:  Hardness = 1,   Gravity = 2,    Cleavage = 3,   Crustal Abundance = 4,  Economic Value = 5
 */
            int cardChoice = 0;     //get what the card's code is
            int cardChoiceValue;    //the value of the chosen card
            String[] handCardName = new String[playerArray[currentWinner].getCardSize()];
            int biggestCard;        //the biggest card's code
            int trumpType;          //the current card's trump type
            int biggestCardValue;   //the biggest card's value

            for (int i=0; i < playerArray[currentWinner].getCardSize();i++) {
                currentHandCardString += ("\nCard "+ i + ": " + cardDeck[playerArray[currentWinner].getCard(i)].printCardDetail());     //to print card details
                handCardName[i] = cardDeck[playerArray[currentWinner].getCard(i)].getName();
            }
            boolean repeatCardChoice = true;

            while (repeatCardChoice) {
                cardChoice = JOptionPane.showOptionDialog(null, playerArray[currentWinner].getName() + "\n\n" +
                        "(Name,Hardness,Gravity,Cleavage,Crustal Abundance,Economical Value)\n" + currentHandCardString, "Input a card!",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, handCardName, handCardName[0]);
                // ^this one is to print the option pane and get input based on the position of the card^

                if ((cardChoice < 0) || (cardChoice > playerArray[currentWinner].getCardSize()))  //making sure input is valid
                    JOptionPane.showMessageDialog(null, "Invalid input!");
                else repeatCardChoice = false;
            }

            JOptionPane.showMessageDialog(null,playerArray[currentWinner].chooseCard(cardChoice));
            JOptionPane.showMessageDialog(null, playerArray[currentWinner].getAllCard());

            biggestCard = playerArray[currentWinner].chooseCard(cardChoice);
            //biggestCardValue = cardDeck[biggestCard].getCardAttributeValue();






        //}


    }   //end of public static void
}       //end of class