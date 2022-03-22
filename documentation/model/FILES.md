# File Management

## File Tracker

This class keeps track of which files are currently being
tracked by the application for use in calculations and graphing.

### Querying

This class provides the query function for the Model to
interface with it. A query consists of an action and the 
target file.

```java
//Actions
public enum FileTrackerQuery {
    TRACK,
    UNTRACK,
    IS_TRACKED,
    READ
}

    //Query Function
    public Optional<Object> query(FileTrackerQuery, String filename) {
    ...
    }
```

Doing it this way allows us to reduce coupling between this
class and the Model class as the Model will only have one 
function to interact with. If we want add extra functionality
to FileTracker, we can simply add the command to 
FileTrackerQuery and an implementation in the query function.

## TrackedFile

This class will read data in from a file on its own thread and
will send it through a PipedOutputStream to be collected by
the FileTracker class to be processed.

A TrackedFile will have a file type that will help us
understand what we can do with it.

## FileType

Each FileType will have a corresponding record to mimic a line
found in the corresponding .csv file. Each record will have a
corresponding producer function that will take a .csv line and
make a record out of it.

