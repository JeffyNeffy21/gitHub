package gitHub;
import java.util.*;

public class TextAdventureMain {
	static Scanner sc=new Scanner(System.in);

	HashMap<String,Room> roomList = new HashMap<String,Room>();
	HashMap<String, Item> itemList = new HashMap<String,Item>(); //list of all item objects
	HashMap<String,Integer> inventory = new HashMap<>();
	public String currentRoom="";
	Player p=new Player();
	static boolean alive=true;
	static boolean won=false;
	boolean sword=false;

	public static void main(String[]args){

		TextAdventureMain t=new TextAdventureMain();
		t.AdventureMain();
	}

	void AdventureMain() {

		boolean playing = true;
		String command = "";
		setup(); //create all objects needed, including map; print intro. message
		System.out.println();
		lookAtRoom(0);

		/***** MAIN GAME LOOP *****/
		while (playing) {

			if (currentRoom.equals("Lava")) {
				System.out.println("You took a bath in lava and died after burning in lava and dying.");
				break;
			}

			if (!alive) {
				System.out.println("You lost, you suck, poor you.");
				break;
			}
			if (won) {
				break;
			}
			System.out.println();
			command = getCommand();
			playing = parseCommand(command);

		}
		System.out.println();
		if (won) {
			System.out.println("Congrats! You Beat the legendary flying turtlce and beat the game!");
		} else {
			System.out.println(" YOU LOSE ");
		}


	}
	void lookAtRoom(int n) {
		Room r=roomList.get(currentRoom);
		if (n==0) {
			if (r.isVisited) {
				System.out.println(r.displayName);
			} else {
				r.isVisited=true;
				System.out.println(r.displayName); 
				System.out.println();
				System.out.println(r.description);
				if (r.itemsHere.length()>0) {
					System.out.println();
					System.out.println(r.itemsHere);
				}
			}
		} else {
			System.out.println(r.displayName);
			System.out.println();
			System.out.println(r.description);
			if (r.itemsHere.length()>0) {
				System.out.println();
				System.out.println(r.itemsHere);
			}
		}
	}

	void moveToRoom(char c) {
		Room r=roomList.get(currentRoom);
		String room=r.getExits(c);
		if (room.equals("")) {
			System.out.println("You cant go there.");
		} else {
			currentRoom = r.getExits(c);
			lookAtRoom(0);
		}
	}

	void showInventory() {
		System.out.println(" INVENTORY: ");
		for (Map.Entry item:inventory.entrySet()) {
			System.out.println(item.getKey()+" : "+item.getValue());
		}
	}
	void checkHP() {
		System.out.println(p.health+"/"+p.maxHp);
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
		text = text.replaceAll("healing pot", "healingpot");
		text = text.replaceAll("atk pot", "atkpot");
		text = text.replaceAll("def pot", "defpot");
		text = text.replaceAll("life pot", "lifepot");
		text = text.replaceAll("dex pot", "dexpot");

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
		if (word2.equals("")) {
			switch(word1) {

			case "quit":
				System.out.print("Are you really a loser? You wanna quit now?");
				String ans = getCommand().toUpperCase();
				if (ans.equals("YES") || ans.equals("Y")) {
					alive=false;
				}	
				break;

			case "hp":
				checkHP();
				break;

			case "stats":
				stats();
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
			case "buy": case "shop":
				buy();
				break; 
			case "look":
				lookAtRoom(1);
				break; 
			case "a":
				Room r = roomList.get(currentRoom);
				if (r.enemy != "") {
					attack(r.enemy);
				} else {
					System.out.println("There are no enemies here");
				}
				break;

			default: 
				System.out.println("Sorry, I don't understand that command");
			}
		}
		if (!word2.equals("")) {


			switch(word1) {

			case "take": case "grab": case "pickup":
				takeItem(word2);
				break;

			case "eat": case "drink":
				eatItem(word2);
				break;	

			case "drop":
				dropItem(word2);
				break;

			case "attack": case "fight": case "kill":
				//fight();
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
		Item i=new Item("","");
		if (!currentRoom.equals("Merchant")) {
			System.out.println("You can't buy anything here");
			return;
		} 
			System.out.println("\t\tSHOP\t\t");
			System.out.println("\t Current Balance: $"+p.money);
			int shopItems=0;
			for (Map.Entry item:itemList.entrySet()) {
				if (itemList.get(item.getKey()).shopIndex!=0) {
					i=itemList.get(item.getKey());
					System.out.printf("%d)     %-14s\t  $%d\n",i.shopIndex,i.displayName,i.price);
				}
			}
			System.out.println("4)     Sword Upgrade      $40");
			System.out.print("type 'Q' to leave shop or type a # to buy item: ");
			String a=sc.nextLine();
			
			if (a.equals("q") || a.equals("Q")) {
				return;
			}
			System.out.println();
			
			for (Map.Entry item:itemList.entrySet()) {
				if (itemList.get(item.getKey()).shopIndex!=0) {
					if (itemList.get(item.getKey()).shopIndex==Integer.parseInt(a)) {
						i=itemList.get(item.getKey());
					}
				}
			}
			switch (a) {
				case "1": case "2": case "3":
					System.out.print("How many "+i.displayName+"s do you want? ");
					int amount=sc.nextInt();
					sc.nextLine();
					if (p.money<amount*i.price) {
						System.out.println("not enough money");
						System.out.println();
					}
					else {
						p.money-=amount*i.price;
						System.out.println("You have bought "+amount+" "+i.displayName+"s.");
						if (!inventory.containsKey(i.name)) {
							inventory.put(i.name, amount);
						} else {
							inventory.put(i.name, inventory.get(i.name)+amount);
						}
					}
					return;
				case "4":
					if (sword) {
						System.out.println("You cant upgrade your sword again.");
						break;
					}
					System.out.print("Do you want to upgrade your sword? (Y/N): ");
					String ans=sc.nextLine();
					System.out.println();
					if (ans.equals("Y") || ans.equals("y")) {
						System.out.println("You upgraded your sword +10 atk");
						p.money-=40;
						p.atk+=10;
					}
					return;
					
				default:
					System.out.println("Not a valid response.");
					break;
			}
		}
void stats() {
	System.out.println(" STATS: ");
	System.out.println("Max hp: "+p.maxHp);
	System.out.println("Hp: "+p.health);
	System.out.println("Attack: "+p.atk);
	System.out.println("Defense: "+p.def);
	System.out.println("Money: "+p.money);
	System.out.println("Dexterity: "+p.dex);
}
void sleep() {
	int rand=(int)(Math.random()*9); 
	if (rand==1) {
		System.out.println("Unlucky. While you were sleeping, you were brutally murdered by a chicken.");

	} else {
		if (p.health<p.maxHp-15) {
			p.health+=15;
			System.out.println("You wake up with some good rest +15hp");
		} else {
			p.health=p.maxHp;
			System.out.println("You wake up with full hp");
		}
	}

}
void printHelp() {
	System.out.println("Move around with directions: n,w,s,e,up,down");
	System.out.println("Common commands include : kill (enemy), eat (item), drop (item), take (item), i (inventory), sleep, observe (item)");
}
void dropItem(String word2) {
	if (!inventory.containsKey(word2)) {
		System.out.println("Cant drop what you dont have bozo.");
	}else {
		int amount = inventory.get(word2)-1;
		System.out.println("You dropped the "+word2);
		System.out.println("You now have "+amount+" "+word2+"s left.");
		inventory.put(word2, amount);
		if (amount==0) {
			inventory.remove(word2);
		}
	} 
}
public void takeItem(String item) {
	if (roomList.get(currentRoom).items.contains(item)) {

		if (inventory.containsKey(item)) { // check for existing items
			inventory.put(item, inventory.get(item)+1);
			System.out.println("You took the "+item+". Now you have "+inventory.get(item)+" "+item+"s.");
		} else {
			inventory.put(item, 1); // add first item
			System.out.println("You took the "+item+".");
		}
		roomList.get(currentRoom).items.remove(item);
	} else {
		System.out.println("There are no "+item+"s in this room.");
	}
}

void eatItem(String item) {
	if (!inventory.containsKey(item) ) { // check if you have item
		System.out.println("You have no "+item+"s.");
		return;
	} 
	if (item.equals("healingpot")) {
		if (p.health==p.maxHp) {
			System.out.println("You are at full hp");
			return;
		} else if (p.health<p.maxHp-50) {
			p.health+=50;
			System.out.println("You are now at "+p.health+" hp.");
		} else {
			p.health=p.maxHp;
			System.out.println("You are now at full hp");
		}
	}

	else if (item.equals("lifepot")) {
		p.maxHp+=15;
		System.out.println("Your max health is now "+p.maxHp);
	} else if (item.equals("defpot")) {
		p.def+=5;
		System.out.println("Your defense is now "+p.def);
	} else if (item.equals("dexpot")) {
		p.dex+=0.2;
		System.out.println("Your dexterity is now "+p.dex);
	} else if (item.equals("atkpot")) {
		p.atk+=3;
		System.out.println("Your attack is now "+p.atk);
	} else {
		System.out.println("You cant consume that...");
		return;
	}

	int itemLeft=inventory.get(item)-1;
	inventory.put(item,itemLeft);
	if (itemLeft==1) {
		System.out.println("You only have one "+item+" left.");
	} else if (itemLeft==0) {
		inventory.remove(item);
	} else {
		System.out.println("You now have "+inventory.get(item)+" "+item+"s left.");
	}

}
void fight() {
	if (!inventory.containsKey("sword")) {
		System.out.println("You have nothing to fight with.");
	} else {

	}
}
boolean attackFirst(double yourDex,double enemyDex) {
	double rand=Math.random()*(yourDex+enemyDex);
	if (rand<yourDex) {
		return true;
	}
	return false;
}

void attack(String enemy) {
	Enemy e = new Enemy(enemy);
	while (e.health > 0 && p.health > 0) {
		System.out.println("Fight or heal?");
		String a = sc.nextLine();
		if (a.equals("f")) {
			if (attackFirst(p.dex, e.dex)) {
				int crit = (int) (Math.random() * 9);
				if (crit == 1) {
					System.out.println("You crit him for " + p.atk * 2 + " damage.");
					e.health = e.health - p.atk * 2;
					System.out.println("It now has " + e.health + " hp.");
				} else {
					e.health = (e.health - p.atk);
					System.out.println("It now has " + e.health + " hp.");
				}
			} else {
				int edmg = (int) (e.atk * (1 - (p.def / 100.0)));
				int crit = (int) (Math.random() * 11);
				if (crit == 1) {
					System.out.println("It crit you for " + edmg * 2 + " damage.");
					p.health = p.health - edmg * 2;
					System.out.println("You now have " + p.health + " hp.");
				}
				p.health = (p.health - edmg);
				System.out.println("You now have " + p.health + " hp.");
			}
		}
		if (a.equals("h")) {
			eatItem("healingpot");
		}
		if (p.health < 0) {
			System.out.println("You lose");
		}
		if (e.health < 0) {
			System.out.println("You win");
		}
	}
}
}

class Player {
	int health=100;
	int maxHp=100;
	double def=5.0f;
	double dex=1.0f;
	int atk=15;
	int money=100;

	Player() {
	}
}

class Room {
	String displayName;
	String description;
	String itemsHere="";
	String N="";
	String W="";
	String S="";
	String E="";
	String enemy = "";
	
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
	static String giveItems(Room r)
	{
		String s="Items in this room |";
		for (int i=0;i<r.items.size();i++) {
			s+=r.items.get(i)+"| ";
		}
		return s.trim();
	}
	static void updateRooms(HashMap<String,Room> roomList) {
		for (Room room:roomList.values()) {
			room.itemsHere=giveItems(room);
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
		r.description=("You are at the cave entrance. This cave is said to be full of legendary treasures, but also monsters!\n" + "Move north to enter the cave...");
		r.setExits("Cave", "", "", "");
		roomList.put("Cave entrance", r);

		//cave
		r=new Room("Cave");
		r.description=("You have entered the strange cave and a cold tingly feeling rides down your spine.\n"+"There seems to be a loot room to the East with weapons and potions...\n"
				+ "You could advance further into the cave North...");
		r.setExits("Cave 2", "Cave entrance", "Loot room", "");
		roomList.put("Cave", r);

		//loot room
		
		r=new Room("Loot room");
		r.items.add("healing pot");
		r.items.add("life pot");
		r.items.add("dex pot");
		r.items.add("sword");
		r.description=("You have entered a cool room with a nice golden chest inside. Whats in the chest?"); 
		r.setExits("", "", "", "Cave");
		roomList.put("Loot room", r);

		//cave 2
		r=new Room("Cave 2");
		r.description=("Further down the cave, the only thing you can see is a bright light further down the cave.");
		r.setExits("Cave 3", "Cave", "", "");
		roomList.put("Cave 2", r);

		//cave 3
		r=new Room("Cave 3");
		r.description=("To the North there is a spooky chamber, to the East there seems to be a merchant.\n"
				+ "To the West, there is a mineshaft. You can always turn back!");
		r.setExits("Boss Room", "Cave 2", "Merchant", "Mineshaft");
		roomList.put("Cave 3", r);

		//merchant
		r=new Room("Merchant");
		r.description=("A cozy room with a kind merchant running a shop.");
		r.setExits("", "", "", "Cave 3");
		roomList.put("Merchant", r);

		//mineshaft
		r=new Room("Mineshaft");
		r.description=("You enter a small mineshaft and you see a golden glowing further in. I could be gold!\n"
				+ "... but it seems quite unsafe. Travel west to venture deeper.");
		r.setExits("", "", "Cave 3", "Lava");
		roomList.put("Mineshaft", r);

		// lava
		r=new Room("Lava");
		r.description=("You finally find out what it is... It is a pool of lava.\n"
				+ "Unfortunately, you are clumsy and fell into the lava.");
		roomList.put("Lava", r);

		//Boss room
		r=new Room("Boss Room");
		r.description=("A huge chamber with a monstrous flying turtle.");
		r.enemy = "Goblin";
		roomList.put("Boss Room", r);

	}

}

class Item {
	String displayName;
	String name;
	String description;
	String writing="";
	int shopIndex=0;
	int price=0;
	static ArrayList<Item> shop=new ArrayList<>();
	
	Item(String name,String displayName) {
		this.name=name;
		this.displayName=displayName;
	}
	
	static void setupItems(HashMap<String,Item> itemList, HashMap<String, Room> roomList) {
		Item z=new Item("sword", "Glowing greatsword");
		z.writing="An ancient sword used to slay evil. Effective against monsters.";
		itemList.put("sword", z);
		roomList.get("Loot room").items.add("sword");

		z=new Item("healingpot", "Healing Potion");
		shop.add(z);
		z.shopIndex=1;
		z.price=10;
		itemList.put("healing pot", z);
		roomList.get("Loot room").items.add("healingpot");

		z=new Item("dexpot", "Dexterity Potion");
		shop.add(z);
		z.shopIndex=2;
		z.price=15;
		itemList.put("dexpot", z);
		roomList.get("Loot room").items.add("dexpot");

		z=new Item("lifepot", "Life Potion");
		shop.add(z);
		z.shopIndex=3;
		z.price=15;
		itemList.put("lifepot", z);
		roomList.get("Loot room").items.add("lifepot");

	}


}

class Enemy {

	int atk;
	double dex;
	int health;

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
