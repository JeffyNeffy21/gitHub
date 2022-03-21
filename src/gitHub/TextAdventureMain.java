package gitHub;
import java.util.*;

/* A skeleton program for a text adventure game */
/* some other parts, like rooms, will be explained in class */

public class TextAdventureMain {

//	static int INVSIZE = 10; //size of inventory	

	//instance variables
	HashMap<String,Room> roomList = new HashMap<String,Room>();
	HashMap<String, Item> itemList = new HashMap<String,Item>(); //list of all item objects

	//the inventory could be an array
	HashMap<String,Integer> inventory = new HashMap<>();
	String currentRoom;
	Player player;
	static boolean alive=true;
	static boolean won=false;
	
	int turns = 0;

	public static void main(String[]args){
		new AdventureMain();
	}

	AdventureMain() {

		boolean playing = true;
		String command = "";

		setup(); //create all objects needed, including map; print intro. message
		
		lookAtRoom(); //display information about the current room

		/***** MAIN GAME LOOP *****/
		while (playing) {

			if (!alive) {
				System.out.println("You lost, you suck, poor you.");
				playing=false;
			}
			if (won) {
				System.out.println("Congrats, you actually won!     ... Or did you?");
				playing=false;
			}

			
			command = getCommand();

			playing = parseCommand(command);

			//check to see if player has died (in whichever various ways the player can die)

			//check to see if the player has won the game
			
		}

		// does anything need to be done after th emain game loop exits?

	}
	void lookAtRoom() {
		Room r=new Room(currentRoom);
		if (r.isVisited) {
			
		}
	}
	void setup() {
		System.out.println("Welcome to the game! You are not gonna have a good time! Good luck on your adventure!");
		Room.setupRooms(roomList);
		// ... more stuff ...
		currentRoom = "clearing";
	}

	String getCommand() {
		Scanner sc = new Scanner(System.in);		
		String text = sc.nextLine();
		if (text.length() == 0) text = "qwerty"; //default command
		//sc.close();
		return text;
	}

	
	boolean parseCommand(String text) {

		/***** PREPROCESSING *****/
		//P1. 
		text = text.toLowerCase().trim();	//the complete string BEFORE parsing
		

		//handle situation where no words entered ...

		
		//P2. word replacement
		text = text.replaceAll(" into ", " in ");
		text = text.replaceAll(" rocks", " rock");
		text = text.replaceAll("pick up", "pickup");
		text = text.replaceAll("look at", "lookat");
		text = text.replaceAll("climb up", "climbup");
		
		
		String words[] = text.split(" ");
		
		//P3. remove all instances of "THE"
		ArrayList<String> wordlist = new ArrayList<String>(Arrays.asList(words));		//array list of words
		for(int i=0; i< wordlist.size(); i++) {
			if (wordlist.get(i).equals("the")) wordlist.remove(i--);			
		}

		//separate out into word1, word2, etc.
		// ...
		String word1=words[0];
		String word2="";
		String word3="";
		if (words.length>1) {
			word2=words[1];
		}
		if (words.length>2) {
			word3=words[2];
		}
		
		

		/***** MAIN PROCESSING *****/
		switch(word1) {
		
		/**** one word commands ****/
			case "quit":
				System.out.print("Are you really a loser? You wanna quit now?");
				String ans = getCommand().toUpperCase();
				if (ans.equals("YES") || ans.equals("Y")) {
					System.out.print("Aww you suck!");
					alive=false;
				}	
			case "n": case "s": case "w": case "e": case "u": case "d":
			case "north": case "south": case "west": case "east": case "up": case "down":
				moveToRoom(word1.charAt(0));
				break;
			case "i": case "inventory":
				showInventory();
				break;
			case "sleep":
				sleep();			
				break;	
			case "help":
				printHelp();
				break;
				
			if (!word2.equals("")) {
			
				switch(word2) {
				
				case "take": case "grab": case "pickup":
					takeItem(word2);
	//			case "read":
	//				readObject(word2);
	//				break;
				case "eat":
					eatItem(word2);
					break;		
				case "drop":
					dropItem(word2);
				case "attack": case "fight": case "kill":
					attack(word2);
				
			}
		}
			
		/**** SPECIAL COMMANDS ****/
		// ...		

		default: 
			System.out.println("Sorry, I don't understand that command");
		}
		return true;
	}
	//tons of other methods go here ...
	void buy() {
		if (!currentRoom.equals(//"room with merchant")) {
			System.out.println("You can't buy anything here");
		} else {
			
		}
	}
	void sleep() {
		int rand=(int)(Math.random()*9); 
			if (rand==1) {
				System.out.println("Unlucky. While you were sleeping, you were brutally murdered by a chicken.");
				
			} else {
				System.out.println("You wake up with some good rest   +15hp");
				if (player.health<player.maxHp-15) {
					player.health=player.health+15;
				} else {
					player.health=player.maxHp;
				}
			}

	}
	void printHelp() {
		System.out.println("Move around with directions: n,w,s,e,up,down");
		System.out.println("Common commands include : kill (enemy), eat (item), drop (item), take (item), i (inventory), sleep (small heal with risk)");
	}
	void dropItem(String item) {
		if (!inventory.containsKey(item)) {
			System.out.println("Cant drop what you dont have bozo.");
		}
	}
    void takeItem(String item) {
    	if (inventory.containsKey(item)) { // check for existing items
    		inventory.put(item, inventory.get(item)+1);
    		System.out.println("You took the "+item+". Now you have "+inventory.get(item)+" "+item+"s.");
    	} else {
    		inventory.put(item, 1); // add first item
    		System.out.println("You took the "+item+".");
    	}
	}
    
    void eatItem(String item) {
    	if (!inventory.containsKey(item) ) { // check if you have item
    		System.out.println("You have no "+item+"s.");
    	} else { // gives hp and effect and removes item
    		if (item.equals("healing pot")) {
    			if (player.health==player.maxHp) {
    				System.out.println("You are at full hp");
    			} else if (player.health<player.maxHp-50) {
    				player.health+=50;
    			} else {
    				player.health=player.maxHp;
    			}
    		}
    		
    		else if (item.equals("life pot")) {
    			player.maxHp+=15;
    		} else if (item.equals("def pot")) {
    			player.def+=5;
    		} else if (item.equals("dex pot")) {
    			player.dex+=0.2;
    		} else if (item.equals("atk pot")) {
    			player.atk+=3;
    		}
    		
    		
    		
    		int itemLeft=inventory.get(item)-1;
    		inventory.put(item,itemLeft);
    		if (itemLeft==1) {
    			System.out.println("You only have one "+item+" left.");
    		} else {
    			System.out.println("You now have "+inventory.get(item)+" "+item+"s left.");
    		}
    	}
    	
    }
    public boolean attackFirst(double yourDex,double enemyDex) {
		double rand=Math.random()*(yourDex+enemyDex);
		if (rand<yourDex) {
			return true;
		}
		return false;
	}
    void attack(String enemy) {
    	Player p=new Player();
    	Enemy e=new Enemy(enemy);
    		if (attackFirst(p.dex,e.dex)) {
    			int crit=(int)(Math.random()*9);
    			if (crit==1) {
    				System.out.println("You crit him for "+p.atk*2+" damage.");
    				e.health=e.health-p.atk*2;
    				System.out.println("It now has "+e.health+" hp.");
    			} else {
    				e.health=e.health-p.atk;
    				System.out.println("It now has "+e.health+" hp.");
    			}
    		} else {
    			int edmg=(int)(e.atk*(1-(p.def/100.0)));
    			int crit=(int)(Math.random()*11);
    			if (crit==1) {
    				System.out.println("It crit you for "+edmg*2+" damage.");
    				p.health=p.health-edmg*2;
    				System.out.println("You now have "+p.health+" hp.");
    			} 
    			p.health=(p.health-edmg);
    			System.out.println("You now have "+p.health+" hp.");
    		
    		}
    }
}
class Player {
	int health=100;
	int maxHp=100;
	double def=5.0f;
	double dex=1.0f;
	int atk=15;
	int money=0;
	
	
}

class Room {
	String name; //caveentrance - cave -cave2 -cave 3
	String displayName;
	String description;
	String N="";
	String W="";
	String S="";
	String E="";
	boolean isVisited=false;
//	boolean isDark;
	
	ArrayList<String> items=new ArrayList<String>();
	
	Room(String displayName) {
		this.displayName=displayName;
	}
	
	void setExits(String N,String S,String E,String W) {
		this.N=N;
		this.S=S;
		this.E=E;
		this.W=W;
	}
	
	String getExits(char dir) {
		switch (dir) {
		case 'N': case 'n': return this.N;
		case 'S': case 's': return this.S;
		case 'W': case 'w': return this.W;
		case 'E': case 'e': return this.E;
		default: return "";
		}
	}
	
	static void setupRooms(HashMap<String,Room> roomList) {
		//
		//
		//
		//
		//  mineshaft       cave 3
		// 					cave 2
		//            		cave             loot 1
		//              cave entrance
		//
		Room r=new Room("Cave entrance", "You are at the cave entrance. Go on, fulfill your destiny and slay Hades \n"
				+ " Move north to entire the cave");
		r.setExits("cave", "", "", "");
		roomList.put("caveentrance", r);
		
		r=new Room("Cave", "You have entered the cave");
		r.setExits("cave2", "caveentrance", "loot1", "");
		roomList.put("cave", r);
	}
	public boolean getRoomStatus() {
		return isVisited;
	}
	public String getDesc() {
		return description;
	}
	
}

class Item {
	String name;
	String description;
	String writing="";
	Item(String name,String description) {
		
	}
	static void setupItems(HashMap<String,Item> itemList, HashMap<String, Room> roomList) {
		Item z=new Item("Holy Sword", "A long sword, seems like there is some writing on it.");
		z.writing="An ancient sword used to slay evil. Effective against demons";
		itemList.put("sword", z);
		roomList.get("loot1").items.add("sword");
	}
}

class Enemy {
	
	int atk=0;
	double dex=0.0f;
	int health=0;
	
	Enemy(String name) {
		
		if (name.equals("Goblin")) {
			atk=15;
			dex=0.9;
			health=50;
		}
		if (name.equals("Demon")) {
			atk=20;
			dex=1.8;
			health=150;
		}
		if (name.equals("Flying turtle")) {
			atk=50;
			dex=0.7;
			health=250;
		}
	}
	
}

