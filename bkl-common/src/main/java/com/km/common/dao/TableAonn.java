package com.km.common.dao;

import java.lang.annotation.*;
import java.lang.reflect.*;
 
// An annotation type declaration.
@Retention(RetentionPolicy.RUNTIME)
public @interface TableAonn {
  String tableName();
}