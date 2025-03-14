package week9Project.exceptions;

//Custom exception for when no active transaction is found
public class NoActiveTransactionException extends Exception {
 public NoActiveTransactionException(String message) {
     super(message);
 }
}
