# Authenticated Connection Example

This is an example designed to test the authentication system and show new users of this API how it all works.
The password is stored in ```char[]``` form in the Common class but in reality you'd want to store the password at both ends in a config.
Try changing ```Common.KEY``` client side to something random, for example ```"PasswordThatWillNotWork".toCharArray()```, you will see that authentication fails and 5 seconds later, the client tries again.

## Why ```char[]``` instead of a ```String```?

The 2 benefits that I know of for storing a password in a character array versus a string are:

1. When a character array is cleared, it instantly goes, whereas a String, being immutable, hangs around until the garbage collector disposes of it. This protects you against some types of attacks.
2. It's harder to accidentally print a character array to a console or log file than it is to print a String  .


    Take the following example:

    ```java
    String password = "SomePassword";
    System.out.println(password);
    ```

    Prints ```SomePassword```, whereas:

    ```java
    char[] password = "Password".toCharArray();
    System.out.println(password);
    ```

    Prints ```[C@2634593e```

    There is a whole discussion on it over at StackOverflow: http://stackoverflow.com/questions/8881291/why-is-char-preferred-over-string-for-passwords