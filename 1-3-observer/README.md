# Observer Design Pattern

This repository contains an exercise on Observer Design Pattern refactoring.

## Problem

We have a server monitoring system (`MessyServerMonitor`) that suffers from common design problems:

**Single class doing everything:**
```java
public void checkServer(Server server) {
    // 1. Check server health
    // 2. Console logging  
    // 3. Email notifications
    // 4. Slack notifications
    // ALL IN ONE METHOD!
}
```

**Key Issues:**
- **Violation of Single Responsibility Principle** - one class handles monitoring, logging, email, and Slack
- **Hard to test** - cannot test individual notification types in isolation
- **Difficult to extend** - adding Discord notifications requires modifying existing code
- **Boolean flag management** - `emailEnabled`, `slackEnabled` doesn't scale
- **Tight coupling** - changing email logic affects the entire system

## Solution: Observer Pattern

The Observer Pattern solves these problems by:
- **Separating concerns** - each observer handles one responsibility
- **Enabling dynamic behavior** - add/remove observers at runtime
- **Supporting easy extension** - new notification types require no changes to existing code
- **Providing loose coupling** - observers don't know about each other

## Refactoring Overview

### New Classes Created

**Core Pattern Interfaces:**
- `Subject` - interface for managing observers and sending notifications
- `Observer` - interface for receiving notifications from subjects

**Refactored Subject:**
- `CleanServerMonitor` - clean implementation focused only on server monitoring and observer management

**Concrete Observers:**
- `ConsoleLoggingObserver` - handles console output with timestamps
- `EmailNotificationObserver` - sends email alerts for critical issues only
- `SlackNotificationObserver` - sends Slack notifications for any problematic status



## Refactoring Process

1. **Extract notification logic** from `MessyServerMonitor.checkServer()` into separate observer classes
2. **Implement Pull Model** - observers receive the subject and extract needed information
3. **Apply different business rules** per observer:
    - Console: logs all status changes
    - Email: alerts only for CRITICAL status
    - Slack: alerts for any problematic status (ERROR, OFFLINE, CRITICAL)
4. **Enable dynamic observer management** - add/remove observers without code changes

### Benefits Achieved

**Before (Messy):**
- Adding Discord = modify `checkServer()` + add boolean flag
- Testing email = testing everything else too
- 4 responsibilities in 1 method

**After (Observer Pattern):**
- Adding Discord = create `DiscordObserver` + `monitor.addObserver()`
- Testing email = test `EmailNotificationObserver` independently
- 1 responsibility per class

## Usage

**Run messy demo:**
```bash
java MessyMonitoringDemo
```

**Run clean demo (enabled in completed branch):**
```bash  
java ObserverPatternDemo
```

**Run behavior tests:**
Change `IMPLEMENTATION` constant in

`RefactoringBehaviorTests` to verify both implementations produce identical behavior.

## Key Learning Points

- How Observer Pattern separates concerns and enables extension
- Pull Model implementation where observers extract needed data
- Behavior-driven testing to verify refactoring correctness
- Single Responsibility and Open/Closed Principles in practice