# WK1_2_Assignment

Problem 1: 
-
Social Media Username Availability Checker

**Scenario:** 

You're building a registration system for a social media platform with 10 million users.
Users frequently check if usernames are available before registering.

**Problem Statement:** 

Design a system to check username availability in real-time. The system
should:

● Check if a username exists in O(1) time

● Handle 1000 concurrent username checks per second

● Suggest similar available usernames if the requested one is taken

● Track popularity of attempted usernames

**Concepts Covered:**

● Hash table basics (key-value mapping)

● O(1) lookup performance

● Collision handling

● Frequency counting

Problem 2: 
-
E-commerce Flash Sale Inventory Manager

**Scenario:** 

During a flash sale, 50,000 customers simultaneously try to purchase limited stock
items (only 100 units available). You need to prevent overselling while maintaining high
performance.

**Problem Statement:**

Implement an inventory management system that:

● Tracks product stock levels in real-time

● Processes purchase requests in O(1) time

● Handles concurrent requests safely

● Maintains a waiting list when stock runs out

● Provides instant stock availability checks

**Concepts Covered:**

● Hash table for instant stock lookup

● Collision resolution (multiple users buying same product)

● Load factor management during high traffic

● Performance benchmarking under load

Problem 3: 
-
DNS Cache with TTL (Time To Live)

**Scenario:** 

Build a DNS resolver cache that stores domain-to-IP mappings to reduce lookup
times from 100ms to <1ms. Cache entries should expire after a specified TTL.

**Problem Statement:** 

Create a DNS caching system that:

● Stores domain name → IP address mappings

● Implements TTL-based expiration (entries expire after X seconds)

● Automatically removes expired entries

● Handles cache misses by querying upstream DNS

● Reports cache hit/miss ratios

● Implements LRU eviction when cache is full

**Concepts Covered:**

● Hash table implementation with custom Entry class

● Chaining for collision resolution

● Time-based operations

● Performance metrics

Problem 4: 
-
Plagiarism Detection System
**Scenario:**

A university needs to check student submissions against a database of 100,000
previous essays to detect plagiarism. Simple string matching is too slow.

**Problem Statement:** 

Build a plagiarism detector that:

● Breaks documents into n-grams (sequences of n words)

● Stores n-grams in a hash table with document references

● Finds matching n-grams between documents

● Calculates similarity percentage

● Identifies the most similar documents in O(n) time

**Concepts Covered:**

● String hashing techniques

● Frequency counting with hash maps

● Good hash function properties

● Performance benchmarking (hash vs. linear search)

Problem 5: 
-
Real-Time Analytics Dashboard for Website
Traffic

**Scenario:**

A news website gets 1 million page views per hour. The marketing team needs
real-time analytics showing top pages, traffic sources, and user locations.

**Problem Statement:** 

Implement a streaming analytics system that:

● Processes incoming page view events in real-time

● Maintains top 10 most visited pages

● Tracks unique visitors per page

● Counts visits by traffic source (Google, Facebook, Direct, etc.)

● Updates dashboard every 5 seconds with zero lag

**Concepts Covered:**

● Frequency counting applications

● Multiple hash tables for different dimensions

● Load factor and resizing under high throughput

● Time/space complexity optimization

Problem 6: 
-
Distributed Rate Limiter for API Gateway

**Scenario:** 

Your API gateway handles requests from 100,000 clients. Each client is allowed 1000
requests per hour. You need to enforce this limit efficiently.

**Problem Statement:** 

Build a token bucket rate limiter that:

● Tracks request counts per client (by API key or IP)

● Allows burst traffic up to limit

● Resets counters every hour

● Responds within 1ms for rate limit checks

● Handles distributed deployment (multiple servers)

● Provides clear error messages when limit exceeded

**Concepts Covered:**

● Hash table for client tracking

● Time-based operations

● Collision handling (multiple clients)

● Performance under concurrent access

Problem 7: 
-
Autocomplete System for Search Engine

**Scenario:**

Build a Google-like autocomplete that suggests queries as users type, based on 10
million previous search queries and their popularity.

**Problem Statement:** 

Create an autocomplete system that:

● Stores search queries with frequency counts

● Returns top 10 suggestions for any prefix in <50ms

● Updates frequencies based on new searches

● Handles typos and suggests corrections

● Optimizes for memory (10M queries × avg 30 characters)

**Concepts Covered:**

● Hash table for query frequency storage

● String hashing techniques

● Performance benchmarking (prefix search)

● Space complexity optimization

Problem 8: 
-
Parking Lot Management with Open
Addressing

**Scenario:** 

A smart parking lot with 500 spots needs to track which vehicles are parked where,
handle collisions when multiple vehicles arrive simultaneously, and optimize spot allocation.

**Problem Statement:** 

Implement a parking system using open addressing that:

● Assigns parking spots based on license plate hash

● Uses linear probing when preferred spot is occupied

● Tracks entry/exit times for billing

● Finds nearest available spot to entrance

● Generates parking statistics (avg occupancy, peak hours)

**Concepts Covered:**

● Open addressing (linear/quadratic probing)

● Collision resolution strategies

● Custom hash functions

● Load factor management

Problem 9: 
-
Two-Sum Problem Variants for Financial
Transactions

**Scenario:**

A payment processing company needs to detect fraudulent transaction pairs that
sum to specific amounts (money laundering), find complementary trades, and identify duplicate
payments.

**Problem Statement:** 

Given millions of daily transactions, implement:
● Classic Two-Sum: Find pairs that sum to target amount

● Two-Sum with time window: Pairs within 1 hour

● K-Sum: Find K transactions that sum to target

● Duplicate detection: Same amount, same merchant, different accounts

● All under 100ms response time

**Concepts Covered:**

● Hash table for complement lookup

● O(1) lookup performance

● Multiple hash tables for different checks

● Time complexity analysis

Problem 10: 
-
Multi-Level Cache System with Hash Tables

**Scenario:**

Design a cache hierarchy for a video streaming service (like Netflix) with L1
(memory), L2 (SSD), and L3 (database) levels. Optimize for 10M concurrent users.

**Problem Statement:** 

Build a multi-level caching system that:

● L1 Cache: 10,000 most popular videos (in-memory HashMap)

● L2 Cache: 100,000 frequently accessed videos (SSD-backed)

● L3: Database (slow, all videos)

● Implements LRU eviction at each level

● Promotes videos between levels based on access patterns

● Tracks cache hit ratios for each level

● Handles cache invalidation when content updates

**Concepts Covered:**

● Multiple hash tables with different purposes

● Resizing/rehashing strategies

● Performance benchmarking across levels

● Load factor optimization for each tier