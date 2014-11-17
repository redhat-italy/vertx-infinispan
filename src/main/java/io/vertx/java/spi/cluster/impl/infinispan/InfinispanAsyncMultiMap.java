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
import io.vertx.core.spi.cluster.AsyncMultiMap;
import io.vertx.core.spi.cluster.ChoosableIterable;
import io.vertx.core.spi.cluster.VertxSPI;
import io.vertx.java.spi.cluster.impl.infinispan.domain.ImmutableChoosableSet;
import io.vertx.java.spi.cluster.impl.infinispan.domain.ImmutableChoosableSetEmpty;
import io.vertx.java.spi.cluster.impl.infinispan.domain.ImmutableChoosableSetImpl;
import org.infinispan.Cache;
import org.infinispan.distexec.DefaultExecutorService;
import org.infinispan.distexec.DistributedCallable;
import org.infinispan.executors.DefaultExecutorFactory;

import java.util.Set;

public class InfinispanAsyncMultiMap<K, V> implements AsyncMultiMap<K, V> {

  private static final Logger log = LoggerFactory.getLogger(InfinispanAsyncMultiMap.class);

  private final VertxSPI vertx;
  private final Cache<K, ImmutableChoosableSet<V>> cache;

  public InfinispanAsyncMultiMap(VertxSPI vertx, Cache<K, ImmutableChoosableSet<V>> cache) {
    this.vertx = vertx;
    this.cache = cache;
  }

  @Override
  public void get(final K k, Handler<AsyncResult<ChoosableIterable<V>>> completionHandler) {
    if (log.isDebugEnabled()) {
      log.debug(String.format("Getting value for key [%s]", k));
    }
    vertx.executeBlocking(() -> {
          ImmutableChoosableSet<V> v = cache.get(k);
          if (log.isDebugEnabled()) {
            log.debug(String.format("Value [%s] for key [%s]", v, k));
          }
          return v;
        }, completionHandler
    );
  }

  @Override
  public void remove(K k, V v, Handler<AsyncResult<Boolean>> completionHandler) {
    vertx.executeBlocking(() -> {
      if (log.isDebugEnabled()) {
        log.debug(String.format("Remove value [%s] for key [%s]", v, k));
      }
      ImmutableChoosableSet<V> oldValue = cache.get(k);
      return (oldValue == null || oldValue.isEmpty()) || cache.replace(k, oldValue, oldValue.remove(v));
    }, completionHandler);
  }

  @Override
  public void add(final K k, final V v, final Handler<AsyncResult<Void>> completionHandler) {
    if (log.isDebugEnabled()) {
      log.debug(String.format("Add value [%s] to key [%s]", v, k));
    }
    vertx.executeBlocking(() -> {
      ImmutableChoosableSetImpl<V> newValue = new ImmutableChoosableSetImpl<>(v);
      ImmutableChoosableSet<V> entry = cache.putIfAbsent(k, newValue);
      if (log.isDebugEnabled()) {
        log.debug(String.format("PutIfAbsent in map [%s, %s] = %s", k, newValue, entry));
      }
      if (entry != null) {
        if (log.isDebugEnabled()) {
          log.debug(String.format("Replace key [%s] value in map %s with %s", k, entry, entry.add(v)));
        }
        if (!cache.replace(k, entry, entry.add(v))) {
          log.error(String.format("Value changed during update for key [%s], retry []", k.toString()));
          throw new RuntimeException("Value changed during update");
        }
      }
      return null;
    }, completionHandler);
  }

  @Override
  public void removeAllForValue(V v, Handler<AsyncResult<Void>> completionHandler) {
    log.error("not yet implemented");
    //This should be a distributed execute on primary(local) key
    throw new UnsupportedOperationException("not yet implemented");
  }

}
