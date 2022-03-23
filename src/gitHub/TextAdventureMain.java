package gitHub;
import java.util.*;

public class TextAdventureMain {
	static Scanner sc=new Scanner(System.in);

	HashMap<String,Room> roomList = new HashMap<String,Room>();
	HashMap<String, Item> itemList = new HashMap<String,Item>(); //list of all item objects
	HashMap<String,Integer> inventory = new HashMap<>();
	public String currentRoom="";
	Player player;
	static boolean alive=true;
	static boolean won=false;
	
	public static void main(String[]args){

		TextAdventureMain t=new TextAdventureMain();
		t.AdventureMain();
	}

	void AdventureMain() {
		if (currentRoom.equals("lava")) {
			System.out.println("You took a bath in lava and died after burning in lava and dying.");
		}
		boolean playing = true;
		String command = "";
		setup(); //create all objects needed, including map; print intro. message
		lookAtRoom();

		/***** MAIN GAME LOOP *****/
		while (playing) {

			if (!alive) {
				System.out.println("You lost, you suck, poor you.");
				break;
			}
			if (won) {
				System.out.println("Congrats, you actually won!     ... Or did you?");
				break;
			}

			
			command = getCommand();
			playing = parseCommand(command);
			System.out.println(playing);

			//check to see if player has died (in whichever various ways the player can die)

			//check to see if the player has won the game
			
		}

		// does anything need to be done after th emain game loop exits?

	}
	void lookAtRoom() {
		Room r=roomList.get(currentRoom);
		if (r.isVisited) {
			System.out.println(r.displayName);
		} else {
			r.isVisited=true;
			System.out.println(r.displayName); 
			System.out.println(r.description);
		}
	}
	
	void moveToRoom(char c) {
		Room r=new Room(currentRoom);
		String room=r.getExits(c);
		if (room.equals("")) {
			System.out.println("You cant go there.");
		} else {
			currentRoom = r.getExits(c);
		}
	}
	
	void showInventory() {
		System.out.println(" INVENTORY: ");
		for (Map.Entry item:inventory.entrySet()) {
			System.out.println(item.getKey()+" : "+item.getValue());
		}
	}
	
	void setup() {
		System.out.println("Welcome to the game! You are not gonna have a good time! Good luck on your adventure!");
		Room.setupRooms(roomList);
		Item.setupItems(itemList, roomList);
		// ... more stuff ...
		currentRoom = "Cave entrance";
	}

	String getCommand() {
		String text = sc.nextLine();
		if (text.length() == 0) text = "qwerty"; //default command
		//sc.close();
		return text;
	}

	
	boolean parseCommand(String text) {

		text = text.toLowerCase().trim();	//the complete string BEFORE parsing
		text = text.replaceAll(" into ", " in ");
		text = text.replaceAll(" rocks", " rock");
		text = text.replaceAll("pick up", "pickup");
		text = text.replaceAll("look at", "lookat");
		text = text.replaceAll("climb up", "climbup");
		
		String words[] = text.split(" ");
		
		ArrayList<String> wordlist = new ArrayList<String>(Arrays.asList(words));		//array list of words
		for(int i=0; i< wordlist.size(); i++) {
			if (wordlist.get(i).equals("the")) wordlist.remove(i--);			
		}

		String word1=words[0];
		String word2="";
		String word3="";
		if (words.length>1) {
			word2=words[1];
		}
//		if (words.length>2) {
//			word3=words[2];
//		}
		/***** MAIN PROCESSING *****/
		switch(word1) {
		
			case "quit":
				System.out.print("Are you really a loser? You wanna quit now?");
				String ans = getCommand().toUpperCase();
				if (ans.equals("YES") || ans.equals("Y")) {
					System.out.print("Aww you suck!");
					alive=false;
				}	
				break;
				
			case "n": case "s": case "w": case "e":
			case "north": case "south": case "west": case "east": 
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
			default: 
				System.out.println("Sorry, I don't understand that command");
		}
		if (!word2.equals("")) {
		
			
			switch(word2) {
				
					case "take": case "grab": case "pickup":
						takeItem(word2);
						break;
						
					case "eat":
						eatItem(word2);
						break;	
						
					case "drop":
						dropItem(word2);
						break;
						
					case "attack": case "fight": case "kill":
						attack(word2);
						break;
				
					default: 
						System.out.println("Sorry, I don't understand that command");
			}
		}
		return true;
	}
	//tons of other methods go here ...
	
	void buy() {
		boolean sword=false;
		if (!currentRoom.equals("Merchant")) {
			System.out.println("You can't buy anything here");
		} else {
			String r;
			do {
				System.out.println("\t\tSHOP\t\t");
				System.out.println("\t Current Balance: $"+player.money);
				System.out.println("1)\tHealing potion    $10\t");
				System.out.println("2)\tAttack potion     $15\t");
				System.out.println("3)\tDefense potion    $15\t");
				System.out.println("4)\tUpgrade Sword     $40\t");
				System.out.print("type 'Q' to leave shop or type a # to buy item: ");
				String a=sc.nextLine();
				r=a;
				System.out.println();
				
				if (a.equals("Q") || a.equals("q")) {
					return;
				}
				if (a.equals("1")) {
					if (player.money<10) {
						System.out.println("Not enough balance.");
					} else {
						System.out.print("How many you want to buy? ");
						int amount=sc.nextInt();
						System.out.println();
						
						if (player.money<(10*amount)) {
							System.out.println("Not enought balance.");
						} else {
							player.money-=(10*amount);
								if (inventory.containsKey("healing pot")) { // check for existing items
						    		inventory.put("healing pot", inventory.get("healing pot")+amount);
						    		System.out.println("You bought the potions. Now you have "+inventory.get("healing pot")+" healing pots.");
						    	} else {
						    		inventory.put("healing pot", 1); // add first item
						    		System.out.println("You bought the healing potion.");
						    	}
						}
					}
				}
				if (a.equals("2")) {
					if (player.money<15) {
						System.out.println("Not enough balance.");
					} else {
						System.out.print("How many you want to buy? ");
						int amount=sc.nextInt();
						System.out.println();
						
						if (player.money<(15*amount)) {
							System.out.println("Not enought balance.");
						} else {
							player.money-=(15*amount);
								if (inventory.containsKey("atk pot")) { // check for existing items
						    		inventory.put("atk pot", inventory.get("atk pot")+amount);
						    		System.out.println("You bought the potions. Now you have "+inventory.get("atk pot")+" attack pots.");
						    	} else {
						    		inventory.put("atk pot", 1); // add first item
						    		System.out.println("You bought the attack potion.");
						    	}
						}
					}
				}
				if (a.equals("3")) {
					if (player.money<15) {
						System.out.println("Not enough balance.");
					} else {
						System.out.print("How many you want to buy? ");
						int amount=sc.nextInt();
						System.out.println();
						
						if (player.money<(15*amount)) {
							System.out.println("Not enought balance.");
						} else {
							player.money-=(15*amount);
								if (inventory.containsKey("def pot")) { // check for existing items
						    		inventory.put("def pot", inventory.get("def pot")+amount);
						    		System.out.println("You bought the potions. Now you have "+inventory.get("def pot")+" defense pots.");
						    	} else {
						    		inventory.put("def pot", 1); // add first item
						    		System.out.println("You bought the defense potion.");
						    	}
						}
					}
				}
				if (a.equals("4")) {
					if (sword) {
						System.out.println("You cant upgrade it again");
					}
					System.out.print("You can only upgrade once, upgrade? (Y/N): ");
					String s= sc.nextLine();
					if (s.equals("Y") || s.equals("y")) {
						if (player.money<40) {
							System.out.println("Not enough balance.");
						} else {
							sword=true;
							player.money-=40;
							player.atk+=10;
							System.out.println("You have successfully upgrade your sword.");
						}
					}
						
				}
			} while (true);
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
		System.out.println("Common commands include : kill (enemy), eat (item), drop (item), take (item), i (inventory), sleep, observe (item)");
	}
	void dropItem(String item) {
		if (!inventory.containsKey(item)) {
			System.out.println("Cant drop what you dont have bozo.");
		}
	}
    public void takeItem(String item) {
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
	int money=10;
}

class Room {
	String displayName;
	String description;
	String N="";
	String W="";
	String S="";
	String E="";
	boolean isVisited=false;
	
	ArrayList<String> items=new ArrayList<String>();
	
	Room(String displayName) {
		this.displayName=displayName;
	}
	
	void setExits(String N,String S,String E,String W) {
		this.N=N; //Cave
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
		//					Boss Room
		// Lava  Mineshaft  Cave 3           Merchant
		// 					Cave 2
		//            		Cave             Loot room
		//              Cave entrance
		
		//cave entrance
		Room r=new Room("Cave entrance");
		r.description=("You are at the cave entrance. Go on, fulfill your destiny and slay things.\n" + "Move north to entire the cave");
		r.setExits("Cave", "", "", "");
		roomList.put("Cave entrance", r);
		
		//cave
		r=new Room("Cave");
		r.description=("You have entered the cave you get this tingly feeling in your spine.\n"+"There seems to be a loot room to the East\n"
				+ "You could advance further into the cave North");
		r.setExits("Cave 2", "Cave entrance", "Loot room", "");
		roomList.put("Cave", r);
		
		//loot room
		r=new Room("Loot room");
		r.description=("Cool room with a nice golden chest inside. Whats in the chest?\n"
				+ "Go West to return into the cave.");
		r.setExits("", "", "", "Cave");
		roomList.put("Loot room", r);
		
		//cave 2
		r=new Room("Cave 2");
		r.description=("Further down the cave, you see a bright light further down North.");
		r.setExits("Cave 3", "Cave", "", "");
		roomList.put("Cave 2", r);
		
		//cave 3
		r=new Room("Cave 3");
		r.description=("To the North you see a formidable enemy, to the East there seems to be a merchant.\n"
				+ "To the West, there is a mineshaft. You can always turn back now.");
		r.setExits("Boss Room", "Cave 2", "Merchant", "Mineshaft");
		roomList.put("Cave 3", r);
		
		//merchant
		r=new Room("Merchant");
		r.description=("A cozy room with a kind merchant running a shop.");
		r.setExits("", "", "", "Cave 3");
		roomList.put("Merchant", r);
		
		//mineshaft
		r=new Room("Mineshaft");
		r.description=("A small mineshaft with a small railroad transport system. "
				+ "Riding the system to the West seems dangerous...");
		r.setExits("", "", "Cave 3", "Lava");
		roomList.put("Mineshaft", r);
		
		// lava
		r=new Room("Lava");
		r.description=("A pool of lava, lava is very hot. Lava is bad.");
		roomList.put("Lava", r);
		
		//Boss room
		r=new Room("Boss Room");
		r.description=("A huge chamber with a monstrous flying turtle.");
		roomList.put("Boss Room", r);
		
	}
	
}

class Item {
	String name;
	String description;
	String writing="";
	Item(String name,String description) {
		
	}
	static void setupItems(HashMap<String,Item> itemList, HashMap<String, Room> roomList) {
		Item z=new Item("sword", "A long sword, seems like there is some writing on it.");
		z.writing="An ancient sword used to slay evil. Effective against monsters.";
		itemList.put("sword", z);
		roomList.get("Loot room").items.add("sword");
		
		z=new Item("healing pot", "This potion heals your wounds, and makes you feel better.");
		itemList.put("healing pot", z);
		roomList.get("Loot room").items.add("healing pot");
		
		z=new Item("dex pot", "This potion makes you feel energized, and lightweight.");
		itemList.put("dex pot", z);
		roomList.get("Loot room").items.add("dex pot");
		
		z=new Item("life pot", "This potion increases your max health, and makes you feel healthier than ever.");
		itemList.put("life pot", z);
		roomList.get("Loot room").items.add("life pot");
		
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
