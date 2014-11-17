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

package io.vertx.java.spi.cluster.impl.infinispan;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.impl.LoggerFactory;
import io.vertx.core.shareddata.AsyncMap;
import io.vertx.core.spi.cluster.VertxSPI;
import org.infinispan.Cache;

public class InfinispanAsyncMap<K, V> implements AsyncMap<K, V> {

  private static final Logger log = LoggerFactory.getLogger(InfinispanAsyncMap.class);

  private VertxSPI vertx;
  private Cache<K, V> map;

  public InfinispanAsyncMap(VertxSPI vertx, Cache<K, V> map) {
    this.vertx = vertx;
    this.map = map;
  }

  @Override
  public void get(K k, Handler<AsyncResult<V>> asyncResultHandler) {
    if (log.isDebugEnabled()) {
      log.debug(String.format("Get value for key [%s]", k));
    }
    vertx.executeBlocking(() -> map.get(k), asyncResultHandler);
  }

  @Override
  public void put(final K k, final V v, Handler<AsyncResult<Void>> completionHandler) {
    if (log.isDebugEnabled()) {
      log.debug(String.format("Put value [%s] for key [%s]", v, k));
    }
    vertx.executeBlocking(() -> {
      map.put(k, v);
      return null;
    }, completionHandler);
  }

  @Override
  public void putIfAbsent(K k, V v, Handler<AsyncResult<V>> completionHandler) {
    if (log.isDebugEnabled()) {
      log.debug(String.format("PutIfAbsent value [%s] for key [%s]", v, k));
    }
    vertx.executeBlocking(() -> map.putIfAbsent(k, v), completionHandler);
  }

  @Override
  public void remove(final K k, Handler<AsyncResult<V>> resultHandler) {
    if (log.isDebugEnabled()) {
      log.debug(String.format("Remove key [%s]", k));
    }
    vertx.executeBlocking(() -> map.remove(k), resultHandler);
  }

  @Override
  public void removeIfPresent(K k, V v, Handler<AsyncResult<Boolean>> resultHandler) {
    if (log.isDebugEnabled()) {
      log.debug(String.format("Remove key [%s] with value [%s]", k, v));
    }
    vertx.executeBlocking(() -> map.remove(k, v), resultHandler);
  }

  @Override
  public void replace(K k, V v, Handler<AsyncResult<V>> resultHandler) {
    if (log.isDebugEnabled()) {
      log.debug(String.format("Replace key [%s] with value [%s]", k, v));
    }
    vertx.executeBlocking(() -> map.replace(k, v), resultHandler);
  }

  @Override
  public void replaceIfPresent(K k, V oldValue, V newValue, Handler<AsyncResult<Boolean>> resultHandler) {
    if (log.isDebugEnabled()) {
      log.debug(String.format("RemoveIfPresent oldvalue [%s] key [%s] with value [%s]", oldValue, k, newValue));
    }
    vertx.executeBlocking(() -> map.replace(k, oldValue, newValue), resultHandler);
  }

  @Override
  public void clear(Handler<AsyncResult<Void>> resultHandler) {
    if (log.isDebugEnabled()) {
      log.debug(String.format("Clear"));
    }
    vertx.executeBlocking(() -> {
      map.clear();
      return null;
    }, resultHandler);
  }
}
