/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.config.internal;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.config.api.dsl.model.ComponentBuildingDefinitionRegistry;
import org.mule.runtime.core.api.MuleContext;

/**
 * Factory to create instances of {@link ComponentBuildingDefinitionRegistry}
 * 
 * @since 4.4.0
 */
@NoImplement
public interface ComponentBuildingDefinitionRegistryFactory {

  /**
   * Creates a new {@link ComponentBuildingDefinitionRegistry}
   *
   * @param muleContext the owning {@link MuleContext}
   * @return a non {@code null} {@link ComponentBuildingDefinitionRegistry}
   */
  ComponentBuildingDefinitionRegistry create(MuleContext muleContext);
}
