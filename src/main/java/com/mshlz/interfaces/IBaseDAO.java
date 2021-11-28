package com.mshlz.interfaces;

public interface IBaseDAO<T> {
    public Boolean save(T model);
}
