import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.*;

/**
 * This program implements a multithreaded web crawler.
 * The crawler starts from a list of seed URLs and explores linked pages concurrently using a fixed thread pool.
 * It fetches content from web pages, extracts links, and continues crawling until no new links are found.
 * The crawled data is stored in a concurrent map.
 */
public class Question_5_b {

    private final ExecutorService executor;
    private final ConcurrentLinkedQueue<String> urlQueue = new ConcurrentLinkedQueue<>();
    private final Set<String> visitedUrls = ConcurrentHashMap.newKeySet();
    private final AtomicInteger taskCount = new AtomicInteger(0);
    private final ConcurrentHashMap<String, String> crawledData = new ConcurrentHashMap<>();

    /**
     * Initializes the web crawler with a fixed number of threads.
     * @param maxThreads the number of threads to use for concurrent crawling.
     */
    public Question_5_b(int maxThreads) {
        executor = Executors.newFixedThreadPool(maxThreads);
    }

    /**
     * Starts crawling from the given seed URLs.
     * Ensures that each URL is only visited once.
     * @param seedUrls list of initial URLs to start crawling.
     */
    public void startCrawling(List<String> seedUrls) {
        seedUrls.forEach(url -> {
            if (visitedUrls.add(url)) { // Ensures each URL is only visited once
                urlQueue.add(url);
                submitTask();
            }
        });
    }

    /**
     * Submits a new crawling task to the thread pool.
     * Increments the task count to keep track of active crawling tasks.
     */
    private void submitTask() {
        taskCount.incrementAndGet();
        executor.submit(new CrawlTask());
    }

    /**
     * Inner class representing a crawling task.
     * Fetches content from a URL, extracts links, and submits new tasks for unvisited links.
     */
    private class CrawlTask implements Runnable {
        @Override
        public void run() {
            try {
                String url = urlQueue.poll();
                if (url == null) return;

                String content = fetchUrl(url);
                crawledData.put(url, content);
                List<String> links = parseLinks(content);

                for (String link : links) {
                    if (visitedUrls.add(link)) { // Ensures each URL is crawled only once
                        urlQueue.add(link);
                        submitTask();
                    }
                }
            } catch (IOException e) {
                System.err.println("Error fetching URL: " + e.getMessage());
            } finally {
                // Decrement task count and shutdown executor if all tasks are done
                if (taskCount.decrementAndGet() == 0 && urlQueue.isEmpty()) {
                    executor.shutdown();
                }
            }
        }

        /**
         * Fetches the HTML content of a given URL.
         * @param url the URL to fetch.
         * @return the HTML content as a string.
         * @throws IOException if an error occurs while fetching.
         */
        private String fetchUrl(String url) throws IOException {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            try (Scanner scanner = new Scanner(conn.getInputStream())) {
                scanner.useDelimiter("\\A");
                return scanner.hasNext() ? scanner.next() : "";
            }
        }

        /**
         * Parses the HTML content and extracts links.
         * @param content the HTML content as a string.
         * @return a list of extracted links.
         */
        private List<String> parseLinks(String content) {
            List<String> links = new ArrayList<>();
            Pattern pattern = Pattern.compile("href=\"(.*?)\"");
            Matcher matcher = pattern.matcher(content);
            while (matcher.find()) {
                links.add(matcher.group(1));
            }
            return links;
        }
    }

    /**
     * Waits for the crawler to finish execution.
     * @throws InterruptedException if interrupted while waiting.
     */
    public void awaitTermination() throws InterruptedException {
        executor.awaitTermination(1, TimeUnit.MINUTES);
    }

    /**
     * Returns the crawled data (URL -> HTML content mapping).
     * @return a map containing crawled URLs and their respective content.
     */
    public Map<String, String> getCrawledData() {
        return crawledData;
    }

    /**
     * Main function to initiate the web crawler.
     * Starts crawling from the seed URLs with a thread pool of size 4.
     */
    public static void main(String[] args) throws InterruptedException {
        List<String> seedUrls = Arrays.asList("http://example.com", "http://example.org");
        Question_5_b crawler = new Question_5_b(4);
        crawler.startCrawling(seedUrls);
        crawler.awaitTermination();
        System.out.println("Crawled URLs: " + crawler.getCrawledData().size());
    }
}

/**
 * Summary:
 * This program implements a concurrent web crawler using a fixed thread pool.
 * The crawler starts from a set of seed URLs, fetches their content, extracts new links, and continues crawling.
 * We use thread-safe collections to manage the URL queue and prevent duplicate visits.
 * Atomic counters track active tasks, ensuring proper shutdown.
 
 */