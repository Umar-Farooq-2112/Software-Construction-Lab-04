/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class FilterTest {

	 @Test
	    public void testWrittenBy_NormalCase() {
	        Tweet tweet1 = new Tweet(1, "Alice", "I love programming in Java", Instant.parse("2023-10-01T10:00:00Z"));
	        Tweet tweet2 = new Tweet(2, "Bob", "Java is awesome!", Instant.parse("2023-10-02T12:30:00Z"));
	        Tweet tweet3 = new Tweet(3, "alice", "Learning Python is fun", Instant.parse("2023-10-03T09:15:00Z"));
	        Tweet tweet4 = new Tweet(4, "Charlie", "Hello World!", Instant.parse("2023-10-04T14:45:00Z"));
	        Tweet tweet5 = new Tweet(5, "Bob", "Advanced Java techniques", Instant.parse("2023-10-05T16:20:00Z"));

	        List<Tweet> tweets = Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5);

	        List<Tweet> result = Filter.writtenBy(tweets, "Alice");

	        assertEquals(2, result.size());
	        assertTrue(result.contains(tweet1));
	        assertTrue(result.contains(tweet3));
	    }

	    @Test
	    public void testWrittenBy_CaseInsensitive() {
	        Tweet tweet1 = new Tweet(1, "Alice", "I love programming in Java", Instant.parse("2023-10-01T10:00:00Z"));
	        Tweet tweet2 = new Tweet(2, "bob", "Java is awesome!", Instant.parse("2023-10-02T12:30:00Z"));
	        Tweet tweet3 = new Tweet(3, "ALICE", "Learning Python is fun", Instant.parse("2023-10-03T09:15:00Z"));

	        List<Tweet> tweets = Arrays.asList(tweet1, tweet2, tweet3);

	        List<Tweet> result = Filter.writtenBy(tweets, "alice");

	        assertEquals(2, result.size());
	        assertTrue(result.contains(tweet1));
	        assertTrue(result.contains(tweet3));
	    }

	    @Test
	    public void testWrittenBy_NoMatches() {
	        Tweet tweet1 = new Tweet(1, "Alice", "I love programming in Java", Instant.parse("2023-10-01T10:00:00Z"));
	        Tweet tweet2 = new Tweet(2, "Bob", "Java is awesome!", Instant.parse("2023-10-02T12:30:00Z"));

	        List<Tweet> tweets = Arrays.asList(tweet1, tweet2);

	        List<Tweet> result = Filter.writtenBy(tweets, "Charlie");

	        assertTrue(result.isEmpty());
	    }


	    @Test
	    public void testInTimespan_NormalCase() {
	        Tweet tweet1 = new Tweet(1, "Alice", "First tweet", Instant.parse("2023-10-01T10:00:00Z"));
	        Tweet tweet2 = new Tweet(2, "Bob", "Second tweet", Instant.parse("2023-10-02T12:30:00Z"));
	        Tweet tweet3 = new Tweet(3, "Charlie", "Third tweet", Instant.parse("2023-10-03T09:15:00Z"));
	        Tweet tweet4 = new Tweet(4, "Dave", "Fourth tweet", Instant.parse("2023-10-04T14:45:00Z"));
	        Tweet tweet5 = new Tweet(5, "Eve", "Fifth tweet", Instant.parse("2023-10-05T16:20:00Z"));

	        List<Tweet> tweets = Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5);

	        Timespan timespan = new Timespan(Instant.parse("2023-10-02T00:00:00Z"),
	                                         Instant.parse("2023-10-04T00:00:00Z"));

	        List<Tweet> result = Filter.inTimespan(tweets, timespan);

	        assertEquals(2, result.size());
	        assertTrue(result.contains(tweet2));
	        assertTrue(result.contains(tweet3));
	    }

	    @Test
	    public void testInTimespan_OnBoundary() {
	        Tweet tweet1 = new Tweet(1, "Alice", "First tweet", Instant.parse("2023-10-01T00:00:00Z"));
	        Tweet tweet2 = new Tweet(2, "Bob", "Second tweet", Instant.parse("2023-10-02T12:30:00Z"));
	        Tweet tweet3 = new Tweet(3, "Charlie", "Third tweet", Instant.parse("2023-10-03T23:59:59Z"));

	        List<Tweet> tweets = Arrays.asList(tweet1, tweet2, tweet3);

	        Timespan timespan = new Timespan(Instant.parse("2023-10-01T00:00:00Z"),
	                                         Instant.parse("2023-10-03T23:59:59Z"));

	        List<Tweet> result = Filter.inTimespan(tweets, timespan);

	        assertEquals(3, result.size());
	        assertTrue(result.containsAll(tweets));
	    }

	    @Test
	    public void testInTimespan_NoMatches() {
	        Tweet tweet1 = new Tweet(1, "Alice", "First tweet", Instant.parse("2023-10-01T10:00:00Z"));
	        Tweet tweet2 = new Tweet(2, "Bob", "Second tweet", Instant.parse("2023-10-02T12:30:00Z"));

	        List<Tweet> tweets = Arrays.asList(tweet1, tweet2);

	        Timespan timespan = new Timespan(Instant.parse("2023-10-03T00:00:00Z"),
	                                         Instant.parse("2023-10-04T00:00:00Z"));

	        List<Tweet> result = Filter.inTimespan(tweets, timespan);

	        assertTrue(result.isEmpty());
	    }

	    @Test
	    public void testContaining_NormalCase() {
	        Tweet tweet1 = new Tweet(1, "Alice", "I love programming in Java", Instant.parse("2023-10-01T10:00:00Z"));
	        Tweet tweet2 = new Tweet(2, "Bob", "Java is awesome!", Instant.parse("2023-10-02T12:30:00Z"));
	        Tweet tweet3 = new Tweet(3, "Charlie", "Learning Python is fun", Instant.parse("2023-10-03T09:15:00Z"));
	        Tweet tweet4 = new Tweet(4, "Dave", "Hello World!", Instant.parse("2023-10-04T14:45:00Z"));
	        Tweet tweet5 = new Tweet(5, "Eve", "Advanced Java techniques", Instant.parse("2023-10-05T16:20:00Z"));

	        List<Tweet> tweets = Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5);

	        List<String> words = Arrays.asList("Java", "Hello");

	        List<Tweet> result = Filter.containing(tweets, words);

	        assertEquals(4, result.size());
	        assertTrue(result.contains(tweet1));
	        assertTrue(result.contains(tweet2));
	        assertTrue(result.contains(tweet4));
	        assertTrue(result.contains(tweet5));
	    }

	    @Test
	    public void testContaining_CaseInsensitive() {
	        Tweet tweet1 = new Tweet(1, "Alice", "I love PROGRAMMING in java", Instant.parse("2023-10-01T10:00:00Z"));
	        Tweet tweet2 = new Tweet(2, "Bob", "JAVA is awesome!", Instant.parse("2023-10-02T12:30:00Z"));
	        Tweet tweet3 = new Tweet(3, "Charlie", "Learning python is fun", Instant.parse("2023-10-03T09:15:00Z"));
	        Tweet tweet4 = new Tweet(4, "Dave", "hello world!", Instant.parse("2023-10-04T14:45:00Z"));

	        List<Tweet> tweets = Arrays.asList(tweet1, tweet2, tweet3, tweet4);

	        List<String> words = Arrays.asList("java", "HELLO");

	        List<Tweet> result = Filter.containing(tweets, words);

	        assertEquals(3, result.size());
	        assertTrue(result.contains(tweet1));
	        assertTrue(result.contains(tweet2));
	        assertTrue(result.contains(tweet4));
	    }

	    @Test
	    public void testContaining_NoMatches() {
	        Tweet tweet1 = new Tweet(1, "Alice", "I love programming in Java", Instant.parse("2023-10-01T10:00:00Z"));
	        Tweet tweet2 = new Tweet(2, "Bob", "Java is awesome!", Instant.parse("2023-10-02T12:30:00Z"));
	        Tweet tweet3 = new Tweet(3, "Charlie", "Learning Python is fun", Instant.parse("2023-10-03T09:15:00Z"));

	        List<Tweet> tweets = Arrays.asList(tweet1, tweet2, tweet3);
	        List<String> words = Arrays.asList("Ruby", "Swift");
	        List<Tweet> result = Filter.containing(tweets, words);

	        assertTrue(result.isEmpty());
	    }


	    @Test
	    public void testContaining_WithPunctuation() {
	        Tweet tweet1 = new Tweet(1, "Alice", "Hello, world!", Instant.parse("2023-10-01T10:00:00Z"));
	        Tweet tweet2 = new Tweet(2, "Bob", "Good morning @alice!", Instant.parse("2023-10-02T12:30:00Z"));
	        Tweet tweet3 = new Tweet(3, "Charlie", "Hello-world is different from Hello World", Instant.parse("2023-10-03T09:15:00Z"));

	        List<Tweet> tweets = Arrays.asList(tweet1, tweet2, tweet3);

	        List<String> words = Arrays.asList("Hello", "Good");

	        List<Tweet> result = Filter.containing(tweets, words);

	        assertEquals(3, result.size());
	        assertTrue(result.contains(tweet1));
	        assertTrue(result.contains(tweet2));
	        assertTrue(result.contains(tweet3));
	    }

	    @Test
	    public void testContaining_EmptyWordsList() {
	        Tweet tweet1 = new Tweet(1, "Alice", "Hello, world!", Instant.parse("2023-10-01T10:00:00Z"));
	        Tweet tweet2 = new Tweet(2, "Bob", "Good morning @alice!", Instant.parse("2023-10-02T12:30:00Z"));

	        List<Tweet> tweets = Arrays.asList(tweet1, tweet2);

	        List<String> words = Collections.emptyList();

	        List<Tweet> result = Filter.containing(tweets, words);

	        assertTrue(result.isEmpty());
	    }

	    @Test
	    public void testContaining_WordsWithSpecialCharacters() {
	        Tweet tweet1 = new Tweet(1, "Alice", "Check out #Java!", Instant.parse("2023-10-01T10:00:00Z"));
	        Tweet tweet2 = new Tweet(2, "Bob", "Using Java_8 features", Instant.parse("2023-10-02T12:30:00Z"));
	        Tweet tweet3 = new Tweet(3, "Charlie", "@Java-Developers are great", Instant.parse("2023-10-03T09:15:00Z"));

	        List<Tweet> tweets = Arrays.asList(tweet1, tweet2, tweet3);

	        List<String> words = Arrays.asList("Java", "Java_8", "Java-Developers");

	        List<Tweet> result = Filter.containing(tweets, words);

	        assertEquals(3, result.size());
	        assertTrue(result.contains(tweet1));
	        assertTrue(result.contains(tweet2));
	        assertTrue(result.contains(tweet3));
	    }

	    @Test
	    public void testContaining_EmptyTweetsList() {
	        List<Tweet> tweets = Collections.emptyList();

	        List<String> words = Arrays.asList("Java", "Hello");

	        List<Tweet> result = Filter.containing(tweets, words);

	        assertTrue(result.isEmpty());
	    }
}
