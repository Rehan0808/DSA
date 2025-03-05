/*
 * This program processes a list of tweets from February 2024 and identifies the top N hashtags based on their frequency.
 *
 * The algorithm works as follows:
 * 1. It filters the tweets to only include those with dates in February 2024.
 * 2. It parses each tweet's text to extract words starting with a '#' symbol and counts their occurrences.
 * 3. It then sorts the hashtags first by descending count, and then by lexicographical order (if counts are equal).
 * 4. Finally, the top N hashtags are formatted into a table and printed.
 *
 * Overall, the algorithm efficiently extracts, counts, and sorts hashtags from a collection of tweets. 
 * The sample test in the main method confirms that the algorithm works as expected.
 */

 import java.time.LocalDate;
 import java.time.Month;
 import java.time.format.DateTimeParseException;
 import java.util.*;
 import java.util.stream.Collectors;
 
 public class Question_4_a {
 
     /**
      * Inner class representing a hashtag and its occurrence count.
      */
     static class HashtagCount {
         String hashtag;
         int count;
 
         /**
          * Constructor for HashtagCount.
          *
          * @param hashtag The hashtag string.
          * @param count The number of occurrences of the hashtag.
          */
         public HashtagCount(String hashtag, int count) {
             this.hashtag = hashtag;
             this.count = count;
         }
 
         /**
          * Returns a formatted string representation of the hashtag and count.
          *
          * @return A string in the format "hashtag count".
          */
         @Override
         public String toString() {
             return hashtag + " " + count;
         }
     }
 
     /**
      * The main method serves as the entry point for the program.
      * It sets up sample tweet data, processes the tweets to find the top hashtags,
      * and then prints the results in a formatted table.
      *
      * @param args Command-line arguments (not used).
      */
     public static void main(String[] args) {
         // Sample Input (February 2024 tweets)
         List<String[]> tweets = new ArrayList<>();
         tweets.add(new String[]{"135", "13", "2024-02-01", "Enjoying a great start to the day. #HappyDay #MorningVibes"});
         tweets.add(new String[]{"136", "14", "2024-02-03", "Another #HappyDay with good vibes! #FeelGood"});
         tweets.add(new String[]{"137", "15", "2024-02-04", "Productivity peaks! #WorkLife #ProductiveDay"});
         tweets.add(new String[]{"138", "16", "2024-02-04", "Exploring new tech frontiers. #TechLife #Innovation"});
         tweets.add(new String[]{"139", "17", "2024-02-05", "Gratitude for today's moments. #HappyDay #Thankful"});
         tweets.add(new String[]{"140", "18", "2024-02-07", "Innovation drives us. #TechLife #FutureTech"});
         tweets.add(new String[]{"141", "19", "2024-02-09", "Connecting with nature's serenity. #Nature #Peaceful"});
 
         // Find top 3 hashtags
         List<HashtagCount> result = findTopHashtags(tweets, 3);
 
         // Print formatted output
         System.out.println("+-----------+-------+");
         System.out.println("| hashtag   | count |");
         System.out.println("+-----------+-------+");
         for (HashtagCount hc : result) {
             System.out.printf("| %-10s| %-6d|%n", hc.hashtag, hc.count);
         }
         System.out.println("+-----------+-------+");
     }
 
     /**
      * Processes a list of tweets to find the top N hashtags.
      *
      * This function first filters tweets to only include those from February 2024.
      * It then extracts hashtags from the tweet text, counts their occurrences,
      * and sorts them in descending order by count (with lexicographical order as a tie-breaker).
      *
      * @param tweets The list of tweets, where each tweet is represented as a String array.
      *               Expected format: [id, user, date, text].
      * @param topN The number of top hashtags to return.
      * @return A list of HashtagCount objects representing the top N hashtags.
      */
     public static List<HashtagCount> findTopHashtags(List<String[]> tweets, int topN) {
         // Filter tweets to include only those from February 2024
         List<String[]> filteredTweets = tweets.stream()
                 .filter(tweet -> isFebruary2024(tweet[2]))
                 .collect(Collectors.toList());
 
         // Count occurrences of hashtags in the filtered tweets
         Map<String, Integer> hashtagCounts = new HashMap<>();
         for (String[] tweet : filteredTweets) {
             String text = tweet[3];
             String[] words = text.split("\\s+"); // Split by spaces
             for (String word : words) {
                 if (word.startsWith("#")) {
                     hashtagCounts.put(word, hashtagCounts.getOrDefault(word, 0) + 1);
                 }
             }
         }
 
         // Sort the hashtag entries by descending count, and then by lexicographical order if counts are equal
         List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(hashtagCounts.entrySet());
         sortedEntries.sort((a, b) -> {
             int countCompare = b.getValue().compareTo(a.getValue()); // Descending order by count
             return (countCompare != 0) ? countCompare : b.getKey().compareTo(a.getKey());
         });
 
         // Convert the top N entries to a list of HashtagCount objects
         return sortedEntries.stream()
                 .limit(topN)
                 .map(entry -> new HashtagCount(entry.getKey(), entry.getValue()))
                 .collect(Collectors.toList());
     }
 
     /**
      * Checks whether a given date string corresponds to a date in February 2024.
      *
      * The function attempts to parse the date string and then verifies if the year is 2024 and the month is February.
      *
      * @param dateStr The date string in ISO format (e.g., "2024-02-01").
      * @return True if the date is in February 2024, false otherwise.
      */
     private static boolean isFebruary2024(String dateStr) {
         try {
             LocalDate date = LocalDate.parse(dateStr);
             return date.getYear() == 2024 && date.getMonth() == Month.FEBRUARY;
         } catch (DateTimeParseException e) {
             return false;
         }
     }
 }
 
 /*
 In Question_4_a, we process tweets from February 2024 to identify the top N hashtags by occurrence.
 The program filters tweets by date, extracts hashtags from tweet texts, counts their frequencies,
 sorts the hashtags in descending order (with lexicographical ordering as a tie-breaker), and prints the top N results in a formatted table.
 The test data in the main method confirms that the algorithm works as expected.
 */
 