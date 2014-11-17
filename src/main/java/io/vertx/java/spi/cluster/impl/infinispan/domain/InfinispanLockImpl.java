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

import io.vertx.core.VertxException;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.impl.LoggerFactory;
import org.jgroups.blocks.locking.LockService;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class InfinispanLockImpl implements io.vertx.core.shareddata.Lock {

  private final static Logger log = LoggerFactory.getLogger(InfinispanLockImpl.class);
  private final Lock lock;

  public InfinispanLockImpl(LockService lockService, String name) {
    lock = lockService.getLock(name);
  }

  public boolean acquire(long timeout) {
    try {
      if (log.isDebugEnabled()) {
        log.debug(String.format("Aquire lock on [%s] with timeout [%d]", lock, timeout));
      }
      return lock.tryLock(timeout, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
    }
    return false;
  }

  @Override
  public void release() {
    lock.unlock();
  }
}
