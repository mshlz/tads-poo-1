### tads-poo-1 _v2_
# Simplified Blackjack

## How to setup?
* Create a database `blackjack` in your PostgreSQL instance (or change the settings in `DatabaseConnection.java`)

* Run the App

## How to reset the database?
* Uncomment this line in the `App.java`:

```java
public static void main(String[] args) {
    // DBHelpers.dropDB();
    ...
}
```

```java
public static void main(String[] args) {
    DBHelpers.dropDB();
    ...
}
```

* Then, run the App. This method will clear **ALL RECORDS** from database.
* After run, you should comment it again, or next time you run it will reset the data over and over again...