package com.example.wordle2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    ImageView[][] greenBoxes = new ImageView[6][5];
    ImageView[][] grayBoxes = new ImageView[6][5];
    ImageView[][] yellowBoxes = new ImageView[6][5];
    TextView[][] guessedLetters = new TextView[6][5];

    int currentRow = 0;
    //  currentCol is where the next letter is going to go!
    int currentCol = 0;

    String userWord = "";
    int letterInd;
    int numInd;
    int countWord = 0;
    int countGuess = 0;
    int getOut = 0;
    int[] abc=new int[26];
    int a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z, a2, b2, c2, d2, e2, f2, g2, h2, i2, j2, k2, l2, m2, n2, o2, p2, q2, r2, s2, t2, u2, v2, w2, x2, y2, z2;

    char[] letters = new char[5];

    ArrayList<String> verbs = getWordBank();

    //ArrayList<String> verbsUpper = new ArrayList<>();
    ImageView kimage;
    ImageView kimage2;
    ImageView kimage3;
    ImageView kimage4;
    ImageView kimage5;
    ImageView kimage6;
    ImageView kimage7;
    ImageView kimage8;
    ImageView kimage9;
    ImageView kimage10;
    ImageView kimage11;
    ImageView kimage12;
    ImageView kimage13;
    ImageView kimage14;
    ImageView kimage15;
    ImageView kimage16;
    ImageView kimage17;
    ImageView kimage18;
    ImageView kimage19;
    ImageView kimage20;
    ImageView kimage21;
    ImageView kimage22;
    ImageView kimage23;
    ImageView kimage24;
    ImageView kimage25;
    ImageView kimage26;
    ImageView kimage27;
    ImageView kimage28;
    TextView textview2;

    String curLetter;

    String word;
    char word2;
    TextView outDisplay;
    TextView winDisplay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String name;
        int resID;
        outDisplay = findViewById(R.id.output);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                name = "greenBox" + (i + 1) + (j + 1);
                System.out.println("Binding green box for " + name);
                resID = getResources().getIdentifier(name, "id", getPackageName());
                greenBoxes[i][j] = findViewById(resID);
                greenBoxes[i][j].animate().alpha(0f);

                name = "grayBox" + (i + 1) + (j + 1);
                System.out.println("Binding gray box for " + name);
                resID = getResources().getIdentifier(name, "id", getPackageName());
                grayBoxes[i][j] = findViewById(resID);
                grayBoxes[i][j].animate().alpha(1f);

                name = "yellowBox" + (i + 1) + (j + 1);
                System.out.println("Binding yellow box for " + name);
                resID = getResources().getIdentifier(name, "id", getPackageName());
                yellowBoxes[i][j] = findViewById(resID);
                yellowBoxes[i][j].animate().alpha(0f);

                name = "textView" + (i + 1) + (j + 1);
                System.out.println("Binding textview for " + name);
                resID = getResources().getIdentifier(name, "id", getPackageName());
                guessedLetters[i][j] = findViewById(resID);
            }
        }

        kimage = findViewById(R.id.keyQ);
        kimage2 = findViewById(R.id.keyW);
        kimage3 = findViewById(R.id.keyE);
        kimage4 = findViewById(R.id.keyR);
        kimage5 = findViewById(R.id.keyT);
        kimage6 = findViewById(R.id.keyY);
        kimage7 = findViewById(R.id.keyU);
        kimage8 = findViewById(R.id.keyI);
        kimage9 = findViewById(R.id.keyO);
        kimage10 = findViewById(R.id.keyP);
        kimage11 = findViewById(R.id.keyA);
        kimage12 = findViewById(R.id.keyS);
        kimage13 = findViewById(R.id.keyD);
        kimage14 = findViewById(R.id.keyF);
        kimage15 = findViewById(R.id.keyG);
        kimage16 = findViewById(R.id.keyH);
        kimage17 = findViewById(R.id.keyJ);
        kimage18 = findViewById(R.id.keyK);
        kimage19 = findViewById(R.id.keyL);
        kimage20 = findViewById(R.id.enter);
        kimage21 = findViewById(R.id.keyZ);
        kimage22 = findViewById(R.id.keyX);
        kimage23 = findViewById(R.id.keyC);
        kimage24 = findViewById(R.id.keyV);
        kimage25 = findViewById(R.id.keyB);
        kimage26 = findViewById(R.id.keyN);
        kimage27 = findViewById(R.id.keyM);
        kimage28 = findViewById(R.id.backspace);
        winDisplay = findViewById(R.id.winText);
        textview2 = findViewById(R.id.textView51);

        //  Choose a random word from the list of words
        int wordInd = (int) ((Math.round(Math.random() * verbs.size())));
        word = (verbs.get(wordInd)).toUpperCase();

        //  FORCE THE WORD TO SOMETHING FOR DEBUGGING!!!!
        //word="ERROR";

        // letters is all the correct letters in the correct order
        for (int i = 0; i < 5; i++) {
            letters[i] = word.charAt(i);
        }
        System.out.println(word);

        // Uppercase the entire word list
        for (int q = 0; q < verbs.size(); q++){
            String test = verbs.get(q).toUpperCase(Locale.ROOT);
            verbs.set(q,test);
        }
    }


    public void keyPressed(View view) {
        outDisplay.setText("");
        if (currentCol == 0) {
            guessedLetters[currentRow][currentCol].setText(view.getTag().toString());
            currentCol += 1;
        } else if (currentCol < 5) {

            guessedLetters[currentRow][currentCol].setText(view.getTag().toString());
            currentCol += 1;
            if (currentCol == 5) {
                currentCol -= 1;
            }
        }
    }

    public void delete(View view) {

        curLetter = guessedLetters[currentRow][currentCol].getText().toString();
        if (curLetter.equalsIgnoreCase("")) {
            if (currentCol > 0) {
                currentCol -= 1;
            }
        }
        guessedLetters[currentRow][currentCol].setText("");

    }


    public void enter(View view) {
        int counter = 0;
        char holder = 0;
        System.out.println("inside enter method");
        String userWord = "";
        //curLetter = guessedLetters[currentRow][4].getText().toString();
        char letterState[] = {'-', '-', '-', '-', '-'};

        // Has the user entered 5 letters?
        if (currentCol!=4) {
            System.out.println("word short");
            outDisplay.setText("Word is too short");
            return;
        }

        // What word has the user entered?
        for (int i = 0; i < 5; i++) {
            String s = guessedLetters[currentRow][i].getText().toString();
            userWord = userWord.concat(s);
        }

        //  Has the user entered a valid word? If not, clear it and get out
        if (!verbs.contains(userWord)) {
            outDisplay.setText("The word you entered is invalid.");
            for (int l = 0; l < 4; l++)  {
                guessedLetters[currentRow][currentCol].setText("");
                currentCol -= 1;
            }

            guessedLetters[currentRow][0].setText("");
            return;
        }

        // Has the user guessed the word correctly?
        if (userWord.equals(word)) {
            for (int i = 0; i < 5; i++) {
                greenBoxes[currentRow][i].animate().alpha(1f).rotationXBy(180).setDuration(500);
                grayBoxes[currentRow][i].animate().alpha(0f);
                winDisplay.setText(word);
                outDisplay.setText("");
            }
            return;
        }

        // Check for letters that are correct and in the correct spot and remember which ones.
        for (int i = 0; i < 5; i++) {
            if (userWord.charAt(i) == letters[i]) {
                greenBoxes[currentRow][i].animate().alpha(1f).rotationXBy(-180).setDuration(500);
                grayBoxes[currentRow][i].animate().alpha(0f);
                //System.out.println("green");
                outDisplay.setText("");
                letterState[i] = 'G';
            }
        }

        // Check for letters that are correct but not in the right spot.
        for (int i = 0; i < 5; i++) {
            if (letterState[i]  != 'G') {
                for (int j = 0; j < 5; j++) {
                    if (letterState[j] == '-' ) {
                        if (userWord.charAt(i) == letters[j]) {
                            //  Flip any correct letter in wrong place to YELLOW
                            yellowBoxes[currentRow][i].animate().alpha(1f).rotationXBy(-180).setDuration(500);
                            grayBoxes[currentRow][i].animate().alpha(0f);
                            //System.out.println("yellow");
                            outDisplay.setText("");
                            letterState[j] = 'Y';
                            break;
                        }
                        else{
                            //  Flip completely incorrect letters from Gray to Gray
                            grayBoxes[currentRow][i].animate().alpha(1f).rotationXBy(-180).setDuration(500);
                        }
                    }
                }
            }
        }


        currentRow += 1;
        currentCol = 0;
        System.out.println(currentRow);

        //  If the user has used all their guesses and NOT gotten the word,
        //  insult them.
        if(currentRow>=6 && !userWord.equalsIgnoreCase(word)){
            System.out.println(word);
            outDisplay.setText("Dumbass, the word was "+word);
        }
    }

    ArrayList<String> getWordBank() {
        ArrayList<String> list = new ArrayList<>(Arrays.asList(
                "Admit", "Adopt", "Agree", "Allow", "Alter", "Apply", "Argue", "Arise", "Avoid", "Begin", "Blame", "Break",
                "Bring", "Build", "Burst", "Carry", "Cause", "Check", "Claim", "Clean", "Clear", "Climb", "Close", "Count",
                "Cover", "Cross", "Dance", "Doubt", "Drink", "Drive", "Enjoy", "Enter", "Exist", "Fight", "Focus", "Force",
                "Guess", "Imply", "Issue", "Judge", "Laugh", "Learn", "Leave", "Limit", "Marry", "Match", "Occur", "Offer",
                "Order", "Phone", "Place", "Point", "Press", "Prove", "Raise", "Reach", "Refer", "Relax", "Serve", "Shall",
                "Share", "Shift", "Shoot", "Sleep", "Solve", "Sound", "Speak", "Spend", "Split", "Stand", "Start", "State",
                "Stick", "Study", "Teach", "Thank", "Think", "Throw", "Touch", "Train", "Treat", "Trust", "Visit", "Voice",
                "Waste", "Watch", "Worry", "Would", "Write", "Above", "Acute", "Alive", "Alone", "Angry", "Aware", "Awful",
                "Basic", "Black", "Blind", "Brave", "Brief", "Broad", "Brown", "Cheap", "Chief", "Civil", "Clean", "Clear",
                "Close", "Crazy", "Daily", "Dirty", "Early", "Empty", "Equal", "Exact", "Extra", "Faint", "False", "Fifth",
                "Final", "First", "Fresh", "Front", "Funny", "Giant", "Grand", "Great", "Green", "Gross", "Happy", "Harsh",
                "Heavy", "Human", "Ideal", "Inner", "Joint", "Large", "Legal", "Level", "Light", "Local", "Loose", "Lucky",
                "Magic", "Major", "Minor", "Moral", "Naked", "Nasty", "Naval", "Other", "Outer", "Plain", "Prime", "Prior",
                "Proud", "Quick", "Quiet", "Rapid", "Ready", "Right", "Roate","Roman", "Rough", "Round", "Royal", "Rural",
                "Sharp", "Sheer", "Short", "Silly", "Sixth", "Small", "Smart", "Solid", "Sorry", "Spare", "Steep", "Still",
                "Super", "Sweet", "Thick", "Third", "Tight", "Total", "Tough", "Upper", "Upset", "Urban", "Usual", "Vague",
                "Valid", "Vital", "White", "Whole", "Wrong", "Young",

                "world", "house", "place", "group", "party", "money", "point", "state", "night", "water", "thing", "order",
                "power", "court", "level", "child", "south", "staff", "woman", "north", "sense", "death", "range", "table",
                "trade", "study", "other", "price", "class", "union", "value", "paper", "right", "voice", "stage", "light",
                "march", "board", "month", "music", "field", "award", "issue", "basis", "front", "heart", "force", "model",
                "space", "peter", "hotel", "floor", "style", "event", "press", "doubt", "blood", "sound", "title", "glass",
                "earth", "river", "whole", "piece", "mouth", "radio", "peace", "start", "share", "truth", "smith", "stone",
                "queen", "stock", "horse", "plant", "visit", "scale", "image", "trust", "chair", "cause", "speed", "crime",
                "pound", "henry", "match", "scene", "stuff", "japan", "claim", "video", "trial", "phone", "train", "sight",
                "grant", "shape", "offer", "while", "smile", "track", "route", "china", "touch", "youth", "waste", "crown",
                "birth", "faith", "entry", "total", "major", "owner", "lunch", "cross", "judge", "guide", "cover", "jones",
                "green", "brain", "phase", "coast", "drink", "drive", "metal", "index", "adult", "sport", "noise", "agent",
                "simon", "motor", "sheet", "brown", "crowd", "shock", "fruit", "steel", "plate", "grass", "dress", "theme",
                "error", "lewis", "white", "focus", "chief", "sleep", "beach", "sugar", "panel", "dream", "bread", "chain",
                "chest", "frank", "block", "store", "break", "drama", "skill", "round", "rugby", "scope", "plane", "uncle",
                "abuse", "limit", "taste", "fault", "tower", "input", "enemy", "anger", "cycle", "pilot", "frame", "novel",
                "reply", "prize", "nurse", "cream", "depth", "sheep", "dance", "spite", "coach", "ratio", "fight", "unity",
                "steam", "final", "clock", "pride", "buyer", "smoke", "score", "watch", "apple", "trend", "proof", "pitch",
                "shirt", "knife", "draft", "shift", "terry", "squad", "layer", "laura", "colin", "curve", "wheel", "topic",
                "guard", "angle", "smell", "grace", "flesh", "mummy", "pupil", "guest", "delay", "mayor", "logic", "album",
                "habit", "billy", "audit", "baker", "paint", "great", "storm", "worth", "black", "daddy", "canal", "robin",
                "kelly", "leave", "lease", "young", "louis", "print", "fleet", "crash", "count", "asset", "cloud", "villa",
                "actor", "ocean", "brand", "craft", "alarm", "bench", "diary", "abbey", "grade", "bible", "jimmy", "shell",
                "cloth", "piano", "clerk", "stake", "barry", "stand", "mouse", "cable", "manor", "local", "penny", "shame",
                "check", "forum", "brick", "fraud", "stick", "grain", "movie", "cheek", "reign", "label", "theft", "lover",
                "shore", "guilt", "devil", "fence", "glory", "panic", "juice", "debut", "laugh", "chaos", "bruce", "strip",
                "derby", "jenny", "chart", "widow", "essay", "fibre", "patch", "fluid", "virus", "pause", "angel", "cliff",
                "brass", "magic", "honey", "rover", "bacon", "sally", "trick", "bonus", "straw", "shelf", "sauce", "grief",
                "verse", "shade", "heath", "sword", "waist", "slope", "betty", "organ", "skirt", "ghost", "serum", "lorry",
                "brush", "spell", "lodge", "devon", "ozone", "nerve", "craig", "rally", "eagle", "bowel", "suite", "ridge",
                "reach", "human", "gould", "breed", "bloke", "photo", "lemon", "charm", "elite", "basin", "venue", "flood",
                "swing", "punch", "grave", "saint", "intel", "corps", "bunch", "usage", "trail", "carol", "tommy", "width",
                "yield", "ferry", "close", "array", "crack", "clash", "alpha", "truck", "trace", "salad", "medal", "cabin",
                "plain", "bride", "stamp", "tutor", "mount", "bobby", "thumb", "mercy", "fever", "laser", "realm", "blade",
                "boost", "flour", "arrow", "pulse", "elbow", "clive", "graph", "flame", "ellen", "skull", "sweat", "texas",
                "arena", "marsh", "maker", "ulcer", "folly", "wrist", "frost", "donna", "choir", "rider", "wheat", "rival",
                "exile", "flora", "spine", "holly", "lobby", "irony", "ankle", "giant", "mason", "dairy", "merit", "chase",
                "ideal", "agony", "gloom", "toast", "linen", "probe", "scent", "canon", "slide", "metre", "beard", "chalk",
                "blast", "tiger", "vicar", "ruler", "motif", "paddy", "beast", "worry", "ivory", "split", "slave", "hedge",
                "lotus", "shaft", "cargo", "prose", "altar", "small", "flash", "piper", "quest", "quota", "catch", "torch",
                "slice", "feast", "siege", "queue", "blame", "bitch", "towel", "rebel", "decay", "stool", "telly", "hurry",
                "onset", "libel", "belly", "grasp", "twist", "basil", "maxim", "urine", "trunk", "mould", "baron", "fairy",
                "batch", "colon", "spray", "madam", "wendy", "guild", "coral", "thigh", "valve", "disco", "drift", "hazel",
                "teddy", "molly", "greek", "drill", "thief", "tweed", "snake", "derry", "tribe", "trout", "morse", "kylie",
                "spoon", "stall", "daily", "surge", "grove", "benny", "treat", "knock", "gooch", "pearl", "nylon", "purse",
                "depot", "delta", "gauge", "rifle", "onion", "odour", "salon", "radar", "chill", "hardy", "globe", "crust",
                "guess", "wigan", "cloak", "orbit", "oscar", "blaze", "midst", "haven", "tooth", "climb", "flock", "malta",
                "brook", "wrong", "short", "daisy", "chess", "burst", "mandy", "nanny", "dolly", "donor", "cohen", "slate",
                "amino", "booth", "duchy", "hobby", "alley", "idiot", "verge", "leigh", "drain", "crane", "scrap", "wagon",
                "stoke", "abbot", "genre", "costa", "chile", "stack", "mungo", "lever", "dwarf", "witch", "whale", "crest",
                "chord", "nancy", "larry", "perry", "tract", "molla", "badge", "pasta", "joint", "slump", "ditch", "locke",
                "jerry", "irene", "minus", "venus", "troop", "curry", "blend", "sweep", "porch", "penis", "lager", "flint",
                "scarf", "tonic", "cough", "litre", "fiver", "attic", "creed", "cocoa", "weber", "goose", "jelly", "greed",
                "carer", "pizza", "brake", "meter", "assay", "boxer", "puppy", "berry", "guido", "couch", "mound", "brief",
                "glare", "inset", "steak", "moran", "hatch", "cider", "apron", "bloom", "newco", "sting", "token", "quote",
                "niece", "query", "robot", "rotor", "thorn", "patio", "gedge", "cigar", "shout", "sperm", "ethos", "ryder",
                "frown", "satin", "bream", "truce", "spark", "niche", "aisle", "locus", "grill", "forth", "beech", "screw",
                "paste", "brink", "metro", "gypsy", "wight", "burke", "tummy", "friar", "swift", "bunny", "oxide", "vowel",
                "sharp", "hurst", "razor", "fancy", "groom", "satan", "haste", "cache", "guise", "strap", "canoe",
                "build", "peach", "vogue", "tenor", "birch", "gamma", "bliss", "stare", "curse", "flute", "parry", "mafia",
                "viola", "dread", "crook", "stain", "glove", "remit", "genus", "honda", "rouge", "candy", "flank", "wreck",
                "vault", "pinch", "float", "foyer", "camel", "modem", "miner", "flair", "stern", "fauna", "wedge", "clown",
                "ghana", "ledge", "gloss", "tramp", "shine", "brent", "jewel", "ethel", "firth", "bodie", "proxy", "roach",
                "maple", "gorge", "crewe", "decor", "throw", "stair", "wrath", "bingo", "groin", "scalp", "belle", "tempo",
                "savoy", "loser", "aroma", "ascot", "motto", "basic", "havoc", "aggie", "willy", "blind", "batty", "monte",
                "yeast", "comic", "scrum", "wharf", "lynch", "ounce", "broom", "click", "snack", "crypt", "spate", "beryl",
                "pouch", "maize", "liner", "tonne", "vinyl", "flush", "dough", "envoy", "smart", "shark", "farce", "arson",
                "payne", "drake", "turbo", "platt", "minor", "boyle", "broad", "munro", "horne", "deity", "synod", "alien",
                "stein", "vodka", "resin", "alloy", "shrug", "trait", "grand", "spade", "sweet", "sauna", "voter", "scout",
                "gemma", "chuck", "franc", "snail", "scorn", "pedal", "shake", "chant", "spear", "demon", "clone", "swell",
                "heron", "noble", "gleam", "booze", "brett", "kitty", "peril", "chunk", "grape", "finch", "madge", "spike",
                "stead", "senna", "patsy", "rogue", "barge", "laird", "suede", "topaz", "plank", "rhyme", "shire", "relay",
                "chick", "scare", "brute", "hitch", "idiom", "flask", "gully", "blitz", "fella", "indie", "tyler", "creek",
                "buddy", "tunic", "gravy", "olive", "laity", "comet", "forte", "crisp", "duvet", "rhine", "gland", "filth",
                "steen", "aunty", "ethic", "tally", "blanc", "shrub", "atlas", "lance", "croft", "cheer", "mince", "dogma",
                "poppy", "lough", "hound", "sigma", "venom", "adobe", "caste", "combo", "prior", "siren", "whore", "chang",
                "dummy", "alert", "scrub", "shoot", "bosom", "forge", "smash", "acorn", "xerox", "logan", "lapse", "denim",
                "smyth", "piety", "rhino", "syrup", "matey", "flake", "amber", "brace", "flare", "smear", "stump",
                "burgh", "avail", "bluff", "foley", "groan", "mucus", "psalm", "crate", "stile", "zebra", "diver", "bully",
                "reeve", "cobra", "shawl", "spire", "torso", "blank", "think", "brunt", "roche", "pixel", "facet", "jetty",
                "gable", "toxin", "leone", "crush", "optic", "harem", "knack", "moray", "strat", "opium", "poker", "vigil",
                "bowie", "swamp", "sheen", "berth", "debit", "sonny", "sewer", "fritz", "taboo", "norma", "woody", "stint",
                "baton", "mixer", "clint", "slang", "ariel", "wally", "shoal", "bulge", "clump", "flick", "slick", "helix",
                "stunt", "timer", "comma", "cadet", "melon", "hinge", "barth", "smack", "hogan", "champ", "comer", "digit",
                "stout", "glint", "relic"
        ));

        return list;
    }
}




