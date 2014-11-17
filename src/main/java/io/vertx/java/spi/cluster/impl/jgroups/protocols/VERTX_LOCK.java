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

package io.vertx.java.spi.cluster.impl.jgroups.protocols;

import org.jgroups.conf.ClassConfigurator;
import org.jgroups.protocols.CENTRAL_LOCK;
import org.jgroups.util.Owner;

public class VERTX_LOCK extends CENTRAL_LOCK {

  static {
    ClassConfigurator.addProtocol((short) 1000, VERTX_LOCK.class);
  }

  public VERTX_LOCK() {
    super();
  }

  @Override
  protected Owner getOwner() {
    return new Owner(local_addr, -1);
  }
}
