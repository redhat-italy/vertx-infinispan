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

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.shareddata.AsyncMap;

import java.util.HashMap;

public class DummyCache implements AsyncMap<String, Long> {

    private HashMap<String, Long> map = new HashMap<>();

    @Override
    public void get(String key, Handler<AsyncResult<Long>> handler) {
        handler.handle(Future.completedFuture(map.get(key)));
    }

    @Override
    public void put(String key, Long value, Handler<AsyncResult<Void>> handler) {
        map.put(key, value);
        handler.handle(Future.completedFuture());
    }

    @Override
    public void putIfAbsent(String key, Long value, Handler<AsyncResult<Long>> handler) {
        handler.handle(Future.completedFuture(map.putIfAbsent(key, value)));
    }

    @Override
    public void remove(String key, Handler<AsyncResult<Long>> handler) {
        handler.handle(Future.completedFuture(map.remove(key)));
    }

    @Override
    public void removeIfPresent(String key, Long value, Handler<AsyncResult<Boolean>> handler) {
        handler.handle(Future.completedFuture(map.remove(key, value)));
    }

    @Override
    public void replace(String key, Long value, Handler<AsyncResult<Long>> handler) {
        handler.handle(Future.completedFuture(map.replace(key, value)));
    }

    @Override
    public void replaceIfPresent(String key, Long oldValue, Long newValue, Handler<AsyncResult<Boolean>> handler) {
        handler.handle(Future.completedFuture(map.replace(key, oldValue, newValue)));
    }

    @Override
    public void clear(Handler<AsyncResult<Void>> handler) {
        map.clear();
        handler.handle(Future.completedFuture());
    }
}
