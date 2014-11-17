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

package io.vertx.java.spi.cluster.impl.infinispan.domain.serializer;

import io.vertx.java.spi.cluster.impl.infinispan.domain.ImmutableChoosableSet;
import io.vertx.java.spi.cluster.impl.infinispan.domain.ImmutableChoosableSetEmpty;
import io.vertx.java.spi.cluster.impl.infinispan.domain.ImmutableChoosableSetImpl;
import org.infinispan.commons.marshall.AdvancedExternalizer;
import org.infinispan.commons.util.Util;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Set;

public class ImmutableChoosableSetSerializer implements AdvancedExternalizer<ImmutableChoosableSet> {

    private final static Set typeClasses = Util.asSet(ImmutableChoosableSetImpl.class, ImmutableChoosableSetEmpty.class);

    @Override
    public Set<Class<? extends ImmutableChoosableSet>> getTypeClasses() {
        return typeClasses;
    }

    @Override
    public Integer getId() {
        return 2001;
    }

    @Override
    public void writeObject(ObjectOutput output, ImmutableChoosableSet object) throws IOException {
        if (!object.isEmpty()) {
            output.writeObject(object.head());
            writeObject(output, object.tail());
        } else {
            output.writeObject(null);
        }
    }

    @Override
    public ImmutableChoosableSet readObject(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        Object o = objectInput.readObject();
        if (o == null) {
            return ImmutableChoosableSetEmpty.emptySet;
        }
        return readObject(objectInput).add(o);
    }
}
