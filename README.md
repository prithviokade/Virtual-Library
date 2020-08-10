# TableTasks

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
For those of us operating virtually this upcoming semester, TableTasks emulates the environment of working with your peers and friends in a library. TableTasks is a group study app where students can check into the app, log their goals, and share resources. Based on this data, they will get recommendations for "tables" they can join to connect with friends or new people; when they join a table, they can text others at the table, video chat to socialize, listen to music, and more.

### App Evaluation

- **Category:** Productivity, Social
- **Mobile:** Uses camera, audio, real-time.
- **Story:** Allows users to engage in a productive, but still social, work environment even when they are not able to physically enter such an evironment.
- **Market:** Anyone who works from home can enjoy this app.
- **Habit:** Users can have this app open consistently during the work day.
- **Scope:** Having the functionality of getting recommendations and joining tables with some communication is interesting to use and build. Added social features make the app more well-rounded and engaging.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* [x] Users can create a new account through the app
* [x] Users can login through the app
* [x] Users can input their current goals and update the status of their goals
* [x] Users can see all tables and join a table
* [x] Users can create a table
* [x] Users can real-time chat with others at their table
* [x] Users can post a new photo resource to their feed
* [x] Users can view other users' resource posts
* [x] Users can view individual profiles from details view of table and see that user's goals/progress
* [x] Users can get recommendations for tables they can join

**Optional Nice-to-have Stories**

* [x] Users can login through Facebook
* [x] Users can invite friends (normal invites and permanent invites for private tables)
* [x] Users gets notifications when they are invited to a table
* [x] Users can follow/friend other users
* [x] Users can temporarily visit a table
* [x] Users can swipe to see the time stamp of a message
* [x] Users can search for other users and for resources
* [x] Users can post a new file resource or link to their feed
* [x] Users can choose how to sort the tables
* [x] Users can video chat with others at their table
* [x] Users can listen to their table's playlist

### 2. Screen Archetypes

* Registration Screen
   * User can create a new account
* Login Screen
   * User can login through the app or through Facebook
* Tables Screen
   * User can see all tables; clicking on the table opens a fragment describing its details
   * User can see the table they're currently at if applicable; clicking on this opens up the current state of the table (texting, video chatting, music, status, etc.)
   * User can see recommendations for the table they should join
   * User can join tables
   * Users can sort tables
   * Users can see invites
* Table Creation
   * User can create their own table for people to join
* Profile Screen
   * User can keep track of how many of their goals they've accomplished by updating the status
   * User can see the resources they have posted
* Goal Creation
   * User can add more goals as they work on new things
* Resources Stream
   * User can view a feed of photos
* Resource Content Creation
   * User can post a new resource to their feed

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Home
* Search
* Progress
* Stream

**Flow Navigation** (Screen to Screen)

* Login ->
   * Home Tab
* Registration ->
   * Home Tab
* Home Tab (all Tables, Current Table) ->
   * Details of each Table (on click)
      * User Profiles (on click)
      * Music Fragment (on click)
      * Video Chat (on click)
      * Invite Users (on click)
* Search Tab ->
   * User Profiles (on click)
* Profile Tab ->
   * Details of each Resource (on click)
   * Goals Tab
* Resources Tab ->
   * Details of each Resource (on click)
* Create a Table ->
   * Details of the created Table
      * User Profiles (on click)
      * Music Fragment (on click)
      * Video Chat (on click)
      * Invite Users (on click)
* Create a resource ->
   * Resources Tab
* Create a goal ->
   * Profile Tab

## Wireframes

<img src='wireframes.png' title='Wireframes' width='' alt='Wireframes' />

## Schema

### Models
* Tables
    * Creator
    * Users
    * Status
    * Size
    * Topic
    * Type
    * Visitors allowed
    * Description
    * Locked
    * Chat
    * Invites
    * Video Chat Channel Name
    * Playlist
    * Current Song
* Goals
    * User
    * Status
    * Goal
* Posts
    * User
    * Caption
    * Subject
    * File + File Information
    * Link
    * Image
* Users (to add)
    * Name
    * Profile Picture
    * Goals
    * Bio
    * Current Table
    * Friends
* Invite
    * Sender
    * Reciever
    * Table
    * Type
* Message
    * Text
    * Sender

### Networking
* (Read) Query all tables for user to join
* (Create) New tables
* (Read) Get user's checklist
* (Create) Update user's checklist
* (Read) Get all posts
* (Create) Make a post
* (Read) Get messages for table
* (Create) New messages
* (Read) Get all invites to a user
* (Create) Send an invite
