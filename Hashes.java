import java.util.Stack;

public class Hashes {
    public String hashtable[], inputStrings[];
    public int tablesize,
                max,
                wordCumulativeFrequency[],
                letterFrequency[],
                letterValues[];
    public char letters[];

            
    public Hashes(Stack<String> stack) {
        tablesize = stack.size();
        max = tablesize / 2;
        hashtable = new String[tablesize];

        addLetters(stack);
        calculateLetterFrequency(stack);

        this.inputStrings = new String[tablesize];
        stack.toArray(inputStrings);

        sortWordsByCumulativeFrequency();

        //System.out.println(inputStrings);

        minimalPerfectHash();
    }

    public void minimalPerfectHash() {
        Stack<String> inputStack = new Stack<String>();

        for (int i = 0; i < inputStrings.length; i++) {
            inputStack.push(inputStrings[i]);
        }

        long startTime = System.nanoTime(); // or System.nanoTime()

        boolean hashSuccess = cichellisAlgorithm(inputStack);

        long endTime = System.nanoTime(); // or System.nanoTime()
        long executionTime = endTime - startTime;

        System.out.println("Execution time: " + executionTime + " nanoseconds");

        if(hashSuccess) {
            System.out.println("Success!");
            for (int i = 0; i < hashtable.length; i++) {
                System.out.println(hashtable[i]);
            }
        }
        else {
            System.out.println("Failure!");
        }       
    }

    public boolean cichellisAlgorithm(Stack<String> inputStack) {
        if(inputStack.size() == 0) {
            return true;
        }
        
        String currentString = inputStack.pop();

        for(int firstValue = 0; firstValue < max; firstValue++) {
            for (int lastValue = 0; lastValue < max; lastValue++) {
                int hash = computeHash(currentString.length(), firstValue, lastValue);

                if(ishashIsAvailable(hash)) {
                    hashtable[hash] = currentString;
                    boolean success = cichellisAlgorithm(inputStack);
                    
                    if(success) 
                        return success;
                    
                    hashtable[hash] = null;
                }
                else {
                    inputStack.push(currentString);
                    return false;
                }
            }
        }
        inputStack.push(currentString);

        return false;
    }

    public int computeHash(int length, int firstValue, int lastValue) {
        return (length + firstValue + lastValue) % tablesize;
    }

    public boolean ishashIsAvailable(int hash) {
        return hashtable[hash] == null;
    }

    public void sortWordsByCumulativeFrequency() {
        
        wordCumulativeFrequency = new int[tablesize];

        for (int i = 0; i < wordCumulativeFrequency.length; i++) {
            String currentString = inputStrings[i];

            int indexOfFirstLetter = findLetter(currentString.charAt(0));
            int indexOfLastLetter = findLetter(currentString.charAt(currentString.length() - 1));

            wordCumulativeFrequency[i] = letterFrequency[indexOfFirstLetter] + letterFrequency[indexOfLastLetter];
        }

        for (int i = 0; i < wordCumulativeFrequency.length-1; i++) {
            for (int j = i+1; j < wordCumulativeFrequency.length; j++) {
                if(wordCumulativeFrequency[i] < wordCumulativeFrequency[j]) {
                    int temp = wordCumulativeFrequency[i];
                    wordCumulativeFrequency[i] = wordCumulativeFrequency[j];
                    wordCumulativeFrequency[j] = temp;

                    String tempString = inputStrings[i];
                    inputStrings[i] = inputStrings[j];
                    inputStrings[j] = tempString;
                }
            }
        }
    }    

    public void calculateLetterFrequency(Stack<String> inputStrings) {
        Stack<String> temp = (Stack<String>) inputStrings.clone();
        letterFrequency = new int[letters.length];

        while(temp.size() > 0) {
            String currentString = temp.pop();
            
            int indexOfFirstLetter = findLetter(currentString.charAt(0));
            int indexOfLastLetter = findLetter(currentString.charAt(currentString.length() - 1));

            letterFrequency[indexOfFirstLetter]++;
            letterFrequency[indexOfLastLetter]++;
        }
        
        
    }

    public void addLetters(Stack<String> inputStrings) {
        letters = new char[tablesize*2];
        int numLetters = 0;

        for (int i = 0; i < tablesize; i++) {
            char firstletter = inputStrings.get(i).charAt(0);

            if (!isLetterInArray(firstletter)) {
                letters[numLetters] = firstletter;
                numLetters++;
            }
        }

        for (int i = 0; i < tablesize; i++) {
            String currentString = inputStrings.get(i).toUpperCase();
            char lastletter = currentString.charAt(currentString.length() - 1);
            

            if (!isLetterInArray(lastletter)) {
                letters[numLetters] = lastletter;
                numLetters++;
            }
        }

        char[] newLetters = new char[numLetters];
        for (int i = 0; i < numLetters; i++) {
            newLetters[i] = letters[i];
        }

        letters = newLetters;
    }

    public boolean isLetterInArray(char letter) {
        if(findLetter(letter) == -1) {
            return false;
        }
        else return true;
    }

    public int findLetter(char letter) {
        for (int i = 0; i < letters.length; i++) {
            if (letters[i] == Character.toUpperCase(letter)) {
                return i;
            }
        }
        return -1;
    }

    public boolean extractKey(String key) {
        boolean found = false;
        

        for(int i = 0; i < max; i++) {
            for (int j = 0; j < max; j++) {
                int index = computeHash(key.length(), i, j);

                if(hashtable[index].equalsIgnoreCase(key)) {
                    found = true;
                    //hashtable[index] = null;
                }
            }   
        }
 
        return found;
    }


    public static void main(String[] args) {
        Stack<String> names = new Stack<String>();

        names.push("John");
        names.push("Mary Jane");
        names.push("Sue");
        names.push("Joe soap");
        names.push("Bob the builder");
        names.push("Bill Burr");
        names.push("Janice");
        names.push("Fred");
        names.push("Marcus");

        Hashes perfectHash = new Hashes(names);

        if(perfectHash.extractKey("Jon")) {
            System.out.println("Found!");
        }
        else {
            System.out.println("Not found!");
        }
    }
}