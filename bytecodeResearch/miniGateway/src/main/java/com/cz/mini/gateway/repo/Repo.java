package com.cz.mini.gateway.repo;

/**
 * code desc
 *
 * @author Zjianru
 */
public abstract class Repo<T> {
    public abstract T queryData(int id);
}
