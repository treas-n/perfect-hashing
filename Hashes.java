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

    /**
    * MinimalPerfectHash is a method that takes a string as input and outputs the hash of that string
    */
    public void minimalPerfectHash() {
        Stack<String> inputStack = new Stack<String>();

        // Pushes the input strings onto the inputStack.
        for (int i = 0; i < inputStrings.length; i++) {
            inputStack.push(inputStrings[i]);
        }

        long startTime = System.nanoTime(); // or System.nanoTime()

        boolean hashSuccess = cichellisAlgorithm(inputStack);

        long endTime = System.nanoTime(); // or System.nanoTime()
        long executionTime = endTime - startTime;

        System.out.println("Execution time: " + executionTime + " nanoseconds");

        // Prints the hash of the hash table.
        if(hashSuccess) {
            System.out.println("Success!");
            // Print all the hashtable in the current thread.
            for (int i = 0; i < hashtable.length; i++) {
                System.out.println(hashtable[i]);
            }
        }
        else {
            System.out.println("Failure!");
        }       
    }

    /**
    * Performs Cichellis algorithm on input stack. This method is called by #run ( Stack ) to perform the algorithm.
    * 
    * @param inputStack - Stack of strings to be operated on
    * 
    * @return true if the algorithm completed successfully false if it was not able to perform the algorithm ( in which case the stack is modified
    */
    public boolean cichellisAlgorithm(Stack<String> inputStack) {
        // Check if the stack is empty.
        if(inputStack.size() == 0) {
            return true;
        }
        
        String currentString = inputStack.pop();

        // Returns true if the algorithm is available for the current string.
        for(int firstValue = 0; firstValue < max; firstValue++) {
            // Returns true if the algorithm is available for the current string.
            for (int lastValue = 0; lastValue < max; lastValue++) {
                int hash = computeHash(currentString.length(), firstValue, lastValue);

                // Checks if the hash is available and if it is available adds the string to the input stack.
                if(ishashIsAvailable(hash)) {
                    hashtable[hash] = currentString;
                    boolean success = cichellisAlgorithm(inputStack);
                    
                    // Returns true if success is true.
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

    /**
    * Computes hash value for given parameters. This is used to determine which table is used for a given set of data.
    * 
    * @param length - Length of data to hash. Must be greater than zero.
    * @param firstValue - First value of the data range. Must be greater than zero.
    * @param lastValue - Last value of the data range. Must be greater than zero.
    * 
    * @return Hash value for given parameters as specified by #hashCode () and #tablesize ( int ) respectively
    */
    public int computeHash(int length, int firstValue, int lastValue) {
        return (length + firstValue + lastValue) % tablesize;
    }

    /**
    * Returns true if the hash is available. This is used to determine if we can get a reference to an object that has been added to the hashtable
    * 
    * @param hash - the hash to check for
    * 
    * @return true if the hash is available false if it is not available or if the object does not have an
    */
    public boolean ishashIsAvailable(int hash) {
        return hashtable[hash] == null;
    }

    /**
    * Sorts the words by cumulative frequency in ascending order. This is done by finding the sum of the first and last letters in each word
    */
    public void sortWordsByCumulativeFrequency() {
        
        wordCumulativeFrequency = new int[tablesize];

        // This method calculates the cumulative frequency of the word cumulative frequency of each word in the inputStrings array.
        for (int i = 0; i < wordCumulativeFrequency.length; i++) {
            String currentString = inputStrings[i];

            int indexOfFirstLetter = findLetter(currentString.charAt(0));
            int indexOfLastLetter = findLetter(currentString.charAt(currentString.length() - 1));

            wordCumulativeFrequency[i] = letterFrequency[indexOfFirstLetter] + letterFrequency[indexOfLastLetter];
        }

        // This method is used to calculate the cumulative frequency of the input strings.
        for (int i = 0; i < wordCumulativeFrequency.length-1; i++) {
            // This method is used to compute the cumulative frequency of the word cumulative frequency.
            for (int j = i+1; j < wordCumulativeFrequency.length; j++) {
                // This method is used to compute the cumulative frequency of the word cumulative frequency.
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

    /**
    * Calculates the letter frequency of each letter in the input string. It does this by iterating over the stack in order to find the first and last letter in each string
    * 
    * @param inputStrings - Stack of strings to
    */
    public void calculateLetterFrequency(Stack<String> inputStrings) {
        Stack<String> temp = (Stack<String>) inputStrings.clone();
        letterFrequency = new int[letters.length];

        // This method is used to find the frequency of the first letter of the string.
        while(temp.size() > 0) {
            String currentString = temp.pop();
            
            int indexOfFirstLetter = findLetter(currentString.charAt(0));
            int indexOfLastLetter = findLetter(currentString.charAt(currentString.length() - 1));

            letterFrequency[indexOfFirstLetter]++;
            letterFrequency[indexOfLastLetter]++;
        }
        
        
    }

    /**
    * Adds letters to the array that are not already in the array. This is done to avoid duplicates in the table
    * 
    * @param inputStrings - the stack of strings
    */
    public void addLetters(Stack<String> inputStrings) {
        letters = new char[tablesize*2];
        int numLetters = 0;

        // Sets letters to the first letter in the inputStrings array.
        for (int i = 0; i < tablesize; i++) {
            char firstletter = inputStrings.get(i).charAt(0);

            // Adds the first letter to the letters array.
            if (!isLetterInArray(firstletter)) {
                letters[numLetters] = firstletter;
                numLetters++;
            }
        }

        // Sets letters to the last letter in the inputStrings array.
        for (int i = 0; i < tablesize; i++) {
            String currentString = inputStrings.get(i).toUpperCase();
            char lastletter = currentString.charAt(currentString.length() - 1);
            

            // Adds the last letter to the letters array.
            if (!isLetterInArray(lastletter)) {
                letters[numLetters] = lastletter;
                numLetters++;
            }
        }

        char[] newLetters = new char[numLetters];
        // Sets the letters of the current set of letters.
        for (int i = 0; i < numLetters; i++) {
            newLetters[i] = letters[i];
        }

        letters = newLetters;
    }

    /**
    * Checks if the letter is in the array. This is used to determine if a letter is part of the word
    * 
    * @param letter - The letter to check for
    * 
    * @return true if the letter is in the array false if it is not and null if the letter is not
    */
    public boolean isLetterInArray(char letter) {
        // Check if the letter is a letter
        if(findLetter(letter) == -1) {
            return false;
        }
        else return true;
    }

    /**
    * Finds the index of the letter in the letters array. This is case insensitive. Returns - 1 if the letter is not found
    * 
    * @param letter - the letter to look for
    * 
    * @return the index of the letter in the letters array or - 1 if the letter is not found in the
    */
    public int findLetter(char letter) {
        // Returns the index of the first letter in the letters array.
        for (int i = 0; i < letters.length; i++) {
            // Returns the index of the letter in the letter array.
            if (letters[i] == Character.toUpperCase(letter)) {
                return i;
            }
        }
        return -1;
    }

    /**
    * Extracts a key from the hashtable. This is useful for debugging and to ensure that the key is removed from the hashtable after it has been extracted
    * 
    * @param key - the key to look for
    * 
    * @return true if the key was found false if it wasn't and null is set to the hash table
    */
    public boolean extractKey(String key) {
        boolean found = false;
        

        // Find the key in the hash table.
        for(int i = 0; i < max; i++) {
            // Searches for the key in the hash table.
            for (int j = 0; j < max; j++) {
                int index = computeHash(key.length(), i, j);

                // Check if the key is in the hashtable.
                if(hashtable[index].equalsIgnoreCase(key)) {
                    found = true;
                    //hashtable[index] = null;
                }
            }   
        }
 
        return found;
    }


    /**
    * Main method for testing. Will take a list of names and print out the key that is found or not.
    * 
    * @param args - String [] of arguments to be passed to the
    */
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

        // This method is used to extract the Jon key from the perfect hash.
        if(perfectHash.extractKey("Jon")) {
            System.out.println("Found!");
        }
        else {
            System.out.println("Not found!");
        }
    }
}