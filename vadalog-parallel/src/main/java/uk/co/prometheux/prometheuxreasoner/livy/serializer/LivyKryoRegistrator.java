package uk.co.prometheux.prometheuxreasoner.livy.serializer;

import java.util.Arrays;

import org.apache.spark.serializer.KryoRegistrator;

import com.esotericsoftware.kryo.Kryo;

/**
 * A custom Kryo Registrator
 * Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */

public class LivyKryoRegistrator implements KryoRegistrator {
	
    @Override
    public void registerClasses(Kryo kryo) {
        kryo.register(Arrays.asList( "" ).getClass(), new ArraysAsListSerializer());
    }

}
