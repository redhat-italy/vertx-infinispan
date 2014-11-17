/*
 * Copyright (c) 2011-2013 The original author or authors
 * ------------------------------------------------------
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *     The Eclipse Public License is available at
 *     http://www.eclipse.org/legal/epl-v10.html
 *
 *     The Apache License v2.0 is available at
 *     http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */

package io.vertx.java.spi.cluster.impl.infinispan.domain;

import java.util.Collections;
import java.util.Iterator;

public class ImmutableChoosableSetEmpty<T> implements ImmutableChoosableSet<T> {

  public static final ImmutableChoosableSet emptySet = new ImmutableChoosableSetEmpty();

  private ImmutableChoosableSetEmpty() {
  }

  @Override
  public ImmutableChoosableSet<T> add(T value) {
    return new ImmutableChoosableSetImpl<T>(value);
  }

  @Override
  public ImmutableChoosableSet<T> remove(T value) {
    return this;
  }

  @Override
  public T head() {
    return null;
  }

  @Override
  public ImmutableChoosableSet<T> tail() {
    return this;
  }

  @Override
  public boolean isEmpty() {
    return true;
  }

  @Override
  public T choose() {
    return null;
  }

  @Override
  public Iterator<T> iterator() {
    return Collections.emptyIterator();
  }


  @Override
  public String toString() {
    return "[]";
  }
}
