# Virtual Library

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
A group study app where students can check into app, log their day's goals/topics they're studying, and input their preferences for their study environment. Based on this data, they will get recommendations for "tables" they can join; when they join a table, they can chat with others at the table, share screen, socialize, listen to music, etc.

### App Evaluation

- **Category:** Productivity, Social
- **Mobile:** Uses camera, audio, real-time.
- **Story:** Allows users to engage in a productive, but still social, work environment even when they are not able to physically enter such an evironment.
- **Market:** Anyone who works at home can enjoy this app.
- **Habit:** Users can have this app open consistently during the work day.
- **Scope:** Having the functionality of getting recommendations and joining tables with some communication is interesting to use and build. Added social features make the app more well-rounded and engaging.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* [x] User can create a new account
* [x] User can login
* [x] User can input their goals and update the status of
* [x] User can see all tables and join a table
* [x] User can chat with others at their table
* [x] User can post a new photo to their feed and view other users' posts
* [ ] User can get recommendations for tables they can join
* [ ] User can view individual profiles and see progress

**Optional Nice-to-have Stories**

* [ ] User can invite friends
* [ ] User can follow other users
* [ ] User can listen to the music at their table
* [ ] User can video chat with others at their table
* [ ] User can screen share
* [ ] User can add a comment/like to a photo
* [ ] User can tap a photo to view a more detailed photo screen with comments

### 2. Screen Archetypes

* Registration Screen
   * User can create a new account
   * User answers some question regarding study habits
* Login Screen
   * User can login
   * User selects their preferences for the day
   * User plans out their goals
* Tables Screen
   * User can see all tables, clicking on the table opens a fragment describing its details
   * User can see the table they're currently at if applicable, clicking on this opens up the current state of the table (chatting, music, etc.)
   * User can see recommendations for the table they should join
* Table Creation
   * User can create their own table for people to join
* Progress
   * User can keep track of how much of their goals they've accomplished
* Stream
   * User can view a feed of photos
* Content Creation
   * User can post a new photo to their feed

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Home
* Progress
* Stream

**Flow Navigation** (Screen to Screen)

* Login ->
   * Home
* Registration ->
   * Home
* Home Tab (all Tables, Current Table) ->
   * Details of each Table (on click)
* Create a Table ->
   * Home
* Create a post ->
   * Stream
* Project progress ->
   * None

## Wireframes

<img src='wireframes.png' title='Wireframes' width='' alt='Wireframes' />

## Schema

### Models
* Tables
    * Users
    * Status
    * Details
    * Chat
* Goals
    * User
    * Checklist
    * Completed
* Posts
    * User
    * Likes
    * Comments
    * Image
    * Captions
* Users (to add)
    * Name
    * Profile Picture
    * Goals and Status of goals
    * Bio

### Networking
* (Read) Query all tables for user to join
* (Create) New tables
* (Read) Get user's checklist
* (Create) Update user's checklist
* (Read) Get all posts
* (Create) Make a post
