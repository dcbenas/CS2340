package com.CeramicKoala.cs2340.model;

public interface AccountInterface {

    /**
     * adds a new account for a specific user
     * @param user
     * @param account
     * @return User newly updated user with account added
     */
    public User addAccount(User user, Account account);
}
