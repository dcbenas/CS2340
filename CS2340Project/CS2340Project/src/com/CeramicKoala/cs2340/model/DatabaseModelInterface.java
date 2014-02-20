package com.CeramicKoala.cs2340.model;

interface DatabaseModelInterface {
    
    
    public User addUser(User user);
    
    public boolean updateUser(User user);
    
    public boolean deleteUser(User user);
    
    public User getUser(String username);
    
    public List<User> getAllUsers();
    
    public int getTableSize();
    
    public boolean resetDatabase();
    
    public String getTableInfo();
   
}
