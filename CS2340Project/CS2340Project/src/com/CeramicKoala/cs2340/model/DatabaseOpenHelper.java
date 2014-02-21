package com.CeramicKoala.cs2340.model;

import java.util.List;

public interface DatabaseOpenHelper<T extends DatabaseElement> {
	
	/**
     * adds a new element to the table. 
     * @param T t element to be added
     * @return T newly added element if successful
     * @throws DatabaseException if unsuccessful
     */
    public T addElement(T t) throws DatabaseException;
    
    /**
     * updates element in table
     * @param T t element with id and new information
     * @return T new element if successful
     * @throws DatabaseException if unsuccessful
     */
    public T updateElement(T t) throws DatabaseException;
    
    /**
     * deletes an element permanently
     * @param T t element to be deleted
     * @return true if successful
     */
    public boolean deleteElement(T t);
    
    /**
     * retrieves an element by name
     * @param name field
     * @return T the element with the input name if successful
     * @throws DatabaseException if element does not exist
     * @throws UnsupportedOperationException if unsupported
     */
    public T getElementByName(String name) throws DatabaseException, UnsupportedOperationException;
    
    /**
     * retrieves an element by unique id
     * @param id
     * @return T the element with the input id if successful
     * @throws DatabaseException if element does not exist
     * @throws UnsupportedOperationException if unsupported
     */
    public T getElementById(int id) throws DatabaseException, UnsupportedOperationException;
    
    
    /**
     * gets a list of all elements
     * @return List<T> all elements in table or empty list
     * @throws UnsupportedOperationException if unsupported
     */
    public List<T> getAllElements() throws UnsupportedOperationException;
    
    /**
     * gets the number of rows in table
     * @return int size of table
     */
    public int getTableSize();
    
    /**
     * deletes all rows in the table
     * @return
     */
    public boolean resetTable();
    
    /**
     * gets table.toString, table size, and optional further
     * information
     * @return String above information
     */
    public String getTableInfo();
    
    /**
     * private method that implementing classes are encouraged to implement
     * to assist with CRUD operations
     * checks if element already exists in table
     * @param user
     * @return true if element already exists in table
     * boolean elementAlreadyExists(T t);
     */
}
