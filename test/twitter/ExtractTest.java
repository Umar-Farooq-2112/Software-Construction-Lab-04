/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class ExtractTest {
     @Test
     public void testGetTimespan_NormalCase() {
         Tweet tweet1 = new Tweet(1, "Alice", "First tweet", Instant.parse("2023-10-01T10:00:00Z"));
         Tweet tweet2 = new Tweet(2, "Bob", "Second tweet", Instant.parse("2023-10-02T12:30:00Z"));
         Tweet tweet3 = new Tweet(3, "Charlie", "Third tweet", Instant.parse("2023-10-03T09:15:00Z"));

         List<Tweet> tweets = Arrays.asList(tweet1, tweet2, tweet3);

         Timespan timespan = Extract.getTimespan(tweets);

         assertEquals(Instant.parse("2023-10-01T10:00:00Z"), timespan.getStart());
         assertEquals(Instant.parse("2023-10-03T09:15:00Z"), timespan.getEnd());
     }

     @Test
     public void testGetTimespan_AllSameTimestamp() {
         Tweet tweet1 = new Tweet(1, "Alice", "Tweet one", Instant.parse("2023-10-01T10:00:00Z"));
         Tweet tweet2 = new Tweet(2, "Bob", "Tweet two", Instant.parse("2023-10-01T10:00:00Z"));
         Tweet tweet3 = new Tweet(3, "Charlie", "Tweet three", Instant.parse("2023-10-01T10:00:00Z"));

         List<Tweet> tweets = Arrays.asList(tweet1, tweet2, tweet3);

         Timespan timespan = Extract.getTimespan(tweets);

         assertEquals(Instant.parse("2023-10-01T10:00:00Z"), timespan.getStart());
         assertEquals(Instant.parse("2023-10-01T10:00:00Z"), timespan.getEnd());
     }

     @Test
     public void testGetTimespan_SingleTweet() {
         Tweet tweet = new Tweet(1, "Alice", "Only tweet", Instant.parse("2023-10-01T10:00:00Z"));

         List<Tweet> tweets = Arrays.asList(tweet);
         Timespan timespan = Extract.getTimespan(tweets);

         assertEquals(Instant.parse("2023-10-01T10:00:00Z"), timespan.getStart());
         assertEquals(Instant.parse("2023-10-01T10:00:00Z"), timespan.getEnd());
     }

     @Test
     public void testGetTimespan_EmptyTweetsList() {
         List<Tweet> tweets = Collections.emptyList();

         assertThrows(IllegalArgumentException.class, () -> {
             Extract.getTimespan(tweets);
         });
     }


     @Test
     public void testGetMentionedUsers_NormalCase() {
         Tweet tweet1 = new Tweet(1, "Alice", "Hello @Bob and @Charlie!", Instant.parse("2023-10-01T10:00:00Z"));
         Tweet tweet2 = new Tweet(2, "Bob", "Hi @Alice, how are you?", Instant.parse("2023-10-02T12:30:00Z"));
         Tweet tweet3 = new Tweet(3, "Charlie", "@Alice @Bob let's meet up.", Instant.parse("2023-10-03T09:15:00Z"));

         List<Tweet> tweets = Arrays.asList(tweet1, tweet2, tweet3);

         Set<String> mentionedUsers = Extract.getMentionedUsers(tweets);

         Set<String> expected = new HashSet<>(Arrays.asList("bob", "charlie", "alice"));

         assertEquals(expected, mentionedUsers);
     }

     @Test
     public void testGetMentionedUsers_NoMentions() {
         Tweet tweet1 = new Tweet(1, "Alice", "Hello world!", Instant.parse("2023-10-01T10:00:00Z"));
         Tweet tweet2 = new Tweet(2, "Bob", "Good morning everyone.", Instant.parse("2023-10-02T12:30:00Z"));

         List<Tweet> tweets = Arrays.asList(tweet1, tweet2);

         Set<String> mentionedUsers = Extract.getMentionedUsers(tweets);

         assertTrue(mentionedUsers.isEmpty());
     }

     @Test
     public void testGetMentionedUsers_InvalidMentions() {
         // Create sample tweets with invalid mentions
         Tweet tweet1 = new Tweet(1, "Alice", "Contact me at alice@example.com", Instant.parse("2023-10-01T10:00:00Z"));
         Tweet tweet2 = new Tweet(2, "Bob", "Thanks for the support @!", Instant.parse("2023-10-02T12:30:00Z"));
         Tweet tweet3 = new Tweet(3, "Charlie", "Great job @Bob123!", Instant.parse("2023-10-03T09:15:00Z"));

         List<Tweet> tweets = Arrays.asList(tweet1, tweet2, tweet3);

         Set<String> mentionedUsers = Extract.getMentionedUsers(tweets);

         Set<String> expected = new HashSet<>(Arrays.asList("bob123"));

         assertEquals(expected, mentionedUsers);
     }

     @Test
     public void testGetMentionedUsers_CaseInsensitiveUniqueness() {
         Tweet tweet1 = new Tweet(1, "Alice", "Hello @Bob!", Instant.parse("2023-10-01T10:00:00Z"));
         Tweet tweet2 = new Tweet(2, "Bob", "Hi @bob!", Instant.parse("2023-10-02T12:30:00Z"));
         Tweet tweet3 = new Tweet(3, "Charlie", "@BOB let's collaborate.", Instant.parse("2023-10-03T09:15:00Z"));

         List<Tweet> tweets = Arrays.asList(tweet1, tweet2, tweet3);

         Set<String> mentionedUsers = Extract.getMentionedUsers(tweets);

         Set<String> expected = new HashSet<>(Arrays.asList("bob"));

         assertEquals(expected, mentionedUsers);
     }

     @Test
     public void testGetMentionedUsers_MentionsAtStringBoundaries() {
         Tweet tweet1 = new Tweet(1, "Alice", "@Bob is coming to the party.", Instant.parse("2023-10-01T10:00:00Z"));
         Tweet tweet2 = new Tweet(2, "Bob", "Looking forward to the party @Alice", Instant.parse("2023-10-02T12:30:00Z"));
         Tweet tweet3 = new Tweet(3, "Charlie", "@Charlie, are you joining us?", Instant.parse("2023-10-03T09:15:00Z"));

         List<Tweet> tweets = Arrays.asList(tweet1, tweet2, tweet3);

         Set<String> mentionedUsers = Extract.getMentionedUsers(tweets);

         Set<String> expected = new HashSet<>(Arrays.asList("bob", "alice", "charlie"));

         assertEquals(expected, mentionedUsers);
     }

     @Test
     public void testGetMentionedUsers_EmptyTweetsList() {
         List<Tweet> tweets = Collections.emptyList();

         Set<String> mentionedUsers = Extract.getMentionedUsers(tweets);

         assertTrue(mentionedUsers.isEmpty());
     }

     @Test
     public void testGetMentionedUsers_MultipleMentionsInSingleTweet() {
         Tweet tweet = new Tweet(1, "Alice", "Hello @Bob, meet @Charlie and @Dave!", Instant.parse("2023-10-01T10:00:00Z"));

         List<Tweet> tweets = Arrays.asList(tweet);

         Set<String> mentionedUsers = Extract.getMentionedUsers(tweets);

         Set<String> expected = new HashSet<>(Arrays.asList("bob", "charlie", "dave"));

         assertEquals(expected, mentionedUsers);
     }

}
