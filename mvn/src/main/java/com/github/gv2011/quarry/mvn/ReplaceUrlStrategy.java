package com.github.gv2011.quarry.mvn;

/*
 * Copyright 2001-2006 Codehaus Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import org.codehaus.plexus.classworlds.realm.ClassRealm;
import org.codehaus.plexus.classworlds.strategy.AbstractStrategy;

/**
 * @author Jason van Zyl
 */
public class ReplaceUrlStrategy
    extends AbstractStrategy
{

    public ReplaceUrlStrategy( final ClassRealm realm )
    {
        super( realm );
    }

    @Override
    public Class<?> loadClass( final String name )
        throws ClassNotFoundException
    {
        Class<?> clazz = realm.loadClassFromImport( name );

        if ( clazz == null )
        {
            clazz = realm.loadClassFromSelf( name );

            if ( clazz == null )
            {
                clazz = realm.loadClassFromParent( name );

                if ( clazz == null )
                {
                    throw new ClassNotFoundException( name );
                }
            }
        }

        return clazz;
    }

    @Override
    public URL getResource( final String name )
    {
        URL resource = realm.loadResourceFromImport( name );

        if ( resource == null )
        {
            resource = realm.loadResourceFromSelf( name );

            if ( resource == null )
            {
                resource = realm.loadResourceFromParent( name );
            }
        }

        return resource;
    }

    @Override
    public Enumeration<URL> getResources( final String name )
        throws IOException
    {
        final Enumeration<URL> imports = realm.loadResourcesFromImport( name );
        final Enumeration<URL> self = realm.loadResourcesFromSelf( name );
        final Enumeration<URL> parent = realm.loadResourcesFromParent( name );

        return combineResources( imports, self, parent );
    }

}
