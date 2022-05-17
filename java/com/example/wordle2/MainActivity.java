package com.example.wordle2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    ImageView[][] greenBoxes = new ImageView[6][5];
    ImageView[][] grayBoxes = new ImageView[6][5];
    ImageView[][] yellowBoxes = new ImageView[6][5];
    TextView[][] guessedLetters = new TextView[6][5];
    ImageView[] keyboard = new ImageView[26];
    Button reset;
    ImageView enter;
    ImageView backspace;
    ImageView space;
    TextView textview2;
    TextView timer;
    TextView enter2;

    //  currentRow is the row the user is working in
    int currentRow = 0;
    //  currentCol is where the next letter is going to go!
    int currentCol = 0;

    long startTime;

    ArrayList<String> verbs = getWordBank();

    char[] letters = new char[5];
    String curLetter;

    String userWord = "";
    String word;
    String newWord;
    TextView outDisplay;
    TextView winDisplay;

    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reset = findViewById(R.id.resetbut);
        reset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
                switchActivities3();
            }
       });

        //  Retrieve the userName from the login screen
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userName = extras.getString("userName");
        }

        reset.setVisibility(View.INVISIBLE);
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

        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i = 0; i < 26; i++) {
            name = "key" + String.valueOf(alphabet.charAt(i));
            System.out.println("Binding keyboard for " + name);
            resID = getResources().getIdentifier(name, "id", getPackageName());
            keyboard[i] = findViewById(resID);
        }
        enter2=findViewById(R.id.textViewEnt);
        enter = findViewById(R.id.enter);
        backspace = findViewById(R.id.backspace);
        space=findViewById(R.id.space);
        winDisplay = findViewById(R.id.winText);
        textview2 = findViewById(R.id.textView51);
        timer = findViewById(R.id.timer);

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
        for (int q = 0; q < verbs.size(); q++) {
            String test = verbs.get(q).toUpperCase(Locale.ROOT);
            verbs.set(q, test);
        }


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    /*
     *   Called whenever a letter is pressed.
     */
    public void keyPressed(View view) {
        if (startTime == 0) {
            startTime = System.currentTimeMillis();
            timer.setText("Timer started...");
        }

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

    /*
     *  Called whenever the delete key is pressed.
     */
    public void delete(View view) {

        curLetter = guessedLetters[currentRow][currentCol].getText().toString();
        if (curLetter.equalsIgnoreCase("")) {
            if (currentCol > 0) {
                currentCol -= 1;
            }
        }
        guessedLetters[currentRow][currentCol].setText("");
    }

    /*
     *  Called whenever the enter key is pressed. Lots to do here!
     */
    public void enter(View view) {
        int counter = 0;
        char holder = 0;
        System.out.println("inside enter method");
        String userWord = "";
        //curLetter = guessedLetters[currentRow][4].getText().toString();
        char letterState[] = {'-', '-', '-', '-', '-'};

        // Has the user entered 5 letters?
        if (currentCol != 4) {
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
            //for (int l = 0; l < 4; l++) {
                //guessedLetters[currentRow][currentCol].setText("");
                //currentCol -= 1;
            //}

            //guessedLetters[currentRow][0].setText("");
            return;
        }

        // Has the user guessed the word correctly?
        if (userWord.equals(word)) {
            for (int i = 0; i < 5; i++) {
                greenBoxes[currentRow][i].animate().alpha(1f).rotationXBy(180).setDuration(500);
                grayBoxes[currentRow][i].animate().alpha(0f);
                winDisplay.setText(word);
                outDisplay.setText("");
                reset.setVisibility(View.VISIBLE);
                //enter2.setText("Reset");

            }
            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            int time = Integer.parseInt(String.valueOf(totalTime));
            timer.setText(millisecondsToTime(totalTime));

            postResults(userName, time, currentRow+1, "solved" );
            return;
        }

        // Check for letters that are correct and in the correct spot and remember which ones.
        for (int i = 0; i < 5; i++) {
            if (userWord.charAt(i) == letters[i]) {
                // Flip the tiles on the board
                greenBoxes[currentRow][i].animate().alpha(1f).rotationXBy(-180).setDuration(500);
                grayBoxes[currentRow][i].animate().alpha(0f);
                //System.out.println("green");
                outDisplay.setText("");
                letterState[i] = 'G';

                setKeyboardColor(userWord.charAt(i), Color.GREEN);
            }
        }

        // Check for letters that are correct but not in the right spot.
        // 'i' loops over the letters the user has entered, skipping anything that's ALREADY Green
        for (int i = 0; i < 5; i++) {
            if (letterState[i] != 'G') {
                // 'j' loops over the letters in the word to be guessed, skipping over
                // anything that's been used in a match already
                for (int j = 0; j < 5; j++) {
                    if (letterState[j] == '-') {
                        if (userWord.charAt(i) == letters[j]) {
                            //  Flip any correct letter in wrong place to YELLOW
                            yellowBoxes[currentRow][i].animate().alpha(1f).rotationXBy(-180).setDuration(500);
                            grayBoxes[currentRow][i].animate().alpha(0f);
                            //System.out.println("yellow");
                            outDisplay.setText("");
                            letterState[j] = 'Y';
                            setKeyboardColor(userWord.charAt(i), Color.YELLOW);
                            break;
                        } else {
                            //  Flip completely incorrect letters from Gray to Gray
                            grayBoxes[currentRow][i].animate().alpha(1f).rotationXBy(-180).setDuration(500);
                            setKeyboardColor(userWord.charAt(i), Color.BLACK);
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
        if (currentRow >= 6 && !userWord.equalsIgnoreCase(word)) {
            System.out.println(word);
            outDisplay.setText("Dumbass, the word was " + word);
            timer.setText("You suck");
            reset.setVisibility(View.VISIBLE);
            postResults(userName, 0, currentRow+1, "unsolved" );
        }
    }

    /*
     *  Format milliseconds as a readable time.
     */
    private String millisecondsToTime(long milliseconds) {
        long minutes = (milliseconds / 1000) / 60;
        long seconds = (milliseconds / 1000) % 60;
        String secondsStr = Long.toString(seconds);
        String secs;
        if (secondsStr.length() >= 2) {
            secs = secondsStr.substring(0, 2);
        } else {
            secs = "0" + secondsStr;
        }
        return minutes + ": " + secs;
    }

    public void reset(View view){
        reset.setVisibility(View.INVISIBLE);
        timer.setText("Timer starts when you click a button");
        startTime=0;
        for (int i=0; i<5; i++){
            for(int j=0;j<6;j++){
                guessedLetters[j][i].setText("");
                greenBoxes[j][i].animate().rotationXBy(180).setDuration(500).alpha(0f);
                grayBoxes[j][i].animate().rotationXBy(180).setDuration(500).alpha(1f);
                yellowBoxes[j][i].animate().rotationXBy(180).setDuration(500).alpha(0f);
                outDisplay.setText("");
                timer.setText("");
                winDisplay.setText("");

                for(int t=0;t<26;t++){
                    char c=(char) ('A'+ t);
                    setKeyboardColor(c,Color.WHITE);
                    currentCol=0;
                    currentRow=0;
                }
            }

        }

        int wordInd = (int) ((Math.round(Math.random() * verbs.size())));
        newWord = (verbs.get(wordInd)).toUpperCase();
        word=newWord;
        // letters is all the correct letters in the correct order
        for (int f = 0; f < 5; f++) {
            letters[f] = word.charAt(f);
        }
        System.out.println(word);
        System.out.println(letters);


    }

    /*
     *  Set the keys to right color, but never flip green to yellow.
     */
    private void setKeyboardColor(char key, int color) {
        int resourceID = getResources().getIdentifier("textView" + String.valueOf(key), "id", getPackageName());
        TextView t = findViewById(resourceID);
        int currentColor = t.getCurrentTextColor();
        System.out.println("Key " + String.valueOf(key) + " is currently color " + color);

        switch (color) {
            case Color.GREEN:
                t.setTextColor(Color.GREEN);
                System.out.println("Set key " + String.valueOf(key) + " to color GREEN = " + color);
                break;
            case Color.BLACK:
                if (currentColor != Color.GREEN && currentColor != Color.YELLOW) {
                    t.setTextColor(Color.BLACK);
                    System.out.println("Set key " + String.valueOf(key) + " to color BLACK = " + color);
                }
                break;
            case Color.WHITE:
                t.setTextColor(Color.WHITE);
                break;
            default:
                if (currentColor != Color.GREEN) {
                    t.setTextColor(Color.YELLOW);
                    System.out.println("Set key " + String.valueOf(key) + " to color YELLOW = " + color);
                }
        }

    }

        /*
     *   This method will push the results of the game to the Kordle Server to be stored.
     */
    public void postResults(String user, int timeMillis, int numTries, String outcome) {
        try {
            //URL url = new URL("http://10.0.2.2:8080/result");
            //URL url = new URL("https://cwfield-kordle.ue.r.appspot.com/result");
            URL url = new URL("https://kordle-svr-5bu27xjxoq-ue.a.run.app/result");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            String input = "{\"user\": \"" + user + "\"," +
                    "\"timeMillis\": \"" + timeMillis + "\"," +
                    "\"numTries\": \"" + numTries + "\"," +
                    "\"outcome\": \"" + outcome + "\"}";
            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();
            //if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println("Failed : HTTP");
            }
            System.out.println("Return code: " + conn.getResponseCode());

//            BufferedReader br = new BufferedReader(new InputStreamReader(
//                    (conn.getInputStream())));
//            String output;
//            System.out.println("Output from Server .... \n");
//            while ((output = br.readLine()) != null) {
//                System.out.println(output);
//            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
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
                "Basic", "Black", "Blind", "Brave", "Brief", "Broad", "Brown", "Cheap", "Chief", "Civil", "again", "acrid",
                "Crazy", "Daily", "Dirty", "Early", "Empty", "Equal", "Exact", "Extra", "Faint", "False", "Fifth",
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
                "space", "hotel", "floor", "style", "event", "press", "blood", "sound", "title", "glass",
                "earth", "river", "whole", "piece", "mouth", "radio", "peace", "start", "share", "truth", "stone",
                "queen", "stock", "horse", "plant", "visit", "scale", "image", "trust", "chair", "speed", "crime",
                "pound", "match", "scene", "stuff", "video", "trial", "phone", "train", "sight",
                "grant", "shape", "offer", "while", "smile", "track", "route", "touch", "youth", "waste", "crown",
                "birth", "faith", "entry", "total", "major", "owner", "lunch", "judge", "guide", "bathe",
                "green", "brain", "phase", "coast", "metal", "index", "adult", "sport", "noise", "agent",
                "motor", "sheet", "brown", "crowd", "shock", "fruit", "steel", "plate", "grass", "dress", "theme",
                "error", "white", "focus", "chief", "sleep", "beach", "sugar", "panel", "dream", "bread", "chain",
                "chest", "frank", "block", "store", "drama", "skill", "round", "rugby", "scope", "plane", "uncle",
                "abuse", "limit", "taste", "fault", "tower", "input", "enemy", "anger", "cycle", "pilot", "frame", "novel",
                "reply", "prize", "nurse", "cream", "depth", "sheep", "spite", "coach", "ratio", "fight", "unity",
                "steam", "final", "clock", "pride", "buyer", "smoke", "score", "watch", "apple", "trend", "proof", "pitch",
                "shirt", "knife", "draft", "shift", "squad", "layer", "curve", "wheel", "topic",
                "guard", "angle", "smell", "grace", "flesh", "mummy", "pupil", "guest", "delay", "mayor", "logic", "album",
                "habit", "billy", "audit", "baker", "paint", "great", "storm", "worth", "black", "daddy", "canal", "robin",
                "leave", "lease", "young", "print", "fleet", "crash", "asset", "cloud", "villa",
                "actor", "ocean", "brand", "craft", "alarm", "bench", "diary", "abbey", "grade", "bible", "jimmy", "shell",
                "cloth", "piano", "clerk", "stake", "stand", "mouse", "cable", "manor", "local", "penny", "shame",
                "forum", "brick", "fraud", "stick", "grain", "movie", "cheek", "reign", "label", "theft", "lover",
                "shore", "guilt", "devil", "fence", "glory", "panic", "juice", "debut", "laugh", "chaos", "strip",
                "derby", "chart", "widow", "essay", "fibre", "patch", "fluid", "virus", "pause", "angel", "cliff",
                "brass", "magic", "honey", "rover", "bacon", "trick", "bonus", "straw", "shelf", "sauce", "grief",
                "verse", "shade", "heath", "sword", "waist", "slope", "organ", "skirt", "ghost", "serum", "lorry",
                "brush", "spell", "lodge", "ozone", "nerve", "rally", "eagle", "bowel", "suite", "ridge",
                "reach", "human", "gould", "breed", "bloke", "photo", "lemon", "charm", "elite", "basin", "venue", "flood",
                "swing", "punch", "grave", "saint", "intel", "corps", "bunch", "usage", "trail", "carol", "width",
                "yield", "ferry", "array", "crack", "clash", "alpha", "truck", "trace", "salad", "medal", "cabin",
                "plain", "bride", "stamp", "tutor", "mount", "bobby", "thumb", "mercy", "fever", "laser", "realm", "blade",
                "boost", "flour", "arrow", "pulse", "elbow", "clive", "graph", "flame", "skull", "sweat", "texas",
                "arena", "marsh", "maker", "ulcer", "folly", "wrist", "frost", "choir", "rider", "wheat", "rival",
                "exile", "spine", "holly", "lobby", "irony", "ankle", "giant", "dairy", "merit", "chase",
                "ideal", "agony", "gloom", "toast", "linen", "probe", "scent", "canon", "slide", "metre", "beard", "chalk",
                "blast", "tiger", "vicar", "ruler", "motif", "paddy", "beast", "worry", "ivory", "split", "slave", "hedge",
                "lotus", "shaft", "cargo", "prose", "altar", "small", "flash", "piper", "quest", "quota", "catch", "torch",
                "slice", "feast", "siege", "queue", "bitch", "towel", "rebel", "decay", "stool", "telly", "hurry",
                "onset", "libel", "belly", "grasp", "twist", "basil", "maxim", "urine", "trunk", "mould", "baron", "fairy",
                "batch", "colon", "spray", "madam", "guild", "coral", "thigh", "valve", "disco", "drift", "hazel",
                "teddy", "molly", "greek", "drill", "thief", "tweed", "snake", "tribe", "trout", "morse",
                "spoon", "stall", "daily", "surge", "grove", "treat", "knock", "pearl", "nylon", "purse",
                "depot", "delta", "gauge", "rifle", "onion", "odour", "salon", "radar", "chill", "hardy", "globe", "crust",
                "guess", "cloak", "orbit", "oscar", "blaze", "midst", "haven", "tooth", "flock", "malta",
                "brook", "wrong", "short", "daisy", "chess", "nanny", "dolly", "donor", "slate",
                "amino", "booth", "duchy", "hobby", "alley", "idiot", "verge", "leigh", "drain", "crane", "scrap", "wagon",
                "stoke", "abbot", "genre", "costa", "chile", "stack", "mungo", "lever", "dwarf", "witch", "whale", "crest",
                "chord", "tract", "badge", "pasta", "joint", "slump", "ditch", "locke",
                "minus", "venus", "troop", "curry", "blend", "sweep", "porch", "lager", "flint",
                "scarf", "tonic", "cough", "litre", "fiver", "attic", "creed", "cocoa", "goose", "jelly", "greed",
                "carer", "pizza", "brake", "meter", "assay", "boxer", "puppy", "berry", "guido", "couch", "mound", "brief",
                "glare", "inset", "steak", "moran", "hatch", "cider", "apron", "bloom", "sting", "token", "quote",
                "niece", "query", "robot", "rotor", "thorn", "patio", "cigar", "shout", "sperm", "ethos", "ryder",
                "frown", "satin", "bream", "truce", "spark", "niche", "aisle", "locus", "grill", "forth", "beech", "screw",
                "paste", "brink", "metro", "gypsy", "wight", "burke", "tummy", "friar", "swift", "bunny", "oxide", "vowel",
                "sharp", "hurst", "razor", "fancy", "groom", "haste", "cache", "guise", "strap", "canoe",
                "peach", "vogue", "tenor", "birch", "gamma", "bliss", "stare", "curse", "flute", "parry", "mafia",
                "viola", "dread", "crook", "stain", "glove", "remit", "genus", "honda", "rouge", "candy", "flank", "wreck",
                "vault", "pinch", "float", "foyer", "camel", "modem", "miner", "flair", "stern", "fauna", "wedge", "clown",
                "ledge", "gloss", "tramp", "shine", "jewel", "firth", "proxy", "roach",
                "maple", "gorge", "decor", "throw", "stair", "wrath", "bingo", "groin", "scalp", "tempo",
                "savoy", "loser", "aroma", "ascot", "motto", "basic", "havoc", "aggie", "willy", "blind", "batty", "monte",
                "yeast", "comic", "scrum", "wharf", "lynch", "ounce", "broom", "click", "snack", "crypt", "spate", "beryl",
                "pouch", "liner", "tonne", "vinyl", "flush", "dough", "envoy", "smart", "shark", "farce", "arson",
                "turbo", "minor", "broad", "deity", "synod", "alien",
                "vodka", "resin", "alloy", "shrug", "trait", "grand", "spade", "sweet", "sauna", "voter", "scout",
                "gemma", "chuck", "franc", "snail", "scorn", "pedal", "shake", "chant", "spear", "demon", "clone", "swell",
                "heron", "noble", "gleam", "booze", "kitty", "peril", "chunk", "grape", "finch", "madge", "spike",
                "stead", "senna", "patsy", "rogue", "barge", "laird", "suede", "plank", "rhyme", "shire", "relay",
                "chick", "scare", "brute", "hitch", "idiom", "flask", "gully", "blitz", "fella", "indie", "creek",
                "buddy", "tunic", "gravy", "olive", "laity", "comet", "forte", "crisp", "duvet", "rhine", "gland", "filth",
                "steen", "aunty", "ethic", "tally", "blanc", "shrub", "atlas", "lance", "croft", "cheer", "mince", "dogma",
                "poppy", "lough", "hound", "sigma", "venom", "adobe", "caste", "combo", "prior", "siren", "whore", "chang",
                "dummy", "alert", "scrub", "shoot", "bosom", "forge", "smash", "acorn", "xerox", "lapse", "denim",
                "piety", "rhino", "syrup", "matey", "flake", "amber", "brace", "flare", "smear", "stump",
                "burgh", "avail", "bluff", "foley", "groan", "mucus", "psalm", "crate", "stile", "zebra", "diver", "bully",
                "reeve", "cobra", "shawl", "spire", "torso", "blank", "think", "brunt", "roche", "pixel", "facet", "jetty",
                "gable", "toxin", "crush", "optic", "harem", "knack", "moray", "opium", "poker", "vigil",
                "swamp", "sheen", "berth", "debit", "sewer", "fritz", "taboo", "woody", "stint",
                "baton", "mixer", "slang", "shoal", "bulge", "clump", "flick", "slick", "helix",
                "stunt", "timer", "comma", "cadet", "melon", "hinge", "barth", "smack", "hogan", "champ", "comer", "digit",
                "stout", "glint", "relic",
                "which", "there", "their", "about", "would", "these", "other", "words", "could", "write", "first", "water",
                "after", "where", "right", "think", "three", "years", "place", "sound", "great", "again", "still", "every",
                "small", "found", "those", "never", "under", "might", "while", "house", "world", "below", "asked", "going",
                "large", "until", "along", "shall", "being", "often", "earth", "began", "since", "study", "night", "light",
                "above", "paper", "parts", "young", "story", "point", "times", "heard", "whole", "white", "given", "means",
                "music", "miles", "thing", "today", "later", "using", "money", "lines", "order", "group", "among", "learn",
                "known", "space", "table", "early", "trees", "short", "hands", "state", "black", "shown", "stood", "front",
                "voice", "kinds", "makes", "comes", "close", "power", "lived", "vowel", "taken", "built", "heart", "ready",
                "quite", "class", "bring", "round", "horse", "shows", "piece", "green", "stand", "birds", "start", "river",
                "tried", "least", "field", "whose", "girls", "leave", "added", "color", "third", "hours", "moved", "plant",
                "doing", "names", "forms", "heavy", "ideas", "cried", "check", "floor", "begin", "woman", "alone", "plane",
                "spell", "watch", "carry", "wrote", "clear", "named", "books", "child", "glass", "human", "takes", "party",
                "build", "seems", "blood", "sides", "seven", "mouth", "solve", "north", "value", "death", "maybe", "happy",
                "tells", "gives", "looks", "shape", "lives", "steps", "areas", "sense", "speak", "force", "ocean", "speed",
                "women", "metal", "south", "grass", "scale", "cells", "lower", "sleep", "wrong", "pages", "ships", "needs",
                "rocks", "eight", "major", "level", "total", "ahead", "reach", "stars", "store", "sight", "terms", "catch",
                "works", "board", "cover", "songs", "equal", "stone", "waves", "guess", "dance", "spoke", "break", "cause",
                "radio", "weeks", "lands", "basic", "liked", "trade", "fresh", "final", "fight", "meant", "drive", "spent",
                "local", "waxes", "knows", "train", "bread", "homes", "teeth", "coast", "thick", "brown", "clean", "quiet",
                "sugar", "facts", "steel", "forth", "rules", "notes", "units", "peace", "month", "verbs", "seeds", "helps",
                "sharp", "visit", "woods", "chief", "walls", "cross", "wings", "grown", "cases", "foods", "crops", "fruit",
                "stick", "wants", "stage", "sheep", "nouns", "plain", "drink", "bones", "apart", "turns", "moves", "touch",
                "angle", "based", "range", "marks", "tired", "older", "farms", "spend", "shoes", "goods", "chair", "twice",
                "cents", "empty", "alike", "style", "broke", "pairs", "count", "enjoy", "score", "shore", "roots", "paint",
                "heads", "shook", "serve", "angry", "crowd", "wheel", "quick", "dress", "share", "alive", "noise", "solid",
                "cloth", "signs", "hills", "types", "drawn", "worth", "truck", "piano", "upper", "loved", "usual", "faces",
                "drove", "cabin", "boats", "towns", "proud", "court", "model", "prime", "fifty", "plans", "yards", "prove",
                "tools", "price", "sheet", "smell", "boxes", "raise", "match", "truth", "roads", "threw", "enemy", "lunch",
                "chart", "scene", "graph", "doubt", "guide", "winds", "block", "grain", "smoke", "mixed", "games", "wagon",
                "sweet", "topic", "extra", "plate", "title", "knife", "fence", "falls", "cloud", "wheat", "plays", "enter",
                "broad", "steam", "atoms", "press", "lying", "basis", "clock", "taste", "grows", "thank", "storm", "agree",
                "brain", "track", "smile", "funny", "beach", "stock", "hurry", "saved", "sorry", "giant", "trail", "offer",
                "ought", "rough", "daily", "avoid", "keeps", "throw", "allow", "cream", "laugh", "edges", "teach", "frame",
                "bells", "dream", "magic", "occur", "ended", "chord", "false", "skill", "holes", "dozen", "brave", "apple",
                "climb", "outer", "pitch", "ruler", "holds", "fixed", "costs", "calls", "blank", "staff", "labor", "eaten",
                "youth", "tones", "honor", "globe", "gases", "doors", "poles", "loose", "apply", "tears", "exact", "brush",
                "chest", "layer", "whale", "minor", "faith", "tests", "judge", "items", "worry", "waste", "hoped", "strip",
                "begun", "aside", "lakes", "bound", "depth", "candy", "event", "worse", "aware", "shell", "rooms", "ranch",
                "image", "snake", "aloud", "dried", "likes", "motor", "pound", "knees", "refer", "fully", "chain", "shirt",
                "flour", "drops", "spite", "orbit", "banks", "shoot", "curve", "tribe", "tight", "blind", "slept", "shade",
                "claim", "flies", "theme", "queen", "fifth", "union", "hence", "straw", "entry", "issue", "birth", "feels",
                "anger", "brief", "rhyme", "glory", "guard", "flows", "flesh", "owned", "trick", "yours", "sizes", "noted",
                "width", "burst", "route", "lungs", "uncle", "bears", "royal", "kings", "forty", "trial", "cards", "brass",
                "opera", "chose", "owner", "vapor", "beats", "mouse", "tough", "wires", "meter", "tower", "finds", "inner",
                "stuck", "arrow", "poems", "label", "swing", "solar", "truly", "tense", "beans", "split", "rises", "weigh",
                "hotel", "stems", "pride", "swung", "grade", "digit", "badly", "boots", "pilot", "sales", "swept", "lucky",
                "prize", "stove", "tubes", "acres", "wound", "steep", "slide", "trunk", "error", "porch", "slave", "exist",
                "faced", "mines", "marry", "juice", "raced", "waved", "goose", "trust", "fewer", "favor", "mills", "views",
                "joint", "eager", "spots", "blend", "rings", "adult", "index", "nails", "horns", "balls", "flame", "rates",
                "drill", "trace", "skins", "waxed", "seats", "stuff", "ratio", "minds", "dirty", "silly", "coins", "hello",
                "trips", "leads", "rifle", "hopes", "bases", "shine", "bench", "moral", "fires", "meals", "shake", "shops",
                "cycle", "movie", "slope", "canoe", "teams", "folks", "fired", "bands", "thumb", "shout", "canal", "habit",
                "reply", "ruled", "fever", "crust", "shelf", "walks", "midst", "crack", "print", "tales", "coach", "stiff",
                "flood", "verse", "awake", "rocky", "march", "fault", "swift", "faint", "civil", "ghost", "feast", "blade",
                "limit", "germs", "reads", "ducks", "dairy", "worst", "gifts", "lists", "stops", "rapid", "brick", "claws",
                "beads", "beast", "skirt", "cakes", "lions", "frogs", "tries", "nerve", "grand", "armed", "treat", "honey",
                "moist", "legal", "penny", "crown", "shock", "taxes", "sixty", "altar", "pulls", "sport", "drums", "talks",
                "dying", "dates", "drank", "blows", "lever", "wages", "proof", "drugs", "tanks", "sings", "tails", "pause",
                "herds", "arose", "hated", "clues", "novel", "shame", "burnt", "races", "flash", "weary", "heels", "token",
                "coats", "spare", "shiny", "alarm", "dimes", "sixth", "clerk", "mercy", "sunny", "guest", "float", "shone",
                "pipes", "worms", "bills", "sweat", "suits", "smart", "upset", "rains", "sandy", "rainy", "parks", "sadly",
                "fancy", "rider", "unity", "bunch", "rolls", "crash", "craft", "newly", "gates", "hatch", "paths", "funds",
                "wider", "grace", "grave", "tides", "admit", "shift", "sails", "pupil", "tiger", "angel", "cruel", "agent",
                "drama", "urged", "patch", "nests", "vital", "sword", "blame", "weeds", "screw", "vocal", "bacon", "chalk",
                "cargo", "crazy", "acted", "goats", "arise", "witch", "loves", "queer", "dwell", "backs", "ropes", "shots",
                "merry", "phone", "cheek", "peaks", "ideal", "beard", "eagle", "creek", "cries", "ashes", "stall", "yield",
                "mayor", "opens", "input", "fleet", "tooth", "cubic", "wives", "burns", "poets", "apron", "spear", "organ",
                "cliff", "stamp", "paste", "rural", "baked", "chase", "slice", "slant", "knock", "noisy", "sorts", "stays",
                "wiped", "blown", "piled", "clubs", "cheer", "widow", "twist", "tenth", "hides", "comma", "sweep", "spoon",
                "stern", "crept", "maple", "deeds", "rides", "muddy", "crime", "jelly", "ridge", "drift", "dusty", "devil",
                "tempo", "humor", "sends", "steal", "tents", "waist", "roses", "reign", "noble", "cheap", "dense", "linen",
                "geese", "woven", "posts", "hired", "wrath", "salad", "bowed", "tires", "shark", "belts", "grasp", "blast",
                "polar", "fungi", "tends", "pearl", "loads", "jokes", "veins", "frost", "hears", "loses", "hosts", "diver",
                "phase", "toads", "alert", "tasks", "seams", "coral", "focus", "naked", "puppy", "jumps", "spoil", "quart",
                "macro", "fears", "flung", "spark", "vivid", "brook", "steer", "spray", "decay", "ports", "socks", "urban",
                "goals", "grant", "minus", "films", "tunes", "shaft", "firms", "skies", "bride", "wreck", "flock", "stare",
                "hobby", "bonds", "dared", "faded", "thief", "crude", "pants", "flute", "votes", "tonal", "radar", "wells",
                "skull", "hairs", "argue", "wears", "dolls", "voted", "caves", "cared", "broom", "scent", "panel", "fairy",
                "olive", "bends", "prism", "lamps", "cable", "peach", "ruins", "rally", "schwa", "lambs", "sells", "cools",
                "draft", "charm", "limbs", "brake", "gazed", "cubes", "delay", "beams", "fetch", "ranks", "array", "harsh",
                "camel", "vines", "picks", "naval", "purse", "rigid", "crawl", "toast", "soils", "sauce", "basin", "ponds",
                "twins", "wrist", "fluid", "pools", "brand", "stalk", "robot", "reeds", "hoofs", "buses", "sheer", "grief",
                "bloom", "dwelt", "melts", "risen", "flags", "knelt", "fiber", "roofs", "freed", "armor", "piles", "aimed",
                "algae", "twigs", "lemon", "ditch", "drunk", "rests", "chill", "slain", "panic", "cords", "tuned", "crisp",
                "ledge", "dived", "swamp", "clung", "stole", "molds", "yarns", "liver", "gauge", "breed", "stool", "gulls",
                "awoke", "gross", "diary", "rails", "belly", "trend", "flask", "stake", "fried", "draws", "actor", "handy",
                "bowls", "haste", "scope", "deals", "knots", "moons", "essay", "thump", "hangs", "bliss", "dealt", "gains",
                "bombs", "clown", "palms", "cones", "roast", "tidal", "bored", "chant", "acids", "dough", "camps", "swore",
                "lover", "hooks", "males", "cocoa", "punch", "award", "reins", "ninth", "noses", "links", "drain", "fills",
                "nylon", "lunar", "pulse", "flown", "elbow", "fatal", "sites", "moths", "meats", "foxes", "mined", "attic",
                "fiery", "mount", "usage", "swear", "snowy", "rusty", "scare", "traps", "relax", "react", "valid", "robin",
                "cease", "gills", "prior", "safer", "polio", "loyal", "swell", "salty", "marsh", "vague", "weave", "mound",
                "seals", "mules", "virus", "scout", "acute", "windy", "stout", "folds", "seize", "hilly", "joins", "pluck",
                "stack", "lords", "dunes", "burro", "hawks", "trout", "feeds", "scarf", "halls", "coals", "towel", "souls",
                "elect", "buggy", "pumps", "loans", "spins", "files", "oxide", "pains", "photo", "rival", "flats", "syrup",
                "rodeo", "sands", "moose", "pints", "curly", "comic", "cloak", "onion", "clams", "scrap", "didst", "couch",
                "codes", "fails", "ounce", "lodge", "greet", "gypsy", "utter", "paved", "zones", "fours", "alley", "tiles",
                "bless", "crest", "elder", "kills", "yeast", "erect", "bugle", "medal", "roles", "hound", "snail", "alter",
                "ankle", "relay", "loops", "zeros", "bites", "modes", "debts", "realm", "glove", "rayon", "swims", "poked",
                "stray", "lifts", "maker", "lumps", "graze", "dread", "barns", "docks", "masts", "pours", "wharf", "curse",
                "plump", "robes", "seeks", "cedar", "curls", "jolly", "myths", "cages", "gloom", "locks", "pedal", "beets",
                "crows", "anode", "slash", "creep", "rowed", "chips", "fists", "wines", "cares", "valve", "newer", "motel",
                "ivory", "necks", "clamp", "barge", "blues", "alien", "frown", "strap", "crews", "shack", "gonna", "saves",
                "stump", "ferry", "idols", "cooks", "juicy", "glare", "carts", "alloy", "bulbs", "lawns", "lasts", "fuels",
                "oddly", "crane", "filed", "weird", "shawl", "slips", "troop", "bolts", "suite", "sleek", "quilt", "tramp",
                "blaze", "atlas", "odors", "scrub", "crabs", "probe", "logic", "adobe", "exile", "rebel", "grind", "sting",
                "spine", "cling", "desks", "grove", "leaps", "prose", "lofty", "agony", "snare", "tusks", "bulls", "moods",
                "humid", "finer", "dimly", "plank", "china", "pines", "guilt", "sacks", "brace", "quote", "lathe", "gaily",
                "fonts", "scalp", "adopt", "foggy", "ferns", "grams", "clump", "perch", "tumor", "teens", "crank", "fable",
                "hedge", "genes", "sober", "boast", "tract", "cigar", "unite", "owing", "thigh", "haiku", "swish", "dikes",
                "wedge", "booth", "eased", "frail", "cough", "tombs", "darts", "forts", "choir", "pouch", "pinch", "hairy",
                "buyer", "torch", "vigor", "waltz", "heats", "herbs", "users", "flint", "click", "madam", "bleak", "blunt",
                "aided", "lacks", "masks", "waded", "risks", "nurse", "chaos", "sewed", "cured", "ample", "lease", "steak",
                "sinks", "merit", "bluff", "bathe", "gleam", "bonus", "colts", "shear", "gland", "silky", "skate", "birch",
                "anvil", "sleds", "groan", "maids", "meets", "speck", "hymns", "hints", "drown", "bosom", "slick", "quest",
                "coils", "spied", "snows", "stead", "snack", "plows", "blond", "tamed", "thorn", "waits", "glued", "banjo",
                "tease", "arena", "bulky", "carve", "stunt", "warms", "shady", "razor", "folly", "leafy", "notch", "fools",
                "otter", "pears", "flush", "genus", "ached", "fives", "flaps", "spout", "smote", "fumes", "adapt", "cuffs",
                "tasty", "stoop", "clips", "disks", "sniff", "lanes", "brisk", "imply", "demon", "super", "furry", "raged",
                "growl", "texts", "hardy", "stung", "typed", "hates", "wiser", "timid", "serum", "beaks", "rotor", "casts",
                "baths", "glide", "plots", "trait", "resin", "slums", "lyric", "puffs", "decks", "brood", "mourn", "aloft",
                "abuse", "whirl", "edged", "ovary", "quack", "heaps", "slang", "await", "civic", "saint", "bevel", "sonar",
                "aunts", "packs", "froze", "tonic", "corps", "swarm", "frank", "repay", "gaunt", "wired", "niece", "cello",
                "needy", "chuck", "stony", "media", "surge", "hurts", "repel", "husky", "dated", "hunts", "mists", "exert",
                "dries", "mates", "sworn", "baker", "spice", "oasis", "boils", "spurs", "doves", "sneak", "paces", "colon",
                "siege", "strum", "drier", "cacao", "humus", "bales", "piped", "nasty", "rinse", "boxer", "shrub", "amuse",
                "tacks", "cited", "slung", "delta", "laden", "larva", "rents", "yells", "spool", "spill", "crush", "jewel",
                "snaps", "stain", "kicks", "tying", "slits", "rated", "eerie", "smash", "plums", "zebra", "earns", "bushy",
                "scary", "squad", "tutor", "silks", "slabs", "bumps", "evils", "fangs", "snout", "peril", "pivot", "yacht",
                "lobby", "jeans", "grins", "viola", "liner", "comet", "scars", "chops", "raids", "eater", "slate", "skips",
                "soles", "misty", "urine", "knobs", "sleet", "holly", "pests", "forks", "grill", "trays", "pails", "borne",
                "tenor", "wares", "carol", "woody", "canon", "wakes", "kitty", "miner", "polls", "shaky", "nasal", "scorn",
                "chess", "taxis", "crate", "shyly", "tulip", "forge", "nymph", "budge", "lowly", "abide", "depot", "oases",
                "asses", "sheds", "fudge", "pills", "rivet", "thine", "groom", "lanky", "boost", "broth", "heave", "gravy",
                "beech", "timed", "quail", "inert", "gears", "chick", "hinge", "trash", "clash", "sighs", "renew", "bough",
                "dwarf", "slows", "quill", "shave", "spore", "sixes", "chunk", "madly", "paced", "braid", "fuzzy", "motto",
                "spies", "slack", "mucus", "magma", "awful", "discs", "erase", "posed", "asset", "cider", "taper", "theft",
                "churn", "satin", "slots", "taxed", "bully", "sloth", "shale", "tread", "raked", "curds", "manor", "aisle",
                "bulge", "loins", "stair", "tapes", "leans", "bunks", "squat", "towed", "lance", "panes", "sakes", "heirs",
                "caste", "dummy", "pores", "fauna", "crook", "poise", "epoch", "risky", "warns", "fling", "berry", "grape",
                "flank", "drags", "squid", "pelts", "icing", "irony", "irons", "barks", "whoop", "choke", "diets", "whips",
                "tally", "dozed", "twine", "kites", "bikes", "ticks", "riots", "roars", "vault", "looms", "scold", "blink",
                "dandy", "pupae", "sieve", "spike", "ducts", "lends", "pizza", "brink", "widen", "plumb", "pagan", "feats",
                "bison", "soggy", "scoop", "argon", "nudge", "skiff", "amber", "sexes", "rouse", "salts", "hitch", "exalt",
                "leash", "dined", "chute", "snort", "gusts", "melon", "cheat", "reefs", "llama", "lasso", "debut", "quota",
                "oaths", "prone", "mixes", "rafts", "dives", "stale", "inlet", "flick", "pinto", "brows", "untie", "batch",
                "greed", "chore", "stirs", "blush", "onset", "barbs", "volts", "beige", "swoop", "paddy", "laced", "shove",
                "jerky", "poppy", "leaks", "fares", "dodge", "godly", "squaw", "affix", "brute", "nicer", "undue", "snarl",
                "merge", "doses", "showy", "daddy", "roost", "vases", "swirl", "petty", "colds", "curry", "cobra", "genie",
                "flare", "messy", "cores", "soaks", "ripen", "whine", "amino", "plaid", "spiny", "mowed", "baton", "peers",
                "vowed", "pious", "swans", "exits", "afoot", "plugs", "idiom", "chili", "rites", "serfs", "cleft", "berth",
                "grubs", "annex", "dizzy", "hasty", "latch", "wasps", "mirth", "baron", "plead", "aloof", "aging", "pixel",
                "bared", "mummy", "hotly", "auger", "buddy", "chaps", "badge", "stark", "fairs", "gully", "mumps", "emery",
                "filly", "ovens", "drone", "gauze", "idiot", "fussy", "annoy", "shank", "gouge", "bleed", "elves", "roped",
                "unfit", "baggy", "mower", "scant", "grabs", "fleas", "lousy", "album", "sawed", "murky", "infer",
                "burly", "waged", "dingy", "brine", "kneel", "creak", "vanes", "smoky", "spurt", "combs", "easel", "laces",
                "humps", "rumor", "aroma", "horde", "swiss", "leapt", "opium", "slime", "afire", "pansy", "mares", "soaps",
                "husks", "snips", "hazel", "lined", "cafes", "naive", "wraps", "sized", "piers", "beset", "agile", "tongs",
                "steed", "fraud", "booty", "valor", "downy", "witty", "mossy", "psalm", "scuba", "tours", "polka", "milky",
                "gaudy", "shrug", "tufts", "wilds", "laser", "truss", "hares", "creed", "lilac", "siren", "tarry", "bribe",
                "swine", "muted", "flips", "cures", "sinew", "boxed", "hoops", "gasps", "hoods", "niche", "yucca", "glows",
                "sewer", "whack", "fuses", "gowns", "droop", "bucks", "pangs", "mails", "whisk", "haven", "clasp", "sling",
                "stint", "urges", "champ", "piety", "chirp", "pleat", "posse", "sunup", "menus", "howls", "quake", "knack",
                "plaza", "fiend", "caked", "bangs", "erupt", "poker", "olden", "cramp", "voter", "poses", "manly", "slump",
                "fined", "grips", "gaped", "purge", "hiked", "maize", "fluff", "strut", "sloop", "prowl", "roach", "cocks",
                "bland", "dials", "plume", "slaps", "soups", "dully", "wills", "foams", "solos", "skier", "eaves", "totem",
                "fused", "latex", "veils", "mused", "mains", "myrrh", "racks", "galls", "gnats", "bouts", "sisal", "shuts",
                "hoses", "dryly", "hover", "gloss", "seeps", "denim", "putty", "guppy", "leaky", "dusky", "filth", "oboes",
                "spans", "fowls", "adorn", "glaze", "haunt", "dares", "obeys", "bakes", "abyss", "smelt", "gangs", "aches",
                "trawl", "claps", "undid", "spicy", "hoist", "fades", "vicar", "acorn", "pussy", "gruff", "musty", "tarts",
                "snuff", "hunch", "truce", "tweed", "dryer", "loser", "sheaf", "moles", "lapse", "tawny", "vexed", "autos",
                "wager", "domes", "sheen", "clang", "spade", "sowed", "broil", "slyly", "studs", "grunt", "donor", "slugs",
                "aspen", "homer", "croak", "tithe", "halts", "avert", "havoc", "hogan", "glint", "ruddy", "jeeps", "flaky",
                "ladle", "taunt", "snore", "fines", "props", "prune", "pesos", "radii", "pokes", "tiled", "daisy", "heron",
                "villa", "farce", "binds", "cites", "fixes", "jerks", "livid", "waked", "inked", "booms", "chews", "licks",
                "hyena", "scoff", "lusty", "sonic", "smith", "usher", "tucks", "vigil", "molts", "sects", "spars", "dumps",
                "scaly", "wisps", "sores", "mince", "panda", "flier", "axles", "plied", "booby", "patio", "rabbi", "petal",
                "polyp", "tints", "grate", "troll", "tolls", "relic", "phony", "bleat", "flaws", "flake", "snags", "aptly",
                "drawl", "ulcer", "soapy", "bossy", "monks", "crags", "caged", "twang", "diner", "taped", "cadet", "grids",
                "spawn", "guile", "noose", "mores", "girth", "slimy", "aides", "spasm", "burrs", "alibi", "lymph", "saucy",
                "muggy", "liter", "joked", "goofy", "exams", "enact", "stork", "lured", "toxic", "omens", "nears", "covet",
                "wrung", "forum", "venom", "moody", "alder", "sassy", "flair", "guild", "prays", "wrens", "hauls", "stave",
                "tilts", "pecks", "stomp", "gales", "tempt", "capes", "mesas", "omits", "tepee", "harry", "wring", "evoke",
                "limes", "cluck", "lunge", "highs", "canes", "giddy", "lithe", "verge", "khaki", "queue", "loath", "foyer",
                "outdo", "fared", "deter", "crumb", "astir", "spire", "jumpy", "extol", "buoys", "stubs", "lucid", "thong",
                "whiff", "maxim", "hulls", "clogs", "slats", "jiffy", "arbor", "cinch", "igloo", "goody", "gazes",
                "dowel", "calms", "bitch", "scowl", "gulps", "coded", "waver", "mason", "lobes", "ebony", "flail", "isles",
                "clods", "dazed", "adept", "oozed", "sedan", "clays", "warts", "ketch", "skunk", "manes", "adore", "sneer",
                "mango", "flora", "roomy", "minks", "thaws", "watts", "freer", "exult", "plush", "paled", "twain",
                "clink", "scamp", "pawed", "grope", "bravo", "gable", "stink", "sever", "waned", "rarer", "regal", "wards",
                "fawns", "babes", "unify", "amend", "oaken", "glade", "visor", "hefty", "nines", "throb", "pecan", "butts",
                "pence", "sills", "jails", "flyer", "saber", "nomad", "miter", "beeps", "domed", "gulfs", "curbs", "heath",
                "moors", "aorta", "larks", "tangy", "wryly", "cheep", "rages", "evade", "lures", "freak", "vogue", "tunic",
                "slams", "knits", "dumpy", "mania", "spits", "firth", "hikes", "trots", "nosed", "clank", "dogma", "bloat",
                "balsa", "graft", "middy", "stile", "keyed", "finch", "sperm", "chaff", "wiles", "amigo", "copra", "amiss",
                "eying", "twirl", "lurch", "popes", "chins", "smock", "tines", "guise", "grits", "junks", "shoal", "cache",
                "tapir", "atoll", "deity", "toils", "spree", "mocks", "scans", "revel", "raven", "hoary", "reels",
                "scuff", "mimic", "weedy", "corny", "truer", "rouge", "ember", "floes", "torso", "wipes", "edict", "sulky",
                "recur", "groin", "baste", "kinks", "surer", "piggy", "moldy", "franc", "liars", "inept", "gusty", "facet",
                "jetty", "equip", "leper", "slink", "soars", "cater", "dowry", "sided", "yearn", "decoy", "taboo", "ovals",
                "heals", "pleas", "beret", "spilt", "rover", "endow", "pygmy", "carat", "abbey", "vents", "waken",
                "chimp", "fumed", "sodas", "vinyl", "clout", "wades", "mites", "smirk", "bores", "bunny", "surly", "frock",
                "foray", "purer", "milks", "query", "mired", "blare", "froth", "gruel", "navel", "paler", "puffy", "casks",
                "grime", "derby", "mamma", "gavel", "teddy", "vomit", "moans", "allot", "defer", "wield", "viper", "louse",
                "erred", "hewed", "abhor", "wrest", "waxen", "adage", "ardor", "stabs", "pored", "rondo", "loped", "fishy",
                "bible", "hires", "foals", "feuds", "jambs", "thuds", "jeers", "knead", "quirk", "rugby", "expel", "greys",
                "rigor", "ester", "lyres", "aback", "glues", "lotus", "lurid", "rungs", "hutch", "thyme", "valet", "tommy",
                "yokes", "epics", "trill", "pikes", "ozone", "caper", "chime", "frees", "famed", "leech", "smite", "neigh",
                "erode", "robed", "hoard", "salve", "conic", "gawky", "craze", "jacks", "gloat", "mushy", "rumps", "fetus",
                "wince", "pinks", "shalt", "toots", "glens", "cooed", "rusts", "stews", "shred", "parka", "chugs", "winks",
                "clots", "shrew", "booed", "filmy", "juror", "dents", "gummy", "grays", "hooky", "butte", "dogie", "poled",
                "reams", "fifes", "spank", "gayer", "tepid", "spook", "taint", "flirt", "rogue", "spiky", "opals", "miser",
                "cocky", "coyly", "balmy", "slosh", "brawl", "aphid", "faked", "hydra", "brags", "chide", "yanks", "allay",
                "video", "altos", "eases", "meted", "chasm", "longs", "excel", "taffy", "impel", "savor", "koala", "quays",
                "dawns", "proxy", "clove", "duets", "dregs", "tardy", "briar", "grimy", "ultra", "meaty", "halve", "wails",
                "suede", "mauve", "envoy", "arson", "coves", "gooey", "brews", "sofas", "chums", "amaze", "zooms", "abbot",
                "halos", "scour", "suing", "cribs", "sagas", "enema", "wordy", "harps", "coupe", "molar", "flops", "weeps",
                "mints", "ashen", "felts", "askew", "munch", "mewed", "divan", "vices", "jumbo", "blobs", "blots", "spunk",
                "acrid", "topaz", "cubed", "clans", "flees", "slurs", "gnaws", "welds", "fords", "emits", "agate", "pumas",
                "mends", "dukes", "plies", "canny", "hoots", "oozes", "lamed", "fouls", "clefs", "nicks", "mated",
                "skims", "brunt", "tuber", "tinge", "fates", "ditty", "thins", "frets", "eider", "bayou", "mulch", "fasts",
                "amass", "damps", "morns", "friar", "palsy", "vista", "croon", "conch", "udder", "tacos", "skits", "mikes",
                "quits", "preen", "aster", "adder", "elegy", "pulpy", "scows", "baled", "hovel", "lavas", "crave", "optic",
                "welts", "busts", "knave", "razed", "shins", "totes", "scoot", "dears", "crock", "mutes", "trims", "skein",
                "doted", "shuns", "veers", "fakes", "yoked", "wooed", "hacks", "sprig", "wands", "lulls", "seers", "snobs",
                "nooks", "pined", "perky", "mooed", "frill", "dines", "booze", "tripe", "prong", "drips", "odder", "levee",
                "antic", "sidle", "pithy", "corks", "yelps", "joker", "fleck", "buffs", "scram", "tiers", "bogey", "doled",
                "irate", "vales", "coped", "hails", "elude", "bulks", "aired", "vying", "stags", "strew", "cocci", "pacts",
                "scabs", "silos", "dusts", "yodel", "terse", "jaded", "baser", "jibes", "foils", "sways", "forgo", "slays",
                "preys", "treks", "quell", "peeks", "assay", "lurks", "eject", "boars", "trite", "belch", "gnash", "wanes",
                "lutes", "whims", "dosed", "chewy", "snipe", "umbra", "teems", "dozes", "upped", "brawn", "doped",
                "shush", "rinds", "slush", "moron", "voile", "woken", "fjord", "sheik", "jests", "kayak", "slews", "toted",
                "saner", "drape", "patty", "raves", "sulfa", "grist", "skied", "vixen", "civet", "vouch", "tiara", "homey",
                "moped", "runts", "serge", "kinky", "rills", "corns", "brats", "pries", "amble", "fries", "loons", "tsars",
                "datum", "musky", "gnome", "ravel", "ovule", "icily", "liken", "lemur", "frays", "silts", "sifts",
                "plods", "ramps", "tress", "earls", "dudes", "waive", "karat", "jolts", "peons", "beers", "horny", "pales",
                "wreak", "lairs", "lynch", "stank", "swoon", "idler", "abort", "blitz", "ensue", "atone", "bingo", "roves",
                "kilts", "scald", "adios", "cynic", "dulls", "memos", "elfin", "dales", "peels", "peals", "bares", "sinus",
                "crone", "sable", "hinds", "shirk", "enrol", "wilts", "roams", "duped", "cysts", "mitts", "safes", "spats",
                "coops", "filet", "knell", "refit", "covey", "punks", "kilns", "fitly", "abate", "heeds", "duels",
                "wanly", "ruffs", "gauss", "lapel", "jaunt", "whelp", "cleat", "gauzy", "dirge", "edits", "wormy", "moats",
                "smear", "prods", "bowel", "frisk", "vests", "bayed", "rasps", "tames", "delve", "embed", "befit", "wafer",
                "ceded", "novas", "feign", "spews", "larch", "huffs", "doles", "mamas", "hulks", "pried", "brims", "irked",
                "aspic", "swipe", "mealy", "skimp", "bluer", "slake", "dowdy", "brays", "egret", "flunk",
                "phlox", "gripe", "peony", "douse", "blurs", "darns", "slunk", "lefts", "chats", "inane", "vials", "stilt",
                "rinks", "woofs", "wowed", "bongs", "frond", "ingot", "evict", "singe", "shyer", "slops", "dolts",
                "drool", "dells", "whelk", "hippy", "feted", "ether", "cocos", "hives", "jibed", "mazes", "trios",
                "squab", "laths", "leers", "pasta", "rifts", "lopes", "alias", "whirs", "diced", "slags", "lodes", "foxed",
                "idled", "prows", "plait", "malts", "chafe", "cower", "toyed", "chefs", "keels", "sties", "racer", "etude",
                "sucks", "sulks", "czars", "copse", "ailed", "abler", "rabid", "golds", "croup", "snaky", "visas",
                "palls", "mopes", "boned", "wispy", "raved", "swaps", "doily", "pawns", "tamer", "poach", "baits",
                "damns", "gumbo", "daunt", "prank", "hunks", "buxom", "honks", "stows", "unbar", "idles", "routs",
                "sages", "goads", "remit", "copes", "deign", "culls", "girds", "haves", "lucks", "stunk", "dodos", "shams",
                "snubs", "icons", "usurp", "dooms", "soled", "comas", "paves", "maths", "perks", "limps", "wombs",
                "blurb", "daubs", "cokes", "sours", "stuns", "cased", "musts", "coeds", "cowed", "aping", "zoned", "rummy",
                "fetes", "skulk", "quaff", "rajah", "deans", "reaps", "galas", "tills", "roved", "kudos", "toned", "pared",
                "scull", "vexes", "punts", "snoop", "bails", "dames", "hazes", "marts", "voids", "ameba", "rakes",
                "adzes", "harms", "rears", "satyr", "swill", "hexes", "colic", "leeks", "hurls", "yowls", "ivies", "plops",
                "musks", "papaw", "jells", "bused", "cruet", "filch", "zests", "rooks", "laxly", "rends", "loams",
                "basks", "sires", "carps", "pokey", "flits", "muses", "bawls", "shuck", "viler", "lisps", "peeps", "sorer",
                "lolls", "prude", "diked", "floss", "flogs", "scums", "dopes", "bogie", "pinky", "tubas", "scads",
                "lowed", "yeses", "biked", "qualm", "evens", "caned", "gawks", "whits", "gluts", "romps", "bests",
                "dunce", "crony", "joist", "tunas", "boner", "malls", "parch", "avers", "crams", "pares", "dally", "bigot",
                "flays", "leach", "gushy", "pooch", "huger", "golfs", "mires", "flues", "loafs", "arced",
                "fiefs", "dints", "dazes", "pouts", "cored", "yules", "lilts", "beefs", "mutts", "fells",
                "cowls", "spuds", "lames", "jawed", "dupes", "bylaw", "noons", "nifty", "clued", "vireo", "gapes",
                "metes", "cuter", "maims", "droll", "cupid", "mauls", "sedge", "papas", "eking", "loots", "hilts",
                "meows", "beaus", "dices", "peppy", "riper", "fogey", "gists", "gilts", "skews", "cedes",
                "alums", "okays", "elope", "grump", "wafts", "blimp", "hefts", "mulls", "hosed", "cress", "doffs",
                "ruder", "pixie", "waifs", "ousts", "pucks", "biers", "gulch", "hobos", "lints", "teals",
                "garbs", "pewee", "helms", "turfs", "quips", "wends", "banes", "napes", "icier", "swats", "bagel", "hexed",
                "ogres", "goner", "gilds", "pyres", "lards", "bides", "paged", "talon", "flout", "medic", "veals", "putts",
                "dirks", "dotes", "blurt", "piths", "acing", "barer", "whets", "gaits", "wools", "dunks",
                "swabs", "surfs", "okapi", "chows", "shoos", "parry", "decal", "furls",
                "cilia", "sears", "novae", "murks", "warps", "slues", "lamer", "saris", "weans", "purrs", "dills", "togas",
                "newts", "meany", "bunts", "razes", "goons", "wicks", "ruses", "vends", "geode", "drake", "lofts",
                "pulps", "lauds", "mucks", "vises", "mocha", "oiled", "roman", "ethyl", "gotta", "fugue", "smack", "gourd",
                "bumpy", "radix", "fatty", "borax", "cubit", "cacti", "gamma", "focal", "avail", "papal", "golly", "elite",
                "versa", "billy", "adieu", "howdy", "rhino", "norms", "bobby", "axiom", "setup", "yolks", "terns",
                "mixer", "genre", "knoll", "abode", "junta", "gorge", "combo", "alpha", "overt", "kinda", "spelt", "prick",
                "nobly", "audio", "modal", "veldt", "warty", "fluke", "bonny", "bream", "rosin", "bolls", "doers",
                "downs", "beady", "motif", "humph", "fella", "mould", "crepe", "aloha", "glyph", "azure", "riser",
                "locus", "lumpy", "beryl", "wanna", "brier", "tuner", "rowdy", "mural", "timer", "canst", "krill",
                "quoth", "lemme", "triad", "tenon", "amply", "deeps", "padre", "pacer", "octal", "dolly", "trans",
                "sumac", "foamy", "lolly", "giver", "codex", "manna", "unwed", "vodka", "ferny", "salon", "duple",
                "boron", "revue", "crier", "alack", "inter", "dilly", "whist", "cults", "spake", "reset", "loess", "decor",
                "mover", "verve", "ethic", "gamut", "lingo", "dunno", "align", "sissy", "incur", "reedy", "avant", "piper",
                "calyx", "basil", "coons", "seine", "piney", "lemma", "trams", "winch", "saith", "ionic",
                "heady", "harem", "tummy", "sally", "shied", "dross", "farad", "saver", "tilde", "jingo", "bower", "serif",
                "facto", "belle", "inset", "bogus", "caved", "forte", "sooty", "bongo", "credo", "basal",
                "aglow", "glean", "gusto", "hymen", "ethos", "terra", "brash", "scrip", "swash", "tinny", "itchy",
                "trice", "jowls", "gongs", "garde", "twill", "sower", "henry", "awash", "libel", "spurn",
                "sabre", "rebut", "penal", "obese", "sonny", "quirt", "tacit", "greek", "xenon", "hullo", "pique",
                "roger", "negro", "hadst", "gecko", "beget", "uncut", "aloes", "louis", "quint", "clunk", "raped", "salvo",
                "diode", "matey", "hertz", "xylem", "kiosk", "apace", "cawed", "peter", "wench", "cohos", "sorta",
                "bytes", "tango", "nutty", "axial", "natal", "clomp", "gored", "bandy", "gunny", "runic",
                "whizz", "rupee", "fated", "wiper", "bards", "briny", "staid", "hocks", "ochre", "yummy", "gents", "soupy",
                "roper", "swath", "cameo", "edger", "spate", "gimme", "ebbed", "breve", "theta", "deems", "dykes", "servo",
                "telly", "tabby", "tares", "blocs", "welch", "ghoul", "vitae", "cumin", "dinky", "bronc", "tabor", "teeny",
                "comer", "borer", "sired", "privy", "mammy", "deary", "gyros", "conga", "quire", "thugs", "furor",
                "bloke", "runes", "bawdy", "cadre", "toxin", "annul", "egged", "anion", "nodes", "picky", "stein", "jello",
                "audit", "echos", "letup", "eyrie", "fount", "caped", "axons", "amuck", "banal", "riled",
                "umber", "miler", "fibre", "agave", "bated", "bilge", "feint", "pudgy", "mater", "manic", "umped",
                "pesky", "strep", "slurp", "pylon", "puree", "caret", "temps", "newel", "yawns", "seedy", "treed", "coups",
                "rangy", "brads", "mangy", "loner", "circa", "tibia", "afoul", "mommy",
                "natty", "ducal", "bidet", "bulgy", "metre", "lusts", "unary", "baler", "sited", "shies", "hasps",
                "titan", "binge", "shunt", "femur",
                "libra", "seder", "honed", "shims",
                "maned", "omega", "reals", "testy",
                "crimp", "splat", "cutie", "pasty", "moray", "levis", "ratty",
                "joust", "motet", "viral", "nukes", "grads", "comfy", "woozy", "blued", "whomp", "sward", "metro",
                "skeet", "chine", "aerie", "bowie", "tubby", "emirs", "unzip", "slobs", "trike", "funky", "ducat",
                "dewey", "skoal", "wadis", "taker",
                "inlay", "venue", "louts", "peaty", "radon", "beaut", "raspy", "unfed", "crick", "nappy",
                "kiwis", "squib", "sitar", "kiddo",
                "lager", "runny", "unpin", "globs",
                "sushi", "tacky", "stoke", "kaput", "butch", "croft", "outgo",
                "cagey", "fudgy", "epoxy", "leggy", "felon", "beefy",
                "caddy", "drear", "turbo", "helix", "zonal",
                "nosey", "fryer", "retch", "tenet", "whiny",
                "begot", "balks", "sines", "sigma", "abase", "unset",
                "sated", "odium", "latin", "dings", "kraut", "dicks",
                "fanny", "gibes", "aural", "rapes", "techs",
                "ninny", "liege",
                "joule",
                "chump", "nanny", "trump", "chomp", "homos",
                "avast", "boded", "lobed", "snoot", "payer", "sappy",
                "aegis", "ditto", "jazzy",
                "bitty", "imbue", "spoof", "phyla", "wacky",
                "skids", "crypt", "faire", "kazoo",
                "limbo", "ducky", "faker", "vibes", "gassy", "unlit", "nervy", "biter",
                "saxes", "recap", "facie", "dicey", "legit", "gurus", "edify", "tweak",
                "typos", "rerun", "polly", "nulls", "hater", "lefty", "mafia", "debug",
                "kilos",
                "trove", "curie",
                "remix", "jimmy", "gamed",
                "bozos", "jocks", "donut", "avian", "chock",
                "spacy", "puked", "leery", "amens", "tesla",
                "intro", "fiver", "coder", "pukes", "haled", "chard",
                "bruin", "reuse", "toons", "frats", "silty", "decaf", "softy",
                "bimbo", "loony", "sarge",
                "coned", "upend", "vegan",
                "reeks",
                "yucky", "hokey", "resew", "nacho", "mimed", "melds",
                "debit", "gulag", "betas",
                "fizzy", "dorky",
                "jades", "rehab", "octet", "homed", "dorks", "eared",
                "macaw", "scone", "hyper", "salsa", "mooch", "gated", "unjam",
                "venal", "knish",
                "fends", "caulk", "hones", "botch", "sully",
                "sooth", "gelds", "raper", "pager", "fixer", "tuxes",
                "wacko", "emote", "xerox", "rebid", "grout",
                "semis", "acmes", "disco", "whore",
                "tutus",
                "nerds", "gizmo", "owlet", "shard",
                "matte", "droid", "yikes",
                "craps", "shags", "clone", "hazed", "macho", "biker", "aquas", "porky",
                "goofs", "divvy", "noels", "jived", "oldie",
                "codas", "zilch", "orcas", "retro", "parse", "rants", "micro",
                "girly", "nexus", "zippy", "wimps",
                "grail", "hales", "roust",
                "hypes", "quark",
                "doper", "linty", "stash",
                "zesty", "wimpy", "kabob",
                "heist", "tarps", "lamas", "busty", "preps",
                "cabby", "gutsy", "faxed", "pushy",
                "retry", "karma", "burps", "deuce",
                "doggy", "scams", "mimes", "promo",
                "muffs", "oinks", "minis", "sauna", 
                "stats", "condo", "loopy", "dorms", "ascot", "dopey",
                "proms", "tweet", "toady", "hider", "nuked", "fatso",
                "narcs", "delis", "hyped", "futon",
                "carom", "kebab", "jilts", "duals", "artsy", "modem", "psych",
                "sicko", "klutz", "piker", "aimer", "limos", "flack",
                "dutch", "mucky", "shire", "layup", "axing",
                "sudsy", "batty", "pitas", "gouda",
                "nitro", "carny", "limey", "orals", "hirer", "taxer", "elate",
                "snots", "manta",
                "lotta", "boggy", "locos",
                "gluey", "vroom", "fetal", "renal", "crocs",
                "wetly", "semen",
                "miked",
                "bally", "plumy", "yourn", "bipod", "ambit",
                "dozer", "groat",
                "lites", "plats", "payed", "areal",
                "thees", "spitz", "gipsy", "sprat",
                "blowy", "wahoo", "astro", "kited",
                "teary",
                "bazar", "fugit", "plunk", "lucre",
                "corky", "rayed", "begat",
                "nippy", "magna", "hydro", "milch", "lazed", "inapt",
                "baulk",
                "beaux", "orate", "intra", "epsom",
                "mezzo", "anima",
                "cycad", "talky", "fucks", "amide",
                "tutti", "tench",
                "missy", "unsee", "tiros", "welsh", "fosse",
                "firma", "laird", "thunk", "uncap",
                "fondu", "coney", "polis",
                "wroth", "chars", "unbox", "syncs",
                "joeys", "bocks", "endue", "darer",
                "nones", "biles", "licit",
                "dweeb",
                "biome", "ginny", "polos", "unman",
                "wedgy", "ridgy", "wifey", "vapes", "whoas", "yolky", "ulnas", "reeky",
                "liker", "fulls", "refry",
                "churl", "tiler", "memes",
                "haler",
                "geoid", "ovate", "torah",
                "yurts", "defog", "nimbi", "mothy", "joyed",
                "faxer", "hoppy", "celeb",
                "ureas", "uvula",
                "webby", "lippy",
                "carer", "rater", "poops", "fecal",
                "oared",
                "herby", "titty", "sepoy", "fusee",
                "shits", "honky", "tacet",
                "gamer", "waspy", "pubic",
                "riced",
                "dhows",
                "washy", "unarm", "styes", "waker",
                "guano", "scuds", "aider", "golem",
                "goths", "vocab", "vined",
                "tikis", "indie", "tater",
                "barfs", "flabs", "punny",
                "fauns", "pseud", "lurer", "palmy", "pocky",
                "pends", "recta", "keens",
                "ulnar", "expos", "oiler",
                "buzzy", "clews", "creme",
                "email", "kulak", "fugal",
                "smogs", "amine", "gonad",
                "zappy", "calks",
                "monad", "cruft",
                "nohow", "podgy",
                "pubes", "paver", "maced", "tubed", "bezel", "porks", "fader", "liers", "smurf", "farts",
                "dildo", "redox", "nerdy"
        ));

        return list;
    }
    private void switchActivities3(){
        System.out.println("switchActivities3");
        Intent switchActivityIntent=new Intent(this,Leaderboard.class);
        startActivity(switchActivityIntent);
    }
}